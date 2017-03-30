package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.entity.Term;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mac on 2017. 1. 16..
 */
public interface TermRepository extends JpaRepository<Term, Integer> {

    // 약관 종류 코드 조건의 리스트
    List<Term> findByCategoryEquals(short val);

    /**
     * 약관 활성화 update
     * @param category
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE ltem.terms SET is_active = false WHERE terms_cate_cd = :#{#category} ", nativeQuery = true)
    int updateIsActiveFalseToCategory(@Param("category") Short category);


    /**
     * 약관 삭제
     * @param termSeq
     * @return
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Term t WHERE t.sequence IN :#{#termSeq}")
    int deleteTerm(@Param("termSeq") List<Integer> termSeq);

    /**
     * 활성화된 약관 조회
     * @return
     */
    List<Term> findByIsActiveTrue(Sort sort);

    /**
     * 활성화된 약관 중 카테고리코드가 맞는 약관 조회
     * @return
     */
    List<Term> findByIsActiveTrueAndCategory(Short category);

    /**
     * 카테고리 조건 조회
     * @param category
     * @param sort
     * @return
     */
    List<Term> findByCategory(Short category, Sort sort);

}
