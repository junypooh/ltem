package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.annotation.PrivacyAuditHistoric;
import com.kt.giga.home.b2b.domain.CompanyInfo;
import com.kt.giga.home.b2b.domain.TermInfo;
import com.kt.giga.home.b2b.entity.Term;
import com.kt.giga.home.b2b.mapper.TermMaskMapper;
import com.kt.giga.home.b2b.mapper.TermUnmaskMapper;
import com.kt.giga.home.b2b.repository.ManagerTermRepository;
import com.kt.giga.home.b2b.repository.TermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DaDa on 2017-03-14.
 */
@Service
public class TermService implements ITermService {

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private TermMaskMapper termMaskMapper;

    @Autowired
    private TermUnmaskMapper termUnmaskMapper;

    @Autowired
    private ManagerTermRepository managerTermRepository;

    /**
     * 약관 리스트 조회
     * @param pageable
     * @return
     */
    @Override
    public Map<String, Object> selectTermInfoList(Pageable pageable) {
        Map<String, Object> map = new HashMap<>();

        Page<Term> page = termRepository.findAll(pageable);
        List<TermInfo> termInfos = termMaskMapper.toTermInfoList(page.getContent());

        map.put("termInfoList", termInfos);
        map.put("page", page);

        return map;
    }


    /**
     * 약관 상세 조회
     * @param termSeq
     * @return
     */
    @Override
    public TermInfo selectTermInfoDetail(int termSeq) {
        return termMaskMapper.toTermInfo(termRepository.findOne(termSeq));
    }


    /**
     * 약관 상세 조회
     * @param termSeq
     * @return
     */
    @Override
    @PrivacyAuditHistoric(menuPath = "서비스관리 > 서비스이용약관상세", unmasked = true, downloaded = false, returnClass = TermInfo.class, targetInfo = "아이디")
    public TermInfo selectUnmaskTermInfoDetail(int termSeq) {
        return termUnmaskMapper.toTermInfo(termRepository.findOne(termSeq));
    }


    /**
     * 약관 등록
     * @param termInfo
     */
    @Override
    public void insertTermInfo(TermInfo termInfo) {

        Term term = termRepository.findOne(termInfo.getTermSeq());

        if(term == null) {
            term = new Term();
        }

        // 해당 약관이 활성화라면
        if(termInfo.isActive()) {
            // 해당 약관 category의 모든 활성화 false update
            termRepository.updateIsActiveFalseToCategory(termInfo.getCategoryCd());
        }

        term.setCategory(Term.Category.getCategory(termInfo.getCategoryCd()));
        term.setTitle(termInfo.getTitle());
        term.setContents(termInfo.getContents());
        term.setVersion(termInfo.getVersion());
        term.setIsActive(termInfo.isActive());

        termRepository.save(term);

    }

    /**
     * 약관 삭제
     * @param termSeqs
     */
    @Override
    public void deleteTermInfo(Integer[] termSeqs) {
        // 약관동의내용 삭제
        managerTermRepository.deleteByTerm(Arrays.asList(termSeqs));
        // 약관 삭제
        termRepository.deleteTerm(Arrays.asList(termSeqs));
    }

    /**
     * 활성화된 약관중 카테고리 조건으로 조회
     * @param category
     * @return
     */
    @Override
    public TermInfo getIsActiveTrueAndCategor(Short category) {
        List<Term> terms = termRepository.findByIsActiveTrueAndCategory(category);
        TermInfo termInfo = new TermInfo();

        if(terms != null && terms.size() > 0) {
            termInfo = termMaskMapper.toTermInfo(terms.get(0));
        }

        return termInfo;
    }

    @Override
    public Map<String, Object> getTermsCategor(Short category, Integer termSeq) {
        Map<String, Object> map = new HashMap<>();

        List<Term> terms = termRepository.findByCategory(category, new Sort(Sort.Direction.DESC, "version"));

        if (terms.size() > 0) {
            if (termSeq == null) {
                map.put("term", termMaskMapper.toTermInfo(termRepository.findOne(terms.get(0).getSequence())));
            } else {
                termMaskMapper.toTermInfo(termRepository.findOne(termSeq));
                map.put("term", termMaskMapper.toTermInfo(termRepository.findOne(termSeq)));
            }
            map.put("termList", termMaskMapper.toTermInfoList(terms));
        }

        return map;
    }


}
