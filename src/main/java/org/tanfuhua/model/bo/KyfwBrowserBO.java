package org.tanfuhua.model.bo;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.tanfuhua.common.config.AppConfig;
import org.tanfuhua.exception.BadRequestException;
import org.tanfuhua.model.entity.UserDO;
import org.tanfuhua.util.*;

import java.util.*;

/**
 * @author: gaofubo
 * @date: 2021/5/11
 */
@Slf4j
public class KyfwBrowserBO {
    private final WebDriver chromeDriver;
    private final WebDriverWait webDriverFastWait;
    private final WebDriverWait webDriverSlowWait;

    public KyfwBrowserBO(WebDriver chromeDriver, WebDriverWait webDriverFastWait, WebDriverWait webDriverSlowWait) {
        this.chromeDriver = chromeDriver;
        this.webDriverFastWait = webDriverFastWait;
        this.webDriverSlowWait = webDriverSlowWait;
        // 加载页面
        AppConfig appConfig = SpringUtil.getBean(AppConfig.class);
        chromeDriver.get(appConfig.getKyfw().getIndexUrl());
        KyfwWebElementUtil.hasLoadUntil(webDriverSlowWait, KyfwWebElementUtil.getJHeaderLogout());
        UserDO userDOCache = ContextUtil.UserHolder.getUserDOCache();
        logCookie(userDOCache.getKyfwAccount());
    }

    public synchronized void login(KyfwLoginBO kyfwLoginBO) {
        AppConfig appConfig = SpringUtil.getBean(AppConfig.class);
        // 加载页面
        chromeDriver.get(appConfig.getKyfw().getIndexUrl());
        // 检查：登录
        if (isShowUserName()) {
            logCookie(kyfwLoginBO.getKyfwAccount());
            return;
        }
        // 点击：页面右上角【登录】按钮
//        WebElement loginButton = webDriverSlowWait.until(KyfwWebElementUtil.getJHeaderLogin());
//        loginButton.click();
        // 点击：账号密码
        WebElement accountLogin = webDriverSlowWait.until(KyfwWebElementUtil.getLoginAccount());
        accountLogin.click();
        // 输入：用户名
        WebElement jUserName = webDriverSlowWait.until(KyfwWebElementUtil.getJUserName());
        KyfwWebElementUtil.setWebEleValue(chromeDriver, jUserName, kyfwLoginBO.getKyfwAccount());
        // 输入：密码
        WebElement jPassword = webDriverSlowWait.until(KyfwWebElementUtil.getJPassword());
        KyfwWebElementUtil.setWebEleValue(chromeDriver, jPassword, kyfwLoginBO.getKyfwPassword());
        // 图片验证码
//                    String jLoginImgSrc = browser.getWebDriverWait().until(KyfwWebElementUtil.getJLoginImgSrc());
        // 图片验证码热点区
//                    WebElement jPassCodeCoin = browser.getWebDriverWait().until(KyfwWebElementUtil.getJPassCodeCoin());
        // 自动识别验证码
//                    String codeHtml = getImageCode(jLoginImgSrc);
//                    ThreadUtil.sleep(1000);
//                    KyfwWebElementUtil.setWebEleInnerHTML(browser.getChromeDriver(), jPassCodeCoin, codeHtml);
        // 点击：登录
        WebElement jLogin = webDriverSlowWait.until(KyfwWebElementUtil.getJLogin());
        jLogin.click();
        // 检查：登录
        if (isShowUserName()) {
            logCookie(kyfwLoginBO.getKyfwAccount());
            return;
        }
        // 滑动：滑块验证码
        WebElement nc1N1z = webDriverSlowWait.until(KyfwWebElementUtil.getNc1N1z());
        autoSlide(chromeDriver, nc1N1z);
        KyfwWebElementUtil.hasLoadUntil(webDriverFastWait, webDriver -> Objects.equals(webDriver.findElement(By.id("modal")).getCssValue("display"), "none"));
        if (isShowUserName()) {
            logCookie(kyfwLoginBO.getKyfwAccount());
            return;
        }
        // 登录失败
        WebElement loginError = webDriverFastWait.until(KyfwWebElementUtil.getJLoginError());
        throw new BadRequestException(loginError.getText());
    }

