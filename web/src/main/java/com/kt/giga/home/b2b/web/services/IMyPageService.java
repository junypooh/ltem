package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.MyPageInfo;

import java.util.Map;

/**
 * Created by DaDa on 2017-01-10.
 */
public interface IMyPageService {

    /**
     * MyPage 회원정보 조회
     * @return
     * @param manager
     */
    MyPageInfo selectMyPageInfo(B2bManager manager);

    /**
     * MyPage 회원정보 수정
     *
     * @param currentManager
     * @param myPageInfo
     * @return
     */
    void updateMyPageInfo(B2bManager currentManager, MyPageInfo myPageInfo);


    /**
     * 휴대폰 인증번호 발송
     * @param myPageInfo
     * @return
     */
    Object sendAuthNumber(MyPageInfo myPageInfo, B2bManager currentManager);

    /**
     * 휴대폰 인증번호 확인
     * @param myPageInfo
     * @return
     */
    Map<String, Object> doesAuthNumberMatch(MyPageInfo myPageInfo, B2bManager currentManager);


}
