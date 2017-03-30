package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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
public interface CompanyRepository extends JpaRepository<Company, String>, JpaSpecificationExecutor<Company> {

    /**
     * 기업 코드로 조회
     * @param code
     * @return
     */
    Company findByCode(String code);

    /**
     * 기업코드 존재 유무
     * @param code
     * @return
     */
    Boolean existsByCode(String code);

    /**
     * 기업 이름이 없는 기업 수
     * @return
     */
    long countByNameIsNull();
}
