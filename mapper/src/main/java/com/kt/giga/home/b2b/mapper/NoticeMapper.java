package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.NoticeInfo;
import com.kt.giga.home.b2b.entity.Manager;
import com.kt.giga.home.b2b.entity.Notice;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Created by DaDa on 2017-03-10.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
@DecoratedWith(NoticeMapperDecorator.class)
public interface NoticeMapper {

    List<NoticeInfo> toNoticeInfoList(List<Notice> notices);

    @Mapping(target = "noticeSeq", source = "sequence")
    NoticeInfo toNoticeInfo(Notice notice);

    B2bManager toB2bManager(Manager manager);

}
