package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.entity.Company;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created by junypooh on 2017-02-28.
 * <pre>
 * com.kt.giga.home.b2b.repository.CompanySpecifications
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-28 오후 3:58
 */
public class CompanySpecifications {

    public static Specification<Company> companyDefault() {
        return (root, query, cb) -> null;
    }

    /**
     * 기업명 조건
     * @param name
     * @return
     */
    public static Specification<Company> likeName(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
    }

    /**
     * 기업코드 조건
     * @param code
     * @return
     */
    public static Specification<Company> likeCode(String code) {
        return (root, query, cb) -> cb.like(root.get("code"), "%" + code + "%");
    }
}
