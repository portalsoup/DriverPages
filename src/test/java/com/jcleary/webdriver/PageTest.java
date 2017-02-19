package com.jcleary.webdriver;

import com.jcleary.core.State;
import com.jcleary.page.BasicSelectorHtmlPage;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by portalsoup on 2/16/17.
 */
public class PageTest {

    @Test
    public void hostInterpolateTest() {
        State mockState = mock(State.class);
        BasicSelectorHtmlPage mockPage = mock(BasicSelectorHtmlPage.class);

        when(mockPage.getHostname()).thenReturn("host");
        when(mockPage.getState()).thenReturn(mockState);

        mockPage.getHostname();
    }
}