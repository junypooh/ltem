package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.CompanyInfo;
import com.kt.giga.home.b2b.entity.Company;
import com.kt.giga.home.b2b.entity.Manager;
import com.kt.giga.home.b2b.service.IMaskingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junypooh on 2017-03-02.
 * <pre>
 * com.kt.giga.home.b2b.mapper.CompanyMaskingMapperDecorator
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-02 오후 3:10
 */
public abstract class CompanyMaskingMapperDecorator implements CompanyMaskingMapper {

    @Autowired
    private IMaskingService iMaskingService;

    @Autowired
    @Qualifier("delegate")
    private CompanyMaskingMapper companyMaskingMapper;

    @Override
    public CompanyInfo toCompanyInfo(Company company) {

        CompanyInfo companyInfo = companyMaskingMapper.toCompanyInfo(company);

        if(StringUtils.isNotBlank(company.getTelephone())) {
            companyInfo.setTelephone(iMaskingService.maskTelephone(company.getTelephone()));
        }

        companyInfo.setManager(this.toB2bManager(company.getManager()));

        return companyInfo;
    }

    @Override
    public B2bManager toB2bManager(Manager manager) {

        if (manager == null) {
            return null;
        }

        B2bManager b2bManager = companyMaskingMapper.toB2bManager(manager);
        if(StringUtils.isNotBlank(manager.getId())) {
            b2bManager.setUsername(iMaskingService.maskFromEnd(manager.getId(), 3));
        }
        if(StringUtils.isNotBlank(manager.getName())) {
            b2bManager.setName(iMaskingService.maskNameOfPerson(manager.getName()));
        }

        return b2bManager;
    }

    @Override
    public List<CompanyInfo> toCompanyInfoList(List<Company> companies) {

        if (companies == null) {
            return null;
        }

        List<CompanyInfo> list = new ArrayList<>();
        companies.forEach(company -> list.add(this.toCompanyInfo(company)));

        return list;
    }
}
