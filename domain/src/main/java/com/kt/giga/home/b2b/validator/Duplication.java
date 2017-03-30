package com.kt.giga.home.b2b.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by DaDa on 2017-03-06.
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = DuplicationValidator.class)
public @interface Duplication {
    String message() default "{Password}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String confirmFieldColumn();

    String fieldColumn();

    String errorMessage();

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Duplication[] value();
    }
}
