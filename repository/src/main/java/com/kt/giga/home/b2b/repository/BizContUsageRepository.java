package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.entity.BizContUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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
public interface BizContUsageRepository extends JpaRepository<BizContUsage, BizContUsage.BizContUsagePK>, JpaSpecificationExecutor {

    List<BizContUsage> findBySerialNumberAndYearOrderByMonthAsc(String bizContSerial, short  year);

    List<BizContUsage> findBySerialNumberOrderByYearDescMonthDesc(String bizContSerial);

    List<BizContUsage> findBySerialNumberAndYearAndMonthInOrderByMonthAsc(String bizContSerial, short year, List<Short> months);

    /**
     * 메인 상단 통계용 Native Query
     * @return
     */
    @Query(value = "select u.biz_cont_serial, u.capacity, u.usage, u.capacity_unit_cd, 0 as a, 0 as b, c.company_name\n" +
            "from\t(\n" +
            "\tselect *  \n" +
            "\tfrom\t(\n" +
            "\t\t\tselect biz_cont_serial, year, month, capacity, usage, capacity_unit_cd , row_number() over (partition by biz_cont_serial order by year desc, month desc) \n" +
            "\t\t\tfrom ltem.biz_cont_usage b\n" +
            "\t\t\twhere usage > capacity\n" +
            "\t\t) b where row_number = 1\n" +
            "\t)u,\n" +
            "\tltem.biz_contract b,\t\n" +
            "\tltem.company c\n" +
            "where u.biz_cont_serial = b.biz_cont_serial\n" +
            "and   b.company_cd = c.company_cd", nativeQuery = true)
    List<Object[]> findByExcessUsageSuperMaster();

    /**
     * 메인 상단 통계용 Native Query
     * @return
     */
    @Query(value = "\n" +
            "select u.biz_cont_serial, u.capacity, u.usage, u.capacity_unit_cd, 0 as a, 0 as b\n" +
            "from\t(\n" +
            "\tselect *  \n" +
            "\tfrom\t(\n" +
            "\t\t\tselect biz_cont_serial, year, month, capacity, usage, capacity_unit_cd , row_number() over (partition by biz_cont_serial order by year desc, month desc) \n" +
            "\t\t\tfrom ltem.biz_cont_usage b\n" +
            "\t\t\twhere usage > capacity\n" +
            "\t\t) b where row_number = 1\n" +
            "\t)u,\n" +
            "\tltem.biz_contract b\t\n" +
            "where u.biz_cont_serial = b.biz_cont_serial\n" +
            "and   b.company_cd = :#{#security.principal.companyCd}", nativeQuery = true)
    List<Object[]> findByExcessUsageMaster();

    /**
     * 메인 상단 통계용 Native Query
     * @return
     */
    @Query(value = "select u.biz_cont_serial, u.capacity, u.usage, u.capacity_unit_cd, 0 as a, 0 as b\n" +
            "from\t(\n" +
            "\tselect *  \n" +
            "\tfrom\t(\n" +
            "\t\t\tselect biz_cont_serial, year, month, capacity, usage, capacity_unit_cd , row_number() over (partition by biz_cont_serial order by year desc, month desc) \n" +
            "\t\t\tfrom ltem.biz_cont_usage b\n" +
            "\t\t\twhere usage > capacity\n" +
            "\t\t) b where row_number = 1\n" +
            "\t)u,\n" +
            "\tltem.biz_contract b,\n" +
            "\tltem.mgr_biz_contract_rel c\t\n" +
            "where u.biz_cont_serial = b.biz_cont_serial\n" +
            "and   b.biz_cont_serial = c.biz_cont_serial\n" +
            "and   c.mgr_seq = :#{#security.principal.mgrSeq}", nativeQuery = true)
    List<Object[]> findByExcessUsageSupervisorAndStaff();







}
