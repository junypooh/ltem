package com.kt.giga.home.b2b.common.configuration;

import com.kt.giga.home.b2b.service.BssIotSenderWebService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * Created by alsdnfl on 2017-02-07.
 */
@Configuration
public class ClientConfigWebService {

    @Bean
    public Jaxb2Marshaller marshaller() throws Exception {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.kt.giga.home.b2b.domain.bssiot.sender");
        return marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller) {
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setMarshaller(marshaller);
        webServiceTemplate.setUnmarshaller(marshaller);


        return webServiceTemplate;
    }

    @Bean
    public BssIotSenderWebService accountEndpoint(Jaxb2Marshaller marshaller, WebServiceTemplate webServiceTemplate) {
        BssIotSenderWebService client = new BssIotSenderWebService(webServiceTemplate);
        client.setDefaultUri("http://localhost:11000/ws");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

}
