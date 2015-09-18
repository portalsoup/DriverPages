package com.jcleary.pageobjects;

import com.jcleary.core.TestState;
import com.jcleary.webdriver.Page;

/**
 * Created by julian on 9/17/2015.
 */
@Page.Info(relativePath = "/r/rocketleague")
public class RocketLeagueSub extends RedditPage {

    public RocketLeagueSub(TestState state) {
        super(state);
    }
}
