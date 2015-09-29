package com.jcleary.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * The test environment.  TODO
 *
 * Created by julian on 9/15/2015.
 */
public class TestState {

    @Getter(AccessLevel.PUBLIC)
    @Accessors(fluent = true)
    private static final long GLOBAL_TIMEOUT = 30000L;

    @Getter(AccessLevel.PUBLIC)
    private WebDriver driver;

    public TestState() {
        driver = new FirefoxDriver();
    }

    public void close() {
        driver.close();
    }

    public void go(String url) {
        driver.get(url);
    }
}
