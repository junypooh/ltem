package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.NoticeInfo;
import com.kt.giga.home.b2b.entity.Notice;
import com.kt.giga.home.b2b.mapper.NoticeMapper;
import com.kt.giga.home.b2b.repository.ManagerRepository;
import com.kt.giga.home.b2b.repository.NoticeRepository;
import com.kt.giga.home.b2b.repository.NoticeSpecifications;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DaDa on 2017-03-10.
 */
@Service
@Slf4j
public class NoticeService implements INoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private ManagerRepository managerRepository;

    /**
     * 상태가 노출인 공지사항 리스트 조회
     * @param pageable
     * @return
     */
    @Override
    public Map<String, Object> selectNoticeInfoIsActiveTrue(Pageable pageable) {

        Map<String, Object> map = new HashMap<>();

        Page<Notice> page = noticeRepository.findByCategoryAndIsActiveTrue((short)10, pageable);
        List<NoticeInfo> notices = noticeMapper.toNoticeInfoList(page.getContent());

        map.put("notices", notices);
        map.put("page", page);

        return map;
    }

    /**
     * 서비스관리 공지사항 리스트 조회
     * @param pageable
     * @return
     */
    @Override
    public Map<String, Object> selectNoticeAllInfo(Short categoryCd, Pageable pageable) {
        Map<String, Object> map = new HashMap<>();

        Page<Notice> page = noticeRepository.findByCategory(categoryCd, pageable);
        List<NoticeInfo> notices = noticeMapper.toNoticeInfoList(page.getContent());

        map.put("notices", notices);
        map.put("page", page);

        return map;

    }

    @Override
    public Map<String, Object> selectNoticeListByCondition(Short categoryCd, String searchKey, String searchText, Pageable pageable) {

        Specifications<Notice> specifications = Specifications.where(NoticeSpecifications.noticeDefault()).and(NoticeSpecifications.equalsCategotyCd(categoryCd));

        if (StringUtils.isNotBlank(searchText)) {

            if("title".equals(searchKey)) {
                specifications = specifications.and(NoticeSpecifications.likeTitle(searchText));
            } else if ("content".equals(searchKey)) {
                specifications = specifications.and(NoticeSpecifications.likeContent(searchText));
            } else {
                specifications = specifications.and(Specifications.where(NoticeSpecifications.likeTitle(searchText)).or(NoticeSpecifications.likeContent(searchText)));
            }
        }

        Page<Notice> page = noticeRepository.findAll(specifications, pageable);
        List<NoticeInfo> notices = noticeMapper.toNoticeInfoList(page.getContent());

        Map<String, Object> map = new HashMap<>();
        map.put("notices", notices);
        map.put("page", page);

        return map;
    }

    /**
     * 공지사항 등록 및 수정
     * @param noticeInfo
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void insertNotice(NoticeInfo noticeInfo) {
        Notice notice = noticeRepository.findOne(noticeInfo.getNoticeSeq());
        if (notice == null) {
            notice = new Notice();
            notice.setManager(managerRepository.findOne(noticeInfo.getMgrSeq()));
        }

        notice.setCategory(Notice.Category.getCategory((short) 10));
        notice.setTitle(noticeInfo.getTitle());
        notice.setContents(noticeInfo.getContents());
        notice.setIsActive(noticeInfo.getIsActive());

        noticeRepository.save(notice);
    }

    /**
     * 공지사항 단건 조회
     * @param noticeSeq
     * @return
     */
    @Override
    public NoticeInfo selectNoticeOne(int noticeSeq) {
        return noticeMapper.toNoticeInfo(noticeRepository.findOne(noticeSeq));
    }

    /**
     * 공지사항 삭제
     * @param noticeSeqs
     */
    @Override
    public void deleteNotice(Integer[] noticeSeqs) {
        noticeRepository.deleteNotice(Arrays.asList(noticeSeqs));
    }

    @Override
    public Map<String, Object> selectNoticeDetail(int noticeSeq) {

        Map<String, Object> map = new HashMap<>();
        map.put("notice", noticeMapper.toNoticeInfo(noticeRepository.findOne(noticeSeq)));
        map.put("after", noticeMapper.toNoticeInfo(noticeRepository.findTopByCategoryAndIsActiveTrueAndSequenceGreaterThanOrderBySequenceAsc((short)10, noticeSeq)));
        map.put("before", noticeMapper.toNoticeInfo(noticeRepository.findTopByCategoryAndIsActiveTrueAndSequenceLessThanOrderBySequenceDesc((short)10, noticeSeq)));

        return map;
    }


}
