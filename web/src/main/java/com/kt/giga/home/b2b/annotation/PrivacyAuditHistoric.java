package com.kt.giga.home.b2b.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by junypooh on 2017-03-21.
 * <pre>
 * com.kt.giga.home.b2b.annotation.PrivacyAuditHistoric
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-21 오후 5:08
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PrivacyAuditHistoric {

    String menuPath() default "";

    boolean unmasked() default false;

    boolean downloaded() default false;

    boolean isList() default false;

    Class returnClass();

    String targetInfo() default "";
}
