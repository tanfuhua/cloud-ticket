package org.tanfuhua.cloudticket.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "proxy")
@Data
public class ProxyConfig {
    private List<String> serverList;
}
