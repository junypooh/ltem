package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.entity.Term;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mac on 2017. 2. 15..
 *
 * 서비스관리 인터페이스
 *
 */
public interface ISvcManageService {

    /*
    * 약관 리스트 조회
    * @categoryCd : 약관 종류
    * */
    @Transactional(readOnly = true)
    List<Term> selectManagerTermInfo(short categoryCd);

    /*
    * 관리자의 생성한 약관 리스트 조회
    * @categoryCd : 약관 종류
    * */
    @Transactional(readOnly = true)
    List<Term> selectUserManagerTermInfo(short categoryCd);

    /*
    * 약관 세부 사항 조회
    * @categoryCd : 약관 시퀀스
    * */
    @Transactional(readOnly = true)
    Term selectManagerTermDetail(int seq);

    /*
    * 약관 추가
    * @term : 추가되는 약관 데이터
    * */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    void insertManagerTerm(Term term);

    /*
    * 약관 수정
    * @term : 수정되는 약관 데이터
    * */
    Term updateManagerTerm(Term term);

    /*
    * 약관 삭제
    * @term : 삭제되는 약관 데이터
    * */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    void deleteManagerTerm(List<Term> delTermList);

}













