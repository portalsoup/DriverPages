package com.jcleary.pageobjects;

import com.jcleary.core.TestState;
import com.jcleary.webdriver.Page;

/**
 * Created by julian on 9/17/2015.
 */
@Page.Info(host = "http://reddit.com")
public class RedditPage implements Page {

    private final TestState state;

    public RedditPage(TestState state) {
        this.state = state;
    }
    @Override
    public TestState getState() {
        return state;
    }
}
