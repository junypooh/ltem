package com.kt.giga.home.b2b.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by junypooh on 2017-01-16.
 */
@Slf4j
public class MyJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.debug(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}
