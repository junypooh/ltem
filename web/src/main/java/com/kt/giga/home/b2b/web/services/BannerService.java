package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.annotation.PrivacyAuditHistoric;
import com.kt.giga.home.b2b.domain.BannerInfo;
import com.kt.giga.home.b2b.entity.Banner;
import com.kt.giga.home.b2b.mapper.BannerMapper;
import com.kt.giga.home.b2b.repository.BannerRepository;
import com.kt.giga.home.b2b.repository.FileManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DaDa on 2017-03-21.
 */
@Service
public class BannerService implements IBannerService {

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private FileManagerRepository fileManagerRepository;

    @Autowired
    private BannerMapper bannerMapper;


    @Override
    public void saveBanner(BannerInfo bannerInfo) {
        Banner banner = new Banner();

        if (bannerInfo.getBannerSeq() != null) {
             banner = bannerRepository.findOne(bannerInfo.getBannerSeq());
        }
        // 노출일 경우
        if(bannerInfo.getCurrent()) {
            // 카테고리별 기존 순서에 해당하는 배너가 있는지 확인
            List<Banner> banners = bannerRepository.findByCurrentTrueAndCategoryAndOrder(bannerInfo.getCategoryCd(), bannerInfo.getOrder());

            // 기존배너가 있다면 비노출, order null update
            if (banners.size() > 0) {
                Banner existingBanner = banners.get(0);
                existingBanner.setCurrent(false);
                existingBanner.setOrder(null);
                bannerRepository.save(existingBanner);
            }

            banner.setOrder(bannerInfo.getOrder());
        } else {
            banner.setOrder(null);
        }

        banner.setCategory(Banner.Category.getCategory(bannerInfo.getCategoryCd()));
        banner.setName(bannerInfo.getName());
        banner.setLink(bannerInfo.getLink());
        banner.setFileManager(fileManagerRepository.findOne(bannerInfo.getFileSeq()));
        banner.setCurrent(bannerInfo.getCurrent());


        if (bannerInfo.getNewWindow() != null) {
            banner.setNewWindow(bannerInfo.getNewWindow());
        } else {
            banner.setNewWindow(false);
        }

        bannerRepository.save(banner);

    }

    @Override
    public Map<String, Object> getBannerList(Pageable pageable) {
        Map<String, Object> map = new HashMap<>();

        Page<Banner> page = bannerRepository.findAll(pageable);
        List<BannerInfo> bannerInfos = bannerMapper.toBannerInfoList(page.getContent());

        map.put("bannerInfoList", bannerInfos);
        map.put("page", page);

        return map;
    }

    @Override
    public void deleteBanner(Integer[] bannerSeq) {
        bannerRepository.deleteBanner(Arrays.asList(bannerSeq));
    }

    @Override
    public BannerInfo getBannerDetail(Integer bannerSeq) {
        Banner banner = bannerRepository.findOne(bannerSeq);
        BannerInfo bannerInfo = null;
        if(banner != null) {
            bannerInfo = bannerMapper.toBannerInfo(bannerRepository.findOne(bannerSeq));
        }
        return bannerInfo;
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "서비스관리 > 배너관리상세", unmasked = true, downloaded = false, returnClass = BannerInfo.class, targetInfo = "아이디")
    public BannerInfo getBannerDetailUnMask(int bannerSeq) {
        return bannerMapper.toBannerInfo(bannerRepository.findOne(bannerSeq));
    }

    @Override
    public List<BannerInfo> selectCurrentTrueAndCategory(Short categoryCd) {
        return bannerMapper.toBannerInfoList(bannerRepository.findByCurrentTrueAndCategory(categoryCd, new Sort(Sort.Direction.ASC, "order")));
    }
}
