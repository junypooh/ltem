package com.kt.giga.home.b2b.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by DaDa on 2017-03-10.
 */
@Data
public class NoticeInfo {

    /** 공지사항 일련번호 */
    private int noticeSeq;

    /** 공지사항 제목 */
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    /** 공지사항 내용 */
    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

    /** 등록자 일련번호 */
    private Long mgrSeq;

    /** 등록자 Info */
    private B2bManager manager;

    /** 공지사항 노출여부 */
    private Boolean isActive;

    /** 등록일 */
    private LocalDateTime created;

    public String getIsActiveName() {
        String str = "비노출";
        if(this.isActive) {
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

    /** 등록일 String 변환 */
    public String getCreatedDtToString() {
        String createdToString = "";
        if( this.created != null) {
            createdToString = this.created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return createdToString;
    }


}
