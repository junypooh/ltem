package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.annotation.PrivacyAuditHistoric;
import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.ContractInfo;
import com.kt.giga.home.b2b.domain.Role;
import com.kt.giga.home.b2b.domain.SvcContractInfo;
import com.kt.giga.home.b2b.entity.BizContract;
import com.kt.giga.home.b2b.entity.Manager;
import com.kt.giga.home.b2b.entity.ServiceContract;
import com.kt.giga.home.b2b.mapper.B2bManagerMaskingMapper;
import com.kt.giga.home.b2b.mapper.BizContractMapper;
import com.kt.giga.home.b2b.repository.BizContractRepository;
import com.kt.giga.home.b2b.repository.BizContractSpecifications;
import com.kt.giga.home.b2b.repository.ManagerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by DaDa on 2017-02-06.
 */
@Service
@Slf4j
public class BizContractService implements IBizContractService {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private BizContractRepository bizContractRepository;

    @Autowired
    private BizContractMapper bizContractMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private B2bManagerMaskingMapper b2bManagerMaskingMapper;

    @Autowired
    private ServiceContractService serviceContractService;

    @Autowired
    private IAccountService accountService;

    @Override
    public List<ContractInfo> selectBizContractsInSearchCurrentUser(B2bManager manager, String contractsState, String contractsColumn, String searchWord, String sortName) {

        // 검색 조건이 없을경우
        if (StringUtils.trimToNull(contractsState) == null && StringUtils.trimToNull(searchWord) == null) {
            return selectBizContractsInCurrentUser(manager, sortName);
        } else {
            // 검색 조건이 있을 경우
            return selectBizContractsBySpecifications(manager, contractsState, contractsColumn, searchWord, sortName);
        }

    }

    private List<ContractInfo> selectBizContractsInCurrentUser(B2bManager manager, String sortName) {
        List<ContractInfo> bizContract;

        if (manager.getRole() == Role.SUPER_MASTER) {    // 슈퍼마스터일경우 모든 계약
            bizContract = bizContractMapper.toContractInfoList(bizContractRepository.findAll(new Sort(Sort.Direction.DESC, sortName)));
        } else if (manager.getRole() == Role.MASTER) {   // 마스터일 경우 회사 계약 전체
            Set<BizContract> bizContractSet = bizContractRepository.findByCompanyCode(manager
                    .getCompanyCd(), new Sort(Sort.Direction.DESC, sortName));
            bizContract = bizContractMapper.toContractInfoList(new ArrayList<>(bizContractSet));
        } else {                                                        // 수퍼바이저나 스탭일 경우 담당 계약만 표시
            bizContract = bizContractMapper.toContractInfoList(bizContractRepository.findByManagers(managerRepository.findOne(manager
                    .getMgrSeq()), new Sort(Sort.Direction.DESC, sortName)));
        }

        bizContract.forEach(this::setManagerClassify);
        return bizContract;
    }

    private List<ContractInfo> selectBizContractsBySpecifications(B2bManager manager, String contractsState, String contractsColumn, String searchWord, String sortName) {

        Specifications specifications;

        // 권한에 따른 조건
        if (manager.getRole() == Role.SUPER_MASTER) {
            specifications = Specifications.where(BizContractSpecifications.contractDefault());
        } else if (manager.getRole() == Role.MASTER) {
            specifications = Specifications.where(BizContractSpecifications.contractsInCompany(manager.getCompanyCd()));
        } else {
            specifications = Specifications.where(BizContractSpecifications.contractsInManagers(manager.getMgrSeq()));
        }

        // 상태값의 조건이 있을경우
        if (StringUtils.trimToNull(contractsState) != null) {
            if ("contractPrev".equals(contractsState)) {
                specifications = specifications.and(BizContractSpecifications.contractsPrev());
            } else if ("contract".equals(contractsState)) {
                specifications = specifications.and(BizContractSpecifications.contractsInUse());
            } else if ("revocationOfContract".equals(contractsState)) {
                specifications = specifications.and(BizContractSpecifications.contractsCancel());
            }
        }

        //  검색의 조건이 있을 경우
        if (StringUtils.trimToNull(searchWord) != null) {
            if ("managersName".equals(contractsColumn)) {
                specifications = specifications.and(BizContractSpecifications.contractsInEqualManagerName(searchWord));
            } else if ("all".equals(contractsColumn)) {
                specifications = specifications.and(Specifications.where(BizContractSpecifications.contractsInEqualManagerName(searchWord))
                        .or(BizContractSpecifications.contractsInLikeCustom("name", searchWord))
                        .or(BizContractSpecifications.contractsInLikeCustom("serialNumber", searchWord)));
            } else {
                specifications = specifications.and(BizContractSpecifications.contractsInLikeCustom(contractsColumn, searchWord));
            }
        }

        List<ContractInfo> bizContract = bizContractMapper.toContractInfoList(bizContractRepository.findAll(specifications, new Sort(Sort.Direction.DESC, sortName)));
        bizContract.forEach(this::setManagerClassify);

        return bizContract;
    }

