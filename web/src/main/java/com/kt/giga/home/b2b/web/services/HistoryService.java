package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.annotation.PrivacyAuditHistoric;
import com.kt.giga.home.b2b.domain.HistoryInfo;
import com.kt.giga.home.b2b.mapper.HistoryMapper;
import com.kt.giga.home.b2b.repository.PrivacyAuditHistoryRepository;
import com.kt.giga.home.b2b.service.IMaskingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DaDa on 2017-03-22.
 */
@Service
public class HistoryService implements IHistoryService {

    @Autowired
    PrivacyAuditHistoryRepository historyRepository;

    @Autowired
    IMaskingService iMaskingService;

    @Autowired
    HistoryMapper historyMapper;


    @Override
    public Map<String, Object> getHistoryInfo(int page) {
        List<Object[]> historyList = historyRepository.findByHistoryInfo(30, 30 * page);

        int totCount = 0;
        if (historyList != null && historyList.size() > 0) {
            Object[] objects = historyList.get(0);
            totCount = ((BigInteger)objects[4]).intValue();
        }

        Map<String, Object> map = new HashMap<>();

        map.put("historyList", historyList);
        map.put("totCount", totCount);
        return map;
    }

    @Override
    public Map<String, Object> getHistoryInfoGroupByCompany(String companyCd, String startDt, String endDt, int page) {

        Map<String, Object> map = new HashMap<>();

        List<Object[]> companyInfo = historyRepository.findByHistoryInfoCompanyInfo(companyCd, startDt, endDt);

        int totCount = 0;
        if (companyInfo != null && companyInfo.size() > 0) {
            Object[] obj = companyInfo.get(0);

            map.put("maskCount", ((BigInteger)obj[0]).intValue());
            map.put("downCount", ((BigInteger)obj[1]).intValue());
            map.put("companyName", String.valueOf(obj[2]));
        }

        selectHitoryInfoGroupByCompanyCd(companyCd, startDt, endDt, page, map);

        return map;
    }

    private void selectHitoryInfoGroupByCompanyCd(String companyCd, String startDt, String endDt, int page, Map<String, Object> map) {

        int totCount = 0;
        List<Object[]> historyInfoGroupByCompanyCd = historyRepository.findByHistoryInfoGroupByCompanyCd(companyCd, startDt, endDt, 30, 30 * page);

        List<HistoryInfo> list = null;
        if (historyInfoGroupByCompanyCd != null && historyInfoGroupByCompanyCd.size() > 0) {
            Object[] objects = historyInfoGroupByCompanyCd.get(0);
            totCount = ((BigInteger)objects[6]).intValue();

            list = new ArrayList<>();
            for (Object[] obj : historyInfoGroupByCompanyCd) {

                HistoryInfo info = new HistoryInfo();
                info.setManagerUserId(String.valueOf(obj[0]));
                if (obj[0] != null)
                    info.setManagerMaskUserId(iMaskingService.maskFromEnd(String.valueOf(obj[0]), 3));
                if (obj[1] != null)
                    info.setManagerName(iMaskingService.maskNameOfPerson(String.valueOf(obj[1])));
                info.setAuthorityName(String.valueOf(obj[2]));
                info.setMaskedCount(((BigInteger)obj[3]).intValue());
                info.setDownloadCount(((BigInteger)obj[4]).intValue());
                info.setLastHistoryDate(String.valueOf(obj[5]));

                list.add(info);
            }
        }
        map.put("historyList", list);
        map.put("totCount", totCount);
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "이력관리 > 기업이력", unmasked = true, returnClass = Map.class, targetInfo = "아이디, 이름")
    public Map<String, Object> getHistoryInfoGroupByUnmaskCompany(String companyCd, String managerUserId, String startDt, String endDt, int page) {

        Map<String, Object> map = new HashMap<>();

        selectHitoryInfoGroupByCompanyCd(companyCd, startDt, endDt, page, map);

        List<Object[]> companyCdAndManagerUserId = historyRepository.findByHistoryInfoGroupByCompanyCdAndManagerUserId(companyCd, managerUserId, startDt, endDt);

        if (companyCdAndManagerUserId != null && companyCdAndManagerUserId.size() > 0) {

            Object[] obj = companyCdAndManagerUserId.get(0);

            HistoryInfo info = new HistoryInfo();
            info.setManagerUserId(String.valueOf(obj[0]));
            info.setManagerName(String.valueOf(obj[1]));
            info.setAuthorityName(String.valueOf(obj[2]));
            info.setMaskedCount(((BigInteger)obj[3]).intValue());
            info.setDownloadCount(((BigInteger)obj[4]).intValue());
            info.setLastHistoryDate(String.valueOf(obj[5]));

            map.put("historyInfo", info);
        }

        return map;
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "이력관리 > 기업이력", downloaded = true, isList = true, returnClass = HistoryInfo.class, targetInfo = "아이디, 이름, 권한, 마스킹해제 수, 다운로드 수, 최종이력 일시")
    public List<HistoryInfo> getCompanyHistoryToExcel(HistoryInfo historyInfo, String companyCd, String startDt, String endDt) {

        List<HistoryInfo> list = new ArrayList<>();

        for (String managerUserId : historyInfo.getManagerUserIds()) {
            List<Object[]> companyCdAndManagerUserId = historyRepository.findByHistoryInfoGroupByCompanyCdAndManagerUserId(companyCd, managerUserId, startDt, endDt);

            Object[] obj = companyCdAndManagerUserId.get(0);

            HistoryInfo info = new HistoryInfo();
            if (obj[0] != null)
                info.setManagerMaskUserId(iMaskingService.maskFromEnd(String.valueOf(obj[0]), 3));
            if (obj[1] != null)
                info.setManagerMaskName(iMaskingService.maskNameOfPerson(String.valueOf(obj[1])));
            info.setManagerName(String.valueOf(obj[1]));
            info.setAuthorityName(String.valueOf(obj[2]));
            info.setMaskedCount(((BigInteger)obj[3]).intValue());
            info.setDownloadCount(((BigInteger)obj[4]).intValue());
            info.setLastHistoryDate(String.valueOf(obj[5]));

            list.add(info);
        }
        return list;
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "이력관리 > 이력조회", downloaded = true, isList = true, returnClass = HistoryInfo.class, targetInfo = "기업명, 마스킹해제 수, 다운로드 수")
    public List<HistoryInfo> getHistoryToExcel(HistoryInfo historyInfo) {
        List<HistoryInfo> list = new ArrayList<>();

        for (String companyCd : historyInfo.getCompanyCds()) {
            List<Object[]> obj = historyRepository.findByHistoryInfoToExcel(companyCd);
            if (obj != null) {
                HistoryInfo info = new HistoryInfo();
                info.setCompanyName((String) obj.get(0)[1]);
                info.setMaskedCount(((BigInteger) obj.get(0)[2]).intValue());
                info.setDownloadCount(((BigInteger) obj.get(0)[3]).intValue());

                list.add(info);
            }
        }

        return list;
    }

