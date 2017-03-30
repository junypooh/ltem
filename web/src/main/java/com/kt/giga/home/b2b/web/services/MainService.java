package com.kt.giga.home.b2b.web.services;

import com.google.common.collect.ImmutableSet;
import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.Role;
import com.kt.giga.home.b2b.entity.BizContract;
import com.kt.giga.home.b2b.entity.Manager;
import com.kt.giga.home.b2b.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by junypooh on 2017-03-09.
 * <pre>
 * com.kt.giga.home.b2b.web.services.MainService
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-09 오후 6:50
 */
@Service
public class MainService implements IMainService {

    @Autowired
    private BizContractRepository bizContractRepository;

    @Autowired
    private ServiceContractRepository serviceContractRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BizContUsageRepository bizContUsageRepository;

    @Override
    public long selectContractsCount(B2bManager b2bManager) {

        if (b2bManager.getRole() == Role.SUPER_MASTER) {    // 슈퍼마스터일경우 모든 계약
            return bizContractRepository.countByContractsSuperMaster();

        } else if (b2bManager.getRole() == Role.MASTER) {   // 마스터일 경우 회사 계약 전체
            return bizContractRepository.countByCompanyCode(b2bManager.getCompanyCd());

        } else {    // 수퍼바이저나 스탭일 경우 담당 계약만 표시
            return bizContractRepository.countByManagers(managerRepository.findBySequence(b2bManager.getMgrSeq()));

        }
    }

    @Override
    public Map<String, Long> selectRegistCompanyCount(B2bManager b2bManager) {

        Map<String, Long> map = new HashMap<>();
        map.put("ALL", companyRepository.count());
        map.put("NULL", companyRepository.countByNameIsNull());

        return map;
    }

    @Override
    public Map<String, Long> selectRegistAccountCount(B2bManager b2bManager) {

        // 슈퍼마스터 : 본인이 발급한 전체 계정 수
        // 마스터 : 본인이 발급한 슈퍼바이져/스탭 계정 수
        // 슈퍼바이져 : 본인이 발급한 스탭 계정 수
        // 스탭 : null

        Map<String, Long> map = new HashMap<>();
        if (b2bManager.getRole().equals(Role.SUPER_MASTER)) {
            map.put("ALL", managerRepository.countByCreatedBy(b2bManager.getUsername()));
        } else if (b2bManager.getRole().equals(Role.MASTER)) {
            map.put("SUPERVISOR", managerRepository.countByCreatedByAndRole(b2bManager.getUsername(), Role.SUPERVISOR.getAuthorityCd()));
            map.put("STAFF", managerRepository.countByCreatedByAndRole(b2bManager.getUsername(), Role.STAFF.getAuthorityCd()));
        } else if (b2bManager.getRole().equals(Role.SUPERVISOR)) {
            map.put("STAFF", managerRepository.countByCreatedByAndRole(b2bManager.getUsername(), Role.STAFF.getAuthorityCd()));
        } else {
            return null;
        }

        return map;
    }

    @Override
    public Map<String, Long> selectUserAddrRegistCount(B2bManager b2bManager) {

        Map<String, Long> map = new HashMap<>();
        if (b2bManager.getRole().equals(Role.SUPER_MASTER)) {
            map.put("ALL", serviceContractRepository.count());
            map.put("NULL", serviceContractRepository.countByDevicePlaceIsNull());
        } else {
            List<BizContract> bizContracts = bizContractRepository.findByManagers(managerRepository.findBySequence(b2bManager
                    .getMgrSeq()), null);

            map.put("ALL", serviceContractRepository.countByHistoricBizContractsIn(ImmutableSet.copyOf(bizContracts)));
            map.put("NULL", serviceContractRepository.countByHistoricBizContractsInAndDevicePlaceIsNull(ImmutableSet.copyOf(bizContracts)));
        }

        return map;
    }

    @Override
    public long selectHubCount(B2bManager b2bManager) {

        if (b2bManager.getRole().equals(Role.SUPER_MASTER)) {
            return serviceContractRepository.countByIsActiveTrue();
        } else {
            List<BizContract> bizContracts = bizContractRepository.findByManagers(managerRepository.findBySequence(b2bManager
                    .getMgrSeq()), null);
            return serviceContractRepository.countByHistoricBizContractsInAndIsActiveTrue(ImmutableSet.copyOf(bizContracts));
        }
    }

    @Override
    public Map<String, Long> selectHubStatusCount(B2bManager b2bManager) {
        // TODO: 2017-03-10 추후 Manager DataBase 연결
        Map<String, Long> map = new HashMap<>();
        map.put("ALL", 0L);
        map.put("ON", 0L);

        return map;
    }

    @Override
    public Map<String, Long> selectNotInChargeContractCount(B2bManager b2bManager) {

        Map<String, Long> map = new HashMap<>();
        map.put("ALL", bizContractRepository.countByCompanyCode(b2bManager.getCompanyCd()));

        Specifications<BizContract> specifications = Specifications.where(BizContractSpecifications.contractsInCompany(b2bManager
                .getCompanyCd()))
                                                                   .and(BizContractSpecifications.contractsNotInMaster());
        map.put("NULL", bizContractRepository.count(specifications));

        return map;
    }

    @Override
    public long selectContrantInChargeStaffCount(B2bManager b2bManager) {

        if (b2bManager.getRole().equals(Role.STAFF)) {
            long count = 0L;
            List<BizContract> bizContracts = bizContractRepository.findByManagers(managerRepository.findBySequence(b2bManager
                    .getMgrSeq()), null);
            for (BizContract bizContract : bizContracts) {

                for (Manager manager : bizContract.getManagers()) {
                    if (manager.getRole().equals(Role.STAFF)) {
                        count++;
                    }
                }

            }
            return count;
        } else {
            return 0L;
        }
    }

    @Override
    public Object selectAvgCapacityOverInfo(B2bManager b2bManager) {
        List<Object[]> excessUsageList;

        if (b2bManager.getAuthorityCd() == Role.SUPER_MASTER.getAuthorityCd()) {
            excessUsageList = bizContUsageRepository.findByExcessUsageSuperMaster();
        } else if (b2bManager.getAuthorityCd() == Role.MASTER.getAuthorityCd()) {
            excessUsageList = bizContUsageRepository.findByExcessUsageMaster();
        } else {
            excessUsageList = bizContUsageRepository.findByExcessUsageSupervisorAndStaff();
        }

        for (Object[] obj : excessUsageList) {
            obj[4] = BizContract.getCalculateCapacityByMb((Character) obj[3], ((BigInteger) obj[1]).longValue());
            obj[5] = BizContract.getCalculateCapacityByMb((Character) obj[3], ((BigInteger) obj[2]).longValue());
        }

        return excessUsageList;
    }

    @Override
    public Object selectMainNoticeInfo() {
        return null;
    }
}
