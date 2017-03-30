package com.kt.giga.home.b2b.audit;

import com.kt.giga.home.b2b.services.IDateTimeService;
import org.springframework.data.auditing.DateTimeProvider;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by cecil on 2017. 1. 13..
 */
public class AuditingDateTimeProvider implements DateTimeProvider {

    private final IDateTimeService dateTimeService;

    public AuditingDateTimeProvider(IDateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    @Override
    public Calendar getNow() {
        return GregorianCalendar.from(ZonedDateTime.of(dateTimeService.getCurrentDateTime(), ZoneId.systemDefault()));
    }
}
