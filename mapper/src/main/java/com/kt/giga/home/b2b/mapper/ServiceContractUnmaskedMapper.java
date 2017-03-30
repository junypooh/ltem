package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.ContractInfo;
import com.kt.giga.home.b2b.domain.DeviceModelInfo;
import com.kt.giga.home.b2b.domain.SvcContractInfo;
import com.kt.giga.home.b2b.entity.BizContract;
import com.kt.giga.home.b2b.entity.DeviceModel;
import com.kt.giga.home.b2b.entity.ServiceContract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Created by alsdnfl on 2017-01-25.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceContractUnmaskedMapper {

    List<SvcContractInfo> toSvcContractInfoList(List<ServiceContract> serviceContracts);

    @Mappings({
            @Mapping(target = "svcContSerial", source = "serialNumber")
    })
    SvcContractInfo toSvcContractInfo(ServiceContract serviceContract);

    @Mappings({
            @Mapping(target = "managers", ignore = true),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "capacityUnitCd", expression = "java(com.kt.giga.home.b2b.domain.CapacityUnit.getCapacityUnit(bizContract.getCapacityUnitCd()).getUnit())"),
            @Mapping(target = "circuitCount", expression = "java( com.kt.giga.home.b2b.entity.BizContract.getCircuitCount(bizContract.getCurrentServiceContracts()) )"),
            @Mapping(target = "startDate", source = "startDate", dateFormat = "yyyyMMdd"),
            @Mapping(target = "endDate", source = "endDate", dateFormat = "yyyy.MM.dd"),
            @Mapping(target = "viewCapacity", expression = "java( com.kt.giga.home.b2b.entity.BizContract.getCalculateCapacityByMb(bizContract.getCapacityUnitCd(), bizContract.getCapacity()) )"),
            @Mapping(target = "hubConnectCount", expression = "java( com.kt.giga.home.b2b.entity.BizContract.getHubConnectCount(bizContract.getCurrentServiceContracts()) )")
    })
    ContractInfo toContractInfo(BizContract bizContract);

    DeviceModelInfo toDeviceModelInfo(DeviceModel deviceModel);
}
