package com.kt.giga.home.b2b.domain;


import com.kt.giga.home.b2b.validator.Password;
import com.kt.giga.home.b2b.validator.PasswordPattern;
import com.kt.giga.home.b2b.validator.Phone;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by DaDa on 2017-01-05.
 */

@Data
@Password(confirmPassword = "confirmPassword", password = "password" , message = "")
public class UserInfo {

    @Length(min = 1, max = 10, message = "! 10자 이내로 입력하세요.")
    private String name;

    @NotEmpty(message = "! 이메일 형식에 맞춰 입력하세요.")
    @Email(message = "! 이메일 형식에 맞춰 입력하세요.")
    //@Pattern(regexp="^[_0-9a-zA-Z-]+@[0-9a-zA-Z]+(.[_0-9a-zA-Z-]+)*$")
    private String email;

    private String division;

    private String position;

    private String accountName;

    //@Pattern(regexp = "^.*(?=^.{6,12}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).*$", message = "! 비밀번호 조건에 맞춰 입력하세요.")
    @PasswordPattern(message = "! 비밀번호 조건에 맞춰 입력하세요.")
    private String password;

    private String confirmPassword;

    @Phone(message = "! 휴대폰 번호를 바르게 입력해주세요.")
    private String phone;

    private String accreditationNum;


}
