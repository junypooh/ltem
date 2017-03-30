package com.kt.giga.home.b2b.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by DaDa on 2017-03-20.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false, of = {"sequence"})
@Table(name = "banner", schema = "ltem")
public class Banner extends BaseCreatedByModifiedByEntity implements Serializable {

    private static final long serialVersionUID = 7718581335427827926L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANNER_SEQ")
    @Column(name = "banner_seq", updatable = false)
    @SequenceGenerator(name = "BANNER_SEQ", sequenceName = "ltem.seq_banner", allocationSize = 1, schema = "ltem")
    private Integer sequence;

    @Column(name = "category")
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

    @Column(name = "name")
    private String name;

    @Column(name = "link")
    private String link;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "file_seq", referencedColumnName = "file_seq")
    private FileManager fileManager;

    @Column(name = "banner_order")
    private Short order;

    @Column(name = "is_current")
    private Boolean current;

    @Column(name = "is_new_window")
    private Boolean newWindow;


    @ToString
    public enum Category {

        /**
         * 서비스 이용 약관
         */
        MAIN_BANNER((short) 1, "메인"),
        /**
         * 개인정보 수집 및 이용 동의
         */
        SUB_BANNER((short) 2, "서브");

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

        public static Banner.Category getCategory(short categoryCd) {
            for (Banner.Category category : values()) {
                if (category.categoryCd == categoryCd)
                    return category;
            }

            throw new IllegalArgumentException("No matching category for category code [" + categoryCd + "] found.");
        }


    }

}
