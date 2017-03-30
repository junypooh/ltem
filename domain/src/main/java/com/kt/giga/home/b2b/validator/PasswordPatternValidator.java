package com.kt.giga.home.b2b.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by DaDa on 2017-01-17.
 */
public class PasswordPatternValidator implements ConstraintValidator<PasswordPattern, String> {

    @Override
    public void initialize(PasswordPattern constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if(StringUtils.trimToNull(password) != null) {
            // 영문, 숫자, 특수문자 포함. 연속된 숫자 4자리 사용 불가
            //return password.matches("^.*(?=^.{6,12}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$") && continueNumberCheck(password);
            // 영문, 숫자, 특수문자 포함.
            return password.matches("^.*(?=^.{6,12}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$");
        } else {
            return true;
        }
    }

    /**
     * 패스워드 구성이 연속된 숫자 4개 이상인지 체크
     * @param password
     * @return
     */
    private boolean continueNumberCheck(String password) {
        int count = 1;

        for(int i = 0 ; i < password.length() ; i++) {
            char temp_p = '\u0000';
            char tempPass = '\u0000';
            char temp_c = '\u0000';

            tempPass = password.charAt(i);      // 입력받은 비밀번호의 char값

            if( 47 < (int) tempPass && (int) tempPass < 58) {   // 숫자일 경우에만..
                if((i+1) == password.length()) {
                    break;
                } else {
                    temp_p = password.charAt(i+1);  // 입력받은 비밀번호의 현재 index의 다음 index char값(index+1)
                    temp_c = (char)(tempPass+1);  // 입력받은 char값의 다음 char값 ( ex. a 다음은 b, 1 다음은 2...)
                }

                if(temp_c == temp_p) {            // 입력받은 char값의 다음 index char값과 다음 char값을 비교
                    count = count + 1;
                } else {
                    count = 1;
                }

                if (count > 3) {
                    break;
                }
            }
        }

        return count <= 3;
    }


}
