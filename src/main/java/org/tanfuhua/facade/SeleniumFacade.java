package org.tanfuhua.facade;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.tanfuhua.common.config.AppConfig;

import java.time.Duration;
import java.util.List;
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
        List<String> userAgentList = appConfig.getUserAgentList();
        int userAgentIndex = ThreadLocalRandom.current().nextInt(userAgentList.size());
        String userAgent = userAgentList.get(userAgentIndex);
        log.info("使用的UserAgent -> 索引：{},UserAgent:{}", userAgentIndex, userAgent);

        ChromeOptions chromeOptions = new ChromeOptions();
        appConfig.getChromeOptionList().forEach(chromeOptions::addArguments);
        chromeOptions.addArguments(userAgent);
        try {
            ChromeDriver chromeDriver = new ChromeDriver(chromeOptions);
            chromeDriver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", appConfig.getSeleniumMap());
            chromeDriver.manage().window().maximize();
            return chromeDriver;
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            throw t;
        }

    }

    public WebDriverWait createWebDriverFastWait(ChromeDriver chromeDriver) {
        return new WebDriverWait(chromeDriver, Duration.ofSeconds(appConfig.getWebDriverFastWaitSecond()));
    }
    public WebDriverWait createWebDriverSlowWait(ChromeDriver chromeDriver) {
        return new WebDriverWait(chromeDriver, Duration.ofSeconds(appConfig.getWebDriverSlowWaitSecond()));
    }

    @Override
    public void afterPropertiesSet() {
        System.setProperty("webdriver.chrome.driver", appConfig.getChromeDriverPath());
    }
}
