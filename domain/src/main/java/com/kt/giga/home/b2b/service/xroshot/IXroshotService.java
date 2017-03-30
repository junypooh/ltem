package com.kt.giga.home.b2b.service.xroshot;

import com.kt.giga.home.b2b.configurations.feign.IFeignServices;
import com.kt.giga.home.b2b.configurations.feign.XroshotFeignConfiguration;
import com.kt.giga.home.b2b.domain.sms.SmsResponse;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * com.kt.giga.home.b2b.service.api
 * <p>
 * Created by cecil on 2017. 3. 8..
 */
@FeignClient(
        name = IFeignServices.GATEWAY,
        configuration = XroshotFeignConfiguration.class,
        fallbackFactory = IXroshotService.XroshotServiceFallbackFactory.class
)
public interface IXroshotService {
    /**
     * @param sendFrom 발신자 번호
     * @param sendTo   수신자 번호
     * @param title    제목
     * @param message  내용
     * @param type     메시지 타입. 1: SMS (80 bytes), 4: LMS (over 80 bytes)
     * @return
     */
    @RequestMapping(value = "/sms-service/xroshot-client/send", method = RequestMethod.GET, consumes = "application/json")
    SmsResponse request(@RequestParam("callerNumber") String sendFrom, @RequestParam("recipientNumber") String sendTo, @RequestParam("title") String title, @RequestParam("message") String message, @RequestParam("messageType") String type);

    @Slf4j
    @Component
    class XroshotServiceFallbackFactory implements FallbackFactory<IXroshotService> {

        @Override
        public IXroshotService create(Throwable cause) {
            return (sendFrom, sendTo, title, message, type) -> {
                if (cause != null) {
                    log.error("IXroshotService#request failed due to :", cause);
                } else {
                    log.error("IXroshotService#request failed due to : Hystrix timeout.");
                }
                return null;
            };
        }
    }
}
