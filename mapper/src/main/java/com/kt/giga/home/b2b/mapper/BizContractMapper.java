package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.ContractInfo;
import com.kt.giga.home.b2b.domain.bssiot.receiver.BssIotBizContractDto;
import com.kt.giga.home.b2b.entity.BizContract;
import com.kt.giga.home.b2b.entity.Manager;
import org.mapstruct.*;

import java.util.List;

/**
 * com.kt.giga.home.b2b.core.mapper
 * <p>
 * Created by cecil on 2017. 1. 22..
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(BizContractMapperDecorator.class)
public interface BizContractMapper {

    @Mappings({
            @Mapping(source = "bizcontno", target = "serialNumber"),
            @Mapping(source = "bizcd", target = "businessCd"),
            @Mapping(target = "periodUnit", expression = "java( com.kt.giga.home.b2b.domain.PeriodUnit.getPeriodUnit( dto.getCperdunitcd().toUpperCase().charAt(0) ) )"),
            @Mapping(source = "cperdval", target = "period"),
            @Mapping(source = "bizcontstdt", target = "startDate", dateFormat = "yyyyMMdd"),
            @Mapping(source = "maxcontfnsdt", target = "endDate", dateFormat = "yyyyMMdd"),
            @Mapping(target = "capacityUnit", expression = "java( com.kt.giga.home.b2b.domain.CapacityUnit.getCapacityUnit( dto.getAlwdqntunitcd().toUpperCase().charAt(0) ) )"),
            @Mapping(source = "alwdqnt", target = "capacity"),
            @Mapping(source = "bizcontdesc", target = "description"),
    })
    BizContract toBizContract(BssIotBizContractDto dto);

    List<ContractInfo> toContractInfoList(List<BizContract> bizContract);

    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "capacityUnitCd", expression = "java(com.kt.giga.home.b2b.domain.CapacityUnit.getCapacityUnit(bizContract.getCapacityUnitCd()).getUnit())"),
            @Mapping(target = "circuitCount", expression = "java( com.kt.giga.home.b2b.entity.BizContract.getCircuitCount(bizContract.getCurrentServiceContracts()) )"),
            @Mapping(target = "companyCd", source = "company.code"),
            @Mapping(target = "devicePlaceIsNullCount", expression = "java( com.kt.giga.home.b2b.entity.BizContract.getDevicePlaceIsNullCount(bizContract.getCurrentServiceContracts()) )"),
            @Mapping(target = "devicePlaceCount", expression = "java( com.kt.giga.home.b2b.entity.BizContract.getDevicePlaceCount(bizContract.getCurrentServiceContracts()) )"),
            @Mapping(target = "viewCapacity", expression = "java( com.kt.giga.home.b2b.entity.BizContract.getCalculateCapacityByMb(bizContract.getCapacityUnitCd(), bizContract.getCapacity()) )"),
            @Mapping(target = "hubConnectCount", expression = "java( com.kt.giga.home.b2b.entity.BizContract.getHubConnectCount(bizContract.getCurrentServiceContracts()) )")
    })
    ContractInfo toContractInfo(BizContract bizContract);

    @Mappings({
            @Mapping(target = "mgrSeq", source = "sequence"),
            @Mapping(target = "username", source = "id")
    })
    B2bManager toB2bManager(Manager manager);

}
