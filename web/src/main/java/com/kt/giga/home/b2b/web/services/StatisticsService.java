package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.annotation.PrivacyAuditHistoric;
import com.kt.giga.home.b2b.domain.*;
import com.kt.giga.home.b2b.entity.BizContUsage;
import com.kt.giga.home.b2b.entity.BizContract;
import com.kt.giga.home.b2b.mapper.BizContUsageMapper;
import com.kt.giga.home.b2b.mapper.BizContractMapper;
import com.kt.giga.home.b2b.repository.BizContUsageRepository;
import com.kt.giga.home.b2b.repository.BizContractRepository;
import com.kt.giga.home.b2b.repository.SvcContUsageRepository;
import com.kt.giga.home.b2b.service.IMaskingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by junypooh on 2017-03-15.
 * <pre>
 * com.kt.giga.home.b2b.web.services.StatisticsService
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-15 오후 4:46
 */
@Service
@Slf4j
public class StatisticsService implements IStatisticsService {

    @Autowired
    private IBizContractService bizContractService;

    @Autowired
    private BizContUsageRepository bizContUsageRepository;

    @Autowired
    private BizContUsageMapper bizContUsageMapper;

    @Autowired
    private BizContractRepository bizContractRepository;

    @Autowired
    private BizContractMapper bizContractMapper;

    @Autowired
    private SvcContUsageRepository svcContUsageRepository;

    @Autowired
    private IMaskingService iMaskingService;

    @Override
    public List<ContractInfo> selectBizContracts(B2bManager manager, String contractsState, String contractsColumn, String searchWord, String sortName) {

        List<ContractInfo> contractInfoList = bizContractService.selectBizContractsInSearchCurrentUser(manager, contractsState, contractsColumn, searchWord, sortName);

        // 전월(최근)데이터 사용량 가져오기
        for (ContractInfo contractInfo : contractInfoList) {

            List<BizContUsage> bizContUsages = bizContUsageRepository.findBySerialNumberOrderByYearDescMonthDesc(contractInfo.getSerialNumber());

            if (CollectionUtils.isNotEmpty(bizContUsages)) {
                BizContUsage bizContUsage = bizContUsages.get(0);
                contractInfo.setRecentContUsage(bizContUsageMapper.toContractUsageInfo(bizContUsage));
            }
        }

        return contractInfoList;
    }

    @Override
    public Map<String, Object> selectTotalUsage(String serialNumber, String year) {

        BizContract bizContract = bizContractRepository.findBySerialNumber(serialNumber);
        ContractInfo toContractInfo = bizContractMapper.toContractInfo(bizContract);

        List<BizContUsage> bizMonthUsages = bizContUsageRepository.findBySerialNumberAndYearOrderByMonthAsc(serialNumber, Short.parseShort(year));
        List<ContractUsageInfo> contractMonthUsages = bizContUsageMapper.toContractUsageListInfo(bizMonthUsages);

        Map<String, Object> map = new HashMap<>();
        map.put("contractInfo", toContractInfo);
        map.put("contractMonthUsages", contractMonthUsages);

        return map;
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "통계관리 > 전체 통계", unmasked = false, downloaded = true, isList = true, returnClass = ContractUsageInfo.class, targetInfo = "월, 허용량(MB), 사용중인 회선수, 허브 사용자")
    public List<ContractUsageInfo> selectTotalUsageToExcel(ContractUsageInfo contractUsageInfo) {

        List<BizContUsage> contUsageList = bizContUsageRepository.findBySerialNumberAndYearAndMonthInOrderByMonthAsc(contractUsageInfo.getSerialNumber(), contractUsageInfo.getYear(), contractUsageInfo.getMonths());
        List<ContractUsageInfo> contractMonthUsages = bizContUsageMapper.toContractUsageListInfo(contUsageList);

        return contractMonthUsages;
    }

