package com.jcleary.state;

import com.jcleary.core.State;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * The test environment.  TODO
 *
 * Created by julian on 9/15/2015.
 */
public class TestState implements State {

    @Getter(AccessLevel.PUBLIC)
    @Accessors(fluent = true)
    private static final long GLOBAL_TIMEOUT = 30000L;

    @Setter(AccessLevel.PRIVATE)
    private WebDriver driver;

    private TestState() {

    }

    public static TestState getInstance() {
        TestState state = new TestState();
        state.setDriver(new FirefoxDriver());
        return state;
    }

    public void close() {
        driver.close();
    }

    public void go(String url) {
        driver.get(url);
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }
}
