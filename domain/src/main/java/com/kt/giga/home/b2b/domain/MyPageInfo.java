package com.kt.giga.home.b2b.domain;

import com.kt.giga.home.b2b.validator.Duplication;
import com.kt.giga.home.b2b.validator.PasswordPattern;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * Created by DaDa on 2017-01-31.
 */
@Data
@Duplication(confirmFieldColumn = "newPasswdConfirm", fieldColumn = "newPasswd", errorMessage = "비밀번호가 일치하지 않습니다.")
public class MyPageInfo {

    /** 현재 비밀번호 */
    private String passwd;

    /** 새로운 비밀번호 */
    @PasswordPattern(message = "비밀번호 패턴에 맞춰주세요.")
    private String newPasswd;

    /** 새로운 비밀번호 확인 */
    private String newPasswdConfirm;

    /** 이메일 */
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식에 맞춰 입력하세요.")
    private String email;

    /** 휴대전화 번호 */
    @NotBlank(message = "휴대폰 번호를 입력해 주세요.")
    private String telephone;
    private String tempTelephone;


    /** 인증번호 */
    private String authenticationNumber;

    /** 최종비밀번호 변경일시 */
    private LocalDateTime passwordModified;

    /** 아이디 */
    private String id;

    /** 이름 */
    private String name;

    /** 회사명 */
    private String companyName;
}
