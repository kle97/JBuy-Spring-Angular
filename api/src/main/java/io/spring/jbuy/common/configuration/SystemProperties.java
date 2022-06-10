package io.spring.jbuy.common.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "system")
public class SystemProperties {
    private String secretKey;
    private String[] urls;
    private boolean tls;
    private String currentVersion;
    private String[] webIgnoreEndpoints = new String[0];
    private Set<String> publicEndpoints;
    private String adminEmail;
    private String adminPassword;
    private String testPassword;
    private boolean cookieSecure;
    private String cookieSameSite;
    private String namingStrategy;
    private Integer loginAttemptLimit;
    private String hazelcastInstanceName;
    private String hazelcastHost;
    private Integer hazelcastPort;
    private String hazelcastManUrl;
    private boolean hazelcastManEnabled;
    private Integer sessionTimeout;
}
