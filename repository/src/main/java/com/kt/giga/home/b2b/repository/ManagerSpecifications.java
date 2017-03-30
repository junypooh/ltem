package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.entity.BizContract;
import com.kt.giga.home.b2b.entity.Manager;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Collection;
import java.util.List;

/**
 * Created by DaDa on 2017-02-23.
 */
public class ManagerSpecifications {

    public static Specification<Manager> managerDefault() {
        return (root, query, cb) -> null;
    }

    /**
     * 자신이 속한 회사의 Manager
     * @param companyCd
     * @return
     */
    public static Specification<Manager> managerInCompany(String companyCd) {
        return (root, query, cb) -> cb.equal(root.get("company").get("code"), companyCd);
    }

    /**
     * 자신의 권한보다 낮거나 같은 권한을 가진 Manager
     * @param authorityCd
     * @return
     */
    public static Specification<Manager> managerInRoleGreaterThanEqual(Short authorityCd) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("role"), authorityCd);
    }

    /**
     * 자신의 권한보다 낮은 권한을 가진 Manager
     * @param authorityCd
     * @return
     */
    public static Specification<Manager> managerInRoleGreaterThan(Short authorityCd) {
        return (root, query, cb) -> cb.greaterThan(root.get("role"), authorityCd);
    }

    /**
     * 컬럼 Custom Equal 조건
     * @param key
     * @param value
     * @return
     */
    public static Specification<Manager> managerInEqualCustom(String key, String value) {
        return (root, query, cb) -> cb.equal(root.get(key), value);
    }

    /**
     * 권한 In 검색
     * @param authorityCds
     * @return
     */
    public static Specification<Manager> managerInAuthorityCds(List<Short> authorityCds) {
        return (root, query, cb) -> root.get("role").in(authorityCds);
    }

    /**
     * 해당계약에 속한 Managers 조회
     * @param serialNumber
     * @return
     */
    public static Specification<Manager> contractAccountMaster(String serialNumber) {
        return (root, query, cb) -> {

            Subquery<BizContract> bizContractSubquery = query.subquery(BizContract.class);
            Root<BizContract> bizContract = bizContractSubquery.from(BizContract.class);
            Expression<Collection<Manager>> managerContract = bizContract.get("managers");
            bizContractSubquery.select(bizContract);
            bizContractSubquery.where(cb.equal(bizContract.get("serialNumber"), serialNumber), cb.isMember(root, managerContract));

            return cb.exists(bizContractSubquery);
        };
    }

    /**
     * 해당계약에 속하지않은 Managers 조회
     * @param serialNumber
     * @return
     */
    public static Specification<Manager> contractAccountNotMaster(String serialNumber) {
        return (root, query, cb) -> {

            Subquery<BizContract> bizContractSubquery = query.subquery(BizContract.class);
            Root<BizContract> bizContract = bizContractSubquery.from(BizContract.class);
            Expression<Collection<Manager>> managerContract = bizContract.get("managers");
            bizContractSubquery.select(bizContract);
            bizContractSubquery.where(cb.equal(bizContract.get("serialNumber"), serialNumber), cb.isMember(root, managerContract));

            return cb.not(cb.exists(bizContractSubquery));
        };
    }


}
