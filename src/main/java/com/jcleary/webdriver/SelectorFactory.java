package com.jcleary.webdriver;

import com.jcleary.core.State;

import static com.jcleary.webdriver.ByFactory.*;

/**
 * Manufactures {@link Selector}s as various presets.  Also may store an unformatted locator to dynamically format
 * into new Selector instances.
 *
 * Created by julian on 9/15/2015.
 */
public class SelectorFactory {

    private State testState;
    private String locator;
    private ByFactory by;

    /**
     * Store an unformatted locator. Invoke {@link #get(Object...)} to dynamically instantiate new Selectors using
     * the formatted locator.
     *
     * @param testState The state of this test environment
     * @param locator The locator for the target node
     * @param byFactory The type of locator this is using
     */
    public SelectorFactory(State testState, String locator, ByFactory byFactory) {
        this.by = byFactory;
        this.testState = testState;
        this.locator = locator;
    }

    /**
     * Instantiate a new Selector using String formatters to replace format variables with values.  Treat this in the
     * same way as a call to System.out.printf(String, Object...) where the string was provided to the constructor
     * of this object, and the arguments are to be passed in now.
     *
     * @param vars Variables to be formatted into the locator
     *
     * @return A new Selector instance using the formatted locator
     */
    public Selector get(Object...vars) {
        return new Selector(testState, String.format(locator, vars), by);
    }

    public static Selector byCss(State state, String cssSelector) {
        return new Selector(state,  cssSelector, CSS);
    }

    public static Selector byCss(Selector relativeRootNode, String cssSelector) {
        return new Selector (relativeRootNode.getState(), relativeRootNode.getLocator().trim() + " " + cssSelector, CSS);
    }

    public static Selector byXpath(State state, String xpath) {
        return new Selector(state, xpath, XPATH);
    }

    public static Selector byId(State state, String id) {
        return new Selector(state, id, ID);
    }

    public static Selector byLinkText(State state, String linkText) {
        return new Selector(state, linkText, LINK_TEXT);
    }

    public static Selector byPartialLinkText(State state, String linkText) {
        return new Selector(state, linkText, PARTIAL_LINK_TEXT);
    }

    public static Selector byName(State state, String name) {
        return new Selector(state, name, NAME);
    }

    public static Selector byTagName(State state, String tagName) {
        return new Selector(state, tagName, TAG_NAME);
    }

    public static Selector byClassName(State state, String className) {
        return new Selector(state, className, CLASS_NAME);
    }
}
