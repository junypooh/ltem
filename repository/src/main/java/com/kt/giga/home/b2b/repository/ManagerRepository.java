package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.domain.Role;
import com.kt.giga.home.b2b.entity.Company;
import com.kt.giga.home.b2b.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * <pre>
 * com.kt.giga.home.b2b.repository
 *      ManagerRepository
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2017-01-12 오후 4:23
 */
public interface ManagerRepository extends JpaRepository<Manager, Long>, JpaSpecificationExecutor {

    Manager findById(String userId);

    Manager findByIdAndStatus(String userId, short status);

    Manager findBySequence(Long mgrSeq);

    boolean existsById(String userId);

    boolean existsByTelephone(String telephone);

    @Query("SELECT m FROM Manager m WHERE m.role = :#{#role.authorityCd}")
    List<Manager> findByRole(@Param("role") Role role);

    @Query("SELECT m FROM Manager m WHERE m.company.code = :#{#security.principal.companyCd} AND m.role > :#{#security.principal.role.authorityCd}")
    Set<Manager> findManagersInSameCompanyAndLowerRolesThanCurrentUser();

    @Query("SELECT m FROM Manager m WHERE m.company.code = :#{#security.principal.companyCd} AND m.role > :#{#security.principal.role.authorityCd} AND m.name LIKE '%' || :#{#name} || '%'")
    Set<Manager> findManagersInSameCompanyAndLowerRolesThanCurrentUserAndNameContaining(@Param("name") String name);

    @Query("SELECT m FROM Manager m WHERE m.role > :#{#security.principal.role.authorityCd}")
    Set<Manager> findManagersInLowerRolesThanCurrentUser();

    @Query("SELECT m FROM Manager m WHERE m.role > :#{#security.principal.role.authorityCd} AND m.name LIKE '%' || :#{#name} || '%'")
    Set<Manager> findManagersInLowerRolesThanCurrentUserAndNameContaining(@Param("name") String name);

    /**
     * 이름, 휴대폰 번호로 검색
     *
     * @param name
     * @return
     */
    Manager findOneByNameAndTelephone(String name, String telephone);

    /**
     * 이름, 휴대폰 번호, 인증번호 로 검색
     *
     * @param name
     * @return
     */
    Manager findOneByNameAndTelephoneAndAuthenticationNumber(String name, String telephone, String authenticationNumber);

    /**
     * 회원일련번호, 인증번호로 검색
     * @param sequence
     * @param authenticationNumber
     * @return
     */
    Manager findBySequenceAndAuthenticationNumber(Long sequence, String authenticationNumber);

    // 약관 내용 저장.
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO ltem.terms_agreed VALUES(:#{#termSeq}, :#{#mngSeq}, now(), null)", nativeQuery = true)
    int saveTermAgreed(@Param("termSeq") int term, @Param("mngSeq") long mng);

    // 약관 내용 삭제
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ltem.terms_agreed WHERE term_seq IN (?1)", nativeQuery = true)
    int deleteTermsAgreed(List<Integer> delTermList);
/*
    // 관리자에 요청된 승인 및 발급 건수
    Long countByStatus(short status);

    // 관리자 이름으로 부분 검색.
    List<Manager> findByNameLike(String userName);

    // 관리자 전화번호로 검색
    List<Manager> findByTelephone(String telephone);*/

    /**
     * 회사코드로 조회
     *
     * @param company
     * @param authorityCd
     * @return
     */
    @Query("SELECT m FROM Manager m WHERE m.company = ?1 AND m.role IN (?2)")
    List<Manager> findByCompanyAndRoles(Company company, List<Short> authorityCd);

    /**
     * 지정한 등급보다 등급이 낮은 관리자 조회
     *
     * @param companyCode 회사 코드
     * @param role        등급
     * @return
     */
    @Query("SELECT m FROM Manager m WHERE m.company.code = :#{#companyCode} AND m.role > :#{#role.authorityCd}")
    Set<Manager> findByCompanyAndRoleLessThan(@Param("companyCode") String companyCode, @Param("role") Role role);

    @Query("SELECT m FROM Manager m WHERE m.role < :#{#role.authorityCd}")
    List<Manager> findByRoleLessThan(@Param("role") Role role);

    /**
     * 지정한 등급보다 등급이 낮은 관리자 중 회사코드와 이름으로 담당자 리스트 조회
     *
     * @param companyCode 회사 코드
     * @param role        등급
     * @param name        관리자 이름
     * @return
     */
    @Query("SELECT m FROM Manager m WHERE m.company.code = :#{#companyCode} AND m.role > :#{#role.authorityCd} AND m.name LIKE concat('%',:#{#name},'%') ")
    Set<Manager> findByCompanyAndRoleLessThanAndNameContaining(@Param("companyCode") String companyCode, @Param("role") Role role, @Param("name") String name);

    /**
     * 선택된 관리자들 중 해당 권한과 같거나 높은 관리자 Count
     * @param mgrSeq
     * @param role
     * @return
     */
    @Query("SELECT count(m) FROM Manager m WHERE m.sequence IN :#{#mgrSeq} AND m.role <= :#{#role.authorityCd}")
    int countBySequenceInAndRoleLessThanEqual(@Param("mgrSeq") List<Long> mgrSeq, @Param("role") Role role);


    /**
     * 회사에 속해있는 담당자 권한 Count
     * @param companyCode
     * @param role
     * @return
     */
    @Query("SELECT count(m) FROM Manager m WHERE m.company.code = :#{#companyCode} AND m.role = :#{#role.authorityCd}")
    int countByCompanyAndRole(@Param("companyCode") String companyCode, @Param("role") Role role);

    /**
     * 계정 삭제
     * @param mgrSeq
     */
    @Modifying
    @Query("DELETE FROM Manager m WHERE m.sequence IN :#{#mgrSeq}")
    void deleteBySequenceIn(@Param("mgrSeq") List<Long> mgrSeq);

    /**
     * 특인 아이디로 생성한 계정 수
     * @param createdBy
     * @return
     */
    long countByCreatedBy(String createdBy);

    /**
     * 특인 아이디로 생성한 특정 권한 계정 수
     * @param createdBy
     * @return
     */
    long countByCreatedByAndRole(String createdBy, short authorityCd);



    List<Manager> findBySequenceIn(List<Long> mgrSeqs);


}
