package com.kt.giga.home.b2b.validator;

import lombok.RequiredArgsConstructor;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Created by 민우 on 2016-11-28.
 */
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Imei.ImeiValidator.class)
public @interface Imei {

    String message() default "invalid IMEI";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @RequiredArgsConstructor
    class ImeiValidator implements ConstraintValidator<Imei, Object> {

        @Override
        public void initialize(Imei constraintAnnotation) {
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {

            return !(value != null && !com.kt.giga.home.b2b.validator.ImeiValidator.validateImei(value.toString()));

        }
    }
}
