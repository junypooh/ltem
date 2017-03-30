package com.kt.giga.home.b2b.service.api;

import com.kt.giga.home.b2b.configurations.feign.IFeignServices;
import com.kt.giga.home.b2b.configurations.feign.ManagerApiFeignConfiguration;
import com.kt.giga.home.b2b.domain.integration.api.ProvisionRequest;
import com.kt.giga.home.b2b.domain.integration.api.ProvisionResponse;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * com.kt.giga.home.b2b.service.api
 * <p>
 * Created by cecil on 2017. 2. 28..
 */
@FeignClient(
        name = IFeignServices.GATEWAY,
        configuration = ManagerApiFeignConfiguration.class,
        fallbackFactory = IProvisionService.ProvisionServiceFallbackFactory.class
)
public interface IProvisionService {
    @RequestMapping(value = "/manager-api/ghms/ltem/ltemSubscription", method = RequestMethod.POST, consumes = "application/json")
    ProvisionResponse request(ProvisionRequest provisionRequest);

    @Slf4j
    @Component
    class ProvisionServiceFallbackFactory implements FallbackFactory<IProvisionService> {

        @Override
        public IProvisionService create(Throwable cause) {
            return provisionRequest -> {
                if (cause != null) {
                    log.error("IProvisionService#request failed due to :", cause);
                } else {
                    log.error("IProvisionService#request failed due to : Hystrix timeout.");
                }
                return new ProvisionResponse(ProvisionResponse.Result.FAILURE);
            };
        }
    }
}
