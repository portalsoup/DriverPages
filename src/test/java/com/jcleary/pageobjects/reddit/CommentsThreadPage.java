package com.jcleary.pageobjects.reddit;

import com.jcleary.core.BasicState;
import com.jcleary.webdriver.Page;
import com.jcleary.webdriver.Selector;
import com.jcleary.webdriver.SelectorFactory;
import com.jcleary.webdriver.UrlParameter;

/**
 * Created by julian on 12/22/2015.
 */
@Page.Info(relativePath = "/comments")
public class CommentsThreadPage extends VariableSubReddit {

    @IsLoaded(containsText = "commentssubscribe", findExactly = 1)
    private final Selector paneStackTitle = SelectorFactory.byCss(getState(), ".panestack-title");

    public CommentsThreadPage(BasicState state, UrlParameter... parameters) {
        super(state, parameters);
    }
}
