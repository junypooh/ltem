package com.kt.giga.home.b2b.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by DaDa on 2017-01-05.
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public void initialize(Phone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNo, ConstraintValidatorContext context) {
        /*
        if(phoneNo == null || phoneNo == ""){
            return false;
        }
        */
        if (phoneNo.matches("\\d{10,11}")) return true;
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{4}[-\\.\\s]\\d{4}")) return true;
        else return false;
    }
}
