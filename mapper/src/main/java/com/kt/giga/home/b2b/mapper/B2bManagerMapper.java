package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.FindAccountInfo;
import com.kt.giga.home.b2b.entity.Manager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * Created by DaDa on 2017-01-23.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface B2bManagerMapper {

    @Mappings({
            @Mapping(target = "mgrSeq", source = "sequence"),
            @Mapping(target = "password", source = "passwd"),
            @Mapping(target = "username", source = "id"),
            @Mapping(target = "companyCd", source = "company.code"),
            @Mapping(target = "companyName", source = "company.name")
    })
    B2bManager toB2bManager(Manager manager);

    B2bManager toB2bManagerFromFindAccountInfo(FindAccountInfo findAccountInfo);

    @Mappings({
            @Mapping(target = "mgrSeq", source = "sequence")
    })
    FindAccountInfo toFindAccountInfoFromManager(Manager manager);
}