    private void logCookie(String account) {
        Map<String, String> map = FunctionUtil.convertCollToMap(getCookieList(), Cookie::getName, Cookie::getValue, TreeMap::new);
        log.info("Account:{}的cookieMap:{}{}", account, System.lineSeparator(), JacksonJsonUtil.toPrettyJsonString(map));
    }

    /**
     * 自动滑块
     */
    private void autoSlide(WebDriver chromeDriver, WebElement nc1N1z) {
        Actions actions = new Actions(chromeDriver);
        nc1N1z.click();
        actions.clickAndHold(nc1N1z).perform();
        int max = 0;
        while (true) {
            int i = RandomUtils.nextInt(10, 80);
            if (max + i >= 300) {
                i = 300 - max;
            }
            max = max + i;
            actions.moveByOffset(i, 0).perform();
            if (max >= 300) {
                actions.release(nc1N1z);
                ThreadUtil.sleep(i);
                break;
            }
            ThreadUtil.sleep(i);
        }
    }

    public synchronized boolean isShowUserName() {
        return KyfwWebElementUtil.hasLoadUntil(webDriverFastWait, KyfwWebElementUtil.getJHeaderLogout());
    }


    /**
     * 获取cookie
     */
    public synchronized List<Cookie> getCookieList() {
        return new ArrayList<>(chromeDriver.manage().getCookies());
    }

    public synchronized void setCookieList(List<Cookie> cookieList) {
        cookieList.forEach(chromeDriver.manage()::addCookie);
    }

    /**
     * 获取用户名
     */
    public synchronized String getUserName() {
        Object userNameObject = KyfwWebElementUtil.execScript(chromeDriver, "return window.getUserName;");
        return userNameObject.toString().trim();
    }

    /**
     * 获取姓名
     */
    public synchronized String getRealName() {
        Object realNameObject = KyfwWebElementUtil.execScript(chromeDriver, "return $('a[login-type=\"personal\"]').text();");
        return realNameObject.toString().trim();
    }

    /**
     * 是否登录
     */
    public synchronized boolean isLogin() {
        refresh();
        Object realNameObject = KyfwWebElementUtil.execScript(chromeDriver, "return window.isLogin;");
        return Objects.equals(realNameObject, "Y");
    }

    /**
     * 刷新页面
     */
    public synchronized void refresh() {
        AppConfig appConfig = SpringUtil.getBean(AppConfig.class);
        log.info("refreshPage开始...");
        chromeDriver.get(appConfig.getKyfw().getIndexUrl());
        log.info("refreshPage完成...");
//        ThreadUtil.sleep(1000);
        // 如果有弹窗，关闭弹窗
//        closeTipMask();
//        logCookie(SessionUtil.getUserBO().getKyfwAccount());
    }

    /**
     * 关闭提示遮层
     */
    public synchronized void closeTipMask() {
        log.info("closeTipMask开始...");
        boolean hasTipBtn = KyfwWebElementUtil.hasLoadUntil(
                webDriverFastWait,
                KyfwWebElementUtil.getBtnPrimary()
        );
        if (hasTipBtn) {
            WebElement btnPrimary = webDriverFastWait.until(KyfwWebElementUtil.getBtnPrimary());
            btnPrimary.click();
        }
        boolean hasDisplayBlock = !KyfwWebElementUtil.hasLoadUntil(
                webDriverFastWait,
                webDriver -> {
                    List<WebElement> elements = chromeDriver.findElements(By.cssSelector(".mask"));
                    if (CollectionUtils.isEmpty(elements)) {
                        return true;
                    }
                    for (WebElement element : elements) {
                        if (!Objects.equals(element.getCssValue("display"), "none")) {
                            return null;
                        }
                    }
                    return true;
                }
        );
        if (hasDisplayBlock) {
            List<WebElement> elementList = webDriverFastWait.until(KyfwWebElementUtil.getMask());
            for (WebElement element : elementList) {
                KyfwWebElementUtil.setWebEleAttr(chromeDriver, element, "style", "display:none;");
            }
        }
        log.info("closeTipMask结束...");
    }

    public synchronized void stop() {
        chromeDriver.close();
        chromeDriver.quit();
    }
}
