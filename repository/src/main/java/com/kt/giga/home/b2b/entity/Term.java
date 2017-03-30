package com.kt.giga.home.b2b.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by mac on 2017. 1. 11..
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false, of = {"sequence"})
@Table(name = "terms", schema = "ltem")
@ToString(exclude = {"managerTerms"})
@SequenceGenerator(name = "TERMS_SEQ", sequenceName = "ltem.seq_terms", initialValue = 1, allocationSize = 1, schema = "ltem")
public class Term extends BaseCreatedByModifiedByEntity implements Serializable {

    private static final long serialVersionUID = 2780256996962348458L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TERMS_SEQ")
    @Column(name = "term_seq", updatable = false)
    private Integer sequence;

    @Column(name = "terms_cate_cd")
    private Short category;

    public void setCategory(Category category) {
        this.category = category.getCategoryCd();
    }

    public Category getCategory() {
        return Category.getCategory(this.category);
    }

    public short getCategoryCd() {
        return this.category;
    }

    @Column(name = "terms_ver")
    private String version;

    @Column(name = "terms_title")
    private String title;

    @Column(name = "terms_content")
    private String contents;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "term", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ManagerTerm> managerTerms;

    @ToString
    public enum Category {

        /**
         * 서비스 이용 약관
         */
        TERMS_OF_SERVICE((short) 10, "이용 약관"),
        /**
         * 개인정보 수집 및 이용 동의
         */
        PRIVACY_POLICY((short) 20, "개인정보 수집 및 이용 동의"),
        /**
         * 제3자 취급 위탁 약관
         */
        THIRD_PARTY_HANDLING_TERMS((short) 30, "개인정보처리방침");

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

        public static Category getCategory(short categoryCd) {
            for (Category category : values()) {
                if (category.categoryCd == categoryCd)
                    return category;
            }

            throw new IllegalArgumentException("No matching category for category code [" + categoryCd + "] found.");
        }


    }
}
