package com.kt.giga.home.b2b.validator;


import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by DaDa on 2017-01-05.
 */
public class PasswordValidator implements ConstraintValidator<Password, Object> {

    private String confirmPassword;
    private String password;

    @Override
    public void initialize(Password constraintAnnotation) {
        confirmPassword = constraintAnnotation.confirmPassword();
        password = constraintAnnotation.password();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            final Object confirmPasswordObj = BeanUtils.getProperty(value, confirmPassword);
            final Object passwordObj = BeanUtils.getProperty(value, password);

            String errorMessage = "비밀번호가 일치하지 않습니다. 다시 입력해주세요."; //change to be property driven

            boolean fieldsMatch;

            fieldsMatch = passwordObj == null && confirmPasswordObj == null || passwordObj != null && passwordObj.equals(confirmPasswordObj);
            //fieldsMatch = passwordObj.toString().trim().equals("") && confirmPasswordObj.toString().trim().equals("")  || passwordObj.toString().trim().equals("") && passwordObj.equals(confirmPasswordObj);

            if (!fieldsMatch) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(errorMessage).addPropertyNode(confirmPassword).addConstraintViolation();
                return false;
            }

            return true;
        }
        catch (final Exception ignore) {
        }
        return false;
    }
}
