package com.jcleary.webdriver;

import com.jcleary.core.State;

import java.util.Locale;

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

    public SelectorFactory(State testState, String locator) {
        this(testState, locator, CSS);
    }

    /**
     * Instantiate a new Selector using String formatters to replace format variables with values.
     * {@link String#format(String, Object...)} is what is used to format the strings and as such
     * all formatting rules that apply to that method apply here.
     *
     * @param vars Variables to be formatted into the locator
     *
     * @return A new Selector instance using the formatted locator
     */
    public Selector get(Object...vars) {
        return new Selector(testState, String.format(locator, vars), by);
    }
}
