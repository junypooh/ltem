package com.kt.giga.home.b2b.validator;

import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by DaDa on 2017-03-06.
 */
public class DuplicationValidator implements ConstraintValidator<Duplication, Object> {
    private String fieldColumn;
    private String confirmFieldColumn;
    private String errorMessage;


    @Override
    public void initialize(Duplication constraintAnnotation) {
        fieldColumn = constraintAnnotation.fieldColumn();
        confirmFieldColumn = constraintAnnotation.confirmFieldColumn();
        errorMessage = constraintAnnotation.errorMessage();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            final Object confirmFieldColumnObj = BeanUtils.getProperty(value, confirmFieldColumn);
            final Object fieldColumnObj = BeanUtils.getProperty(value, fieldColumn);

            String errorMessage = this.errorMessage; //change to be property driven

            boolean fieldsMatch;

            fieldsMatch = fieldColumnObj == null && confirmFieldColumnObj == null || fieldColumnObj != null && fieldColumnObj.equals(confirmFieldColumnObj);
            //fieldsMatch = passwordObj.toString().trim().equals("") && confirmPasswordObj.toString().trim().equals("")  || passwordObj.toString().trim().equals("") && passwordObj.equals(confirmPasswordObj);

            if (!fieldsMatch) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(errorMessage).addPropertyNode(confirmFieldColumn).addConstraintViolation();
                return false;
            }

            return true;
        }
        catch (final Exception ignore) {
        }
        return false;
    }
}
