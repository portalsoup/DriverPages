package com.jcleary.webdriver;

import org.openqa.selenium.By;

/**
 *  Allows selecting and passing Selenium By types as values.  Calling {@link #get(String)} will instantiate a new By
 * object of consistent type with the enum constant.
 *
 * Created by julian on 9/16/2015.
 */
public enum ByFactory {
    CSS(By::cssSelector),
    XPATH(By::xpath),
    ID(By::id),
    LINK_TEXT(By::linkText),
    PARTIAL_LINK_TEXT(By::partialLinkText),
    NAME(By::name),
    TAG_NAME(By::tagName),
    CLASS_NAME(By::className);

    /**
     * The functional interface that each constant will hold a unique implementation for.
     */
    FunctionalBy by;

    ByFactory(FunctionalBy by) {
        this.by = by;
    }
    
    public By get(String locator) {
        return by.get(locator);
    }
}

/**
 * Functional interface to accept a locator string and return a WebDriver By instance.
 */
@FunctionalInterface
interface FunctionalBy {

    /**
     * Accept a locator String and internally call the matching Selenium By factory method to get a new By instance.
     *
     * @param locator A locator string whether css, xpath or other
     *
     * @return A new By instance for the appropriate type of locator
     */
    By get(String locator);
}