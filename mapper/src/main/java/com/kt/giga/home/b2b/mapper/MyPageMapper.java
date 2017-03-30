package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.MyPageInfo;
import com.kt.giga.home.b2b.entity.Manager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Created by DaDa on 2017-01-25.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface MyPageMapper {

    @Mapping(target = "companyName", source = "company.name")
    MyPageInfo toMyPageResInfo(Manager manager);

}
