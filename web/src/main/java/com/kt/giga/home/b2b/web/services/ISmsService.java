package com.kt.giga.home.b2b.web.services;

import java.util.Optional;

/**
 * Created by junypooh on 2017-02-28.
 * <pre>
 * com.kt.giga.home.b2b.web.services.ISmsService
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-28 오전 10:45
 */
public interface ISmsService {

    /**
     * SMS 전송
     * @param sendFrom
     * @param sendTo
     * @param title
     * @param message
     */
    Optional<String> sendMessage(String sendFrom, String sendTo, String title, String message);
}
