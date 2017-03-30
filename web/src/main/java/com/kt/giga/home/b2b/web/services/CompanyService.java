package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.annotation.PrivacyAuditHistoric;
import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.CompanyInfo;
import com.kt.giga.home.b2b.entity.Company;
import com.kt.giga.home.b2b.mapper.CompanyMapper;
import com.kt.giga.home.b2b.mapper.CompanyMaskingMapper;
import com.kt.giga.home.b2b.repository.CompanyRepository;
import com.kt.giga.home.b2b.repository.CompanySpecifications;
import com.kt.giga.home.b2b.repository.FileManagerRepository;
import com.kt.giga.home.b2b.repository.ManagerRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by junypooh on 2017-02-28.
 * <pre>
 * com.kt.giga.home.b2b.web.services.CompanyService
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-28 오후 12:18
 */
@Service
public class CompanyService implements ICompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyMaskingMapper companyMaskingMapper;

    @Autowired
    private FileManagerRepository fileManagerRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public Map<String, Object> selectCompanies(B2bManager manager, Pageable pageable, String column, String searchWord) {

        Specifications<Company> specifications = Specifications.where(CompanySpecifications.companyDefault());

        // 검색 조건이 있을 경우
        if(StringUtils.isNotBlank(searchWord)) {

            // 기업 코드 검색일 경우
            if("code".equals(column)) {
                specifications = specifications.and(CompanySpecifications.likeCode(searchWord));
            } else if("name".equals(column)) {
            // 기업명 검색일 경우
                specifications = specifications.and(CompanySpecifications.likeName(searchWord));
            } else {
                specifications = specifications.and(Specifications.where(CompanySpecifications.likeCode(searchWord)).or(CompanySpecifications.likeName(searchWord)));
            }
        }

        Page<Company> page = companyRepository.findAll(specifications, pageable);

        List<CompanyInfo> list = companyMaskingMapper.toCompanyInfoList(page.getContent());

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("page", page);

        return map;
    }

    @Override
    public CompanyInfo selectCompanyInfo(String code) {

        Company company = companyRepository.findByCode(code);

        if(StringUtils.isNotBlank(company.getName())) {
            return selectMaskedCompanyInfo(code);
        } else {
            return selectUnmaskedCompanyInfo(code);
        }
    }

    @Override
    public CompanyInfo selectMaskedCompanyInfo(String code) {

        Company company = companyRepository.findByCode(code);
        return companyMaskingMapper.toCompanyInfo(company);
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "계정관리 > 기업관리 > 기업상세", unmasked = true, downloaded = false, returnClass = CompanyInfo.class, targetInfo = "아이디, 이름, 대표번호")
    public CompanyInfo selectUnmaskedCompanyInfo(String code) {

        Company company = companyRepository.findByCode(code);
        return companyMapper.toCompanyInfo(company);
    }

    @Override
    public void updateCompanyInfo(CompanyInfo companyInfo) {

        Company company = companyRepository.findByCode(companyInfo.getCode());

        company.setName(companyInfo.getName());
        company.setTelephone(companyInfo.getTelephone());
        if(companyInfo.getFileSeq() != null && companyInfo.getFileSeq() > 0L) {
            company.setFileManager(fileManagerRepository.findOneByFileSeq(companyInfo.getFileSeq()));
        }
        if(companyInfo.getMgrSeq() != null && companyInfo.getMgrSeq() > 0L) {
            company.setManager(managerRepository.findBySequence(companyInfo.getMgrSeq()));
        }

//        companyRepository.saveAndFlush(company);
        companyRepository.save(company);

    }
}
