package com.kt.giga.home.b2b.gateway.configurations;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * com.kt.giga.home.b2b.gateway.configurations
 * <p>
 * Created by cecil on 2017. 3. 10..
 */
@Slf4j
@Setter
@Getter
@ConfigurationProperties("ssl")
@Configuration
public class ServletContainerConfiguration {

    private String hostname;
    private int    port;
    private String keyStore;
    private String keyStorePassword;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();

        tomcat.addAdditionalTomcatConnectors(getSslConnector());
        return tomcat;
    }

    private Connector[] getSslConnector() {
        List<Connector> result = new ArrayList<>();

        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("https");
        connector.addSslHostConfig(getSslHostConfig());
        connector.setPort(port);
        connector.setSecure(true);
        connector.setAttribute("SSLEnabled", true);
        result.add(connector);

        return result.toArray(new Connector[]{});
    }

    private SSLHostConfig getSslHostConfig() {
        SSLHostConfig sslHostConfig = new SSLHostConfig();
        sslHostConfig.setHostName(hostname);
        sslHostConfig.setConfigType(SSLHostConfig.Type.JSSE);
        sslHostConfig.setCertificateKeystoreFile(keyStore);
        sslHostConfig.setCertificateKeystorePassword(keyStorePassword);
        return sslHostConfig;
    }
}
