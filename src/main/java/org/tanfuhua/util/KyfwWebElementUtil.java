package org.tanfuhua.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: gaofubo
 * @date: 2021/5/11
 */
@Slf4j
@UtilityClass
public class KyfwWebElementUtil {




    /**
     * 登录页面：右上角登录按钮
     */
    public static Function<WebDriver, WebElement> getJHeaderLogin() {
        return webDriver -> {
            List<WebElement> elements = webDriver.findElements(By.id("J-header-login"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            elements = elements.get(0).findElements(By.tagName("a"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            WebElement element = elements.get(0);
            if (!Objects.equals(element.getText(), "登录")) {
                return null;
            }
            return element;
        };
    }

    /**
     * 登录页面：右上角用户名按钮
     */
    public static Function<WebDriver, WebElement> getJHeaderLogout() {
        return webDriver -> {
            List<WebElement> elements = webDriver.findElements(By.id("J-header-logout"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            elements = elements.get(0).findElements(By.tagName("a"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            String text = elements.get(0).getText();
            if (StringUtils.isBlank(text) || text.contains("登录")) {
                return null;
            }
            return elements.get(0);
        };
    }

    /**
     * 登录页面：账号登录按钮
     */
    public static Function<WebDriver, WebElement> getLoginAccount() {
        return webDriver -> {
            List<WebElement> elements = webDriver.findElements(By.className("login-hd-code"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            return elements.get(0);
        };
    }

    /**
     * 登录页面：用户名输入框
     */
    public static Function<WebDriver, WebElement> getJUserName() {
        return webDriver -> {
            List<WebElement> elements = webDriver.findElements(By.id("J-userName"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            return elements.get(0);
        };
    }

    /**
     * 登录页面：密码输入框
     */
    public static Function<WebDriver, WebElement> getJPassword() {
        return webDriver -> {
            List<WebElement> elements = webDriver.findElements(By.id("J-password"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            return elements.get(0);
        };
    }

    /**
     * 登录页面：图片验证码
     */
    public static Function<WebDriver, String> getJLoginImgSrc() {
        return webDriver -> {
            List<WebElement> elements = webDriver.findElements(By.id("J-loginImg"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            String imgSrc = elements.get(0).getAttribute("src");
            if (StringUtils.isBlank(imgSrc)) {
                return null;
            }
            String[] srcSplit = imgSrc.split(",");
            if (srcSplit.length != 2) {
                return null;
            }
            return srcSplit[1];
        };
    }

    /**
     * 登录页面：图片验证码【刷新】按钮
     */
    public static Function<WebDriver, WebElement> getLgcodeRefresh() {
        return webDriver -> {
            List<WebElement> elements = webDriver.findElements(By.className("lgcode-refresh"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            return elements.get(0);
        };
    }

    /**
     * 登录页面：登录二维码
     */
    public static Function<WebDriver, String> getJQrImgSrc() {
        return webDriver -> {
            List<WebElement> elements = webDriver.findElements(By.id("J-qrImg"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            String imgSrc = elements.get(0).getAttribute("src");
            if (StringUtils.isBlank(imgSrc)) {
                return null;
            }
            String[] srcSplit = imgSrc.split(",");
            if (srcSplit.length != 2) {
                return null;
            }
            return srcSplit[1];
        };
    }

    /**
     * 登录页面：图片验证码热点区
     */
    public static Function<WebDriver, WebElement> getJPassCodeCoin() {
        return webDriver -> {
            List<WebElement> elements = webDriver.findElements(By.id("J-passCodeCoin"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            return elements.get(0);
        };
    }

    /**
     * 登录页面：提交登录按钮
     */
    public static Function<WebDriver, WebElement> getJLogin() {
        return webDriver -> {
            List<WebElement> elements = webDriver.findElements(By.id("J-login"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            return elements.get(0);
        };
    }

    /**
     * 登录页面：滑块验证码
     */
    public static Function<WebDriver, WebElement> getNc1N1z() {
        return webDriver -> {
            List<WebElement> elements = webDriver.findElements(By.id("nc_1_n1z"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            return elements.get(0);
        };
    }

    /**
     * 登录页面：滑块验证码 文本
     */
    public static Function<WebDriver, WebElement> getNc1ScaleText() {
        return webDriver -> {
            List<WebElement> elements = webDriver.findElements(By.id("nc_1__scale_text"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            return elements.get(0);
        };
    }

    /**
     * 登录页面：登录错误信息
     */
    public static Function<WebDriver, WebElement> getJLoginError() {
        return webDriver -> {
            List<WebElement> elements = webDriver.findElements(By.id("J-login-error"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            return elements.get(0);
        };
    }


    /**
     * 个人中心页面：提示填写联系人手机号：确定按钮
     */
    public static Function<WebDriver, WebElement> getBtnPrimary() {
        return webDriver -> {
            List<WebElement> elements = webDriver.findElements(By.cssSelector(".btn-primary"));
            if (CollectionUtils.isEmpty(elements)) {
                return null;
            }
            WebElement element = elements.get(0);
            if (!Objects.equals(element.getText(), "确定")) {
                return null;
            }
            return element;
        };
    }

    /**
     * 个人中心页面：遮罩
     */
    public static Function<WebDriver, List<WebElement>> getMask() {
        return webDriver -> {
            List<WebElement> elements = webDriver.findElements(By.cssSelector(".mask"));
            if (CollectionUtils.isEmpty(elements)) {
                return Collections.emptyList();
            }
            return elements.stream()
                    .filter(e -> !Objects.equals(e.getCssValue("display"), "none"))
                    .collect(Collectors.toList());
        };
    }

    /**
     * 设置元素某属性值
     */
    public static void setWebEleAttr(ChromeDriver webDriver, WebElement webElement, String attr, String value) {
        webDriver.executeScript("arguments[0]." + attr + " = '" + value + "';", webElement);
    }

    /**
     * 设置元素的value值
     */
    public static void setWebEleValue(ChromeDriver webDriver, WebElement webElement, String value) {
        setWebEleAttr(webDriver, webElement, "value", value);
    }

    /**
     * 设置元素的innerHTML
     */
    public static void setWebEleInnerHTML(ChromeDriver webDriver, WebElement webElement, String innerHTML) {
        setWebEleAttr(webDriver, webElement, "innerHTML", innerHTML);
    }

    /**
     * 判断条件是否加载
     */
    public static <T> boolean hasLoadUntil(WebDriverWait webDriverWait,
                                           Function<WebDriver, T> function) {
        try {
            webDriverWait.until(function);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
