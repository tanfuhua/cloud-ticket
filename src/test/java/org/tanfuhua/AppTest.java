package org.tanfuhua;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.tanfuhua.common.constant.Constant;
import org.tanfuhua.util.DateUtil;

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
