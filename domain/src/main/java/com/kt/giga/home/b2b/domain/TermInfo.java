package com.kt.giga.home.b2b.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by DaDa on 2017-02-21.
 */
@Data
public class TermInfo {

    /** 약관 seq */
    private int termSeq;

    /** 약관 Category 코드 */
    private short categoryCd;

    /** 약관 Category 명 */
    private String categoryName;

    /** 약관 버전 */
    @NotBlank(message = "버전을 입력해주세요.")
    private String version;

    /** 약관 제목 */
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    /** 약관 내용 */
    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

    /** 활성화 여부 */
    private boolean isActive;

    /** 등록일 */
    private LocalDateTime created;

    /** 등록자 */
    private String createdBy;

    /** 등록자 Masking */
    private String createdByMask;

    /** 등록일 String 변환 */
    public String getCreatedToString() {
        String createdToString = "";
        if( this.created != null) {
            createdToString = this.created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return createdToString;
    }

    public String getIsActiveName() {
        String str = "비노출";
        if(this.isActive) {
            str = "노출";
        }
        return str;
    }





}
