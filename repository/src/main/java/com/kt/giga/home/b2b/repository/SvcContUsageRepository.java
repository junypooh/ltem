package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.entity.SvcContUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
* Created by mac on 2017. 1. 16..
*/
public interface SvcContUsageRepository extends JpaRepository<SvcContUsage, SvcContUsage.SvcContUsagePK> {

    @Query(value =
            "    select                                                                                                                                             " +
            "        a.svc_cont_serial, b.device_place, b.user_name, a.avg_usage, a.avg_usage_unit_cd, b.usage, b.usage_unit_cd, count(1)over() as tot_cnt          " +
            "    from                                                                                                                                               " +
            "    (                                                                                                                                                  " +
            "        select                                                                                                                                         " +
            "            u.svc_cont_serial, avg(u.usage) as avg_usage, max(u.usage_unit_cd) as avg_usage_unit_cd                                                    " +
            "        from ltem.svc_cont_usage u, ltem.biz_cont_svc_cont_rel r                                                                                       " +
            "        where u.svc_cont_serial = r.svc_cont_serial                                                                                                    " +
            "        and r.biz_cont_serial = :bizContSerial                                                                                                         " +
            "       group by u.svc_cont_serial                                                                                                                      " +
            "    ) a                                                                                                                                                " +
            "    , (                                                                                                                                                " +
            "        select                                                                                                                                         " +
            "        *                                                                                                                                              " +
            "        from (                                                                                                                                         " +
            "            select                                                                                                                                     " +
            "                u.svc_cont_serial, u.usage, u.usage_unit_cd, s.device_place, s.user_name                                                               " +
            "                , row_number() over(partition by u.svc_cont_serial order by u.year desc, u.month desc)                                                 " +
            "            from ltem.svc_cont_usage u, ltem.biz_cont_svc_cont_rel r, ltem.svc_contract s                                                                   " +
            "            where u.svc_cont_serial = r.svc_cont_serial                                                                                                " +
            "            and u.svc_cont_serial = s.svc_cont_serial                                                                                                  " +
            "            and r.biz_cont_serial = :bizContSerial                                                                                                     " +
            "        ) b                                                                                                                                            " +
            "        where row_number = 1                                                                                                                           " +
            "    ) b                                                                                                                                                " +
            "    where a.svc_cont_serial = b.svc_cont_serial                                                                                                        " +
            "    limit :limit offset :offset                                                                                                     ", nativeQuery = true)
    List<Object[]> findByStatisticsByHubInfos(@Param("bizContSerial") String bizContSerial, @Param("limit") int limit, @Param("offset") int offset);

    @Query(value =
            "    select                                                                                                                                             " +
            "        a.svc_cont_serial, b.device_place, b.user_name, a.avg_usage, a.avg_usage_unit_cd, b.usage, b.usage_unit_cd                                     " +
            "    from                                                                                                                                               " +
            "    (                                                                                                                                                  " +
            "        select                                                                                                                                         " +
            "            u.svc_cont_serial, avg(u.usage) as avg_usage, max(u.usage_unit_cd) as avg_usage_unit_cd                                                    " +
            "        from ltem.svc_cont_usage u, ltem.biz_cont_svc_cont_rel r                                                                                       " +
            "        where u.svc_cont_serial = r.svc_cont_serial                                                                                                    " +
            "        and r.biz_cont_serial = :bizContSerial                                                                                                         " +
            "       group by u.svc_cont_serial                                                                                                                      " +
            "    ) a                                                                                                                                                " +
            "    , (                                                                                                                                                " +
            "        select                                                                                                                                         " +
            "        *                                                                                                                                              " +
            "        from (                                                                                                                                         " +
            "            select                                                                                                                                     " +
            "                u.svc_cont_serial, u.usage, u.usage_unit_cd, s.device_place, s.user_name                                                               " +
            "                , row_number() over(partition by u.svc_cont_serial order by u.year desc, u.month desc)                                                 " +
            "            from ltem.svc_cont_usage u, ltem.biz_cont_svc_cont_rel r, ltem.svc_contract s                                                                   " +
            "            where u.svc_cont_serial = r.svc_cont_serial                                                                                                " +
            "            and u.svc_cont_serial = s.svc_cont_serial                                                                                                  " +
            "            and r.biz_cont_serial = :bizContSerial                                                                                                     " +
            "        ) b                                                                                                                                            " +
            "        where row_number = 1                                                                                                                           " +
            "    ) b                                                                                                                                                " +
            "    where a.svc_cont_serial = b.svc_cont_serial                                                                                                        " +
            "    and a.svc_cont_serial = :svcContSerial                                                                                          ", nativeQuery = true)
    List<Object[]> findByStatisticsByHubInfo(@Param("bizContSerial") String bizContSerial, @Param("svcContSerial") Long svcContSerial);

}