package com.jcleary.pageobjects.reddit;

import com.jcleary.state.TestState;
import com.jcleary.webdriver.Page;
import com.jcleary.webdriver.UrlParameter;

/**
 * Created by julian on 10/3/2015.
 */
@Page.Info(relativePath = "/r")
public class SubReddit extends RedditPage {

    public SubReddit(TestState state) {
        super(state);
    }

    public SubReddit(TestState state, UrlParameter... parameters) {
        super(state, parameters);
    }
}
