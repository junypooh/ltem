package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.TermInfo;
import com.kt.giga.home.b2b.entity.Term;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Created by junypooh on 2017-03-22.
 * <pre>
 * com.kt.giga.home.b2b.mapper.TermUnmaskMapper
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-22 오후 2:17
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TermUnmaskMapper {

    @Mapping(target = "termSeq", source = "sequence")
    @Mapping(target = "categoryCd", expression = "java(com.kt.giga.home.b2b.entity.Term.Category.getCategory(term.getCategoryCd()).getCategoryCd())")
    @Mapping(target = "categoryName", expression = "java(com.kt.giga.home.b2b.entity.Term.Category.getCategory(term.getCategoryCd()).getDescription())")
    @Mapping(target = "active", source = "isActive")
    TermInfo toTermInfo(Term term);

    List<TermInfo> toTermInfoList(List<Term> terms);
}
