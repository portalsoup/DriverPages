package com.jcleary.webdriver;

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
public interface Page extends Loadable {

    /**
     * Declare a page object to have meta-data
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Info {

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
         * represented with this pattern: '{{@literal@}uniqueIdentifier parameterValue}'.
         *
         * For more information on url variables, refer to: TODO
         *
         * @return                      The relative path to the current end point
         */
        String relativePath() default "";

    }

    /**
     * Wait until the predicate is satisfied and returns true.
     *
     * @param condition         A condition to wait for
     *
     * @return                  True if the condition is met within the time limit
     */
    default boolean waitUntil(Predicate<Page> condition) {

        Clock clock = new SystemClock();
        long delay = clock.now();

        while (clock.isNowBefore(delay)) {
            if (condition.test(this)) {
                return true;
            }
        }
        return false;
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
    default String getHostname() {
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
     * Search this page object for the {@link Page.Info} class annotation that is storing
     * the relative path to this page.
     *
     * @return                      The full relative path to this page.  Or an empty string if no relative path
     *                              is found.  Or if the path is explicitly set to be empty
     */
    default String getRelativePath() {
        for (Annotation anAnnotation : getClass().getDeclaredAnnotations()) {
            if (anAnnotation instanceof Info) {
                Info info = (Info) anAnnotation;
                if (info.relativePath() != null && !info.relativePath().isEmpty()) {
                    return info.relativePath();
                }
            }
        }
        return "";
    }

    /**
     * Search this page for a hostname and a relative path and then
     * concatenate them into a ready to use URL to this page.
     *
     * @return                      The full URL of this page
     */
    default String url() {
        return getHostname() + getRelativePath();
    }
}
