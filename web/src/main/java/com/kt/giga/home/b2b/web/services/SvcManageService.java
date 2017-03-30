package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.entity.Manager;
import com.kt.giga.home.b2b.entity.ManagerTerm;
import com.kt.giga.home.b2b.entity.Term;
import com.kt.giga.home.b2b.repository.ManagerRepository;
import com.kt.giga.home.b2b.repository.TermRepository;
import com.kt.giga.home.b2b.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 2017. 2. 15..
 *
 * 서비스관리 클래스
 *
 */
@Service
public class SvcManageService implements ISvcManageService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private TermRepository termRepository;



    @Override
    @Transactional(readOnly = true)
    public List<Term> selectManagerTermInfo(short categoryCd) {
        return termRepository.findByCategoryEquals(categoryCd);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Term> selectUserManagerTermInfo(short categoryCd) {
        Manager manager = managerRepository.findOne(getUserSeq());

        List<Term> list = new ArrayList<>();
        manager.getManagerTerms().forEach(item->{
            // 활성화된 약관에서 일치하는 종류 리스트 구성.
            if(item.getTerm().getIsActive() && categoryCd == item.getTerm().getCategoryCd()){
                list.add(item.getTerm());
            }
        });

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Term selectManagerTermDetail(int seq) {
        return termRepository.findOne(seq);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void insertManagerTerm(Term term) {
        Manager manager = managerRepository.findOne(getUserSeq());

        termRepository.save(term);                  // 약관 추가.
        termRepository.flush();

        managerRepository.saveTermAgreed(term.getSequence(), getUserSeq());   // termsAgreed 데이터 추가

        ManagerTerm mitem = new ManagerTerm();
        mitem.setManager(manager);
        mitem.setTerm(termRepository.findOne(term.getSequence()));
        mitem.setAgreed(LocalDateTime.now());
        mitem.setWithdrawn(LocalDateTime.now());
        manager.getManagerTerms().add(mitem);
        managerRepository.save(manager);            // manager 정보 저장.
        managerRepository.flush();
    }

    @Override
    public Term updateManagerTerm(Term term) {
        term.setModifiedBy(SessionUtils.getUser().getUsername());
        termRepository.save(term);
        termRepository.flush();

        return termRepository.findOne(term.getSequence());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void deleteManagerTerm(List<Term> delTermList) {
        List<Integer> delList = new ArrayList<>();
        delTermList.forEach(item -> delList.add(item.getSequence()));

        managerRepository.deleteTermsAgreed(delList);
        termRepository.delete(delTermList);
        termRepository.flush();
    }

    private long getUserSeq(){
        return SessionUtils.getUser().getMgrSeq();
    }
}




















