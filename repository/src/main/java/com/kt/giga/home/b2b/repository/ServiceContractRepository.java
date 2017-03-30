package com.kt.giga.home.b2b.repository;

import com.kt.giga.home.b2b.domain.DeviceStatus;
import com.kt.giga.home.b2b.entity.BizContract;
import com.kt.giga.home.b2b.entity.ServiceContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 *
 *
 *
 */
public interface ServiceContractRepository extends JpaRepository<ServiceContract, Long>, JpaSpecificationExecutor {

    @Query("SELECT COUNT(s) FROM ServiceContract  s WHERE s.serialNumber in (?1) and s.currentBizContract = ?2 ")
    Long countBySerialNumbersAndCurrentBizContract(List<Long> numbers, BizContract bizContract);

    @Query("SELECT s FROM ServiceContract s WHERE s.serialNumber in (?1) and s.currentBizContract = ?2 ")
    List<ServiceContract> findBySerialNumbersAndCurrentBizContract(List<Long> serialNumbers, BizContract bizContract);

    List<ServiceContract> findBySerialNumberIn(List<Long> serialNumbers);

    @Query("SELECT s FROM ServiceContract s WHERE s.imei = ?1 and s.deviceStatus = ?2 ")
    ServiceContract findOpenByImei(String imei, Short deviceStatus);

    @Query("SELECT s FROM ServiceContract s WHERE s.imei = ?1 and s.deviceStatus >= ?2 and s.deviceStatus <= ?3 " )
    ServiceContract findCloseByImei(String imei, Short deviceStatusStart, Short deviceStatusEnd);

    ServiceContract findOneByImei(String imei);

    ServiceContract findOneBySerialNumber(Long serialNumber);

    ServiceContract findOneBySvcTgtSeq(Long svcTgtSeq);

    Page<ServiceContract> findByCurrentBizContract(BizContract bizContract, Pageable Pageable);

    long countByDevicePlaceIsNull();

    long countByHistoricBizContractsIn(Set<BizContract> historicBizContracts);

    long countByHistoricBizContractsInAndDevicePlaceIsNull(Set<BizContract> historicBizContracts);

    long countByIsActiveTrue();

    long countByHistoricBizContractsInAndIsActiveTrue(Set<BizContract> historicBizContracts);
}
