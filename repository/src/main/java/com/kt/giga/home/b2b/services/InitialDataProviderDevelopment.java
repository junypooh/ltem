package com.kt.giga.home.b2b.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * com.kt.giga.home.b2b.services
 * <p>
 * Created by cecil on 2017. 3. 18..
 */
@Profile("!prd")
@Component
public class InitialDataProviderDevelopment implements IInitialDataProvider {
    @Override
    public void initData() {

    }
}
