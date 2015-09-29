package com.jcleary.webdriver;

import static com.jcleary.core.Ternary.*;

import com.jcleary.core.Ternary;
import com.jcleary.core.TestState;
import com.jcleary.exceptions.PageException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.SystemClock;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

/**
 * Generic page object functionality for storing and using meta-data involving a page object.
 *
 * Created by julian on 9/17/2015.
 */
public interface Page {

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

        /**
         * Declare that this page should inherit all the {@link Selector} instance fields annotated with
         * {@link IsLoaded} and {@link Loader} in the super class and include their criteria to determine
         * if the page has loaded.
         *
         * @return True if the page will inherit IsLoaded criteria from the super class
         */
        boolean isLoadedInheritance() default false;

    }

    /**
     * Attach this to {@link Selector} instance fields to expect and depend on one or more elements being in a
     * particular state as part of the page's criteria to load.
     *
     * Unless explicitly stated otherwise, each option
     * is implied to operate implicitly on the first found WebElement that matches it's {@link Selector#locator}.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface IsLoaded {

        /**
         * Assert the expected state of this element's presence when this page to be considered loaded.  Declaring
         * {@link Ternary#TRUE} or {@link Ternary#FALSE} will create an expectation of that state.  Declaring
         * {@link Ternary#UNKNOWN} will skip it.
         *
         * @return                      True if any single element is located in the DOM of the current page
         */
        Ternary presence() default TRUE;

        /**
         * Assert the expected state of this element's visiblity when this page to be considered loaded.  Declaring
         * {@link Ternary#TRUE} or {@link Ternary#FALSE} will create an expectation of that state.  Declaring
         * {@link Ternary#UNKNOWN} will skip it.
         *
         * @return                      True if the first located element is visible to the user
         */
        Ternary visibility() default UNKNOWN;

        /**
         * Assert that this element must contain a particular string for this page to be considered loaded.
         *
         * @return                      The text the element must contain
         */
        String containsText() default "";

        /**
         * Assert that this element must contain one or more css classes for this page to be considered loaded.
         *
         * @return                      An array of all declared css classes
         */
        String[] containsCssClasses() default "";

        /**
         * Assert that this element must contain a particular id attribute for this page to be considered loaded.
         *
         * @return                      The id value as seen in the html id attribute
         */
        String hasId() default "";

        /**
         * Assert a lower bound for the quantity of located elements for this page to be considered loaded.  If
         * {@link #findExactly()} is declared, then this value is ignored.
         *
         * @return                      A count of the minimum number of WebElements to be expected
         */
        int findAtLeast() default -1;

        /**
         * Assert an upper bound for the quantity of located elements for this page to be considered loaded. If
         * {@link #findExactly()} is declared, then this value is ignored.
         *
         * @return                      A count of the maximum number of WebElements to be expected
         */
        int findAtMost() default -1;

        /**
         * Asserts an exact number of located elements for this page to be considered loaded.
         *
         * @return                      A count of the exact number of WebElements to be expected
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
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Loader {

        /**
         * When the page finishes loading, should this loader element be present in the DOM?  Declaring
         * {@link Ternary#TRUE} or {@link Ternary#FALSE} will create an expectation of that state.  Declaring
         * {@link Ternary#UNKNOWN} will skip it.
         *
         * @return                      True if this loader element is expected to be present
         *                              and locatable in the DOM when the page loading completes
         */
        Ternary presentOnFinish() default UNKNOWN;

        /**
         * When the page finishes loading, should this loader element be visible to the user?  Declaring
         * {@link Ternary#TRUE} or {@link Ternary#FALSE} will create an expectation of that state.  Declaring
         * {@link Ternary#UNKNOWN} will skip it.
         *
         * @return                      True if this loader element is expected to be visible
         *                              to the user when the page loading completes
         */
        Ternary visibleOnFinish() default UNKNOWN;
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
     * TODO Add logging and create an exception that contains a report instead of just returning false
     *
     * @return                          True if this page is considered loaded
     */
    default boolean isLoaded() {

        /*      Order of operations:

                split elements into two lists: ordinary elements and loaders

                for each loader element:
                    check presence if required

                    check visibility if required

                for each ordinary element:
                    check presence if required

                    check visibility if required

                    Check containing css classes if required

                    Check id if required

                    if visibility ->
                        check containing text if required

                    if findExactly ->
                        check find exactly, skip check for at most and at least
                    else ->
                        check find at least if required
                        check find at most if required
         */

        Map<Selector, Loader> loaders = new HashMap<>();
        Map<Selector, IsLoaded> ordinaries = new HashMap<>();
        Class<?> currentClass = getClass();

        /*
                Scan for all annotated Selectors that this page is configured to look at.

                While the current class has an Info annotation, if the current class is a parent class and it allows
                isLoaded inheritance and the current class is assignable from Page:
         */
        while (currentClass.getDeclaredAnnotation(Info.class) != null
                && Page.class.isAssignableFrom(currentClass)
                && (currentClass.equals(getClass()) ||
                (currentClass.getDeclaredAnnotation(Info.class).isLoadedInheritance()
                && !currentClass.equals(getClass())))) {

            /*
                Get all the fields in the current class
             */
            for (Field f : currentClass.getDeclaredFields()) {

                Object s;
                IsLoaded isLoaded;
                Loader loader;

                try {
                    f.setAccessible(true);
                    s = f.get(this); // Get instance
                } catch (IllegalAccessException e) {
                    throw new WebDriverException(e);
                }

                // If our field is a selector
                if (s instanceof Selector) {

                    isLoaded = f.getDeclaredAnnotation(IsLoaded.class); // Get possible IsLoaded annotation
                    loader = f.getDeclaredAnnotation(Loader.class); // Get possible Loader annotation

                    if (isLoaded != null) {
                        ordinaries.put((Selector) s, isLoaded); // Annotated with IsLoaded, put in ordinary map
                    } else if (loader != null) {
                        loaders.put((Selector) s, loader); // Annotated with Loader, put in loaders map
                    }
                }

            }
            currentClass = currentClass.getSuperclass();
        }

        /* We finished scanning for elements, now lets iterate through them and deal with their criteria */

        // Loaders first
        for (Selector loader : loaders.keySet()) {

            Ternary present = loaders.get(loader).presentOnFinish();
            Ternary visible = loaders.get(loader).visibleOnFinish();

            if (present != UNKNOWN) {
                if (present.XNOR(loader.isPresent()) != TRUE) {
                    return false;
                }
            }

            if (visible != UNKNOWN) {
                if (visible.XNOR(loader.isDisplayed()) != TRUE) {
                    return false;
                }
            }

        }

        for (Selector ordinary : ordinaries.keySet()) {

            Ternary presence = ordinaries.get(ordinary).presence();
            Ternary visibility = ordinaries.get(ordinary).visibility();
            String containsText = ordinaries.get(ordinary).containsText();
            String[] cssClasses = ordinaries.get(ordinary).containsCssClasses();
            String id = ordinaries.get(ordinary).hasId();
            int findAtLeast = ordinaries.get(ordinary).findAtLeast();
            int findAtMost = ordinaries.get(ordinary).findAtMost();
            int findExactly = ordinaries.get(ordinary).findExactly();

            if (presence != UNKNOWN) {
                if (presence.XNOR(ordinary.isPresent()) != TRUE) {
                    return false;
                }
            }

            if (visibility != UNKNOWN) {
                if (visibility.XNOR(ordinary.isDisplayed()) != TRUE) {
                    return false;
                }
            }

            if (containsText != null && !containsText.isEmpty()) {
                try {
                    if (!ordinary.getText().contains(containsText)) {
                        return false;
                    }
                } catch (NoSuchElementException e) {
                    return false;
                }
            }

            if (cssClasses != null && (cssClasses.length == 1 && !cssClasses[0].isEmpty())) {
                String[] fromElement = ordinary.get().getAttribute("class").split(" ");
                if (!Arrays.asList(fromElement).containsAll(Arrays.asList(cssClasses))) {
                    return false;
                }
            }

            if (id != null && !id.isEmpty()) {
                if (!ordinary.get().getAttribute("id").equals(id)) {
                    return false;
                }
            }

            if (findExactly >= 0) {
                if (ordinary.getMultiple().size() != findExactly) {
                    return false;
                }
            } else {
                if (findAtLeast >= 0) {
                    if (ordinary.getMultiple().size() < findAtLeast) {
                        return false;
                    }
                }

                if (findAtMost >= 0) {
                    if (ordinary.getMultiple().size() > findAtMost) {
                        return false;
                    }
                }
            }
        }

        return true;
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

        Clock clock = new SystemClock();

        long delay = clock.laterBy(timeout);

        while (clock.isNowBefore(delay)) {
            if (isLoaded()) {
                return true;
            }
        }
        return false;
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
