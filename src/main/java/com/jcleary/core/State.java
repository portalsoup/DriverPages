package com.jcleary.core;

import com.jcleary.core.store.StateStore;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.io.Closeable;

/**
 * Created by julian on 12/21/2015.
 */
public class State implements Closeable {

    public final long GLOBAL_TIMEOUT = 30000L;

    private WebDriver driver;

    private final StateStore store;

    public State() {
        this(Browser.FIREFOX);
    }

    public State(Browser browser) {
        store = new StateStore();

        switch (browser) {
            case CHROME:
                setDriver(new ChromeDriver());
                break;
            case FIREFOX:
                setDriver(new FirefoxDriver());
                break;
            case PHANTOM:
                setDriver(new PhantomJSDriver());
            default:
                throw new IllegalArgumentException("Unsupported browser: [" + browser + " ]");
        }
    }

    public State(WebDriver driver) {
        if (driver == null) {
            throw new NullPointerException();
        }
        store = new StateStore();
        setDriver(driver);
    }

    public StateStore store() {
        return store;
    }

    protected void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Close this state.  Once closed it cannot be used again.
     */
    public void close() {
        driver.quit();
    }
}
