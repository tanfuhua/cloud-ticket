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

    private Chrome chrome;

    private Edge edge;

    private Browser browser;

    private Kyfw kyfw;

    @Setter
    @Getter
    public static class Chrome {
        private String driverKey;
        private String driverPath;
    }

    @Setter
    @Getter
    public static class Browser {
        private String type;
        private List<String> driverOptionList;
        private Map<String, Map<String, Object>> driverCdpCommandMap;
        private int driverFastWaitSecond;
        private int driverSlowWaitSecond;
        private List<String> userAgentList;

    }

    @Setter
    @Getter
    public static class Edge {
        private String driverKey;
        private String driverPath;

    }

    @Setter
    @Getter
    public static class Kyfw {
        private String indexUrl;
        private Map<String, String> urlRefererMap;
        private int checkSubmitOrderWaitSecond;
    }
}
