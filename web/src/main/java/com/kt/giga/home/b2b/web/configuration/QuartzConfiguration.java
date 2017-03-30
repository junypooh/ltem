package com.kt.giga.home.b2b.web.configuration;

import com.kt.giga.home.b2b.quartz.AutowiringSpringBeanJobFactory;
import com.kt.giga.home.b2b.job.MyJob;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by junypooh on 2017-01-16.
 */
@ConditionalOnBean({DataSource.class, PlatformTransactionManager.class})
@Configuration
public class QuartzConfiguration {

    // this data source points to the database that contains Quartz tables
    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SchedulerFactoryBean quartzScheduler() {
        SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();

        quartzScheduler.setQuartzProperties(quartzProperties());
        quartzScheduler.setDataSource(dataSource);
        quartzScheduler.setTransactionManager(transactionManager);
        quartzScheduler.setOverwriteExistingJobs(true);

        // Custom job factory of spring with DI support for @Autowired
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        quartzScheduler.setJobFactory(jobFactory);

        Trigger[] triggers = {
                processMyJobTrigger().getObject(),
        };

        quartzScheduler.setTriggers(triggers);

        return quartzScheduler;
    }

    @Bean
    public JobDetailFactoryBean processMyJob() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(MyJob.class);
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean
    // Configure cron to fire trigger every 1 minute
    public CronTriggerFactoryBean processMyJobTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(processMyJob().getObject());
        cronTriggerFactoryBean.setCronExpression("0 0 23 L * ?"); // 매월 말일 23시 0분 0초
        return cronTriggerFactoryBean;
    }

    @Bean
    public Properties quartzProperties() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("properties/quartz.properties"));
        Properties properties;

        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();
        } catch (IOException e) {
            throw new RuntimeException("Unable to load quartz.properties", e);
        }

        return properties;
    }
}
