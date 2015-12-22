package com.jcleary.pageobjects.reddit;

import com.jcleary.core.BasicState;
import com.jcleary.webdriver.Page;
import com.jcleary.webdriver.Selector;
import com.jcleary.webdriver.SelectorFactory;
import com.jcleary.webdriver.UrlParameter;
import org.openqa.selenium.WebElement;

/**
 * A simple page implementation demonstrating a single implementation working with multiple pages
 * using dynamic content in the url.
 *
 * Created by julian on 10/4/2015.
 */
@Page.Info(relativePath = "/r/{@subreddit}")
public class VariableSubReddit extends RedditPage {

    private final Selector subName = SelectorFactory.byCss(getState(), ".redditname");

    public VariableSubReddit(BasicState state, UrlParameter... parameters) {
        super(state, parameters);
    }

    public String getSubName() {
        return subName.waitUntil(WebElement::isDisplayed).getText();
    }
}
