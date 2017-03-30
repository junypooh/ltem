package com.kt.giga.home.b2b.domain;

import com.kt.giga.home.b2b.validator.Duplication;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * Created by DaDa on 2017-03-03.
 */
@Data
@Duplication.List({
        @Duplication(confirmFieldColumn = "userId", fieldColumn = "tempId", errorMessage = "아이디 중복확인을 해주세요."),
        @Duplication(confirmFieldColumn = "telephone", fieldColumn = "tempTelephone", errorMessage = "휴대폰 번호 중복확인을 해주세요.")
})
public class AccountInfo {

    /** 기업 코드 */
    @NotBlank(message = "기업코드를 입력해주세요.")
    private String companyCd;

    /** 기업 명 */
    private String companyName;

    /** 아이디 */
    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z0-9_-]{6,12}$", message = "아이디는 6~12자 사이의 영문,숫자만 입력가능합니다.")
    private String userId;
    private String tempId;

    /** 사용자 이름 */
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    /** 휴대폰번호 */
    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    private String telephone;
    private String tempTelephone;

    /** 이메일 */
    @Email(message = "이메일 형식에 맞지않습니다.")
    private String email;

    /** 권한 코드 */
    @NotNull(message = "권한을 선택해주세요.")
    private Short authorityCd;

    /** 계약번호 */
    private String[] serialNumber;

    /** 상태코드 */
    private Short mgrStatusCd;

    /** 계정 seq */
    private Long mgrSeq;

    /** 변경일 */
    public LocalDateTime modified;

    /** 마지막 로그인 일시 */
    public LocalDateTime lastLoggedIn;

}
