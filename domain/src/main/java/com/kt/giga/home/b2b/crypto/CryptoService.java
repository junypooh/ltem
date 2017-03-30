package com.kt.giga.home.b2b.crypto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

/**
 * com.kt.giga.home.b2b.common.crypto
 * <p>
 * Created by cecil on 2017. 1. 20..
 */
@Service
public class CryptoService implements ICryptoService {

    private static TextEncryptor TEXT_ENCRYPTOR;

    @Autowired
    public CryptoService(@Value("${b2b.security.password:dusakfwjdtks}") String password, @Value("${b2b.security.salt:ac359e36490284bd}") String salt) {
        TEXT_ENCRYPTOR = Encryptors.queryableText(password, salt);
    }

    @Override
    public String encrypt(String textToEncrypt) {
        return (null == textToEncrypt) ? null : TEXT_ENCRYPTOR.encrypt(textToEncrypt);
    }

    @Override
    public String decrypt(String textToDecrypt) {
        return (null == textToDecrypt) ? null : TEXT_ENCRYPTOR.decrypt(textToDecrypt);
    }

}
