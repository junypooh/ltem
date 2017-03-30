package com.kt.giga.home.b2b.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by junypooh on 2017-02-10.
 * <pre>
 * com.kt.giga.home.b2b.annotation.ExcelFieldName
 *
 * 엑셀파일 다운로드 처리를 위한 필드명 매핑 인터페이스
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-10 오후 12:29
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelFieldName {

    /**
     * 엑셀 필드 명
     */
    String name() default "";

    /**
     * 필드 순서
     */
    int order() default -1;

    /**
     * boolean 필드 일 경우 엑셀에 노출한 값 (/ 로 구분)
     * ex. 예/아니오
     */
    String booleanValue() default "";
}
