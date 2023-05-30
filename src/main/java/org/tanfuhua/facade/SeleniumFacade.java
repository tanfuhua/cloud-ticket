package org.tanfuhua.facade;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.tanfuhua.common.config.AppConfig;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: gaofubo
 * @date: 2021/5/9
 */
@Slf4j
@Component
@AllArgsConstructor
public class SeleniumFacade implements InitializingBean {

    private AppConfig appConfig;

    public ChromeDriver createChromeDriver() {
        List<String> userAgentList = appConfig.getChrome().getUserAgentList();
        int userAgentIndex = ThreadLocalRandom.current().nextInt(userAgentList.size());
        String userAgent = userAgentList.get(userAgentIndex);
        log.info("使用的UserAgent -> 索引：{},UserAgent:{}", userAgentIndex, userAgent);

        ChromeOptions chromeOptions = new ChromeOptions();
        appConfig.getChrome().getDriverOptionList().forEach(chromeOptions::addArguments);
        chromeOptions.addArguments(userAgent);
        try {
            ChromeDriver chromeDriver = new ChromeDriver(chromeOptions);
            appConfig.getChrome().getDriverCdpCommandMap().forEach(chromeDriver::executeCdpCommand);
            chromeDriver.manage().window().maximize();
            return chromeDriver;
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            throw t;
        }

    }

    public WebDriverWait createWebDriverFastWait(ChromeDriver chromeDriver) {
        return new WebDriverWait(chromeDriver, Duration.ofSeconds(appConfig.getChrome().getDriverFastWaitSecond()));
    }

    public WebDriverWait createWebDriverSlowWait(ChromeDriver chromeDriver) {
        return new WebDriverWait(chromeDriver, Duration.ofSeconds(appConfig.getChrome().getDriverSlowWaitSecond()));
    }

    @Override
    public void afterPropertiesSet() {
        URL url = appConfig.getClass().getResource(appConfig.getChrome().getDriverPath());
        if (Objects.isNull(url)) {
            throw new RuntimeException(String.format("路径：%s不存在driver", appConfig.getChrome().getDriverPath()));
        }
        System.setProperty("webdriver.chrome.driver", url.getFile());
    }
}
