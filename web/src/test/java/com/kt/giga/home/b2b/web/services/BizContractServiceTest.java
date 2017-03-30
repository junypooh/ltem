package com.kt.giga.home.b2b.web.services;

import com.google.common.collect.Lists;
import com.kt.giga.home.b2b.domain.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * com.kt.giga.home.b2b.web.services
 * <p>
 * Created by cecil on 2017. 2. 11..
 */
@RunWith(SpringRunner.class)
public class BizContractServiceTest {

    @InjectMocks
    private BizContractService bizContractService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(bizContractService);
    }

    @Test
    public void getUnderRoleTest() {
        List<Role> lowerRoles = bizContractService.getRolesLessThan(Role.SUPERVISOR);
        ArrayList<Role> expected = Lists.newArrayList(Role.STAFF);

        assertTrue(lowerRoles.equals(expected));
    }
}