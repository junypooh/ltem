package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.entity.ServiceContract;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created by junypooh on 2017-02-24.
 * <pre>
 * com.kt.giga.home.b2b.repository.ServiceContractSpecifications
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-24 오후 2:00
 */
public class ServiceContractSpecifications {

    /**
     * 계약번호 조건
     * @param bizContSerial
     * @return
     */
    public static Specification<ServiceContract> equalsBizContSerial(String bizContSerial) {
        return (root, query, cb) -> cb.equal(root.get("currentBizContract").get("serialNumber"), bizContSerial);
    }

    /**
     * 서비스계약번호 조건
     * @param svcContSerial
     * @return
     */
    public static Specification<ServiceContract> equalsSvcContSerial(Long svcContSerial) {
        return (root, query, cb) -> cb.equal(root.get("serialNumber"), svcContSerial);
    }

    /**
     * 사용자명 조건
     * @param userName
     * @return
     */
    public static Specification<ServiceContract> equalsUserName(String userName) {
        return (root, query, cb) -> cb.equal(root.get("userName"), userName);
    }

    /**
     * 휴대폰번호 조건
     * @param userCtn
     * @return
     */
    public static Specification<ServiceContract> equalsCtn(String userCtn) {
        return (root, query, cb) -> cb.equal(root.get("userCtn"), userCtn);
    }

    /**
     * 유심일련번호 조건
     * @param iccId
     * @return
     */
    public static Specification<ServiceContract> equalsIccId(String iccId) {
        return (root, query, cb) -> cb.equal(root.get("iccId"), iccId);
    }

    /**
     * 단말일련번호 조건
     * @param imei
     * @return
     */
    public static Specification<ServiceContract> equalsImei(String imei) {
        return (root, query, cb) -> cb.equal(root.get("imei"), imei);
    }
}
