package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.sms.SmsResponse;
import com.kt.giga.home.b2b.service.xroshot.IXroshotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Optional;

/**
 * Created by junypooh on 2017-02-28.
 * <pre>
 * com.kt.giga.home.b2b.web.services.CtnCheckService
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-28 오전 10:50
 */
@Profile("!local")
@Service
@Slf4j
public class SmsService implements ISmsService {

    @Autowired
    private IXroshotService xroshotService;

    @Override
    public Optional<String> sendMessage(String sendFrom, String sendTo, String title, String message) {
        SmsResponse smsResponse = xroshotService.request(sendFrom, sendTo, title, message, (message.getBytes(Charset
                .forName("EUC-KR")).length <= 80) ? "1" : "4");
        log.debug("SmsResponse : {}", smsResponse);
        return (null != smsResponse ? Optional.of(smsResponse.getTransactionId()) : Optional.empty());
    }

}
