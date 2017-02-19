package com.jcleary.webdriver;

import com.jcleary.core.State;
import com.jcleary.exceptions.PageException;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.SystemClock;

import java.lang.annotation.*;
import java.util.function.Predicate;

/**
 * Generic page object functionality for storing and using meta-data involving a page object.
 *
 * Created by julian on 9/17/2015.
 */
public abstract class Page {

    /**
     * Declare a page object's meta-data
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Info {

        /**
         * The hostname that this page is hosted on.  This route will not include any relative portions of the url
         * that would be appended after the TLD (if one exists).
         *
         * @return                      The hostname hosting this page.  Such as 'http://www.google.com'
         */
        String host() default "";

        /**
         * The port number that the hostname is currently listening on.
         *
         * @return                      The remote listening port
         */
        int port() default 80;

        /**
         * The relative path portion of the url that is appended after the hostname to route to the
         * particular endpoint.  This path may or may not have variables.  Any dynamic portions of this path can be
         * represented with this pattern: '{{@literal@}uniqueIdentifier}'.
         *
         * For more information on url variables, refer to: {@link #getRelativePath()}
         *
         * @return                      The relative path to the current end point
         */
        String relativePath() default "";

    }

    private final State state;
    private long defaultTimeout = 30 * 1000;

    public Page(State state) {
        this.state = state;
    }

    public Page(Page page) {
        this.state = page.getState();
    }

    public void setDefaultTimeout(long timeoutMillis) {
        this.defaultTimeout = timeoutMillis;
    }

    public long getDefaultTimeout() {
        return defaultTimeout;
    }

    /**
     * Wait until a predicate returns true.
     *
     * @param condition         A condition to wait for
     * @param waitMillis          How long to wait before failing
     *
     * @return                  True if the condition is met within the time limit
     */
    public boolean waitUntil(Predicate<Page> condition, long waitMillis ) {

        Clock clock = new SystemClock();
        long delay = clock.now() + waitMillis;

        while (clock.isNowBefore(delay)) {
            if (condition.test(this)) {
                return true;
            }
            sleep(500);
        }
        return false;
    }

    public boolean waitUntil(Predicate<Page> condition) {
        return waitUntil(condition, defaultTimeout);
    }

    /**
     * Simulate pushing the back button in the browser to return to the previous page.
     */
    public Page back() {
        getState().getDriver().navigate().back();
        return this;
    }

    /**
     * Simulate pushing the forward button in the browser to go forward in history one page.
     */
    public Page forward() {
        getState().getDriver().navigate().forward();
        return this;
    }

    /**
     * Simulate hitting the refresh button in the browser to refresh the page.
     */
    public Page refresh() {
        getState().getDriver().navigate().refresh();
        return this;
    }

    public Page go() {
        getState().getDriver().get(url());
        return this;
    }

    /**
     * Search this page object for the {@link Page.Info} class annotation that is storing
     * the hostname that this page uses.  If the annotation isn't found, or if the annotation is found, but
     * a hostname is not.  The class hierarchy will be climbed until the closest parent with a declared
     * hostname is found.
     *
     * @return                      The full hostname that this page is hosted in
     *
     * @exception PageException     If the page can't find a hostname in this class
     *                              or any other hierarchical parent class
     */
    public String getHostname() {
        Class currentClass = getClass();
        while (!currentClass.getSimpleName().equals("Object")) {
            Info info = (Info) currentClass.getDeclaredAnnotation(Info.class);
            if (info != null) {
                if (info.host() != null && !info.host().isEmpty()) {
                    return info.host() + (info.port() == 80 ? "" : ":" + info.port());
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        throw new PageException("Could not find a hostname on the "
                + getClass().getSimpleName() + " page or any parent page.");
    }

    /**
     * Search this page object then each page object in the hierarchical tree for the {@link Page.Info} class
     * annotation that is storing the relative path to this page.  Each segment of the relative url is inserted at the
     * beginning of the string for each super page class crawled.
     *
     * @return                      The full relative path to this page.  Or an empty string if no relative path
     *                              is found.  Or if the path is explicitly set to be empty
     */
    public String getRelativePath() {

        Class<?> currentPage = this.getClass();
        StringBuilder relativePath = new StringBuilder("");

        // Build url
        while (Page.class.isAssignableFrom(currentPage)) {
            for (Annotation anAnnotation : currentPage.getDeclaredAnnotations()) {
                if (anAnnotation instanceof Info) {
                    Info info = (Info) anAnnotation;
                    if (!info.relativePath().isEmpty()) {
                        relativePath.insert(0, info.relativePath());
                    }
                }
            }
            currentPage = currentPage.getSuperclass();
        }

        return relativePath.toString();
    }

    /**
     * Search this page for a hostname and a relative path and then
     * concatenate them into a ready to use URL to this page.
     *
     * @return                      The full URL of this page
     */
    public String url() {
        return getHostname() + getRelativePath();
    }

    public State getState() {
        return state;
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
