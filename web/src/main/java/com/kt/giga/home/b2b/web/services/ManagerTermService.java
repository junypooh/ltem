package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.entity.Term;
import com.kt.giga.home.b2b.mapper.TermMaskMapper;
import com.kt.giga.home.b2b.repository.ManagerTermRepository;
import com.kt.giga.home.b2b.repository.TermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DaDa on 2017-03-15.
 */
@Service
public class ManagerTermService implements IManagerTermService {

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private ManagerTermRepository managerTermRepository;

    @Autowired
    private TermMaskMapper termMaskMapper;

    /**
     * 약관 동의 여부
     * @param b2bManager
     * @return
     */
    @Override
    public Boolean isManagerTermsCheck(B2bManager b2bManager) {
        // 활성화된 약관 조회
        List<Term> terms = termRepository.findByIsActiveTrue(new Sort(Sort.Direction.ASC, "category"));
        if(terms != null) {
            for(Term term : terms) {
                // 약관 동의 내역이 없다면
                if(managerTermRepository.countByTermAndManager(term.getSequence(), b2bManager.getMgrSeq()) < 1 ) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 동의하지 않은 약관 조회
     * @param b2bManager
     * @return
     */
    @Override
    public Map<String, Object> selectManagerTermsCheck(B2bManager b2bManager) {
        Map<String, Object> rtnMap = new HashMap<>();
        List<Term> terms = termRepository.findByIsActiveTrue(new Sort(Sort.Direction.ASC, "category"));
        List<Term> removeList = new ArrayList<>();

        if (terms != null) {
            for(Term term : terms) {
                // 약관 동의 내역이 있다면
                if(managerTermRepository.countByTermAndManager(term.getSequence(), b2bManager.getMgrSeq()) > 0) {
                    // 동의한 약관 remove
                    removeList.add(term);
                }
            }
            terms.removeAll(removeList);

            // 동의 할 약관이 있음
            if (terms.size() > 0) {
                rtnMap.put("code" , -1);
                rtnMap.put("terms", termMaskMapper.toTermInfoList(terms));
            } else {
                rtnMap.put("code" , 0);
            }
        } else {
            // 활성화된 약관이 없다면..
            rtnMap.put("code" , 0);
        }

        return rtnMap;
    }

    /**
     * 약관 동의 저장
     * @param b2bManagers
     * @param termSeqs
     */
    @Override
    public void managerTermsSave(B2bManager b2bManagers, Integer[] termSeqs) {
        if (termSeqs != null) {
            for(Integer termSeq : termSeqs) {
                managerTermRepository.saveTermManager(termSeq, b2bManagers.getMgrSeq());
            }
        }

    }
}
