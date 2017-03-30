package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by mac on 2017. 2. 22..
 */
public interface NoticeRepository extends JpaRepository<Notice, Integer> , JpaSpecificationExecutor {

    /**
     * 상태가 노출인 카테고리별 공지사항 조회
     * @param categoryCd
     * @param Pageable
     * @return
     */
    Page<Notice> findByCategoryAndIsActiveTrue(Short categoryCd, Pageable Pageable);

    /**
     * 카테고리 조건 공지사항 조회
     * @param categoryCd
     * @param pageable
     * @return
     */
    Page<Notice> findByCategory(Short categoryCd, Pageable pageable);

    /**
     * 공지사항 삭제
     * @param noticeSeq
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Notice n WHERE n.sequence IN :#{#noticeSeq}")
    int deleteNotice(@Param("noticeSeq") List<Integer> noticeSeq);

    /**
     * 이전 글 1개 ( sequence < ? )
     * @param sequence
     * @return
     */
    Notice findTopByCategoryAndIsActiveTrueAndSequenceLessThanOrderBySequenceDesc(Short categoryCd, Integer sequence);

    /**
     * 다음 글  1개( sequence > ? )
     * @param sequence
     * @return
     */
    Notice findTopByCategoryAndIsActiveTrueAndSequenceGreaterThanOrderBySequenceAsc(Short categoryCd, Integer sequence);
}
