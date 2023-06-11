package org.tanfuhua.facade;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
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

    public WebDriver createChromeDriver() {

        List<String> userAgentList = appConfig.getBrowser().getUserAgentList();
        int userAgentIndex = ThreadLocalRandom.current().nextInt(userAgentList.size());
        String userAgent = userAgentList.get(userAgentIndex);
        log.info("使用的UserAgent -> 索引：{},UserAgent:{}", userAgentIndex, userAgent);

        ChromeOptions chromeOptions = new ChromeOptions();
        appConfig.getBrowser().getDriverOptionList().forEach(chromeOptions::addArguments);
        chromeOptions.addArguments(userAgent);
        try {
            ChromeDriver chromeDriver = new ChromeDriver(chromeOptions);
            appConfig.getBrowser().getDriverCdpCommandMap().forEach(chromeDriver::executeCdpCommand);
            chromeDriver.manage().window().maximize();
            return chromeDriver;
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            throw t;
        }

    }

    public WebDriver createEdgeDriver() {
        List<String> userAgentList = appConfig.getBrowser().getUserAgentList();
        int userAgentIndex = ThreadLocalRandom.current().nextInt(userAgentList.size());
        String userAgent = userAgentList.get(userAgentIndex);
        log.info("使用的UserAgent -> 索引：{},UserAgent:{}", userAgentIndex, userAgent);

        EdgeOptions edgeOptions = new EdgeOptions();
        appConfig.getBrowser().getDriverOptionList().forEach(edgeOptions::addArguments);
        edgeOptions.addArguments(userAgent);
        try {
            EdgeDriver edgeDriver = new EdgeDriver(edgeOptions);
            appConfig.getBrowser().getDriverCdpCommandMap().forEach(edgeDriver::executeCdpCommand);
            edgeDriver.manage().window().maximize();
            return edgeDriver;
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            throw t;
        }

    }

    public WebDriverWait createWebDriverFastWait(WebDriver webDriver) {
        return new WebDriverWait(webDriver, Duration.ofSeconds(appConfig.getBrowser().getDriverFastWaitSecond()));
    }

    public WebDriverWait createWebDriverSlowWait(WebDriver webDriver) {
        return new WebDriverWait(webDriver, Duration.ofSeconds(appConfig.getBrowser().getDriverSlowWaitSecond()));
    }

    @Override
    public void afterPropertiesSet() {
        String type = appConfig.getBrowser().getType();
        String driverKey = "edge".equalsIgnoreCase(type) ? appConfig.getEdge().getDriverKey() : appConfig.getChrome().getDriverKey();
        String driverPath = "edge".equalsIgnoreCase(type) ? appConfig.getEdge().getDriverPath() : appConfig.getChrome().getDriverPath();
        log.info("driverPath：{}", driverPath);
        URL url = appConfig.getClass().getResource(driverPath);
        if (Objects.isNull(url)) {
            throw new RuntimeException(String.format("路径：%s不存在driver", appConfig.getChrome().getDriverPath()));
        }
        System.setProperty(driverKey, url.getFile());
    }
}
