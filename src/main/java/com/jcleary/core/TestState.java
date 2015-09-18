package com.jcleary.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Created by julian on 9/15/2015.
 */
public class TestState {

    private WebDriver driver;

    public TestState() {
        driver = new PhantomJSDriver();
    }

    public void close() {
        driver.close();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void go(String url) {
        driver.get(url);
    }
}
