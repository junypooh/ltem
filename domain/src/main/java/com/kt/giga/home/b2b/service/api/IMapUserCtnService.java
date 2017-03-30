package com.kt.giga.home.b2b.service.api;

import com.kt.giga.home.b2b.configurations.feign.IFeignServices;
import com.kt.giga.home.b2b.configurations.feign.ManagerApiFeignConfiguration;
import com.kt.giga.home.b2b.domain.integration.api.MapUserCtnRequest;
import com.kt.giga.home.b2b.domain.integration.api.MapUserCtnResponse;
import com.kt.giga.home.b2b.domain.integration.api.UnmapUserCtnRequest;
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
        fallbackFactory = IMapUserCtnService.MapUserCtnServiceFallbackFactory.class
)
public interface IMapUserCtnService {
    @RequestMapping(value = "/manager-api/ghms/ltem/ltemConfirm", method = RequestMethod.POST, consumes = "application/json")
    MapUserCtnResponse request(MapUserCtnRequest mapUserCtnRequest);

    @RequestMapping(value = "/manager-api/ghms/ltem/ltemUserMappCcl", method = RequestMethod.POST, consumes = "application/json")
    MapUserCtnResponse unmap(UnmapUserCtnRequest unmapUserCtnRequest);

    @Slf4j
    @Component
    class MapUserCtnServiceFallbackFactory implements FallbackFactory<IMapUserCtnService> {

        @Override
        public IMapUserCtnService create(Throwable cause) {
            return new IMapUserCtnService() {
                @Override
                public MapUserCtnResponse request(MapUserCtnRequest mapUserCtnRequest) {
                    if (cause != null) {
                        log.error("IMapUserCtnService#request failed due to :", cause);
                    } else {
                        log.error("IMapUserCtnService#request failed due to : Hystrix timeout.");
                    }
                    return new MapUserCtnResponse(MapUserCtnResponse.Result.FAILURE);
                }

                @Override
                public MapUserCtnResponse unmap(UnmapUserCtnRequest unmapUserCtnRequest) {
                    if (cause != null) {
                        log.error("IMapUserCtnService#ummap failed due to :", cause);
                    } else {
                        log.error("IMapUserCtnService#ummap failed due to : Hystrix timeout.");
                    }
                    return new MapUserCtnResponse(MapUserCtnResponse.Result.FAILURE);
                }
            };
        }
    }
}