    private void setManagerClassify(ContractInfo contractInfo) {
        Set<B2bManager> masterList     = new HashSet<>();
        Set<B2bManager> supervisorList = new HashSet<>();
        Set<B2bManager> staffList      = new HashSet<>();

        for (B2bManager manager : contractInfo.getManagers()) {
            if (manager.getRole() == Role.MASTER) {
                masterList.add(manager);
            } else if (manager.getRole() == Role.SUPERVISOR) {
                supervisorList.add(manager);
            } else if (manager.getRole() == Role.STAFF) {
                staffList.add(manager);
            }
        }

        contractInfo.setMaster(masterList);
        contractInfo.setSupervisor(supervisorList);
        contractInfo.setStaff(staffList);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMgrContractRel(String serialNumber, String[] mgrSeqs) {
        BizContract bizContract = bizContractRepository.findBySerialNumber(serialNumber);
        for (String mgrSeq : mgrSeqs) {
            Manager manager = new Manager();
            manager.setSequence(Long.valueOf(mgrSeq));
            bizContractRepository.insertManagerInCharge(bizContract, manager);
        }

    }

    @Override
    public List<B2bManager> selectManagersToBeInCharge(String serialNumber, String managerName, Role currentUsersRole) {

        Set<Manager> subManagers;

        if (StringUtils.isEmpty(managerName)) {
            subManagers = selectManagersInRolesLowerThanCurrentUser(currentUsersRole);
        } else {
            subManagers = selectManagersInRolesLowerThanCurrentUserAndNameContaining(currentUsersRole, managerName);
        }

        // 해당 계약에 추가되어있는 담당자 remove
        BizContract bizContract = bizContractRepository.findOne(serialNumber);
        subManagers.removeAll(bizContract.getManagers());

        return b2bManagerMaskingMapper.toB2bManagerList(new ArrayList<>(subManagers));
    }

    private Set<Manager> selectManagersInRolesLowerThanCurrentUser(Role currentUsersRole) {
        Set<Manager> subManagers;

        if (currentUsersRole == Role.SUPER_MASTER) {
            subManagers = managerRepository.findManagersInLowerRolesThanCurrentUser();
        } else {
            subManagers = managerRepository.findManagersInSameCompanyAndLowerRolesThanCurrentUser();
        }
        return subManagers;
    }

    private Set<Manager> selectManagersInRolesLowerThanCurrentUserAndNameContaining(Role currentUsersRole, String managerName) {
        Set<Manager> subManagers;

        if (currentUsersRole == Role.SUPER_MASTER) {
            subManagers = managerRepository.findManagersInLowerRolesThanCurrentUserAndNameContaining(managerName);
        } else {
            subManagers = managerRepository.findManagersInSameCompanyAndLowerRolesThanCurrentUserAndNameContaining(managerName);
        }
        return subManagers;
    }

    List<Role> getRolesLessThan(Role role) {
        List<Role> lowerRoles = new ArrayList<>();

        Role current = role;

        while (current != null && current.getChildRole() != null) {
            lowerRoles.add(current.getChildRole());
            current = current.getChildRole();
        }

        return lowerRoles;
    }

    @Override
    public void updateBizContractName(String serialNumber, String contractName) {

        BizContract bizContract = bizContractRepository.findBySerialNumber(serialNumber);
        bizContract.setName(contractName);

        bizContractRepository.saveAndFlush(bizContract);

    }

    @Override
    @SuppressWarnings("unchecked")
    public void saveRecentlyViewed(ContractInfo toContractInfo) {

        List<ContractInfo> recentContracts = (List<ContractInfo>) httpSession.getAttribute("recentContracts");

        if (recentContracts == null || recentContracts.isEmpty()) {
            recentContracts = new ArrayList<>();
        }

        recentContracts.remove(toContractInfo);
        recentContracts.add(toContractInfo);

        // 최대 3건 처리.
        if (recentContracts.size() > 3)
            recentContracts.remove(0);

        httpSession.setAttribute("recentContracts", recentContracts);
    }

    @Override
    public Map<String, Object> getDetail(String serialNumber, String statusCode, String searchKey, String searchText, Pageable pageable) {

        Page<ServiceContract> serviceContractPage = serviceContractService.getServiceContracts(serialNumber, statusCode, searchKey, searchText, pageable);

        List<SvcContractInfo> svcContractInfos = serviceContractService.getSvcContractInfos(serviceContractPage.getContent());

        BizContract bizContract = bizContractRepository.findBySerialNumber(serialNumber);

        ContractInfo toContractInfo = bizContractMapper.toContractInfo(bizContract);

        log.debug("{}", svcContractInfos);

        Map<String, Object> map = new HashMap<>();
        map.put("contents", svcContractInfos);
        map.put("page", serviceContractPage);
        map.put("contractInfo", toContractInfo);

        return map;
    }

    /**
     * Annotation을 통해 이용
     *
     * @param serialNumber
     * @return
     */
    @Override
    public Boolean isInChargeOf(String serialNumber) {

        log.debug("Contract serialNumber : {}", serialNumber);

        return bizContractRepository.isInChargeOf(serialNumber);
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "사용자관리 > 계약 담당자 추가", unmasked = true, downloaded = false, returnClass = B2bManager.class, targetInfo = "아이디, 이름, 휴대폰번호")
    public B2bManager getUnmaskedContractInCharge(Long mgrSeq) {
        return accountService.unmaskManager(mgrSeq);
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "사용자관리 > 전체 계약 담당자", unmasked = true, downloaded = false, returnClass = B2bManager.class, targetInfo = "아이디, 이름, 휴대폰번호")
    public B2bManager getUnmaskedContractAllInCharge(Long mgrSeq) {
        return accountService.unmaskManager(mgrSeq);
    }


}
