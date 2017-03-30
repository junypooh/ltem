package com.kt.giga.home.b2b.services;

import com.kt.giga.home.b2b.domain.Role;
import com.kt.giga.home.b2b.entity.Company;
import com.kt.giga.home.b2b.entity.Manager;
import com.kt.giga.home.b2b.repository.CompanyRepository;
import com.kt.giga.home.b2b.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * com.kt.giga.home.b2b.services
 * <p>
 * Created by cecil on 2017. 3. 18..
 */
@Profile("prd")
@Component
public class InitialDataProviderProduction implements IInitialDataProvider {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private CompanyRepository companyRepository;


    @Override
    public void initData() {
        Company kthCompany = companyRepository.findByCode("001");

        if (kthCompany == null) {
            kthCompany = new Company();
            kthCompany.setCode("001");
            kthCompany.setName("KTH");
            companyRepository.saveAndFlush(kthCompany);
        }

        Manager kthadm = managerRepository.findById("kimjh");
        if (kthadm == null) {
            kthadm = new Manager();
            kthadm.setCompany(kthCompany);
            kthadm.setEmail("kim.jh@kt.com");
            kthadm.setId("kimjh");
            kthadm.setDepartment("홈IoT사업팀");
            kthadm.setRole(Role.SUPER_MASTER);
            kthadm.setRank("대리");
            kthadm.setStatus(Manager.Status.ACTIVATED);
            kthadm.setName("김재현");
            kthadm.setPasswd("new12#$!");
            kthadm.setTelephone("01077482016");
            managerRepository.saveAndFlush(kthadm);
        }
    }
}
