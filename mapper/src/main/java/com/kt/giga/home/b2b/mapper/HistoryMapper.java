package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.HistoryInfo;
import com.kt.giga.home.b2b.entity.PrivacyAuditHistory;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Created by DaDa on 2017-03-24.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
@DecoratedWith(HistoryMapperDecorator.class)
public interface HistoryMapper {

    @Mapping(target = "workHistoryDate", source = "audited")
    @Mapping(target = "workContext", source = "targetInfo")
    HistoryInfo toHistoryInfo(PrivacyAuditHistory privacyAuditHistory);
}
