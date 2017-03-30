package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.AccountInfo;
import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.entity.Manager;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Created by DaDa on 2017-02-23.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(AccountManagerMapperDecorator.class)
public interface AccountManagerMapper {

    List<B2bManager> toB2bManagerList(List<Manager> managers);

    @Mapping(target = "companyCd", source = "company.code")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "mgrSeq", source = "sequence")
    @Mapping(target = "username", source = "id")
    @Mapping(target = "mgrStatusCd", expression = "java( com.kt.giga.home.b2b.entity.Manager.Status.getStatus(manager.getMgrStatusCd()).getStatusCd() )")
    B2bManager toB2bManager(Manager manager);


    @Mapping(target = "companyCd", source = "company.code")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "userId", source = "id")
    @Mapping(target = "mgrStatusCd", expression = "java( com.kt.giga.home.b2b.entity.Manager.Status.getStatus(manager.getMgrStatusCd()).getStatusCd() )")
    @Mapping(target = "mgrSeq", source = "sequence")
    AccountInfo toAccountInfo(Manager manager);
}
