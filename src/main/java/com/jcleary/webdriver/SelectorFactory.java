package com.jcleary.webdriver;

import com.jcleary.core.TestState;

import static com.jcleary.webdriver.ByFactory.*;

/**
 * Created by julian on 9/15/2015.
 */
public class SelectorFactory {

    private TestState testState;
    private String locator;
    private ByFactory by;

    //TODO this constructor will be so you can manufacture selectors with string formatted differences in the locator
    public SelectorFactory(TestState testState, String locator, ByFactory byFactory) {
        this.by = byFactory;
        this.testState = testState;
        this.locator = locator;
    }

    /**
     * Instantiate a new Selector using String formatters to replace format variables with values.  Treat this in the
     * same way as a call to System.out.printf(String, Object...) where the string was provided to the constructor
     * of this object, and the arguments are to be passed in now.
     *
     * @param vars
     *
     * @return
     */
    public Selector get(String...vars) {
        return new Selector(testState, String.format(locator, vars), by);
    }

    public static Selector byCss(TestState state, String cssSelector) {
        return new Selector(state,  cssSelector, CSS);
    }

    public static Selector byCss(Selector relativeRootNode, String cssSelector) {
        return new Selector (relativeRootNode.getState(), relativeRootNode.getLocator().trim() + " " + cssSelector, CSS);
    }

    public static Selector byXpath(TestState state, String xpath) {
        return new Selector(state, xpath, XPATH);
    }

    public static Selector byId(TestState state, String id) {
        return new Selector(state, id, ID);
    }

    public static Selector byLinkText(TestState state, String linkText) {
        return new Selector(state, linkText, LINK_TEXT);
    }

    public static Selector byPartialLinkText(TestState state, String linkText) {
        return new Selector(state, linkText, PARTIAL_LINK_TEXT);
    }

    public static Selector byName(TestState state, String name) {
        return new Selector(state, name, NAME);
    }

    public static Selector byTagName(TestState state, String tagName) {
        return new Selector(state, tagName, TAG_NAME);
    }

    public static Selector byClassName(TestState state, String className) {
        return new Selector(state, className, CLASS_NAME);
    }
}
