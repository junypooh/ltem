package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.NoticeInfo;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Created by DaDa on 2017-03-10.
 */
public interface INoticeService {

    /**
     * 상태가 노출인 공지사항 리스트 조회
     * @return
     */
    Map<String, Object> selectNoticeInfoIsActiveTrue(Pageable pageable);

    /**
     * 모든 공지사항 리스트 조회
     * @param pageable
     * @return
     */
    Map<String, Object> selectNoticeAllInfo(Short categoryCd, Pageable pageable);

    /**
     * 검색 조건 리스트 조회
     * @param pageable
     * @return
     */
    Map<String, Object> selectNoticeListByCondition(Short categoryCd, String searchKey, String searchText, Pageable pageable);

    /**
     * 공지사항 등록
     * @param noticeInfo
     */
    void insertNotice(NoticeInfo noticeInfo);

    /**
     * 공지사항 단건 조회
     * @param noticeSeq
     * @return
     */
    NoticeInfo selectNoticeOne(int noticeSeq);

    /**
     * 공지사항 삭제
     * @param noticeSeqs
     */
    void deleteNotice(Integer[] noticeSeqs);

    /**
     * 공지사항 상세 조회
     * @param noticeSeq
     * @return
     */
    Map<String, Object> selectNoticeDetail(int noticeSeq);


}
