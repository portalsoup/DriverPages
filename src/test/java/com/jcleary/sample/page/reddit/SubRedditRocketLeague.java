package com.jcleary.sample.page.reddit;

import com.jcleary.core.State;
import com.jcleary.webdriver.Loadable;
import com.jcleary.webdriver.Page;
import com.jcleary.webdriver.Selector;

import static com.jcleary.util.Ternary.*;

/**
 * The rocket league subreddit.
 *
 * Created by julian on 9/17/2015.
 */
@Page.Info(relativePath = "/rocketleague")
public class SubRedditRocketLeague extends SubReddit {

    @Loadable.IsLoaded(containsText = "RocketLeague", visibility = TRUE, findExactly = 2)
    Selector subName = new Selector(getState(), ".redditname");

    public SubRedditRocketLeague(State state) {
        super(state);
    }
}
