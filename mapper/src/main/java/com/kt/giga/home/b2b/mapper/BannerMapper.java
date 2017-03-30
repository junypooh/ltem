package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.BannerInfo;
import com.kt.giga.home.b2b.entity.Banner;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Created by DaDa on 2017-03-21.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
@DecoratedWith(BannerMapperDecorator.class)
public interface BannerMapper {


    List<BannerInfo> toBannerInfoList(List<Banner> banners);

    @Mapping(target = "bannerSeq", source = "sequence")
    @Mapping(target = "oriFileName", source = "banner.fileManager.oriFileName")
    @Mapping(target = "fileSeq", source = "banner.fileManager.fileSeq")
    @Mapping(target = "categoryCd", expression = "java(com.kt.giga.home.b2b.entity.Banner.Category.getCategory(banner.getCategoryCd()).getCategoryCd())")
    @Mapping(target = "categoryName", expression = "java(com.kt.giga.home.b2b.entity.Banner.Category.getCategory(banner.getCategoryCd()).getDescription())")
    BannerInfo toBannerInfo(Banner banner);


}
