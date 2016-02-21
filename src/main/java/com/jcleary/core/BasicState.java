package com.jcleary.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Created by julian on 12/21/2015.
 */
public class BasicState implements State, AutoCloseable {

    @Getter(AccessLevel.PUBLIC)
    @Accessors(fluent = true)
    private static final long GLOBAL_TIMEOUT = 30000L;

    @Setter(AccessLevel.PRIVATE)
    private WebDriver driver;

    private BasicState() {

    }

    /**
     * Get the default state instance that is using Firefox.  Note that Firefox must also be installed and
     * accessible via PATH.
     *
     * @return
     */
    public static BasicState getInstance() {
        return getInstance(Browser.FIREFOX);
    }

    /**
     * Get a new state instance that is using the selected browser.  Note that the selected
     * browser must not only be installed and accessible via PATH.  But it's driver executable
     * must also be accessible.
     *
     * @param browser The browser to use.
     *
     * @return
     */
    public static BasicState getInstance(Browser browser) {

        BasicState state = new BasicState();

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

    /**
     * Close this state.  Once closed it cannot be used again.
     */
    public void close() {
        driver.close();
    }

    /**
     * Shortcut to navigate the browser to a specific url.
     *
     * @param url
     */
    public void go(String url) {
        driver.get(url);
    }

    /**
     * Get the WebDriver instance this state is holding.
     *
     * @return
     */
    public WebDriver getDriver() {
        return driver;
    }
}
