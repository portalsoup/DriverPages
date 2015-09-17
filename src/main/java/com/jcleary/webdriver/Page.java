package com.jcleary.webdriver;

import com.jcleary.core.TestState;
import com.jcleary.exceptions.PageException;

import java.lang.annotation.Annotation;

/**
 * Generic page object functionality for storing and using meta-data involving a page object.
 *
 * Created by julian on 9/17/2015.
 */
public interface Page {

    /**
     * Declare a page object to have meta-data
     */
    @interface Info {

        /**
         * The hostname that this page is hosted on.  This route will not include any relative portions of the url
         * that would be appended after the TLD (if one exists).
         *
         * @return The hostname hosting this page.  Such as 'http://www.google.com'
         */
        String host() default "/";

        /**
         * The port number that the hostname is currently listening on.
         *
         * @return
         */
        int port() default 80;

        /**
         * The relative path portion of the url that is appended after the hostname to route to the
         * particular endpoint.  This path may or may not have variables.  Any dynamic portions of this path can be
         * represented with this pattern: '{{@literal@}uniqueIdentifier parameterValue}'.
         *
         * For more information on url variables, refer to: TODO
         *
         * @return
         */
        String relativePath() default "";

        /**
         * Declare that this page should inherit all the {@link Selector} instance fields annotated with
         * {@link IsLoaded} and {@link Loader} in the super class and include their criteria to determine
         * if the page has loaded.
         *
         * @return True if the page will inherit IsLoaded criteria from the super class
         */
        boolean isLoadedInheritance() default true;

    }

    /**
     * Attach this to {@link Selector} instance fields to expect and depend on one or more elements being in a
     * particular state as part of the page's criteria to load.
     *
     * Unless explicitly stated otherwise, each option
     * is implied to operate implicitly on the first found WebElement that matches it's {@link Selector#locator}.
     */
    @interface IsLoaded {

        /**
         * Assert the expected state of this element's presence when this page to be considered loaded.
         *
         * @return                      True if any single element is located in the DOM of the current page
         */
        boolean presence() default true;

        /**
         * Assert the expected state of this element's visiblity when this page to be considered loaded.
         *
         * @return                      True if the first located element is visible to the user
         */
        boolean visibility() default false;

        /**
         * Assert that this element must contain a particular string for this page to be considered loaded.
         *
         * @return                      True if the first located element contains the provide text
         */
        String containsText() default "";

        /**
         * Assert that this element must contain one or more css classes for this page to be considered loaded.
         *
         * @return                      True if the first located element
         *                              contains all the declared css classes
         */
        String[] containsCssClasses() default "";

        /**
         * Assert that this element must contain a particular id attribute for this page to be considered loaded.
         *
         * @return                      True if the first located element contains
         *                              an id attribute of the declared value
         */
        String hasId() default "";

        /**
         * Assert a lower bound for the quantity of located elements for this page to be considered loaded.  If
         * {@link #findExactly()} is declared, then this value is ignored.
         *
         * @return                      True if this annotated Selector locates an equal or
         *                              greater number of elements than the declared quantity
         */
        int findAtLeast() default -1;

        /**
         * Assert an upper bound for the quantity of located elements for this page to be considered loaded. If
         * {@link #findExactly()} is declared, then this value is ignored.
         *
         * @return                      True if this annotated Selector locates less than or
         *                              equal of number of elements than the declared quantity
         */
        int findAtMost() default -1;

        /**
         * Asserts an exact number of located elements for this page to be considered loaded.
         *
         * @return                      True if this annotated Selector locates a number
         *                              of elements equaal to the declared quantity
         */
        int findExactly() default -1;
    }

    /**
     * A loader element's purpose is to declare that loading is not complete.  Elements of these nature usually exist
     * while the page is loading resources, but change to an invisible state or are removed from the DOM when page
     * loading is considered complete.
     *
     * Attach this to {@link Selector} instance fields to act as a marker that has two states; loading and finish.
     *
     * Unless explicitly stated otherwise, each option
     * is implied to operate implicitly on the first found WebElement that matches it's {@link Selector#locator}.
     */
    @interface Loader {

        /**
         * While the page is loading, should this loader element be present in the DOM?
         *
         * @return                      True if this loader element is present and locatable
         *                              in the DOM during the page loading state
         */
        boolean presentWhileLoading() default true;

        /**
         * While the page is loading, should this loader element be visible to the user?
         *
         * @return                      True if this loader element is present and visible
         *                              to the user during the page loading state
         */
        boolean visibleWhileLoading() default true;

        /**
         * When the page finishes loading, should this loader element be present in the DOM?
         *
         * @return                      True if this loader element is expected to be present
         *                              and locatable in the DOM when the page loading completes
         */
        boolean presentOnFinish() default false;

        /**
         * When the page finishes loading, should this loader element be visible to the user?
         *
         * @return                      True if this loader element is expected to be visible
         *                              to the user when the page loading completes
         */
        boolean visibleOnFinish() default false;
    }

    /**
     * Get the TestState instance that contains the state of the test environment, such as the WebDriver instance used.
     *
     * @return                          The TestState object being used by the page
     */
    TestState getState();

    /**
     * Searches this page object for all {@link Selector} instance fields annotated with {@link IsLoaded} or
     * {@link Loader} and asserts their criteria to determine if the page has finished loading.
     *
     * Set {@link Info#isLoadedInheritance()} to true for this method to also assert annotated Selectors in the
     * immediate parent class to this page object.
     *
     * @return                          True if this page is considered loaded
     */
    default boolean isLoaded() {
        return false; // TODO
    }

    /**
     * Wait a duration for the page to become considered loaded as dictated by the criteria outlined by
     * {@link #isLoaded()}.
     *
     * @param timeout                   The maximum number of milliseconds to allow the page to finish loading
     *
     * @return                          True if the page is considered loaded before the duration completes
     */
    default boolean waitUntilLoaded(final long timeout) {
        return false; // TODO
    }

    /**
     * Search this page object for the {@link Page.Info} class annotation that is storing
     * the hostname that this page uses.  If the annotation isn't found, or if the annotation is found, but
     * a hostname is not.  The class hierarchy will be crawled until the closest parent with a declared
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
           for (Annotation anAnnotation : currentClass.getDeclaredAnnotations()) {
               if (anAnnotation instanceof Info) {
                   Info info = (Info) anAnnotation;
                   if (info.host() != null && !info.host().isEmpty()) {
                       return info.host() + (info.port() == 80 ? "" : ":" + info.port());
                   }
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
