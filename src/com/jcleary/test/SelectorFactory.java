package com.jcleary.test;

import org.openqa.selenium.By;

import static com.jcleary.test.ByFactory.*;

/**
 * Created by julian on 9/15/2015.
 */
public class SelectorFactory {

    private String TestState;
    private String locator;
    private ByFactory by;

    public SelectorFactory(TestState state, ByFactory byFactory) {
        this.by = byFactory;
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