package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.TermInfo;
import com.kt.giga.home.b2b.entity.Term;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Created by DaDa on 2017-01-23.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
@DecoratedWith(TermMaskMapperDecorator.class)
public interface TermMaskMapper {

    @Mapping(target = "termSeq", source = "sequence")
    @Mapping(target = "categoryCd", expression = "java(com.kt.giga.home.b2b.entity.Term.Category.getCategory(term.getCategoryCd()).getCategoryCd())")
    @Mapping(target = "categoryName", expression = "java(com.kt.giga.home.b2b.entity.Term.Category.getCategory(term.getCategoryCd()).getDescription())")
    @Mapping(target = "active", source = "isActive")
    TermInfo toTermInfo(Term term);

    List<TermInfo> toTermInfoList(List<Term> terms);
}
