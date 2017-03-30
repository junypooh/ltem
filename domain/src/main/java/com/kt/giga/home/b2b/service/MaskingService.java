package com.kt.giga.home.b2b.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Masking Util Class
 * <p>
 * Created by DaDa on 2017-01-04.
 */
@Component
public class MaskingService implements IMaskingService {

    /**
     * e-mail Masking
     * <p>
     * <pre>
     *     MaskingService.maskEmail("abcdefg@naver.com")  = "abcd***@naver.com"
     * </pre>
     *
     * @param email
     * @return
     */
    @Override
    public String maskEmail(String email) {
        Assert.notNull(email, "Email must be not null.");

        if (email.trim().equals("")) {
            return "";
        }

        if (!email.contains("@")) {
            return email;
        }
        int lastIdx = email.indexOf("@");
        int strIdx  = lastIdx - 3;

        return StringUtils.overlay(email, StringUtils.repeat("*", 3), strIdx, lastIdx);
    }

    /**
     * 전화번호 Masking
     * <pre>
     *     MaskingService.maskTelephone("010-1234-5678")  = "010-12**-*678"
     * </pre>
     *
     * @param telephoneNumber
     * @return
     */
    @Override
    public String maskTelephone(String telephoneNumber) {
        Assert.notNull(telephoneNumber, "Telephone number must be not null.");

        if (telephoneNumber.equals("")) {
            return "";
        }
        if (telephoneNumber.split("-").length < 3) {
            return telephoneNumber;
        }
        int idx = telephoneNumber.lastIndexOf("-");
        telephoneNumber = StringUtils.overlay(telephoneNumber, StringUtils.repeat("*", 2), idx - 2, idx);
        telephoneNumber = StringUtils.overlay(telephoneNumber, StringUtils.repeat("*", 1), idx + 1, idx + 2);
        return telephoneNumber;

    }

    /**
     * 성명(외국인명) Masking
     * <pre>
     *     MaskingService.maskNameOfPerson("홍길동")  = "홍길*"
     * </pre>
     *
     * @param nameOfPerson
     * @return
     */
    @Override
    public String maskNameOfPerson(String nameOfPerson) {
        Assert.notNull(nameOfPerson, "Name of a person must be not null.");

        if (nameOfPerson.equals("")) {
            return "";
        }
        // 글자수 기준 뒤 1/3
        int idx = (int) Math.round((double) nameOfPerson.length() / 3); // 반올림

        return StringUtils.overlay(nameOfPerson, StringUtils.repeat("*", idx), (nameOfPerson.length() - idx), nameOfPerson
                .length());
    }

    /**
     * All Masking
     * <pre>
     *     MaskingService.maskAll("123456789")  = "*********"
     * </pre>
     *
     * @param str
     * @return
     */
    @Override
    public String maskAll(String str) {
        Assert.notNull(str, "String must be not null.");

        if (str.equals("")) {
            return "";
        }
        return StringUtils.overlay(str, StringUtils.repeat("*", str.length()), 0, str.length());
    }

    /**
     * 뒤 [count] 글자 Masking
     * <pre>
     *     MaskingService.maskFromEnd("abcdefg", 3)  = "abcd***"
     * </pre>
     *
     * @param str
     * @param count
     * @return
     */
    @Override
    public String maskFromEnd(String str, int count) {
        Assert.notNull(str, "String must be not null.");

        if (str.equals("")) {
            return "";
        }
        return StringUtils.overlay(str, StringUtils.repeat("*", count), (str.length() - count), str.length());
    }

    /**
     * 앞 [count] 글자 Masking
     * <pre>
     *     MaskingService.maskFromStart("abcdefg", 3)  = "***defg"
     * </pre>
     *
     * @param str
     * @param count
     * @return
     */
    @Override
    public String maskFromStart(String str, int count) {
        Assert.notNull(str, "String must be not null.");

        if (str.equals("")) {
            return "";
        }
        return StringUtils.overlay(str, StringUtils.repeat("*", count), 0, count);
    }


}
