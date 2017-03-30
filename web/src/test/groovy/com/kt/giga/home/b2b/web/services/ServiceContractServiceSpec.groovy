package com.kt.giga.home.b2b.web.services

import com.kt.giga.home.b2b.domain.DeviceStatus
import com.kt.giga.home.b2b.domain.UserInformation
import com.kt.giga.home.b2b.domain.integration.api.MapUserCtnResponse
import com.kt.giga.home.b2b.domain.integration.api.ProvisionResponse
import com.kt.giga.home.b2b.entity.DeviceModel
import com.kt.giga.home.b2b.entity.ServiceContract
import com.kt.giga.home.b2b.mapper.ServiceContractMapper
import com.kt.giga.home.b2b.mapper.ServiceContractUnmaskedMapper
import com.kt.giga.home.b2b.repository.ServiceContractRepository
import com.kt.giga.home.b2b.service.IBssIotSenderService
import com.kt.giga.home.b2b.service.api.IMapUserCtnService
import com.kt.giga.home.b2b.service.api.IProvisionService
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader
import spock.lang.Specification
import spock.mock.DetachedMockFactory

/**
 * com.kt.giga.home.b2b.web.services
 *
 * Created by cecil on 2017. 3. 23..
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader)
class ServiceContractServiceSpec extends Specification {

    @Configuration
    static class ServiceContractServiceContextConfiguration {

        def mockFactory = new DetachedMockFactory()

        @Bean
        public ServiceContractService serviceContractService() {
            return new ServiceContractService();
        }

        @Bean
        public ServiceContractRepository serviceContractRepository() {
            return mockFactory.Stub(ServiceContractRepository);
        }

        @Bean
        public ServiceContractMapper serviceContractMapper() {
            return Mockito.mock(ServiceContractMapper.class);
        }

        @Bean
        public ServiceContractUnmaskedMapper serviceContractUnmaskedMapper() {
            return Mockito.mock(ServiceContractUnmaskedMapper.class);
        }

        @Bean
        IProvisionService provisionService() {
            return mockFactory.Mock(IProvisionService)
        }

        @Bean
        public IMapUserCtnService mapUserCtnService() {
            return mockFactory.Mock(IMapUserCtnService);
        }

        @Bean
        public IBssIotSenderService bssIotSenderService() {
            return mockFactory.Mock(IBssIotSenderService);
        }

        @Bean
        public ISmsService smsService() {
            return mockFactory.Mock(ISmsService)
        }
    }

    UserInformation userInformation

    @Autowired
    ServiceContractService serviceContractService

    @Autowired
    ServiceContractRepository serviceContractRepository

    @Autowired
    IProvisionService provisionService

    @Autowired
    IMapUserCtnService mapUserCtnService

    @Autowired
    ISmsService smsService

    @Autowired
    IBssIotSenderService bssIotSenderService

    def setup() {
        userInformation = UserInformation.builder()
                .userCtn("01012341234")
                .userName("홍길동")
                .devicePlace("주소")
                .status(true)
                .build()

    }

    def "DMS로부터 인증 정보 수신 전에 유저 매핑 요청"() {

        given:

        ServiceContract serviceContract = new ServiceContract(serialNumber: 1234L, isActive: true, deviceStatus: DeviceStatus.CONTRACT_RECEIVED)
        serviceContractRepository.findOneBySerialNumber(_ as Long) >> serviceContract

        when:

        serviceContractService.updateSvcContractInfo(1234L, userInformation)

        then: "IllegalStateException 발생"

        thrown IllegalStateException
    }

    def "이미 이용자와 매핑되었는데 사용자 해제 없이 전화번호 변경"() {

        given:

        ServiceContract serviceContract = new ServiceContract(serialNumber: 1234L, isActive: true, deviceStatus: DeviceStatus.PROVISION_COMPLETED, isUserMapped: true, userCtn: "01043214321")
        serviceContractRepository.findOneBySerialNumber(_ as Long) >> serviceContract

        when:

        serviceContractService.updateSvcContractInfo(1234L, userInformation)

        then: "IllegalStateException 발생"

        thrown IllegalStateException
    }


    def "계약 만료 상태인 경우"() {

        given:

        ServiceContract serviceContract = new ServiceContract(serialNumber: 1234L, isActive: true, deviceStatus: DeviceStatus.EXPIRATION_RECEIVED, isUserMapped: true, userCtn: "01043214321")
        serviceContractRepository.findOneBySerialNumber(_ as Long) >> serviceContract

        when:

        serviceContractService.updateSvcContractInfo(1234L, userInformation)

        then: "IllegalStateException 발생"
        thrown IllegalStateException
    }

    def "사용자 등록 안된 상태에서 일시정지로 등록"() {

        given:

        userInformation = new UserInformation(status: false)
        ServiceContract serviceContract = new ServiceContract(serialNumber: 1234L, isActive: true, deviceStatus: DeviceStatus.EXPIRATION_RECEIVED, isUserMapped: true, userCtn: "01043214321")
        serviceContractRepository.findOneBySerialNumber(_ as Long) >> serviceContract

        when:
        serviceContractService.updateSvcContractInfo(1234L, userInformation)

        then: "IllegalStateException 발생"
        thrown IllegalStateException

    }

    def "기기 인증 정보 수신 전에 사용자 등록"() {

        given:

        ServiceContract serviceContract = new ServiceContract(serialNumber: 1234L, isActive: false, deviceStatus: DeviceStatus.CONTRACT_RECEIVED)
        serviceContractRepository.findOneBySerialNumber(_ as Long) >> serviceContract

        when:

        serviceContractService.updateSvcContractInfo(1234L, userInformation)

        then: "IllegalStateException"

        thrown IllegalStateException
    }

    def "이미 활성화된 상태에서 사용자 등록"() {

        given:

        DeviceModel deviceModel = new DeviceModel(name: "MC03-611ZM", code: "K7004292")

        ServiceContract serviceContract = new ServiceContract(serialNumber: 1234L, isActive: true, deviceStatus: DeviceStatus.PROVISION_RECEIVED, isUserMapped: false
                , isSuspended: false, deviceModel: deviceModel, ctn: "01012341111",iccId: "123", imei: "123", secretKey: "1001")

        serviceContractRepository.findOneBySerialNumber(_ as Long) >> serviceContract

        when:
        serviceContractService.updateSvcContractInfo(1234L, userInformation)

        then: "Success"

        1 * provisionService.request(_) >> Stub(ProvisionResponse) {
            getServiceTargetSequence() >> "4321"
            gerResult() >> ProvisionResponse.Result.SUCCESS
        }
        1 * mapUserCtnService.request(_) >> Stub(MapUserCtnResponse) { getResult() >> MapUserCtnResponse.Result.SUCCESS }
        1 * smsService.sendMessage("100", _ as String, _ as String, _ as String ) >> Optional.of("transactionId")
        0 * bssIotSenderService.serviceActivation(_)
    }
}
