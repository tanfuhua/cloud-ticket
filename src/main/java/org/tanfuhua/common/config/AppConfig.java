package org.tanfuhua.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @author gaofubo
 * @date 2023/3/24
 */
@ConfigurationProperties(prefix = "app")
@Configuration
@Setter
@Getter
public class AppConfig {

    private String chromeDriverPath;

    private Map<String, Object> seleniumMap;

    private List<String> chromeOptionList;

    private List<String> userAgentList;

    private int webDriverWaitSecond;

    private int checkSubmitOrderWaitSecond;

    private String kyfwUrl;

    private Map<String, String> kyfwUrlRefererMap;


}
