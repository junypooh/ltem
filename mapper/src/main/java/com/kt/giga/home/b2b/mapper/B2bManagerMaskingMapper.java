package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.entity.Manager;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Created by DaDa on 2017-02-10.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(B2bManagerMaskingMapperDecorator.class)
public interface B2bManagerMaskingMapper {

    List<B2bManager> toB2bManagerList(List<Manager> managers);

    @Mapping(target = "companyCd", source = "company.code")
    @Mapping(target = "mgrSeq", source = "sequence")
    B2bManager toB2bManager(Manager manager);
}
