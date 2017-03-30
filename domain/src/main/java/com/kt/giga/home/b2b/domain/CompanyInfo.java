package com.kt.giga.home.b2b.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by junypooh on 2017-02-28.
 * <pre>
 * com.kt.giga.home.b2b.domain.CompanyInfo
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-28 오후 3:52
 */
@Data
public class CompanyInfo {

    /**
     * 회사코드
     */
    private String code;

    /**
     * 회사이름
     */
    @NotBlank(message = "기업명을 입력해주세요.")
    private String name;

    @NotNull(message = "담당자를 입력해주세요.")
    private Long mgrSeq;

    /**
     * 담당자
     */
    private B2bManager manager;

    /**
     * 대표 전화번호 (AES256)
     */
    @NotBlank(message = "대표번호를 입력해주세요.")
    private String telephone;

    /**
     * 기업 로그 파일 SEQ
     */
    @NotNull(message = "로고를 등록해주세요.")
    private Long fileSeq;

    /**
     * 변경일
     */
    private LocalDateTime modified;

    /** 변경일 String 변환 */
    public String getModifiedToString() {
        String modifiedToString = "";
        if( this.modified != null) {
            modifiedToString = this.modified.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return modifiedToString;
    }
}