    @Override
    public Map<String, Object> getHistoryInfoGroupByManager(String managerUserId, String startDt, String endDt, int page) {
        HistoryInfo managerHistory = new HistoryInfo();
        List<Object[]> historyHeader = historyRepository.findByHistoryInfoGroupByManagerHeader(managerUserId, startDt, endDt);

        if(historyHeader != null && historyHeader.size() > 0) {
            managerHistory.setManagerUserId(String.valueOf(historyHeader.get(0)[0]));
            managerHistory.setManagerMaskUserId(iMaskingService.maskFromEnd(String.valueOf(historyHeader.get(0)[0]), 3));
            managerHistory.setAuthorityName(String.valueOf(historyHeader.get(0)[1]));
            managerHistory.setMaskedCount(((BigInteger) historyHeader.get(0)[2]).intValue());
            managerHistory.setDownloadCount(((BigInteger) historyHeader.get(0)[3]).intValue());
            managerHistory.setStartDt(String.valueOf(historyHeader.get(0)[4]));
            managerHistory.setEndDt(String.valueOf(historyHeader.get(0)[5]));
        }


        List<HistoryInfo> historyList = new ArrayList<>();
        List<Object[]> userHistory = historyRepository.findByHistoryInfoGroupByManager(managerUserId, startDt, endDt, 30, 30 * page);
        int totCount = 0;
        if (userHistory != null && userHistory.size() > 0) {
            Object[] objects = userHistory.get(0);
            totCount = ((BigInteger)objects[8]).intValue();

            for (Object[] obj : userHistory) {
                HistoryInfo historyInfo = new HistoryInfo();
                historyInfo.setHistorySeq((Integer) obj[0]);
                historyInfo.setBizContSerial(String.valueOf(obj[1]));
                historyInfo.setMenuPath(String.valueOf(obj[2]));
                if (StringUtils.trimToNull(String.valueOf(obj[3])) != null) {
                    historyInfo.setUserName(iMaskingService.maskNameOfPerson(String.valueOf(obj[3])));
                } else {
                    historyInfo.setUserName("-");
                }
                historyInfo.setTargetInfo(String.valueOf(obj[4]));
                historyInfo.setWorkContext(String.valueOf(obj[5]));
                historyInfo.setReason(String.valueOf(obj[6]));
                historyInfo.setWorkHistoryDate(String.valueOf(obj[7]));

                historyList.add(historyInfo);
            }
        }

        Map<String, Object> map = new HashMap<>();

        map.put("managerHistory", managerHistory);
        map.put("historyList", historyList);
        map.put("totCount", totCount);

        return map;
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "이력관리 > 담당자이력", downloaded = true, isList = true, returnClass = HistoryInfo.class, targetInfo = "이름")
    public List<HistoryInfo> getHistoryGroupByManagerToExcel(HistoryInfo historyInfo) {

        List<HistoryInfo> list = new ArrayList<>();

        for(Integer historySeq : historyInfo.getHistorySeqs()) {
            List<Object[]> userHistory = historyRepository.findByHistoryGroupByManagerToExcel(historyInfo.getManagerUserId(), historyInfo.getStartDt(), historyInfo.getEndDt(), historySeq);

            if (userHistory != null && userHistory.size() > 0) {
                Object[] obj = userHistory.get(0);

                HistoryInfo info = new HistoryInfo();
                info.setHistorySeq((Integer) obj[0]);
                info.setBizContSerial(String.valueOf(obj[1]));
                info.setMenuPath(String.valueOf(obj[2]));
                if (StringUtils.trimToNull(String.valueOf(obj[3])) != null) {
                    info.setUserName(iMaskingService.maskNameOfPerson(String.valueOf(obj[3])));
                } else {
                    info.setUserName("-");
                }
                info.setTargetInfo(String.valueOf(obj[4]));
                info.setWorkContext(String.valueOf(obj[5]));
                info.setReason(String.valueOf(obj[6]));
                info.setWorkHistoryDate(String.valueOf(obj[7]));

                list.add(info);
            }
        }
        return list;
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "이력관리 > 담당자이력", unmasked = true, returnClass = Map.class, targetInfo = "이름")
    public HistoryInfo getHistoryInfoGroupByUnmaskManager(Integer historySeq) {
        return historyMapper.toHistoryInfo(historyRepository.findOne(historySeq));
    }
}
