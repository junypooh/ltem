package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.MyPageInfo;
import com.kt.giga.home.b2b.entity.Manager;
import com.kt.giga.home.b2b.mapper.MyPageMapper;
import com.kt.giga.home.b2b.repository.ManagerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by DaDa on 2017-01-25.
 */
@Slf4j
@Service
public class MyPageService implements IMyPageService {

    @Autowired
    StandardPasswordEncoder standardPasswordEncoder;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    MyPageMapper myPageMapper;

    @Autowired
    ISmsService iSmsService;

    @Override
    public MyPageInfo selectMyPageInfo(B2bManager currentManager) {

        return myPageMapper.toMyPageResInfo(managerRepository.findBySequence(currentManager.getMgrSeq()));
    }

    @Override
    public void updateMyPageInfo(B2bManager currentManager, MyPageInfo myPageInfo) {
        // Entity set
        Manager manager = managerRepository.findBySequence(currentManager.getMgrSeq());

        // 새로운 비밀번호로 변경
        if(StringUtils.trimToNull(myPageInfo.getNewPasswd()) != null) {
            manager.setPasswd(standardPasswordEncoder.encode(myPageInfo.getNewPasswd())); // 암호화
            manager.setPasswordModified(LocalDateTime.now());

            // session 비밀번호 변경
            currentManager.setPassword(standardPasswordEncoder.encode(myPageInfo.getNewPasswd()));
        }

        /*
        // 인증번호 확인 로직
        if (StringUtils.trimToNull(myPageInfo.getAuthenticationNumber()) != null) {
            Manager authenticationNumberManager = managerRepository.findBySequenceAndAuthenticationNumber(currentManager.getMgrSeq(), myPageInfo.getAuthenticationNumber());
            if(authenticationNumberManager != null) {
                //인증번호 확인 후 휴대폰번호 저장
                manager.setTelephone(myPageInfo.getTelephone());
            }
        }
        */
        // 휴대폰번호 저장
        manager.setTelephone(myPageInfo.getTelephone());



        // 이메일 저장
        manager.setEmail(myPageInfo.getEmail());

        // 저장
        managerRepository.save(manager);
    }

    @Override
    public Object sendAuthNumber(MyPageInfo myPageInfo, B2bManager currentManager) {

        Map<String, Object> rtnMap = new HashMap<>();


        String authenticationNumber = RandomStringUtils.randomNumeric(6);
        String title = "LTE-M 인증번호";
        String message = "LTE-M 인증번호는 [" + authenticationNumber + "] 입니다.";
        log.debug("SMS message : " + message);

        // SMS 발송
        Optional<String> tranctionId = iSmsService.sendMessage("100", myPageInfo.getTempTelephone().replaceAll("-", ""), title, message);
        if (tranctionId.isPresent()) {

            Manager manager = managerRepository.findOne(currentManager.getMgrSeq());

            manager.setAuthenticationNumber(authenticationNumber);
            manager.setAuthenticationNumberSent(LocalDateTime.now());

            managerRepository.save(manager);

            rtnMap.put("errorCd", 0);

        } else {
            rtnMap.put("errorCd", -1);
        }

        return rtnMap;
    }

    @Override
    public Map<String, Object> doesAuthNumberMatch(MyPageInfo myPageInfo,  B2bManager currentManager) {

        Map<String, Object> rtnMap = new HashMap<>();

        Manager manager = managerRepository.findBySequenceAndAuthenticationNumber(currentManager.getMgrSeq(), myPageInfo.getAuthenticationNumber());

        if (manager != null) {
            rtnMap.put("errorCd", 0);
            rtnMap.put("msg", "인증이 완료되었습니다.");
        } else {
            rtnMap.put("errorCd", -1);
            rtnMap.put("msg", "인증번호가 일치하지 않습니다.");
        }
        return rtnMap;
    }
}
