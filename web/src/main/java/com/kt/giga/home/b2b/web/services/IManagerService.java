package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.FindAccountInfo;
import com.kt.giga.home.b2b.domain.TermInfo;

import java.util.List;

/**
 * <pre>
 * com.kt.giga.home.b2b.services
 *      IManagerService
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2017-01-11 오전 11:08
 */
public interface IManagerService {

    /**
     * 보안문자 실패 횟수 update
     * @param b2bManager
     */
    void updateCaptchaFailCount(B2bManager b2bManager);

    /**
     * 비밀번호 실패 횟수 update
     * @param b2bManager
     */
    void updatePasswordFailCount(B2bManager b2bManager);

    /**
     * 보안문자, 비밀번호 실패 횟수 초기화
     * @param b2bManager
     */
    void initializeFailCount(B2bManager b2bManager);

    /**
     * 최종 로그인 시간 업데이트
     * @param b2bManager
     */
    void updateLastLoggedIn(B2bManager b2bManager);

    /**
     * 비밀번호 변경 30일 경과 여부
     * @param b2bManager
     * @return
     */
    boolean isPasswordChangeRequired(B2bManager b2bManager);

    /**
     * 휴대폰 번호 인증 발송
     * @param findAccountInfo
     */
    Object sendAuthNumber(FindAccountInfo findAccountInfo);

    /**
     * 휴대폰 번호 인증 확인
     * @param findAccountInfo
     * @return 계정 정보
     */
    FindAccountInfo doesAuthNumberMatch(FindAccountInfo findAccountInfo);

    /**
     * 회원 휴대폰 인증번호 초기화
     * @param mgrSeq
     */
    void initializeAuthNumber(Long mgrSeq);

    /**
     * 사용자 아이디 존재 여부
     * @param userId
     * @return
     */
    Boolean existsUserId(String userId);

    /**
     * 비밀번호 변경
     * @param findAccountInfo
     */
    void updatePassword(FindAccountInfo findAccountInfo);

    /**
     * 서비스 약관 조회
     * 필요시 동의하지 않은 약관 만 조회
     * @param b2bManager
     * @return
     */
    List<TermInfo> selectTermsInfo(B2bManager b2bManager);

    /**
     * 약관 동의 내역 등록
     * @param b2bManager
     */
    void insertTermsAgree(B2bManager b2bManager);


}
