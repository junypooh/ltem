package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.annotation.PrivacyAuditHistoric;
import com.kt.giga.home.b2b.domain.*;
import com.kt.giga.home.b2b.entity.BizContract;
import com.kt.giga.home.b2b.entity.Company;
import com.kt.giga.home.b2b.entity.Manager;
import com.kt.giga.home.b2b.mapper.AccountManagerMapper;
import com.kt.giga.home.b2b.mapper.B2bManagerMapper;
import com.kt.giga.home.b2b.mapper.BizContractMapper;
import com.kt.giga.home.b2b.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.*;

/**
 * Created by DaDa on 2017-02-23.
 */
@Service
@Slf4j
public class AccountService implements IAccountService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private BizContractRepository bizContractRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AccountManagerMapper accountManagerMapper;

    @Autowired
    private B2bManagerMapper b2bManagerMapper;

    @Autowired
    private BizContractMapper bizContractMapper;

    @Autowired
    private StandardPasswordEncoder standardPasswordEncoder;

    @Autowired
    private ISmsService smsService;

    @Autowired
    private ManagerTermRepository managerTermRepository;

    @Value("#{T(java.util.Arrays).asList('${lte-m.not.use.ids}')}")
    private List<String> notUseIds;


    @Override
    public Map<String, Object> selectAccountManagers(B2bManager manager, Pageable pageable, String status, String column, String searchWord) {

        Specifications specifications;

        // 권한에 따른 조건
        if (manager.getRole() == Role.SUPER_MASTER) {
            specifications = Specifications.where(ManagerSpecifications.managerDefault());
        } else {
            specifications = Specifications.where(ManagerSpecifications.managerInCompany(manager.getCompanyCd()))
                                           .and(ManagerSpecifications.managerInRoleGreaterThanEqual(manager.getAuthorityCd()));
        }


        // 검색 조건
        specifications = getSearchAccount(status, column, searchWord, specifications);

        Page<Manager> pageList = managerRepository.findAll(specifications, pageable);

        List<B2bManager> b2bManagers = accountManagerMapper.toB2bManagerList(pageList.getContent());

        Map<String, Object> map = new HashMap<>();
        map.put("list", b2bManagers);
        map.put("page", pageList);


        return map;
    }

    /**
     * 계정 검색 조건
     * @param status
     * @param column
     * @param searchWord
     * @param specifications
     */
    public Specifications getSearchAccount(String status, String column, String searchWord, Specifications specifications) {
        // 검색조건이 있을경우
        if(StringUtils.trimToNull(status) != null || StringUtils.trimToNull(searchWord) != null) {
            // 사용자 상태에 따른 조건
            if(StringUtils.trimToNull(status) != null) {
                specifications = specifications.and(ManagerSpecifications.managerInEqualCustom("status", status));
            }
            // 컬럼별 조건
            if(StringUtils.trimToNull(searchWord) != null) {
                List<Short> authorityCds = new ArrayList<>();
                if("role".equals(column)) {
                    for(Role role : Role.values()) {
                        if (role.name().replace("_", "-").contains(searchWord.toUpperCase())) {
                            authorityCds.add(role.getAuthorityCd());
                        }
                    }
                    if (!CollectionUtils.isEmpty(authorityCds)) {
                        specifications = specifications.and(ManagerSpecifications.managerInAuthorityCds(authorityCds));
                    } else {
                        specifications = specifications.and(ManagerSpecifications.managerInAuthorityCds(Collections.singletonList((short)0)));
                    }
                } else {
                    specifications = specifications.and(ManagerSpecifications.managerInEqualCustom(column, searchWord));
                }
            }
        }

        return specifications;

    }

    @Override
    public Map<String, Object> deleteAccountConfirm(Long[] mgrSeqs) {
        return deleteAccount(mgrSeqs, false);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> deleteAccountManagers(Long[] mgrSeqs) {
        return deleteAccount(mgrSeqs, true);
    }

    private Map<String, Object> deleteAccount(Long[] mgrSeqs, boolean booleanDel) {
        Map<String, Object> rtnMap = new HashMap<>();

        // 마스터 이상의 권한이 있는지 체크
        int masterOverCnt = managerRepository.countBySequenceInAndRoleLessThanEqual(Arrays.asList(mgrSeqs), Role.MASTER);



        // 마스터 이상의 권한이 있다면 return
        if (masterOverCnt > 0) {
            Manager manager = managerRepository.findOne(mgrSeqs[0]);
            if (manager.getRole().getAuthorityCd() < Role.MASTER.getAuthorityCd()) {
                rtnMap.put("code", "02");   // 슈퍼마스터일 경우
            } else {
                rtnMap.put("code", "01");
            }

        } else {
            // 계정삭제
            rtnMap.put("code", "00");

            // 삭제 성공 시 SMS 발송
            if(booleanDel) {
                List<Manager> managers = managerRepository.findBySequenceIn(Arrays.asList(mgrSeqs));

                for(Manager manager : managers) {
                    Optional<String> transactionId = smsService.sendMessage("100", manager.getTelephone().replaceAll("-", ""), "GiGA Iot 홈 Biz 서비스", "계정이 삭제처리 되었습니다.");
                    log.debug("Response : {}", transactionId);
                }
            }

            if(booleanDel) managerTermRepository.deleteByManager(Arrays.asList(mgrSeqs));
            if(booleanDel) managerRepository.deleteBySequenceIn(Arrays.asList(mgrSeqs));
        }

/*
        // 여러개 삭제일때
        if (mgrSeqs.length > 1) {
            // 마스터 이상의 권한이 있다면 return
            if (masterOverCnt > 0) {
                rtnMap.put("code", "01");
            } else {
                // 계정삭제
                rtnMap.put("code", "00");
                if(booleanDel) managerRepository.deleteBySequenceIn(Arrays.asList(mgrSeqs));
            }
        }
        // 단일 삭제일때
        else {
            // 마스터 이상 권한의 계정이라면
            if (masterOverCnt > 0) {
                Manager manager = managerRepository.findOne(mgrSeqs[0]);

                // 슈퍼마스터일 경우
                if (manager.getRole().getAuthorityCd() < 20 ) {
                    rtnMap.put("code", "03");
                } else {
                    // 마스터권한 삭제 가능 -> 회사의 마스터가 여러개이고, 해당 마스터가 다른 계약에 물려있지 않으면 삭제가능
                    Set<BizContract> bizContracts = manager.getBizContracts();
                    Company company = manager.getCompany();
                    int companyInMasterCnt = managerRepository.countByCompanyAndRole(company.getCode(), Role.MASTER);

                    if( bizContracts.size() > 0 || companyInMasterCnt < 2) {
                        // 계약에 물려있거나 , 회사의 마스터가 1개 이하이므로 return
                        rtnMap.put("code", "02");
                    } else {
                        // 계정삭제
                        rtnMap.put("code", "00");
                        if(booleanDel) managerRepository.deleteBySequenceIn(Arrays.asList(mgrSeqs));
                    }
                }
            } else {
                // 계정삭제
                rtnMap.put("code", "00");
                if(booleanDel) managerRepository.deleteBySequenceIn(Arrays.asList(mgrSeqs));
            }
        }
        */

        // 삭제 성공 시 SMS 발송
        /*
        if(booleanDel && "00".equals(rtnMap.get("code"))) {
            List<Manager> managers = managerRepository.findBySequenceIn(Arrays.asList(mgrSeqs));

            for(Manager manager : managers) {
                Optional<String> transactionId = smsService.sendMessage("100", manager.getTelephone().replaceAll("-", ""), "GiGA Iot 홈 Biz 서비스", "계정이 삭제처리 되었습니다.");
                log.debug("Response : {}", transactionId);
            }
        }
        */
        return rtnMap;
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "계정관리 > 계정관리 > 계정목록", unmasked = true, downloaded = false, returnClass = B2bManager.class, targetInfo = "아이디, 이름, 휴대폰번호")
    public B2bManager getUnmaskedAccountManager(Long mgrSeq) {
        return unmaskManager(mgrSeq);
    }

    @Override
    public B2bManager unmaskManager(Long mgrSeq) {
        Manager manager = managerRepository.findOne(mgrSeq);
        B2bManager accountManager = accountManagerMapper.toB2bManager(manager);
        B2bManager b2bManager= b2bManagerMapper.toB2bManager(manager);

        b2bManager.setContractsName(accountManager.getContractsName());

        return b2bManager;
    }

    @Override
    public Map<String, Object> selectAccountManager(Long mgrSeq) {
        Map<String, Object> rtnMap = new HashMap<>();

        Manager manager = managerRepository.findOne(mgrSeq);
        rtnMap.put("contract", bizContractMapper.toContractInfoList(new ArrayList<>(manager.getBizContracts())));
        rtnMap.put("manager", accountManagerMapper.toB2bManager(manager));

        return rtnMap;
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "계정관리 > 계정관리 > 계정상세", unmasked = true, downloaded = false, returnClass = Map.class, targetInfo = "아이디, 이름, 휴대폰번호")
    public Map<String, Object> selectUnmaskAccountManager(Long mgrSeq) {
        Map<String, Object> rtnMap = new HashMap<>();

        Manager manager = managerRepository.findOne(mgrSeq);
        rtnMap.put("contract", bizContractMapper.toContractInfoList(new ArrayList<>(manager.getBizContracts())));
        rtnMap.put("manager", b2bManagerMapper.toB2bManager(manager));

        return rtnMap;
    }

    /**
     * 기업 코드에 따른 권한 return
     * @param companyCd
     * @param currentManager
     * @return
     */
    @Override
    public Map<String, Object> selectCompanyInfo(String companyCd, B2bManager currentManager) {

        Map<String, Object> rtnMap = new HashMap<>();
        Company company = companyRepository.findOne(companyCd);

        if (company != null) {
            List<Map<String,Object>> list = getAuthorityList(company.getCode(), currentManager);
            rtnMap.put("code", "00");
            rtnMap.put("msg", "입력한 기업으로 등록 가능합니다.");
            rtnMap.put("authorityList", list);
        } else {
            rtnMap.put("code", "01");
            rtnMap.put("msg", "등록된 기업 없습니다. 신규 등록 후 계정을 등록 해주세요");
        }

        return rtnMap;
    }

    /**
     * 기업 코드와 로그인한사람의 권한에 따른 권한등록가능 리스트
     * @param companyCd
     * @param currentManager
     * @return
     */
    @Override
    public List<Map<String,Object>> getAuthorityList(String companyCd, B2bManager currentManager) {

        List<Map<String,Object>> list = new ArrayList<>();


        // kth나 kt 라면 superMaster만 등록가능
        if ("KT".equals(companyCd) || "KTH".equals(companyCd)) {
            Map<String,Object> map = new HashMap<>();
            map.put("authorityCd", Role.SUPER_MASTER.getAuthorityCd());
            map.put("authorityName", Role.SUPER_MASTER.name());
            list.add(map);
        }
        // 다른 기업이라면
        else {
            Specifications specifications = Specifications.where(ManagerSpecifications.managerInCompany(companyCd)).and(ManagerSpecifications.managerInEqualCustom("role", "20"));
            List<Manager> manager = managerRepository.findAll(specifications);

            if (Role.MASTER.getAuthorityCd() > currentManager.getAuthorityCd()) {
                // Master가 존재하지 않을때
                if(manager.size() < 1) {
                    Map<String,Object> master_map = new HashMap<>();
                    master_map.put("authorityCd", Role.MASTER.getAuthorityCd());
                    master_map.put("authorityName", Role.MASTER.name());
                    list.add(master_map);
                }
            }
            if (Role.SUPERVISOR.getAuthorityCd() > currentManager.getAuthorityCd() ) {
                Map<String,Object> supervisor_map = new HashMap<>();
                supervisor_map.put("authorityCd", Role.SUPERVISOR.getAuthorityCd());
                supervisor_map.put("authorityName", Role.SUPERVISOR.name());
                list.add(supervisor_map);
            }
            if (Role.STAFF.getAuthorityCd() > currentManager.getAuthorityCd() ) {
                Map<String,Object> staff_map = new HashMap<>();
                staff_map.put("authorityCd", Role.STAFF.getAuthorityCd());
                staff_map.put("authorityName", Role.STAFF.name());
                list.add(staff_map);
            }
        }

        return list;

    }


    /**
     * ID 중복체크
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> selectUserId(String id) {

        Map<String, Object> rtnMap = new HashMap<>();
        Boolean usableId = true;

        for(String ids : notUseIds) {
            if(id.contains(ids)) {
                usableId = false;
                break;
            }
        }

        if(usableId) {
            if (StringUtils.trimToNull(id) != null) {
                Boolean existsId =  managerRepository.existsById(id);
                if(existsId) {
                    rtnMap.put("code", "01");
                    rtnMap.put("msg", "현재 사용중인 아이디 입니다.");
                } else {
                    rtnMap.put("code", "00");
                    rtnMap.put("msg", "사용 가능한 아이디 입니다.");
                }
            } else {
                rtnMap.put("code", "02");
                rtnMap.put("msg", "아이디를 입력해주세요.");
            }
        } else {
            rtnMap.put("code", "01");
            rtnMap.put("msg", "사용할수 없는 아이디 입니다.");
        }
        return rtnMap;
    }

    @Override
    public Map<String, Object> selectTelephone(String telephone) {
        Map<String, Object> rtnMap = new HashMap<>();
        if (StringUtils.trimToNull(telephone) != null) {
            Boolean existsTelephone =  managerRepository.existsByTelephone(telephone);
            if(existsTelephone) {
                rtnMap.put("code", "01");
                rtnMap.put("msg", "현재 사용중인 휴대폰번호 입니다.");
            } else {
                rtnMap.put("code", "00");
                rtnMap.put("msg", "사용 가능한 휴대폰번호 입니다.");
            }
        } else {
            rtnMap.put("code", "02");
            rtnMap.put("msg", "휴대폰번호를 입력해주세요.");
        }

        return rtnMap;
    }


    /**
     * 해당 계정이 선택한 계약을 제외한 회사의 계약 리스트 조회
     * @param companyCd
     * @param serialNumber
     * @return
     */
    @Override
    public List<ContractInfo> getRealTimeAccountConstractList(String companyCd, String[] serialNumber) {
        List<BizContract> bizCompany = bizContractRepository.findAll(Specifications.where(BizContractSpecifications.contractsInCompany(companyCd)).and(BizContractSpecifications.contractsInMaster()));

        // 담당자로 추가 예정인 계약들 remove
        if (serialNumber != null) {
            List<BizContract> serialList = new ArrayList<>();
            for(String serialNum: serialNumber) {
                BizContract bizContract = bizContractRepository.findOne(serialNum);
                serialList.add(bizContract);
            }

            bizCompany.removeAll(serialList);
        }

        return bizContractMapper.toContractInfoList(bizCompany);
    }

    /**
     * 해당 계정이 선택한 계약 리스트 조회
     * @param companyCd
     * @param serialNumber
     * @return
     */
    @Override
    public List<ContractInfo> getUserChoiceAccountConstractList(String companyCd, String[] serialNumber) {
        List<BizContract> serialList = new ArrayList<>();
        if (serialNumber != null) {
            for(String serialNum: serialNumber) {
                BizContract bizContract = bizContractRepository.findOne(serialNum);
                serialList.add(bizContract);
            }
        }

        return bizContractMapper.toContractInfoList(serialList);
    }

    /**
     * 등록 및 수정 할 경우 Validation
     * @param accountInfo
     * @param bindingResult
     * @param type
     * @return
     */
    @Override
    public BindingResult insertAccountManagerValidation(AccountInfo accountInfo, BindingResult bindingResult, String type) {

        // 기업코드 체크
        if (!companyRepository.existsByCode(accountInfo.getCompanyCd())) {
            bindingResult.addError(new FieldError("accountInfo", "companyCd", "기업코드 검색을 해주세요."));
        }

        if (!"update".equals(type)) {
            // 아이디 체크
            if (managerRepository.existsById(accountInfo.getUserId())) {
                bindingResult.addError(new FieldError("accountInfo", "userId", "아이디 중복확인을 해주세요."));
            }

            // 휴대폰번호 체크
            if (managerRepository.existsByTelephone(accountInfo.getTelephone())) {
                bindingResult.addError(new FieldError("accountInfo", "telephone", "휴대폰번호 중복확인을 해주세요."));
            }
        }

        // 상태값
        if (accountInfo.getMgrStatusCd() != 40){
            // 사용일경우
            // Supervisor, Staff 일경우 관리계약건이 있어야함
            if(accountInfo.getAuthorityCd() == 30 || accountInfo.getAuthorityCd() == 40) {
                if(accountInfo.getSerialNumber() == null){
                    bindingResult.addError(new FieldError("accountInfo", "serialNumber", "관리 계약건을 선택 해주세요."));
                }
            }
        } else {
            // 정지일경우
            // Supervisor, Staff 일경우 관리계약건이 없어야함
            if(accountInfo.getAuthorityCd() == 30 || accountInfo.getAuthorityCd() == 40) {
                if(accountInfo.getSerialNumber() !=null) {
                    bindingResult.addError(new FieldError("accountInfo", "mgrStatusCd", "상태가 정지인 경우 관리 계약을 추가 할수 없습니다."));
                }
            }
        }

        return bindingResult;
    }

    /**
     * 등록 프로세스
     * @param accountInfo
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public AccountInfo insertAccountManager(AccountInfo accountInfo) {
        Manager manager = new Manager();

        //패스워드 초기화 여부 - true 일 경우 로그인시 패스워드 강제 변경 필요
        manager.setPasswordInitialized(true);

        String ramdomPassWord = RandomStringUtils.randomAlphanumeric(10);
        log.debug("ramdomPassWord : " + ramdomPassWord);

        // SMS 발송
        String title = "GiGA Iot 홈 Biz 서비스";
        String message = "[GiGA Iot 홈 Biz 서비스] \n" +
                "계정이 신규 등록되었습니다. 발급된 임시비밀번호로 로그인 해주세요. [임시비밀번호 : " + ramdomPassWord + "]";

        Optional<String> transactionId = smsService.sendMessage("100", accountInfo.getTelephone()
                                                                                  .replaceAll("-", ""), title, message);
        log.debug("Response : {}", transactionId);
        log.debug("SMS message : " + message);

        if(transactionId.isPresent()) {
            manager.setPasswd(standardPasswordEncoder.encode(ramdomPassWord));

            Company company = new Company();
            company.setCode(accountInfo.getCompanyCd());

            manager.setCompany(company);
            manager.setId(accountInfo.getUserId());
            manager.setName(accountInfo.getName());
            manager.setTelephone(accountInfo.getTelephone());
            manager.setEmail(accountInfo.getEmail());
            manager.setRole(Role.getRole(accountInfo.getAuthorityCd()));
            manager.setStatus(Manager.Status.getStatus(accountInfo.getMgrStatusCd()));

            manager.setBizContracts(setSerialNumberAccount(accountInfo));
            Manager saveMgr = managerRepository.save(manager);

            return accountManagerMapper.toAccountInfo(saveMgr);
        } else {
            return null;
        }
    }

    /**
     * 관리 계약건 set
     * @param accountInfo
     * @return
     */
    private Set<BizContract> setSerialNumberAccount(AccountInfo accountInfo) {
        Set<BizContract> bizContracts = new HashSet<>();

        // 마스터일 경우 회사에 속해있는 모든 계약 Mapping
        if(Role.MASTER.getAuthorityCd() == accountInfo.getAuthorityCd()) {
            bizContracts =  bizContractRepository.findByCompanyCode(accountInfo.getCompanyCd(), new Sort("created"));
        } else if (Role.SUPERVISOR.getAuthorityCd() == accountInfo.getAuthorityCd() || Role.STAFF.getAuthorityCd() == accountInfo.getAuthorityCd()) {
            if (accountInfo.getSerialNumber() != null) {
                for(String serialNumber : accountInfo.getSerialNumber()) {
                    BizContract bizContract = new BizContract();
                    bizContract.setSerialNumber(serialNumber);
                    bizContracts.add(bizContract);
                }
            }
        }
        return bizContracts;
    }

    /**
     * 상세 리스트관련 정보 조회
     * @param mgrSeq
     * @param companyCd
     * @param currentManager
     * @return
     */
    @Override
    @PrivacyAuditHistoric(menuPath = "계정관리 > 계정관리 > 계정상세", unmasked = true, downloaded = false, returnClass = Map.class, targetInfo = "아이디, 이름, 휴대폰번호")
    public Map<String, Object> getUnmaskedAccountManagerDetail(Long mgrSeq, String companyCd, B2bManager currentManager) {
        Map<String, Object> rtnMap = new HashMap<>();
        Manager manager = managerRepository.findOne(mgrSeq);
        rtnMap.put("accountInfo", accountManagerMapper.toAccountInfo(manager));
        rtnMap.put("contract", bizContractMapper.toContractInfoList(new ArrayList<>(manager.getBizContracts())));
        rtnMap.put("bizContract", selectAccountConstractInfo(manager.getCompany().getCode(), String.valueOf(mgrSeq), manager.getAuthorityCd()));
        rtnMap.put("authorityCd", getAuthorityList(companyCd, currentManager));


        return rtnMap;
    }


    /**
     * 해당 회사에 속한 계약중
     * 해당 mgrSeq가 포함되지 않은 계약 조회
     * @param companyCd 회사코드
     * @param mgrSeq    회원일련번호
     * @return
     */
    public List<ContractInfo> selectAccountConstractInfo(String companyCd, String mgrSeq, Short authorityCd) {

        Specifications specifications;
        // SuperMaster일때
        if (authorityCd == Role.SUPER_MASTER.getAuthorityCd()) {
            specifications = Specifications.where(BizContractSpecifications.contractsInCompany(companyCd));
        }
        // mgrSeq가 Master일때 -> 계약건에 Mater담당이 없는 계약만
        else if ( authorityCd == Role.MASTER.getAuthorityCd() ){
            specifications = Specifications.where(BizContractSpecifications.contractsInCompany(companyCd)).and(BizContractSpecifications.contractsNotInMaster());
        }
        // mgrSeq가 Supervisor, Staff일때 -> 계약건에 Mater담당이 있는 계약만
        else {
            specifications = Specifications.where(BizContractSpecifications.contractsInCompany(companyCd)).and(BizContractSpecifications.contractsInMaster());
        }



        List<BizContract> bizCompany = bizContractRepository.findAll(specifications);
        if ( StringUtils.trimToNull(mgrSeq) != null){
            List<BizContract> bizManager = bizContractRepository.findAll(BizContractSpecifications.contractsInManagers(Long.valueOf(mgrSeq)));
            bizCompany.removeAll(bizManager);
        }

        return bizContractMapper.toContractInfoList(bizCompany);
    }


    /**
     * 수정 프로세스
     * @param accountInfo
     */
    @Override
    public void updateAccountManager(AccountInfo accountInfo) {

        Manager manager = managerRepository.findOne(accountInfo.getMgrSeq());
        manager.setName(accountInfo.getName());
        manager.setEmail(accountInfo.getEmail());
        manager.setRole(Role.getRole(accountInfo.getAuthorityCd()));
        manager.setStatus(Manager.Status.getStatus(accountInfo.getMgrStatusCd()));

        smsService.sendMessage("100", manager.getTelephone().replaceAll("-", ""), "GiGA Iot 홈 Biz 서비스", "계정이 수정되었습니다.");

        if (Role.SUPERVISOR.getAuthorityCd() == accountInfo.getAuthorityCd() || Role.STAFF.getAuthorityCd() == accountInfo.getAuthorityCd()) {
            manager.setBizContracts(setSerialNumberAccount(accountInfo));
        }

        managerRepository.save(manager);

    }

    /**
     * 해당계약의 담당자 리스트 조회
     * @param serialNumber
     * @return
     */
    @Override
    public Map<String, Object> getContractManagerlist(String serialNumber, Pageable pageable, String status, String column, String searchWord) {

        Specifications specifications = Specifications.where(ManagerSpecifications.contractAccountMaster(serialNumber));

        // 검색 조건
        specifications = getSearchAccount(status, column, searchWord, specifications);

        Page<Manager> pageList = managerRepository.findAll(specifications, pageable);
        List<B2bManager> b2bManagers = accountManagerMapper.toB2bManagerList(pageList.getContent());

        Map<String, Object> map = new HashMap<>();

        map.put("list", b2bManagers);
        map.put("page", pageList);

        return map;
    }

    @Override
    public Map<String, Object> getContractManagerAddlist(B2bManager manager, String serialNumber, String companyCd, Pageable pageable, String status, String column, String searchWord) {

        Specifications specifications = Specifications.where(ManagerSpecifications.managerInCompany(companyCd))
                                                      .and(ManagerSpecifications.managerInRoleGreaterThan(manager.getAuthorityCd()))
                                                      .and(ManagerSpecifications.contractAccountNotMaster(serialNumber));

        // 검색 조건
        specifications = getSearchAccount(status, column, searchWord, specifications);
        Page<Manager> pageList = managerRepository.findAll(specifications, pageable);
        List<B2bManager> b2bManagers = accountManagerMapper.toB2bManagerList(pageList.getContent());

        Map<String, Object> map = new HashMap<>();
        map.put("list", b2bManagers);
        map.put("page", pageList);

        return map;
    }

    @Override
    public Boolean existsMasterInCompany(String companyCd) {

        List<Short> authorityCds = new ArrayList<>();
        authorityCds.add(Role.MASTER.getAuthorityCd());
        Specifications specifications = Specifications.where(ManagerSpecifications.managerInCompany(companyCd))
                .and(ManagerSpecifications.managerInAuthorityCds(authorityCds));

        long count = managerRepository.count(specifications);

        return (count > 0L);
    }
}
