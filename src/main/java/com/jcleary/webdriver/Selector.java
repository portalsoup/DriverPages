package com.jcleary.webdriver;

import com.jcleary.annotations.Hack;
import com.jcleary.core.State;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static com.jcleary.webdriver.ByFactory.*;
import static java.util.stream.Collectors.toList;

/**
 * A mechanism to locate elements on demand and perform operations on them.  This class' main goal is to eliminate
 * state management from Selenium's WebElement class.  An instantiated WebElement goes stale as soon as the DOM
 * changes in such a way that the expected element is not in the exact same position.  Assuming the locator to an
 * element remains constant, this class should avoid throwing stale element exceptions.
 *
 * Of course, {@link #get()} is equivalent to {@code WebDriver.findElement(By)} and {@link #getMultiple()} is
 * equivalent to {@code WebDriver.findElements(By} if regular WebElement instances are required.
 *
 * Created by julian on 9/15/2015.
 */
public class Selector {

    /**
     * The default timeout in milliseconds for all methods
     * that wait for an element to change into a particular state.
     */
    private long timeoutMillis = 10000L;

    /**
     * The default polling speed in milliseconds for all methods
     * that wait for an element to change into a particular state.
     */
    private long pollingIntervalPeriodMillis = 200L;

    /**
     * Used for getting system time and performing basic operations.
     */
    private final Clock clock;

    /**
     * Used for sleeping to control polling speed.
     */
    private final Sleeper sleeper;

    /**
     * The locator string used to identify one or more WebElements using locator types with Selenium's By.
     */
    private final String locator;

    /**
     * The ByType instance is used to determine the type of locator used by this Selector.
     */
    private final ByFactory type;

    /**
     * The test environment state object that holds all stated information about the test session this Selector
     * is running in.
     */
    private final State state;

    public Selector(State state, String locator, ByFactory type) {
        this.locator = locator;
        this.type = type;
        this.state = state;
        clock = new SystemClock();
        sleeper = Sleeper.SYSTEM_SLEEPER;
    }

    public Selector(State state, String cssLocator) {
        this(state, cssLocator, CSS);
    }

    public Selector(Selector relativeRootNode, String cssLocator) {
        this(relativeRootNode.getState(), relativeRootNode.getLocator().trim() + " " + cssLocator);
    }

    /*/****************
     * Public Methods *
     ******************/

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public long getPollingIntervalPeriodMillis() {
        return pollingIntervalPeriodMillis;
    }

    public void setPollingIntervalPeriodMillis(long pollingIntervalPeriodMillis) {
        this.pollingIntervalPeriodMillis = pollingIntervalPeriodMillis;
    }

    public By getBy() {
        return getType().get(getLocator());
    }

    /**
     * Get the first found occurrence of a WebElement that matches this Selector's {@link #locator}.
     *
     * @return                              The first WebElement found by WebDriver
     *
     * @exception NoSuchElementException    If no element is found by WebDriver
     */
    public WebElement get() {
        return state.getDriver().findElement(type.get(locator));
    }

    /**
     * Get the first matching occurrence of a WebElement that satisfies a predicate.
     *
     * @param condition                     A predicate that accepts a WebElement
     *                                      parameter and evaluate to true or false
     *
     * @return                              The first WebElement found by WebDriver
     *                                      that satisfies the predicate
     *
     * @exception NoSuchElementException    If no element is found by WebDriver that satisfies the predicate
     */
    public WebElement getWhere(Predicate<WebElement> condition) {
        try {
            return getMultiple().stream().filter(condition::test).findFirst().get();
        } catch (java.util.NoSuchElementException e) {
            throw new NoSuchElementException("Could not find an element that satisfies the predicate.", e);
        }
    }

    /**
     * Get a List of each WebElement found by WebDriver that
     * matches this Selector's {@link #locator}.
     *
     * @return                              A List of all WebElements that are found by this Selector's locator
     */
    public List<WebElement> getMultiple() {
        return state.getDriver().findElements(type.get(locator));
    }

    /**
     * Get a {@link List} of each {@link WebElement} found with this Selector's
     * {@link #locator} that also satisfies a predicate.
     *
     * @param condition                     A predicate that accepts a WebElement parameter and results to true or false
     *
     * @return                              A List of all WebElements that both is found by this
     *                                      Selector's locator and satisfies the predicate
     */
    public List<WebElement> getMultipleWhere(Predicate<WebElement> condition) {
        return getMultiple().stream().filter(condition::test).collect(toList());
    }

