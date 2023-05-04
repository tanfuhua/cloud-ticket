package org.tanfuhua.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author gaofubo
 * @date 2023/3/24
 */
@ConfigurationProperties(prefix = "lowcode")
@Configuration
@Setter
@Getter
public class LowcodeConfig {

    private String username;
    private String password;
    private String config;
    private String schema;
    private String packages;

}
