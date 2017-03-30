package com.kt.giga.home.b2b.core.configuration;

import com.kt.giga.home.b2b.core.bssiot.service.IBssIotService;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import javax.xml.ws.Endpoint;

/**
 * Created by 민우 on 2016-12-06.
 */
@Configuration
public class CxfServletConfiguration extends SpringBootServletInitializer {

    private static final String SERVLET_MAPPING_URL_PATH     = "/bss/*";
    private static final String SERVICE_NAME_URL_PATH        = "/contract";
    private static final String APPLICATION_MAPPING_URL_PATH = "/*";


    @Autowired
    private IBssIotService bssIotService;

    @Bean
    public ServletRegistrationBean dispatcherServlet() {
        return new ServletRegistrationBean(new CXFServlet(), SERVLET_MAPPING_URL_PATH);
    }

    @Bean
    public ServletRegistrationBean RsRegistrationBean(ApplicationContext applicationContext) {
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        return new ServletRegistrationBean(servlet, APPLICATION_MAPPING_URL_PATH);
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public Endpoint endpoint() {
        Object       implementor = bssIotService;
        EndpointImpl endpoint    = new EndpointImpl(springBus(), implementor);
        endpoint.publish(SERVICE_NAME_URL_PATH);
        return endpoint;
    }

}
