package com.kt.giga.home.b2b.service;

/**
 * Created by cecil on 2017. 1. 4..
 */
public interface IMaskingService {
    String maskEmail(String email);

    String maskTelephone(String telephoneNumber);

    String maskNameOfPerson(String nameOfPerson);

    String maskAll(String str);

    String maskFromEnd(String str, int count);

    String maskFromStart(String str, int count);
}
