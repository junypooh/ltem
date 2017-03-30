package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.TermInfo;
import com.kt.giga.home.b2b.entity.Term;
import com.kt.giga.home.b2b.service.IMaskingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DaDa on 2017-03-14.
 */
public abstract class TermMapperDecorator implements TermMapper {

    @Autowired
    private IMaskingService iMaskingService;

    @Autowired
    private TermMapper termMapper;

    @Override
    public TermInfo toTermInfo(Term term) {
        TermInfo termInfo = termMapper.toTermInfo(term);

        if (StringUtils.isNotBlank(termInfo.getCreatedBy())) {
            termInfo.setCreatedByMask(iMaskingService.maskFromEnd(term.getCreatedBy(), 3));
        }

        return termInfo;
    }

    @Override
    public List<TermInfo> toTermInfoList(List<Term> terms) {
        if (terms == null) {
            return null;
        }
        List<TermInfo> list = new ArrayList<>();
        terms.forEach(term -> list.add(this.toTermInfo(term)));
        return list;
    }
}
