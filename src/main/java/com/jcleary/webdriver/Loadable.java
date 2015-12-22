package com.jcleary.webdriver;

import com.jcleary.core.Ternary;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.SystemClock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.jcleary.core.Ternary.TRUE;
import static com.jcleary.core.Ternary.UNKNOWN;

/**
 * Determines that this object is loadable.  Examples of loadable objects are pages or components.  A page that is
 * loadable must
 * Created by julian on 9/29/2015.
 */
public interface Loadable {

    /**
     * Attach this to {@link Selector} instance fields to expect and depend on one or more elements being in a
     * particular state as part of the object's criteria to load.
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
     * Assign for this class to inherit it's parent's loadables.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface InheritLoaders {

    }

    /**
     * Searches this page object for all {@link Selector} instance fields annotated with {@link IsLoaded} or
     * {@link Loader} and asserts their criteria to determine if the page has finished loading.
     *
     * Use {@link com.jcleary.webdriver.Loadable.InheritLoaders} for this method to also assert annotated Selectors
     * in the immediate parent class to this page object.
     *
     * TODO Add logging and create an exception that contains a report instead of just returning false
     *
     * @return                          True if this page is considered loaded
     */
    default boolean isLoaded() {

        /*      Order of operations:

                split elements into two lists: ordinary elements and loaders

                First:
                for each loader element:
                    check presence if required

                    check visibility if required

                Second:
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

                Climb the hierarchical tree and check each Loadable component for Selectors
         */
        while (Loadable.class.isAssignableFrom(currentClass) && currentClass.equals(getClass()) ||
                currentClass.getDeclaredAnnotation(InheritLoaders.class) != null && !currentClass.equals(getClass())) {

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

            // Populate data from annotation
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
     * @param timeoutMillis             The maximum number of milliseconds to allow the page to finish loading
     *
     * @return                          True if the page is considered loaded before the duration completes
     */
    default boolean waitUntilLoaded(final long timeoutMillis) {

        Clock clock = new SystemClock();

        long delay = clock.laterBy(timeoutMillis);

        while (clock.isNowBefore(delay)) {
            if (isLoaded()) {
                return true;
            }
        }
        return false;
    }
}
