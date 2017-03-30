package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.ContractInfo;
import com.kt.giga.home.b2b.domain.DeviceModelInfo;
import com.kt.giga.home.b2b.domain.SvcContractInfo;
import com.kt.giga.home.b2b.domain.bssiot.receiver.PreOpenRstLs;
import com.kt.giga.home.b2b.entity.BizContract;
import com.kt.giga.home.b2b.entity.DeviceModel;
import com.kt.giga.home.b2b.entity.ServiceContract;
import org.mapstruct.*;

import java.util.List;

/**
 * Created by alsdnfl on 2017-01-25.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(ServiceContractMapperDecorator.class)
public interface ServiceContractMapper {

    @Mappings({
            @Mapping(source = "bizContract", target = "currentBizContract"),
            @Mapping(source = "dto.iccid", target = "iccId"),
            @Mapping(source = "dto.imei", target = "imei"),
            @Mapping(source = "dto.svccontno", target = "serialNumber"),
            @Mapping(source = "deviceModel", target = "deviceModel"),
            @Mapping(target = "deviceStatus", expression = "java( com.kt.giga.home.b2b.domain.DeviceStatus.CONTRACT_RECEIVED)"),
    })
    ServiceContract toServiceContract(PreOpenRstLs dto, BizContract bizContract, DeviceModel deviceModel);

    List<SvcContractInfo> toSvcContractInfoList(List<ServiceContract> serviceContracts);

    @Mappings({
            @Mapping(target = "iccId", ignore = true),
            @Mapping(target = "imei", ignore = true),
            @Mapping(target = "userCtn", ignore = true),
            @Mapping(target = "devicePlace", ignore = true),
            @Mapping(target = "userName", ignore = true),
            @Mapping(target = "svcContSerial", source = "serialNumber")
    })
    SvcContractInfo toSvcContractInfo(ServiceContract serviceContract);

    DeviceModelInfo toDeviceModelInfo(DeviceModel deviceModel);

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
}
