package com.kt.giga.home.b2b.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by DaDa on 2017-01-04.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MaskingServiceTest {

    @Autowired
    private IMaskingService IMaskingService;

    @Test
    public void maskEmailSuccessTest() throws Exception {
        String strText = "abcdef@naver.com";
        String expected  = "abc***@naver.com";
        assertEquals(expected, IMaskingService.maskEmail(strText));
    }

    @Test
    public void maskEmailShortLengthFailTest() {
        String strText = "@";
        String expected = "***@";
        assertEquals(expected, IMaskingService.maskEmail(strText));
    }

    @Test(expected = IllegalArgumentException.class)
    public void maskEmailNullFailTest() {
        IMaskingService.maskEmail(null);
    }

    @Test
    public void maskEmailWithoutAtTest() {
        String strText = "abcde";
        String expected = "abcde";
        assertEquals(expected, IMaskingService.maskEmail(strText));
    }

    @Test
    public void maskEmailShortLengthWithoutAtTest() {
        String strText = "a";
        String expected = "a";
        assertEquals(expected, IMaskingService.maskEmail(strText));
    }

    @Test
    public void maskTelephoneSuccessTest() throws Exception {
        String strText = "010-2222-3333";
        String expected  = "010-22**-*333";
        assertEquals(expected, IMaskingService.maskTelephone(strText));
    }

    @Test
    public void maskTelephoneShortLengthFailTest() {
        String strText = "010-2222";
        String expected  = "010-2222";
        assertEquals(expected, IMaskingService.maskTelephone(strText));
    }

    @Test(expected = IllegalArgumentException.class)
    public void maskTelephoneNullFailTest() {
        IMaskingService.maskTelephone(null);
    }

    @Test
    public void maskTelephoneWithoutAtTest() {
        String strText = "010";
        String expected = "010";
        assertEquals(expected, IMaskingService.maskTelephone(strText));
    }

    @Test
    public void maskTelephoneShortLengthWithoutAtTest() {
        String strText = "0";
        String expected = "0";
        assertEquals(expected, IMaskingService.maskTelephone(strText));
    }

    @Test
    public void maskNameSuccessTest() throws Exception {
        String strText = "홍길동";
        String expected  = "홍길*";
        assertEquals(expected, IMaskingService.maskNameOfPerson(strText));
    }

    @Test
    public void maskNameShortLengthSuccessTest() {
        String strText = "홍길";
        String expected  = "홍*";
        assertEquals(expected, IMaskingService.maskNameOfPerson(strText));
    }

    @Test
    public void maskNameLongLengthSuccessTest() {
        String strText = "의적홍길동";
        String expected  = "의적홍**";
        assertEquals(expected, IMaskingService.maskNameOfPerson(strText));
    }

    @Test(expected = IllegalArgumentException.class)
    public void maskNameNullFailTest() {
        IMaskingService.maskNameOfPerson(null);
    }


    @Test
    public void maskAllSuccessTest() throws Exception {
        String strText = "allMasking";
        String expected  = "**********";
        assertEquals(expected, IMaskingService.maskAll(strText));
    }

    @Test(expected = IllegalArgumentException.class)
    public void maskAllNullFailTest() {
        IMaskingService.maskAll(null);
    }

    @Test
    public void maskFromEndSuccessTest() throws Exception {
        String strText = "backSetingMasking";
        String expected  = "backSetingMas****";
        assertEquals(expected, IMaskingService.maskFromEnd(strText, 4));
    }

    @Test
    public void maskFromEndOverflowFailTest() throws Exception {
        String strText = "e";
        String expected  = "****";
        assertEquals(expected, IMaskingService.maskFromEnd(strText, 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void maskFromEndNullFailTest() {
        IMaskingService.maskFromEnd(null, 1);
    }

    @Test
    public void maskFromStartSuccessTest() throws Exception {
        String strText = "frontSettingMasking";
        String expected  = "****tSettingMasking";
        assertEquals(expected, IMaskingService.maskFromStart(strText, 4));
    }

    @Test
    public void maskFromStartOverflowFailTest() throws Exception {
        String strText = "e";
        String expected  = "***";
        assertEquals(expected, IMaskingService.maskFromStart(strText, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void maskFromStartNullFailTest() {
        IMaskingService.maskFromStart(null, 1);
    }

}