package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.BannerInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by DaDa on 2017-03-21.
 */
public interface IBannerService {

    /**
     * 배너 저장
     * @param bannerInfo
     */
    void saveBanner(BannerInfo bannerInfo);

    /**
     * 배너 리스트 조회
     * @param pageable
     * @return
     */
    Map<String, Object> getBannerList(Pageable pageable);

    /**
     * 배너 삭제
     * @param bannerSeq
     */
    void deleteBanner(Integer[] bannerSeq);

    /**
     * 배너 상세 조회
     * @param bannerSeq
     * @return
     */
    BannerInfo getBannerDetail(Integer bannerSeq);

    /**
     * 배너 상세 조회
     * Masking 해제 용
     * @param bannerSeq
     * @return
     */
    BannerInfo getBannerDetailUnMask(int bannerSeq);

    /**
     * 노출, 카테고리 조건
     * @param categoryCd
     * @return
     */
    List<BannerInfo> selectCurrentTrueAndCategory(Short categoryCd);


}
