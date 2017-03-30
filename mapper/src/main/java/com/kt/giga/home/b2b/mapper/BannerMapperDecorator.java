package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.BannerInfo;
import com.kt.giga.home.b2b.entity.Banner;
import com.kt.giga.home.b2b.service.IMaskingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DaDa on 2017-03-21.
 */
public abstract class BannerMapperDecorator implements BannerMapper {

    @Autowired
    private IMaskingService iMaskingService;

    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public List<BannerInfo> toBannerInfoList(List<Banner> banners) {
        if (banners == null) {
            return null;
        }
        List<BannerInfo> list = new ArrayList<>();
        banners.forEach(banner -> list.add(this.toBannerInfo(banner)));
        return list;
    }

    @Override
    public BannerInfo toBannerInfo(Banner banner) {
        BannerInfo bannerInfo = bannerMapper.toBannerInfo(banner);

        if (StringUtils.isNotBlank(bannerInfo.getCreatedBy())) {
            bannerInfo.setCreatedByMask(iMaskingService.maskFromEnd(banner.getCreatedBy(), 3));
        }

        return bannerInfo;
    }
}