    @Override
    public Map<String, Object> selectHubUsage(String serialNumber, int page) {

        BizContract bizContract = bizContractRepository.findBySerialNumber(serialNumber);
        ContractInfo toContractInfo = bizContractMapper.toContractInfo(bizContract);

        List<Object[]> statisticsByHubInfo = svcContUsageRepository.findByStatisticsByHubInfos(serialNumber, 30, 30 * page);

        int totCount = 0;
        List<StatisticsByHubInfo> statisticsByHubInfos = null;
        if(statisticsByHubInfo != null && statisticsByHubInfo.size() > 0) {

            Object[] objects = statisticsByHubInfo.get(0);
            totCount = ((BigInteger)objects[7]).intValue();

            statisticsByHubInfos = mappingStatisticsByHubInfo(statisticsByHubInfo);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("contractInfo", toContractInfo);
        map.put("statisticsByHubInfos", statisticsByHubInfos);
        map.put("totCount", totCount);

        return map;
    }

    private List<StatisticsByHubInfo> mappingStatisticsByHubInfo(List<Object[]> statisticsByHubInfo) {

        List<StatisticsByHubInfo> statisticsByHubInfos = new ArrayList<>();

        for(Object[] obj : statisticsByHubInfo) {
            StatisticsByHubInfo info = new StatisticsByHubInfo();
            info.setSerialNumber(((BigInteger)obj[0]).longValue());
            if(obj[1] != null)
                info.setDevicePlace(iMaskingService.maskAll((String)obj[1]));
            if(obj[2] != null)
                info.setUserName(iMaskingService.maskNameOfPerson((String)obj[2]));
            info.setAvgUsage(((BigDecimal)obj[3]).doubleValue());
            info.setAvgUsageUnitCd((Character)obj[4]);
            info.setUsage(((BigInteger)obj[5]).doubleValue());
            info.setUsageUnitCd((Character)obj[6]);
            info.setViewAvgUsage(BizContract.getCalculateCapacityByMb((Character)obj[4], ((BigDecimal)obj[3]).longValue()));
            info.setViewUsage(BizContract.getCalculateCapacityByMb((Character)obj[6], ((BigInteger)obj[5]).longValue()));

            statisticsByHubInfos.add(info);
        }

        return statisticsByHubInfos;

    }

    @Override
    @PrivacyAuditHistoric(menuPath = "통계관리 > 허브별 통계", unmasked = true, downloaded = false, returnClass = Map.class, targetInfo = "상세주소, 이름")
    public Map<String, Object> selectHubUsageUnMask(String serialNumber, Long svcContSerial, int page) {

        List<Object[]> statisticsByHubInfo = svcContUsageRepository.findByStatisticsByHubInfos(serialNumber, 30, 30 * page);

        List<StatisticsByHubInfo> statisticsByHubInfos = null;
        if(statisticsByHubInfo != null && statisticsByHubInfo.size() > 0) {
            statisticsByHubInfos = mappingStatisticsByHubInfo(statisticsByHubInfo);
        }

        StatisticsByHubInfo info = getStatisticsByHubInfo(serialNumber, svcContSerial);

        Map<String, Object> map = new HashMap<>();
        map.put("statisticsByHubInfos", statisticsByHubInfos);
        map.put("unmaskInfo", info);

        return map;
    }

    @Override
    public StatisticsByHubInfo getStatisticsByHubInfo(String serialNumber, Long svcContSerial) {
        List<Object[]> obj = svcContUsageRepository.findByStatisticsByHubInfo(serialNumber, svcContSerial);
        StatisticsByHubInfo info = null;
        if(obj != null) {
            info = new StatisticsByHubInfo();
            info.setBizContSerial(serialNumber);
            info.setSerialNumber(((BigInteger)obj.get(0)[0]).longValue());
            if(obj.get(0)[1] != null)
                info.setDevicePlace(iMaskingService.maskAll((String)obj.get(0)[1]));
            if(obj.get(0)[2] != null)
                info.setUserName(iMaskingService.maskNameOfPerson((String)obj.get(0)[2]));
            info.setAvgUsage(((BigDecimal)obj.get(0)[3]).doubleValue());
            info.setAvgUsageUnitCd((Character)obj.get(0)[4]);
            info.setUsage(((BigInteger)obj.get(0)[5]).doubleValue());
            info.setUsageUnitCd((Character)obj.get(0)[6]);
            info.setViewAvgUsage(BizContract.getCalculateCapacityByMb((Character)obj.get(0)[4], ((BigDecimal)obj.get(0)[3]).longValue()));
            info.setViewUsage(BizContract.getCalculateCapacityByMb((Character)obj.get(0)[6], ((BigInteger)obj.get(0)[5]).longValue()));
        }
        return info;
    }

    @Override
    @PrivacyAuditHistoric(menuPath = "통계관리 > 허브별 통계", unmasked = false, downloaded = true, isList = true, returnClass = StatisticsByHubInfo.class, targetInfo = "서비스계약번호, 상세주소, 이름, 평균사용량(MB), 전월사용량(MB)")
    public List<StatisticsByHubInfo> selectHubUsageToExcel(StatisticsByHubInfo statisticsByHubInfo) {

        List<StatisticsByHubInfo> list = new ArrayList<>();
        for(Long serialNumber : statisticsByHubInfo.getSerialNumbers()) {

            List<Object[]> obj = svcContUsageRepository.findByStatisticsByHubInfo(statisticsByHubInfo.getBizContSerial(), serialNumber);
            if(obj != null) {
                StatisticsByHubInfo info = new StatisticsByHubInfo();
                info.setBizContSerial(statisticsByHubInfo.getBizContSerial());
                info.setSerialNumber(((BigInteger)obj.get(0)[0]).longValue());
                if(obj.get(0)[1] != null)
                    info.setDevicePlace(iMaskingService.maskAll((String)obj.get(0)[1]));
                if(obj.get(0)[2] != null)
                    info.setUserName(iMaskingService.maskNameOfPerson((String)obj.get(0)[2]));
                info.setAvgUsage(((BigDecimal)obj.get(0)[3]).doubleValue());
                info.setAvgUsageUnitCd((Character)obj.get(0)[4]);
                info.setUsage(((BigInteger)obj.get(0)[5]).doubleValue());
                info.setUsageUnitCd((Character)obj.get(0)[6]);
                info.setViewAvgUsage(BizContract.getCalculateCapacityByMb((Character)obj.get(0)[4], ((BigDecimal)obj.get(0)[3]).longValue()));
                info.setViewUsage(BizContract.getCalculateCapacityByMb((Character)obj.get(0)[6], ((BigInteger)obj.get(0)[5]).longValue()));

                list.add(info);
            }

        }
        return list;
    }
}
