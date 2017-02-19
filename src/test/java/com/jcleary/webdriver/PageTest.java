package com.jcleary.webdriver;

import com.jcleary.core.BasicState;
import com.jcleary.core.State;
import com.jcleary.page.BasicSelectorHtmlPage;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

/**
 * Created by portalsoup on 2/16/17.
 */
public class PageTest {

    @Test
    public void test() {
        BasicState mockState = mock(BasicState.class);
        BasicSelectorHtmlPage mockPage = mock(BasicSelectorHtmlPage.class);

        when(mockPage.getHostname()).thenReturn("host");
        when(mockPage.getState()).thenReturn(mockState);

        mockPage.getHostname();
    }
}