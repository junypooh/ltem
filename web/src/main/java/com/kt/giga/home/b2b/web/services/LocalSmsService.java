package com.kt.giga.home.b2b.web.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by junypooh on 2017-03-13.
 * <pre>
 * com.kt.giga.home.b2b.web.services.LocalSmsService
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-13 오후 2:41
 */
@Profile("local")
@Service
@Slf4j
public class LocalSmsService implements ISmsService {

    @Override
    public Optional<String> sendMessage(String sendFrom, String sendTo, String title, String message) {
        return Optional.of("111111111111111");
    }

}