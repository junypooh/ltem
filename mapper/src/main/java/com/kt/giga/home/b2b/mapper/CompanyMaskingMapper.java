package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.CompanyInfo;
import com.kt.giga.home.b2b.entity.Company;
import com.kt.giga.home.b2b.entity.Manager;
import org.mapstruct.*;

import java.util.List;

/**
 * Created by junypooh on 2017-02-28.
 * <pre>
 * com.kt.giga.home.b2b.mapper.CompanyMapper
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-28 오후 4:08
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
@DecoratedWith(CompanyMaskingMapperDecorator.class)
public interface CompanyMaskingMapper {

    @Mappings({
            @Mapping(target = "telephone", ignore = true),
            @Mapping(target = "fileSeq", expression = "java( com.kt.giga.home.b2b.entity.Company.getFileSeq( company.getFileManager() ) )"),
    })
    CompanyInfo toCompanyInfo(Company company);

    @Mappings({
            @Mapping(target = "mgrSeq", source = "sequence"),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "username", ignore = true)
    })
    B2bManager toB2bManager(Manager manager);

    List<CompanyInfo> toCompanyInfoList(List<Company> companies);
}
