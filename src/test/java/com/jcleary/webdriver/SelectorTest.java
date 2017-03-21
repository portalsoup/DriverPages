package com.jcleary.webdriver;

import com.google.common.collect.ImmutableList;
import com.jcleary.core.State;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.SystemClock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.jcleary.webdriver.ByFactory.CSS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

/**
 * Created by portalsoup on 3/6/17.
 */
public class SelectorTest {

    private State mockState;
    private Selector selector;
    private String cssLocator;
    private By expectedBy;
    private List<WebElement> elementList;
    private WebElement mockElement1;
    private WebElement mockElement2;
    private WebElement mockElement3;
    private WebDriver mockDriver;

    @BeforeMethod
    public void setup() {
        mockState = mock(State.class);
        cssLocator = "div#id";
        selector = new Selector(mockState, cssLocator, CSS);
        selector.setTimeoutMillis(1L);
        selector.setPollingIntervalPeriodMillis(1L);
        expectedBy = By.cssSelector(cssLocator);

        mockDriver = mock(ChromeDriver.class);
        mockElement1 = mock(WebElement.class);
        mockElement2 = mock(WebElement.class);
        mockElement3 = mock(WebElement.class);
        elementList = new ArrayList<>();

        elementList.add(mockElement1);
        elementList.add(mockElement2);
        elementList.add(mockElement3);
        when(mockDriver.findElement(expectedBy)).thenReturn(mockElement1);
        when(mockState.getDriver()).thenReturn(mockDriver);
        when(mockDriver.findElements(expectedBy)).thenReturn(elementList);

        when(mockElement1.isDisplayed()).thenReturn(false);
        when(mockElement1.isSelected()).thenReturn(false);
        when(mockElement2.isDisplayed()).thenReturn(true);
        when(mockElement2.isSelected()).thenReturn(true);
        when(mockElement3.isDisplayed()).thenReturn(false);
        when(mockElement3.isSelected()).thenReturn(true);
    }

    @Test
    public void getByTest() {
        assertThat(selector.getBy(), equalTo(expectedBy));
    }

    @Test
    public void getTest() {
        assertThat(selector.get(), equalTo(mockElement1));
    }

    @Test
    public void getWhereSucceedTest() {
        assertThat(selector.getWhere(WebElement::isDisplayed), equalTo(mockElement2));
    }

    @Test
    public void getWhereFailTest() {
        try {
            selector.getWhere(WebElement::isEnabled);
            assert false;
        } catch (NoSuchElementException nsee) {
            assertThat(nsee.getMessage(), startsWith("Could not find an element that satisfies the predicate."));
        }

    }

    @Test
    public void getMultipleWhereTest() {
        assertThat(selector.getMultipleWhere(WebElement::isSelected), equalTo(ImmutableList.of(mockElement2, mockElement3)));
    }

    @Test
    public void isPresentSucceedTest() {
        assertThat(selector.isPresent(), equalTo(true));
    }

    @Test
    public void isPresentFailTest() {
        State mockState = mock(State.class);
        WebDriver mockDriver = mock(ChromeDriver.class);
        when(mockState.getDriver()).thenReturn(mockDriver);
        when(mockDriver.findElement(expectedBy)).thenThrow(new NoSuchElementException("Flabbergasted."));
        Selector selector1 = new Selector(mockState, cssLocator, CSS);

        assertThat(selector1.isPresent(), equalTo(false));
    }

    @Test
    public void isDisplayedSucceedTest() {
        State mockState = mock(State.class);
        WebDriver mockDriver = mock(ChromeDriver.class);

        when(mockState.getDriver()).thenReturn(mockDriver);
        when(mockDriver.findElement(expectedBy)).thenReturn(mockElement2);
        Selector selector1 = new Selector(mockState, cssLocator, CSS);

        assertThat(selector1.isDisplayed(), equalTo(true));
    }

    @Test
    public void isDisplayedFailTest() {
        assertThat(selector.isDisplayed(), equalTo(false));
    }

    @Test
    public void clickTest() {
        selector.click();
        verify(mockElement1, times(1)).click();
    }

    @Test
    public void submitTest() {
        selector.submit();
        verify(mockElement1, times(1)).submit();
    }

    @Test
    public void sendKeysTest() {
        String chars = "abcdef";
        selector.sendKeys(chars);
        verify(mockElement1, times(1)).sendKeys(chars);
    }

    @Test
    public void clearTest() {
        selector.clear();
        verify(mockElement1, times(1)).clear();
    }

    @Test
    public void getTagNameTest() {
        selector.getTagName();
        verify(mockElement1, times(1)).getTagName();
    }

    @Test
    public void getAttributeTest() {
        String name = "name";
        selector.getAttribute(name);
        verify(mockElement1, times(1)).getAttribute(name);
    }

    @Test
    public void getText() {
        selector.getText();
        verify(mockElement1, times(1)).getText();
    }

    @Test
    public void waitUntilSingleIterationTest() {
        selector.waitUntil(e -> true);
        verify(mockDriver, times(1)).findElement(expectedBy);
    }

    @Test
    public void waitUntilTest() {
        selector.setTimeoutMillis(5000L);
        selector.setPollingIntervalPeriodMillis(50L);

        Clock clock = new SystemClock();
        long now = clock.now();
        long delay = 2000;
        selector.waitUntil(e -> clock.isNowBefore(delay + now));

        assertTrue(clock.isNowBefore(delay + now));
        verify(mockDriver).findElement(expectedBy);
    }

    @Test
    public void waitUntilThrowWithCauseTest() {
        try {
            selector.waitUntil(e -> {
                throw new NoSuchElementException("You got thrown");
            });
        } catch (Throwable e) {
            assertThat(e.getCause(), instanceOf(NoSuchElementException.class));
            assertThat(e.getCause().getLocalizedMessage(), containsString("You got thrown"));
        }
    }

    @Test
    public void waitUntilThrowWithoutCauseTest() {
        try {
            selector.waitUntil( e -> false);
        } catch (Throwable e) {
            assertTrue(e.getCause() == null);
        }
    }

    @Test
    public void waitForFirstOccuranceWhereSuccessTest() {
        selector.setTimeoutMillis(5000L);
        selector.setPollingIntervalPeriodMillis(50L);

        WebElement element = selector.waitForFirstOccurrenceWhere(WebElement::isDisplayed);

        assertThat(element, equalTo(mockElement2));
    }

    @Test
    public void waitForFirstOccuranceNotFoundTest() {
        selector.setTimeoutMillis(5000L);
        selector.setPollingIntervalPeriodMillis(50L);

        when(mockDriver.findElements(expectedBy)).thenThrow(new NoSuchElementException(""));

        try {
            WebElement element = selector.waitForFirstOccurrenceWhere(WebElement::isDisplayed);
        } catch (Throwable e) {
            assertThat(e, instanceOf(TimeoutException.class));
        }
    }
}