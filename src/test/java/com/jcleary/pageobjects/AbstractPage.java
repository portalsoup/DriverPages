package com.jcleary.pageobjects;

import com.jcleary.core.TestState;
import com.jcleary.webdriver.Page;

/**
 * Abstract implementation to store the page state.
 *
 * Created by julian on 9/18/2015.
 */
public class AbstractPage implements Page {

    private TestState state;

    public AbstractPage(TestState state) {
        this.state = state;
    }

    @Override
    public TestState getState() {
        return state;
    }
}
