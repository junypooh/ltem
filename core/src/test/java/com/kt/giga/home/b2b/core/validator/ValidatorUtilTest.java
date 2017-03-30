package com.kt.giga.home.b2b.core.validator;

import com.kt.giga.home.b2b.validator.ImeiValidator;
import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by 민우 on 2016-12-01.
 */
public class ValidatorUtilTest {

    @Mock
    //DeviceOpenRequest deviceOpenRequest;
    private LocalValidatorFactoryBean localValidatorFactory;

    @Before
    public void setup() {
        localValidatorFactory = new LocalValidatorFactoryBean();
        localValidatorFactory.setProviderClass(HibernateValidator.class);
        localValidatorFactory.afterPropertiesSet();
    }

    @Test
    public void dmsValidatorTest() {
//
//        Mockito.when(deviceOpenRequest.getCtn()).thenReturn("010633611").toString();
//        Mockito.when(deviceOpenRequest.getModelName()).thenReturn("modelName").toString();
//        Mockito.when(deviceOpenRequest.getSecretKey()).thenReturn("secretKey").toString();
//        Mockito.when(deviceOpenRequest.getImei()).thenReturn("Imei").toString();
//
//        Set<ConstraintViolation<DeviceOpenRequest>> constraintViolations =      localValidatorFactory.validate(deviceOpenRequest);
//        assertFalse("ERROR", constraintViolations.isEmpty());
//
//        assertTrue(deviceOpenRequest != null);
    }

    @Test
    public void imeiValidatorRightTest() {
        assertTrue("OK imei", ImeiValidator.validateImei("353316078437871"));
    }

    @Test
    public void imeiValidatorWrongTest() {
        assertFalse("wrong imei", ImeiValidator.validateImei("353316078437872"));
    }

    @Test
    public void imeiValidatorShortTest() {
        assertFalse("15  under", ImeiValidator.validateImei("35331607843787"));
    }

    @Test
    public void imeiValidatorLargeTest() {
        assertFalse("15  over", ImeiValidator.validateImei("3533160784378712"));
    }

    @Test
    public void testNullValidationError() {
        //     final MyForm myForm= new MyForm ();
        //    deviceOpenRequest.setName(null);
    }
}