package com.kt.giga.home.b2b.crypto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.keygen.KeyGenerators;

/**
 * com.kt.giga.home.b2b.common.crypto
 * <p>
 * Created by cecil on 2017. 1. 20..
 */
public class CryptoServiceTest {


    private static ICryptoService cryptoService;

    @Before
    public void setUp() throws Exception {
        cryptoService = new CryptoService("dusakfwjdtks", "ac359e36490284bd");
    }

    @Test
    public void encryptTest() throws Exception {
        String original  = "010-1234-1234";
        String encrypted = cryptoService.encrypt(original);
        Assert.assertEquals("Encrypted Text does not match.", "7089c47e7ae13b6fb9e38c17471862de", encrypted);
    }

    @Test
    public void decryptTest() throws Exception {
        String original  = "5636ecc39cd0860f544602a24f0446c9";
        String encrypted = cryptoService.decrypt(original);
        Assert.assertEquals("Encrypted Text does not match.", "7089c47e7ae13b6fb9e38c17471862de", encrypted);
    }

    @Test
    public void generateKey() {
        String key = KeyGenerators.string().generateKey();
        System.out.println(key);
    }

    @Test
    public void encryptEmailTest() {
        String email     = "abdqwerasdfasdf@qwerqwerqwerqwerqwer.com";
        String encrypted = cryptoService.encrypt(email);
        Assert.assertEquals("Encrypted Email does not match", "asdf", encrypted);
    }

}