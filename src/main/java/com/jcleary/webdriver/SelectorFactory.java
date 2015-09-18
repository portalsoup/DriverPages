package com.jcleary.webdriver;

import com.jcleary.core.TestState;

import java.util.ArrayList;
import java.util.List;

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
     * Instantiate a new Selector using TODO
     *
     * @param vars
     *
     * @return
     */
    public Selector get(String...vars) {
        return new Selector(testState, String.format(locator, vars), by);
    }

    /**
     * each row is a unique set of formatter values for the selector this instance manufactures.  A list of selectors
     * the size of the number of columns will be returned
     *
     * @param vars
     *
     * @return
     */
    public List<Selector> getMultiple(String[][] vars) {
        List<Selector> multiple = new ArrayList<>();

        for (String[] aSelector : vars) {
            multiple.add(get(aSelector));
        }

        return multiple;
    }

    public static Selector byCss(TestState state, String cssSelector) {
        return new Selector(state,  cssSelector, CSS);
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
