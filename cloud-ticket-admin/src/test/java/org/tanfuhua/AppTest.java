package org.tanfuhua;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.tanfuhua.cloudticket.common.constant.Constant;
import org.tanfuhua.cloudticket.util.DateUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Locale;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */

    @Test
    public void testLocalHost() throws UnknownHostException {
        long s = System.currentTimeMillis();
        InetAddress localHost = InetAddress.getLocalHost();
        long e = System.currentTimeMillis();
        System.out.println(localHost);
        System.out.println(e - s);
    }

    @Test
    public void shouldAnswerWithTrue() {
        System.setProperty("webdriver.chrome.driver", "/Users/gaofubo/Software/Mac/chromedriver_mac64/chromedriver_111.0.5563.64");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(chromeOptions);

        driver.get("https://selenium.dev");

        driver.quit();
    }

    @Test
    public void testDate() {
        // Wed Apr 28 2021 00:00:00 GMT+0800 (中国标准时间)
        Date date1 = new Date();
        String stringFromDate = DateUtil.dateToString(
                DateUtil.stringToDate("2023-03-27", Constant.Str.DATE_FORMAT),
                Constant.Str.DATE_TIME_KYFW_FORMAT, Locale.ENGLISH, Constant.Time.GMT_PLUS_8);
        System.out.println(stringFromDate);

    }
}
