package com.jcleary.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * Created by portalsoup on 2/24/17.
 */
public class StateTest {

    @Test
    public void closeDriverTest() {
        WebDriver mockDriver = mock(FirefoxDriver.class);
        State state = spy(new State(mockDriver));

        state.close();
        verify(mockDriver, times(1)).quit();
    }

    @Test
    public void nullDriverTest() {
        try {
            new State((WebDriver) null);
            assert false;
        } catch (NullPointerException npe) {

        }
    }
}