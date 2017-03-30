package com.kt.giga.home.b2b.crypto;

/**
 * com.kt.giga.home.b2b.common.crypto
 * <p>
 * Created by cecil on 2017. 1. 20..
 */
public interface ICryptoService {

    String encrypt(String textToEncrypt);

    String decrypt(String textToDecrypt);
}
