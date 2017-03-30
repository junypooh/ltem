package com.kt.giga.home.b2b.common.constant;

import java.util.concurrent.TimeUnit;

/**
 * Created by junypooh on 2017-02-20.
 * <pre>
 * com.kt.giga.home.b2b.common.constant.CodeConstants
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-20 오후 1:54
 */
public class CodeConstants {

    /**
     * CACHE time out
     */
    public final static long CACHE_TIME_OUT = 5L;

    /**
     * CACHE time unit
     */
    public final static TimeUnit CACHE_TIME_UNIT = TimeUnit.MINUTES;

    /**
     * 서비스관리 > 계약 상세 수정
     *
     * name of the distributed cache map
     */
    public final static String CONTRACT_CACHE_MAP_NAME = "contractEdit";

    /**
     * 계정관리 > 기업 상세 수정
     *
     * name of the distributed cache map
     */
    public final static String COMPANY_CACHE_MAP_NAME = "companyEdit";

    /**
     * 계정관리 > 계정 상세 수정 및 가려진 정보 해제
     *
     * name of the distributed cache map
     */
    public final static String ACCOUNT_CACHE_MAP_NAME = "accountEdit";

    /**
     * 서비스관리 > 배너 상세 수정 및 가려진 정보 해제
     *
     * name of the distributed cache map
     */
    public final static String BANNER_CACHE_MAP_NAME = "bannerEdit";


}