    public boolean isPresent() {
        try {
            get();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isDisplayed() {
        return get().isDisplayed();
    }

    /**
     * Click on the first found WebElement that matches this Selector's {@link #locator}.
     *
     * @return                              This Selector instance
     */
    public Selector click() {
        get().click();
        return this;
    }

    /**
     * If the first found WebElement is a form, or is an element within a form, this will submit the form.
     *
     * @return                              This Selector instance
     */
    public Selector submit() {
        get().submit();
        return this;
    }

    /**
     * Send a series of keystrokes to the first WebElement that matches this Selector's {@link #locator}.
     *
     * @param chars                         A string of characters to be sent to an element
     *
     * @return                              This Selector instance
     */
    public Selector sendKeys(CharSequence...chars) {
        get().sendKeys(chars);
        return this;
    }

    /**
     * If this element is a text entry element, this will clear the value.
     *
     * @return                              This Selector instance
     */
    public Selector clear() {
        get().clear();
        return this;
    }

    /**
     * Get the html tag name of this element.
     *
     * @return                              The tag name of the first found element
     */
    public String getTagName() {
        return get().getTagName();
    }

    public String getAttribute(String name) {
        return get().getAttribute(name);
    }

    /**
     * Get the text of the first found WebElement found by this Selector's {@link #locator}
     *
     * @return                              All visible text contained in the first found WebElement
     */
    public String getText() {
        return get().getText();
    }

    /**
     * Wait until the first found WebElement satisfies a boolean returning predicate.
     *
     * @param condition                     A predicate that accepts a WebElement
     *                                      parameter and results to true or false
     *
     * @return                              This Selector instance
     *
     * @exception TimeoutException          If the first found WebElement does not satisfy
     *                                      the predicate before the time limit
     */
    public Selector waitUntil(Predicate<WebElement> condition) {
        long delay = clock.laterBy(timeoutMillis);

        // Stores the latest thrown exception while waiting.
        Throwable storedException = null;

        while (clock.isNowBefore(delay)) {

            try {
                if (condition.test(get())) {
                    return this;
                }
            } catch (NoSuchElementException e) {
                storedException = e;
            }
            try {
                sleeper.sleep(new Duration(pollingIntervalPeriodMillis, TimeUnit.MILLISECONDS));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        String throwMessage = "Timed out after " + timeoutMillis + " milliseconds waiting for the " +
                "first found element to match the predicate.";

        // Check if we have causation
        if (storedException == null) {
            throw new TimeoutException(throwMessage);
        } else {
            throw new TimeoutException(throwMessage, storedException);
        }

    }

    /**
     * Wait until the first found WebElement meets the expectations of an ExpectedCondition.
     *
     * @param condition                     The ExpectedCondition that should return a true, non-null
     *                                      value to satisfy the criteria
     *
     * @return                              This Selector instance
     *
     * @exception TimeoutException          If the first found WebElement does not meet
     *                                      the expectations of the ExpectedConditions
     */
    public Selector waitUntilExpectedCondition(ExpectedCondition<WebElement> condition) {
        new WebDriverWait(state.getDriver(), 10).until(condition);
        return this;
    }

    /**
     * Wait until this Selector's locator finds at least one WebElement under the given Predicate.
     *
     * @param condition The condition to be satisfied.  Returns true or false
     *
     * @return                              The first found WebElement
     *
     * @exception TimeoutException          If no elements are found within the timeout
     */
    public WebElement waitForFirstOccurrenceWhere(Predicate<WebElement> condition) {
        long delay = clock.now() + timeoutMillis;

        while (clock.isNowBefore(delay)) {

            try {
                Optional<WebElement> maybeWebElement = getMultiple()
                        .stream()
                        .filter(condition)
                        .findFirst();

                if (maybeWebElement.isPresent()) {
                    return maybeWebElement.get();
                }
            } catch (NoSuchElementException e) {
                continue;
            }
            try {
                sleeper.sleep(new Duration(pollingIntervalPeriodMillis, TimeUnit.MILLISECONDS));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }

        }
        throw new TimeoutException("Timed out waiting for the first occurrence of an element that matches the predicate.");
    }

    public State getState() {
        return state;
    }

    public String getLocator() {
        return locator;
    }

    public ByFactory getType() {
        return type;
    }
}