package com.kt.giga.home.b2b.aop;

import com.kt.giga.home.b2b.annotation.PrivacyAuditHistoric;
import com.kt.giga.home.b2b.domain.*;
import com.kt.giga.home.b2b.entity.PrivacyAuditHistory;
import com.kt.giga.home.b2b.repository.PrivacyAuditHistoryRepository;
import com.kt.giga.home.b2b.util.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * com.kt.giga.home.b2b.aop.FormConfigurationAspect
 *
 * Controller Aspect Class
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2017-01-09 오후 5:43
 */
@Slf4j
@Aspect
@Component
public class ConfigurationAspect {

    private final HttpServletRequest request;

    private final PrivacyAuditHistoryRepository privacyAuditHistoryRepository;

    @Autowired
    public ConfigurationAspect(HttpServletRequest request, PrivacyAuditHistoryRepository privacyAuditHistoryRepository) {
        this.request = request;
        this.privacyAuditHistoryRepository = privacyAuditHistoryRepository;
    }

    @Before(value = "execution(* com.kt.giga.home.b2b.web.controller..*.*(..)) && !execution(* com.kt.giga.home.b2b.web.controller.ProvisionController.*(..))")
    public void checkTermsAgreeRequired(JoinPoint joinPoint) {

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        // 약관동의 필요 여부 확인
        Object termsAgreeRequired = request.getSession().getAttribute("termsAgreeRequired");
        if(termsAgreeRequired != null) {
            if((boolean) termsAgreeRequired) {
                try {
                    response.sendRedirect(request.getContextPath() + "/provision/termAgreement");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @AfterReturning(value = "execution(* com.kt.giga.home.b2b.web.services..*.*(..))", returning = "returnVal")
    public void setPrivacyAuditHistory(JoinPoint joinPoint, Object returnVal) {

        Annotation[] annotations = ((MethodSignature) joinPoint.getSignature()).getMethod().getDeclaredAnnotations();

        PrivacyAuditHistoric privacyAuditHistoric = (PrivacyAuditHistoric) CollectionUtils.find(Arrays.asList(annotations), PredicateUtils.instanceofPredicate(PrivacyAuditHistoric.class));

        if(privacyAuditHistoric != null) {

            if(privacyAuditHistoric.isList()) {

                List<?> list = (List<?>) returnVal;
                for( Object obj : list) {
                    savePrivacyAuditHistory(privacyAuditHistoric, obj);
                }

            } else {
                savePrivacyAuditHistory(privacyAuditHistoric, returnVal);
            }
        }

    }

    private void savePrivacyAuditHistory(PrivacyAuditHistoric privacyAuditHistoric, Object returnVal) {

        B2bManager currentManager = SessionUtils.getUser();

        PrivacyAuditHistory privacy = new PrivacyAuditHistory();
        privacy.setCompanyCd(currentManager.getCompanyCd());
        privacy.setCompanyName(currentManager.getCompanyName());
        privacy.setManagerUserId(currentManager.getUsername());
        privacy.setManagerName(currentManager.getName());
        privacy.setAuthorityName(Role.getRole(currentManager.getAuthorityCd()).name());
        privacy.setMenuPath(privacyAuditHistoric.menuPath());
        privacy.setUnmasked(privacyAuditHistoric.unmasked());
        privacy.setDownloaded(privacyAuditHistoric.downloaded());
        privacy.setAudited(LocalDateTime.now());
        privacy.setTargetInfo(privacyAuditHistoric.targetInfo());
        privacy.setReason(request.getParameter("reason"));

        // bizContSerial
        // userName
        if (returnVal instanceof SvcContractInfo) {
            privacy.setBizContSerial(((SvcContractInfo) returnVal).getBizContSerial());
            privacy.setUserName(((SvcContractInfo) returnVal).getUserName());
        } else if (returnVal instanceof B2bManager) {
            privacy.setUserName(((B2bManager) returnVal).getName());
        } else if (returnVal instanceof CompanyInfo) {
            privacy.setUserName(((CompanyInfo) returnVal).getManager().getName());
        } else if (returnVal instanceof StatisticsByHubInfo) {
            privacy.setUserName(((StatisticsByHubInfo) returnVal).getUserName());
            privacy.setBizContSerial(((StatisticsByHubInfo) returnVal).getBizContSerial());
        } else if (returnVal instanceof TermInfo) {
            privacy.setUserName(((TermInfo) returnVal).getCreatedBy());
        } else if (returnVal instanceof BannerInfo) {
            privacy.setUserName(((BannerInfo) returnVal).getCreatedBy());
        } else if (returnVal instanceof ContractUsageInfo) {
            privacy.setBizContSerial(((ContractUsageInfo) returnVal).getSerialNumber());
        } else if (returnVal instanceof HistoryInfo) {
            privacy.setUserName(StringUtils.isNotBlank(((HistoryInfo) returnVal).getManagerName()) ? ((HistoryInfo) returnVal).getManagerName() : ((HistoryInfo) returnVal).getUserName());
        } else if (returnVal instanceof Map) {
            // 계정관리 상세
            if ( ((Map) returnVal).get("manager") != null ) {
                B2bManager bManager = (B2bManager) ((Map) returnVal).get("manager");// B2bManager
                privacy.setUserName(bManager.getName());
            }
            // 계정관리 상세
            if ( ((Map) returnVal).get("accountInfo") != null ) {
                AccountInfo accountInfo = (AccountInfo) ((Map) returnVal).get("accountInfo");// AccountInfo
                privacy.setUserName(accountInfo.getName());
            }
            // 허브별 통계
            if ( ((Map) returnVal).get("unmaskInfo") != null ) {
                StatisticsByHubInfo info = (StatisticsByHubInfo) ((Map) returnVal).get("unmaskInfo");// unmaskInfo
                privacy.setUserName(info.getUserName());
                privacy.setBizContSerial(info.getBizContSerial());
            }
            // 이력관리
            if ( ((Map) returnVal).get("historyInfo") != null ) {
                HistoryInfo info = (HistoryInfo) ((Map) returnVal).get("historyInfo");// historyInfo
                privacy.setUserName(StringUtils.isNotBlank(info.getManagerName()) ? info.getManagerName() : info.getUserName());
            }

        }

        privacyAuditHistoryRepository.save(privacy);

    }
}
