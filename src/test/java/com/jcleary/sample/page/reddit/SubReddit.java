package com.jcleary.sample.page.reddit;

import com.jcleary.core.State;
import com.jcleary.webdriver.Page;
import com.jcleary.webdriver.UrlParameter;

/**
 * Created by julian on 10/3/2015.
 */
@Page.Info(relativePath = "/r")
public class SubReddit extends RedditPage {

    public SubReddit(State state) {
        super(state);
    }

    public SubReddit(State state, UrlParameter... parameters) {
        super(state);
    }
}
