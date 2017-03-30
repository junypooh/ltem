package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.FindAccountInfo;
import com.kt.giga.home.b2b.domain.TermInfo;
import com.kt.giga.home.b2b.entity.Manager;
import com.kt.giga.home.b2b.mapper.B2bManagerMapper;
import com.kt.giga.home.b2b.repository.ManagerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by junypooh on 2017-02-03.
 */
@Service
@Slf4j
public class ManagerService implements IManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private B2bManagerMapper b2bManagerMapper;

    @Autowired
    private StandardPasswordEncoder standardPasswordEncoder;

    @Autowired
    private ISmsService smsService;

    @Override
    public void updateCaptchaFailCount(B2bManager b2bManager) {

        Manager manager = managerRepository.findBySequence(b2bManager.getMgrSeq());

        int captchaFailedCount = manager.getCaptchaFailedCount();
        captchaFailedCount++;

        manager.setCaptchaFailedCount((short) captchaFailedCount);
        if (captchaFailedCount >= 5) {
            manager.setLocked(true);
        }

        managerRepository.save(manager);
    }

    @Override
    public void updatePasswordFailCount(B2bManager b2bManager) {

        Manager manager = managerRepository.findBySequence(b2bManager.getMgrSeq());

        int passwordFailedCount = manager.getPasswordFailedCount();
        passwordFailedCount++;

        manager.setPasswordFailedCount((short) passwordFailedCount);
        if (passwordFailedCount >= 5) {
            manager.setLocked(true);
        }

        managerRepository.save(manager);

    }

    @Override
    public void initializeFailCount(B2bManager b2bManager) {

        Manager manager = managerRepository.findBySequence(b2bManager.getMgrSeq());

        manager.setCaptchaFailedCount((short) 0);
        manager.setPasswordFailedCount((short) 0);

        managerRepository.save(manager);

    }

    @Override
    public void updateLastLoggedIn(B2bManager b2bManager) {

        Manager manager = managerRepository.findBySequence(b2bManager.getMgrSeq());
        manager.setLastLoggedIn(LocalDateTime.now());

        managerRepository.save(manager);
    }

    @Override
    public boolean isPasswordChangeRequired(B2bManager b2bManager) {

        Manager manager = managerRepository.findBySequence(b2bManager.getMgrSeq());

        // 최종 변경일이 없을 경우
        if (manager.getPasswordModified() == null) {
            return true;
        }

        // 패스워드 초기화 되었을 경우
        if (manager.isPasswordInitialized())
            return true;

        // 최종 변경일 30일 지난 경우
        Period between = Period.between(manager.getPasswordModified().toLocalDate(), LocalDateTime.now().toLocalDate());
        int    days    = between.getDays();

        if (log.isDebugEnabled())
            log.debug("password changed {} days", days);

        return days > 30;

    }

    @Override
    public Object sendAuthNumber(FindAccountInfo findAccountInfo) {

        B2bManager b2bManager = b2bManagerMapper.toB2bManagerFromFindAccountInfo(findAccountInfo);

        Map<String, Object> rtnMap   = new HashMap<>();
        Manager manager = managerRepository.findOneByNameAndTelephone(b2bManager.getName(), b2bManager.getTelephone());

        if (manager != null) {

            String authenticationNumber = RandomStringUtils.randomNumeric(6);
            String title = "LTE-M 인증번호";
            String message = "LTE-M 인증번호는 [" + authenticationNumber + "] 입니다.";
            log.debug("SMS message : " + message);

            // SMS 발송
            Optional<String> tranctionId = smsService.sendMessage("100", b2bManager.getTelephone().replaceAll("-", ""), title, message);

            if(tranctionId.isPresent()) {
                manager.setAuthenticationNumber(authenticationNumber);
                manager.setAuthenticationNumberSent(LocalDateTime.now());

                managerRepository.save(manager);

                rtnMap.put("errorCd", 0);
                rtnMap.put("msg", "");
            } else {
                rtnMap.put("errorCd", -2);
                rtnMap.put("msg", "SMS 발송에 실패 하였습니다. 잠시 후 다시 이용해 주세요.");
            }

        } else {
            rtnMap.put("errorCd", -3);
            rtnMap.put("msg", "입력한 정보와 일치하는 아이디가 없습니다. 다시 확인해주시기 바랍니다.");
        }

        return rtnMap;

    }

    @Override
    public FindAccountInfo doesAuthNumberMatch(FindAccountInfo findAccountInfo) {

        B2bManager b2bManager = b2bManagerMapper.toB2bManagerFromFindAccountInfo(findAccountInfo);

        Manager manager = managerRepository.findOneByNameAndTelephoneAndAuthenticationNumber(b2bManager.getName(), b2bManager.getTelephone(), findAccountInfo.getAuthenticationNumber());

        return b2bManagerMapper.toFindAccountInfoFromManager(manager);
    }

    @Override
    public void initializeAuthNumber(Long mgrSeq) {

        Manager manager = managerRepository.findBySequence(mgrSeq);

        if (manager != null) {
//            manager.setAuthenticationNumberSent(null);
            manager.setAuthenticationNumber(null);

            managerRepository.save(manager);
        }

    }

    @Override
    public Boolean existsUserId(String userId) {

        return (managerRepository.existsById(userId));
    }

    @Override
    public void updatePassword(FindAccountInfo findAccountInfo) {

        Manager manager = managerRepository.findBySequence(findAccountInfo.getMgrSeq());

        manager.setPasswd(standardPasswordEncoder.encode(findAccountInfo.getNewPasswd()));
        manager.setPasswordModified(LocalDateTime.now());
        manager.setPasswordInitialized(false);

        // 저장
        managerRepository.saveAndFlush(manager);
    }

    @Override
    public List<TermInfo> selectTermsInfo(B2bManager b2bManager) {
        return null;
    }

    @Override
    public void insertTermsAgree(B2bManager b2bManager) {

    }


}
