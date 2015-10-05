package com.jcleary.pageobjects.reddit;

import com.jcleary.state.TestState;
import com.jcleary.webdriver.Page;
import com.jcleary.webdriver.Selector;
import com.jcleary.webdriver.SelectorFactory;

import static com.jcleary.core.Ternary.*;

/**
 * The rocket league subreddit.
 *
 * Created by julian on 9/17/2015.
 */
@Page.Info(relativePath = "/rocketleague")
public class RocketLeagueSub extends SubReddit {

    @IsLoaded(containsText = "RocketLeague", visibility = TRUE, findExactly = 2)
    Selector subName = SelectorFactory.byCss(getState(), ".redditname");

    public RocketLeagueSub(TestState state) {
        super(state);
    }
}
