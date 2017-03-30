package com.kt.giga.home.b2b.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by DaDa on 2017-01-17.
 */
@Documented
@Constraint(validatedBy = PasswordPatternValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordPattern {
    String message() default "{PasswordPattern}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
