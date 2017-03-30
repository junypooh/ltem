package com.kt.giga.home.b2b.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by DaDa on 2017-03-20.
 */
@Data
public class BannerInfo {

    /** 배너 일련번호 */
    private Integer bannerSeq;

    /** 메인: 1, 서브: 2 */
    private Short categoryCd;

    /** 메인: 1, 서브: 2 */
    private String categoryName;

    /** 배너명 */
    @NotBlank(message = "배너명을 입력해주세요.")
    private String name;

    /** 링크 */
    @NotBlank(message = "URL을 입력해주세요.")
    private String link;

    /** 파일번호 */
    private Long fileSeq;

    /** 원본 파일 명 */
    private String oriFileName;

    /** 배치순서 */
    private Short order;

    /** 노출여부(true: 노출,  false: 비노출 */
    private Boolean current;

    /** 새창이동 여부(true:새창) */
    private Boolean newWindow;

    /** 등록자 */
    private String createdBy;

    /** 등록자 Masking */
    private String createdByMask;

    /** 작성일 */
    private LocalDateTime created;

    public String getCurrentName() {
        if (this.current == null) {
            return "";
        }

        String str = "비노출";
        if (this.current) {
            str = "노출";
        }
        return str;
    }

    /** 등록일 String 변환 */
    public String getCreatedToString() {
        String createdToString = "";
        if( this.created != null) {
            createdToString = this.created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return createdToString;
    }


}
