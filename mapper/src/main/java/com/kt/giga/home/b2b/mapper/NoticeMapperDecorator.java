package com.kt.giga.home.b2b.mapper;


import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.NoticeInfo;
import com.kt.giga.home.b2b.entity.Manager;
import com.kt.giga.home.b2b.entity.Notice;
import com.kt.giga.home.b2b.service.IMaskingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DaDa on 2017-03-13.
 */
public abstract class NoticeMapperDecorator implements NoticeMapper {

    @Autowired
    private IMaskingService iMaskingService;

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public List<NoticeInfo> toNoticeInfoList(List<Notice> notices) {

        if(notices == null) {
            return null;
        }

        List<NoticeInfo> list = new ArrayList<>();
        notices.forEach(notice -> list.add(this.toNoticeInfo(notice)));
        return list;
    }

    @Override
    public NoticeInfo toNoticeInfo(Notice notice) {

        if(notice == null) {
            return null;
        }
        NoticeInfo noticeInfo = noticeMapper.toNoticeInfo(notice);

        noticeInfo.setManager(this.toB2bManager(notice.getManager()));

        return noticeInfo;
    }

    @Override
    public B2bManager toB2bManager(Manager manager) {

        if(manager == null) {
            return null;
        }

        B2bManager b2bManager = noticeMapper.toB2bManager(manager);
        if(StringUtils.isNotBlank(manager.getId())) {
            b2bManager.setUsername(iMaskingService.maskFromEnd(manager.getId(), 3));
        }

        return b2bManager;
    }
}
