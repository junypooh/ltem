package com.kt.giga.home.b2b.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by mac on 2017. 2. 22..
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false, of = {"sequence"})
@Table(name = "notice", schema = "ltem")
@ToString(exclude = {"manager"})
@SequenceGenerator(name = "NOTICE_SEQ", sequenceName = "ltem.seq_notice", initialValue = 1, allocationSize = 1, schema = "ltem")
public class Notice extends BaseCreatedByModifiedByEntity implements Serializable {
    private static final long serialVersionUID = 2780256996962348461L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTICE_SEQ")
    @Column(name = "notice_seq", updatable = false)
    private Integer sequence;

    @Column(name = "notice_cate_cd")
    private Short category;

    public void setCategory(Notice.Category category) {
        this.category = category.getCategoryCd();
    }

    public Notice.Category getCategory() {
        return Notice.Category.getCategory(this.category);
    }

    public short getCategoryCd() {
        return this.category;
    }

    @Column(name = "notice_title")
    private String title;

    @Column(name = "notice_content")
    private String contents;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mgr_seq", referencedColumnName = "mgr_seq", updatable = false)
    private Manager manager;


    @ToString
    public enum Category {

        /**
         * 서비스관리 공지
         */
        SERVICE_NOTICE((short) 10, "서비스관리 공지"),
        /**
         * 고객센터 공지
         */
        USER_NOTICE((short) 20, "고객센터 공지");

        private short  categoryCd;
        private String description;

        Category(short categoryCd, String description) {
            this.categoryCd = categoryCd;
            this.description = description;
        }

        public short getCategoryCd() {
            return this.categoryCd;
        }

        public String getDescription() {
            return description;
        }

        public static Notice.Category getCategory(short categoryCd) {
            for (Notice.Category category : values()) {
                if (category.categoryCd == categoryCd)
                    return category;
            }

            throw new IllegalArgumentException("No matching category for category code [" + categoryCd + "] found.");
        }
    }

}



















