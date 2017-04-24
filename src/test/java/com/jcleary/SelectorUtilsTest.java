package com.jcleary;

import com.jcleary.core.State;
import com.jcleary.webdriver.ByFactory;
import com.jcleary.webdriver.Selector;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * Created by portalsoup on 3/11/17.
 */
public class SelectorUtilsTest {

    @org.testng.annotations.Test
    public void containsTextPassTest() {
        WebDriver mockDriver = mock(ChromeDriver.class);
        State mockState = mock(State.class);
        WebElement mockElement = mock(WebElement.class);

        when(mockState.getDriver()).thenReturn(mockDriver);
        when(mockDriver.findElement(By.cssSelector(".abc"))).thenReturn(mockElement);
        when(mockElement.getText()).thenReturn("test");

        Selector selector = new Selector(mockState, ".abc", ByFactory.CSS);

        selector.waitUntil(SelectorUtils.containsText("test"));

        verify(mockElement, times(1)).getText();
    }
}