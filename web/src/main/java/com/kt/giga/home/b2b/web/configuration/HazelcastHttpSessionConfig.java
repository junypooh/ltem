package com.kt.giga.home.b2b.web.configuration;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.apache.bval.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.session.hazelcast.HazelcastSessionRepository;
import org.springframework.session.hazelcast.PrincipalNameExtractor;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * com.kt.giga.home.b2b.web.configuration
 * <p>
 * Created by cecil on 2017. 2. 5..
 */
@Slf4j
@EnableHazelcastHttpSession
@ConfigurationProperties("hazelcast")
public class HazelcastHttpSessionConfig {

    @NotNull
    @NotEmpty(message = "Hazelcast Cluster 서버 목록 설정이 필요합니다.")
    private List<String> servers = new ArrayList<>();

    @NotNull(message = "Hazelcast Cluster Group 이름 설정이 필요합니다.")
    private String groupName;

    @NotNull(message = "Hazelcast Cluster Group 비밀번호 설정이 필요합니다.")
    private String groupPassword;

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupPassword(String groupPassword) {
        this.groupPassword = groupPassword;
    }

    public List<String> getServers() {
        return servers;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupPassword() {
        return groupPassword;
    }

    @Bean
    public Config config() {

        log.debug("Hazelcast GroupName : {}", groupName);
        log.debug("Hazelcast GroupPassword : {}", groupPassword);
        log.debug("Hazelcast Server List : {}", servers);

        Config config = new Config();
        config.getGroupConfig().setName(groupName).setPassword(groupPassword);

        JoinConfig joinConfig = config.getNetworkConfig().getJoin();

        joinConfig.getMulticastConfig().setEnabled(false);

        if (servers != null && !servers.isEmpty()) {
            joinConfig.getTcpIpConfig().setEnabled(true).setMembers(servers);
        }


        return config;
    }

    @Bean(destroyMethod = "shutdown")
    public HazelcastInstance hazelcastInstance() {
        MapAttributeConfig attributeConfig = new MapAttributeConfig()
                .setName(HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
                .setExtractor(PrincipalNameExtractor.class.getName());


        Config config = config();

        SerializerConfig serializer = new SerializerConfig()
                .setImplementation(new ObjectStreamSerializer())
                .setTypeClass(Object.class);

        config.getSerializationConfig()
              .addSerializerConfig(serializer);

        config.getMapConfig("spring:session:sessions")
              .addMapAttributeConfig(attributeConfig)
              .addMapIndexConfig(new MapIndexConfig(
                      HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE, false));
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(Hazelcast.class.getClassLoader());
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        Thread.currentThread().setContextClassLoader(currentClassLoader);
        return hazelcastInstance;
    }

}
