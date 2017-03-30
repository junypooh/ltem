package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.entity.BizContract;
import com.kt.giga.home.b2b.entity.Manager;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * <pre>
 * com.kt.giga.home.b2b.repository
 *      CompanyRepository
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2017-01-12 오후 4:41
 */
public interface BizContractRepository extends JpaRepository<BizContract, String> , JpaSpecificationExecutor {

    @Modifying
    @Query(value = "DELETE FROM ltem.mgr_biz_contract_rel WHERE mgr_seq = :#{#manager.sequence} AND biz_cont_serial = :#{#bizContract.serialNumber}", nativeQuery = true)
    void removeManagerInCharge(@Param("bizContract") BizContract bizContract, @Param("manager") Manager manager);

    /**
     * 해당 계약의 담당자 관계 All Delete
     * @param bizContract
     */
    @Modifying
    @Query(value = "DELETE FROM ltem.mgr_biz_contract_rel WHERE biz_cont_serial = :#{#bizContract.serialNumber}", nativeQuery = true)
    void removeAllManagerInCharge(@Param("bizContract") BizContract bizContract);

    /**
     * 관리자 계약 관계 등록
     * @param bizContract
     * @param manager
     * @return
     */
    @Modifying
    @Query(value = "INSERT INTO ltem.mgr_biz_contract_rel VALUES(:#{#bizContract.serialNumber}, :#{#manager.sequence})", nativeQuery = true)
    void insertManagerInCharge(@Param("bizContract") BizContract bizContract, @Param("manager") Manager manager);

    /**
     * 계약번호에 해당하는 계약정보 조회
     * @param serialNumber
     * @return
     */
    BizContract findBySerialNumber(String serialNumber);

    /**
     * 계약번호 + 담당자로 계약정보 조회
     * @param serialNumber
     * @param managers
     * @return
     */
    BizContract findBySerialNumberAndManagers(String serialNumber, Set<Manager> managers);

    @Query("SELECT CASE WHEN COUNT(C) > 0 THEN true ELSE false END FROM BizContract C JOIN C.managers M WHERE C.serialNumber = :contractNumber AND M.sequence = :#{#security.principal.mgrSeq}")
    Boolean isInChargeOf(@Param("contractNumber") String contractNumber);

    @Query("SELECT b FROM BizContract b WHERE b.company.code = ?1 ")
    Set<BizContract> findByCompanyCode(String companyCode, Sort sort);

    @Query("SELECT count(b) FROM BizContract b WHERE b.company.code = ?1 ")
    long countByCompanyCode(String companyCode);

    /**
     * 담당자가 속해져있는 계약정보 조회
     * @param managers
     * @return
     */
    List<BizContract> findByManagers(Manager managers, Sort sort);

    /**
     * 담당자가 속해져있는 계약정보 갯수 조회
     * @param managers
     * @return
     */
    long countByManagers(Manager managers);

    /**
     * 슈퍼마스터용 계약건수
     * @return
     */
    @Query(value = "SELECT count(m) FROM ltem.biz_contract m WHERE to_date(to_char(now(), 'yyyymmdd'), 'yyyymmdd') BETWEEN start_date AND end_date", nativeQuery = true)
    long countByContractsSuperMaster();
}
