package com.jcleary.pageobjects.reddit;

import com.jcleary.core.BasicState;
import com.jcleary.webdriver.Page;
import com.jcleary.webdriver.UrlParameter;

/**
 * Created by julian on 10/3/2015.
 */
@Page.Info(relativePath = "/r")
public class SubReddit extends RedditPage {

    public SubReddit(BasicState state) {
        super(state);
    }

    public SubReddit(BasicState state, UrlParameter... parameters) {
        super(state, parameters);
    }
}
