package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.entity.Banner;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by DaDa on 2017-03-21.
 */
public interface BannerRepository extends JpaRepository<Banner, Integer> {

    /**
     * 노출, 카테고리별 순서 조건
     * @param categoryCd
     * @param order
     * @return
     */
    List<Banner> findByCurrentTrueAndCategoryAndOrder(Short categoryCd, Short order);

    /**
     * 배너 삭제
     * @param bannerSeq
     * @return
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Banner b WHERE b.sequence IN :#{#bannerSeq}")
    int deleteBanner(@Param("bannerSeq") List<Integer> bannerSeq);

    /**
     * 노출, 카테고리별 조건
     * @param categoryCd
     * @param sort
     * @return
     */
    List<Banner> findByCurrentTrueAndCategory(Short categoryCd, Sort sort);
}
