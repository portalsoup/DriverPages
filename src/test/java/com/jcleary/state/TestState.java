package com.jcleary.state;

import com.jcleary.core.State;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * The test environment.  TODO
 *
 * Created by julian on 9/15/2015.
 */
public class TestState implements State, AutoCloseable {

    @Getter(AccessLevel.PUBLIC)
    @Accessors(fluent = true)
    private static final long GLOBAL_TIMEOUT = 30000L;

    @Setter(AccessLevel.PRIVATE)
    private WebDriver driver;

    private TestState() {

    }

    public static TestState getInstance() {
        return getInstance(Browser.FIREFOX);
    }

    public static TestState getInstance(Browser browser) {

        TestState state = new TestState();

        switch (browser) {
            case CHROME:
                state.setDriver(new ChromeDriver());
                break;
            case FIREFOX:
                state.setDriver(new FirefoxDriver());
                break;
            case PHANTOM:
                state.setDriver(new PhantomJSDriver());
            default:
                throw new IllegalArgumentException("Unsupported browser: [" + browser + " ]");
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return state;
    }

    public void close() {
        driver.close();
    }

    public void go(String url) {
        driver.get(url);
    }

    public WebDriver getDriver() {
        return driver;
    }
}
