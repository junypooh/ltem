package com.kt.giga.home.b2b.services;

import java.time.LocalDateTime;

/**
 * Created by cecil on 2017. 1. 13..
 */
public class CurrentDateTimeService implements IDateTimeService {
    @Override
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
