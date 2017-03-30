package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.entity.BizContract;
import com.kt.giga.home.b2b.entity.Manager;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Created by DaDa on 2017-02-16.
 */
public class BizContractSpecifications {


    public static Specification<BizContract> contractsInIsEmptyInCharge() {
        return (root, query, cb) -> {
            root.join("managers", JoinType.LEFT);
            return cb.isEmpty(root.get("managers"));
        };
    }

    public static Specification<BizContract> contractsInManagers(Long mgrSeq) {
        return (root, query, cb) -> {
            query.distinct(true);
            Subquery<Manager> managerSubquery = query.subquery(Manager.class);
            Root<Manager> manager = managerSubquery.from(Manager.class);
            Expression<Collection<BizContract>> contractManager = manager.get("bizContracts");
            managerSubquery.select(manager);
            managerSubquery.where(cb.equal(manager.get("sequence"), mgrSeq), cb.isMember(root, contractManager));
            return cb.exists(managerSubquery);
        };
    }

    public static Specification<BizContract> contractsInCompany(String companyCd) {
        return (root, query, cb) -> cb.equal(root.get("company").get("code"), companyCd);
    }

    public static Specification<BizContract> contractsInLikeCustom(String column, String searchWord) {
        return (root, query, cb) -> cb.like(root.get(column), "%"+searchWord+"%");
    }

    public static Specification<BizContract> contractsInEqualManagerName(String searchWord) {
        return (root, query, cb) -> {
            query.distinct(true);
            Subquery<Manager> managerSubquery = query.subquery(Manager.class);
            Root<Manager> manager = managerSubquery.from(Manager.class);
            Expression<Collection<BizContract>> contractManager = manager.get("bizContracts");
            managerSubquery.select(manager);
//            managerSubquery.where(cb.like(manager.get("name"), "%"+searchWord+"%"), cb.isMember(bizContract, contractManager));
            managerSubquery.where(cb.equal(manager.get("name"), searchWord), cb.isMember(root, contractManager));
            return cb.exists(managerSubquery);
        };
    }

    public static Specification<BizContract> contractsPrev() {
        return (root, query, cb) -> cb.greaterThan(root.get("startDate"), Date.valueOf(LocalDateTime.now().toLocalDate()));
    }

    public static Specification<BizContract> contractsInUse() {
        return (root, query, cb) -> cb.and(
                cb.lessThanOrEqualTo(root.get("startDate"), Date.valueOf(LocalDateTime.now().toLocalDate())),
                cb.greaterThanOrEqualTo(root.get("endDate"), Date.valueOf(LocalDateTime.now().toLocalDate()))
        );
    }

    public static Specification<BizContract> contractsCancel() {
        return (root, query, cb) -> cb.and(
                cb.lessThanOrEqualTo(root.get("startDate"), Date.valueOf(LocalDateTime.now().toLocalDate())),
                cb.lessThan(root.get("endDate"), Date.valueOf(LocalDateTime.now().toLocalDate()))
        );
    }
    public static Specification<BizContract> contractDefault() {
        return (root, query, cb) -> null;
    }

    public static Specification<BizContract> contractsInMaster() {
        return (root, query, cb) -> {
            query.distinct(true);
            Subquery<Manager> managerSubquery = query.subquery(Manager.class);
            Root<Manager> manager = managerSubquery.from(Manager.class);
            Expression<Collection<BizContract>> contractManager = manager.get("bizContracts");
            managerSubquery.select(manager);
            managerSubquery.where(cb.equal(manager.get("role"), 20), cb.isMember(root, contractManager));
            return cb.exists(managerSubquery);
        };
    }

    public static Specification<BizContract> contractsNotInMaster() {
        return (root, query, cb) -> {
            query.distinct(true);
            Subquery<Manager> managerSubquery = query.subquery(Manager.class);
            Root<Manager> manager = managerSubquery.from(Manager.class);
            Expression<Collection<BizContract>> contractManager = manager.get("bizContracts");
            managerSubquery.select(manager);
            managerSubquery.where(cb.equal(manager.get("role"), 20), cb.isMember(root, contractManager));

            return cb.not(cb.exists(managerSubquery));
        };
    }
}
