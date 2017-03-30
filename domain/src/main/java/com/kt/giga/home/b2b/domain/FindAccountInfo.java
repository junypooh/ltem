package com.kt.giga.home.b2b.domain;

import com.kt.giga.home.b2b.validator.Password;
import com.kt.giga.home.b2b.validator.PasswordPattern;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by junypooh on 2017-02-21.
 * <pre>
 * com.kt.giga.home.b2b.domain.FindAccountInfo
 *
 * 아이디 찾기 domain Class
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-21 오후 12:14
 */
@Data
@Password(confirmPassword = "newPasswdConfirm", password = "newPasswd" , message = "")
public class FindAccountInfo {

    private Long mgrSeq;

    private String id;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "휴대전화번호를 입력해 주세요.")
    private String telephone;

    private String authenticationNumber;

    /** 새로운 비밀번호 */
    @PasswordPattern(message = "비밀번호 설정 조건에 맞지 않습니다. 다시 확인해주세요.")
    private String newPasswd;

    /** 새로운 비밀번호 확인 */
    private String newPasswdConfirm;
}
