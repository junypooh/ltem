package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.ContractUsageInfo;
import com.kt.giga.home.b2b.entity.BizContUsage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Created by junypooh on 2017-03-16.
 * <pre>
 * com.kt.giga.home.b2b.mapper.BizContUsageMapper
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-16 오전 11:26
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BizContUsageMapper {

    @Mapping(target = "viewCapacity", expression = "java( com.kt.giga.home.b2b.entity.BizContUsage.getCalculateCapacityByMb(bizContUsage.getCapacityUnitCd(), bizContUsage.getCapacity()) )")
    ContractUsageInfo toContractUsageInfo(BizContUsage bizContUsage);

    List<ContractUsageInfo> toContractUsageListInfo(List<BizContUsage> bizContUsage);
}
