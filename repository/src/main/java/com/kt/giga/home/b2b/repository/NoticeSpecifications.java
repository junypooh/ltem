package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.entity.Notice;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created by junypooh on 2017-03-24.
 * <pre>
 * com.kt.giga.home.b2b.repository.NoticeSpecifications
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-24 오후 1:57
 */
public class NoticeSpecifications {

    public static Specification<Notice> noticeDefault() {
        return (root, query, cb) -> cb.isTrue(root.get("isActive"));
    }

    public static Specification<Notice> equalsCategotyCd(short category) {
        return (root, query, cb) -> cb.equal(root.get("category"), category);
    }

    /**
     * 제목 조건
     * @param title
     * @return
     */
    public static Specification<Notice> likeTitle(String title) {
        return (root, query, cb) -> cb.like(root.get("title"), "%" + title + "%");
    }

    /**
     * 내용 조건
     * @param content
     * @return
     */
    public static Specification<Notice> likeContent(String content) {
        return (root, query, cb) -> cb.like(root.get("contents"), "%" + content + "%");
    }
}
