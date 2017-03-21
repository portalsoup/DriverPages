package com.jcleary;

import org.openqa.selenium.WebElement;

import java.util.function.Predicate;

/**
 * Created by portalsoup on 3/11/17.
 */
public class SelectorUtils {
    public static final Predicate<WebElement> visible = (WebElement::isDisplayed);

    public static Predicate<WebElement> containsText(final String text) {
        return (e -> e.getText().equals(text));
    }
}
