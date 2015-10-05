package com.jcleary.pageobjects.reddit;

import com.jcleary.state.TestState;
import com.jcleary.exceptions.PageException;
import com.jcleary.pageobjects.AbstractPage;
import com.jcleary.webdriver.Page;
import com.jcleary.webdriver.Selector;
import com.jcleary.webdriver.SelectorFactory;
import com.jcleary.webdriver.UrlParameter;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Base reddit page.  Things that all pages on reddit share should go here.
 *
 * Created by julian on 9/17/2015.
 */
@Page.Info(host = "http://reddit.com")
public class RedditPage extends AbstractPage {

    /**
     * Encapsulates a single reddit post, containing it's karma and name.
     */
    public class RedditPost {

        private String karma;
        private String name;

        private RedditPost(String karma, String name) {
            this.karma = karma;
            this.name = name;
        }

        public String getKarma() {
            return karma;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return karma + " karma: " + name;
        }
    }

    // This is our critera to know if we're anywhere on reddit.  (In addition to checking the url which we can do)
    @IsLoaded
    Selector subName = SelectorFactory.byCss(getState(), ".redditname");

    Selector postRow = SelectorFactory.byClassName(getState(), "thing");
    Selector postTitle = SelectorFactory.byCss(getState(), "a.title");
    Selector postKarma = SelectorFactory.byCss(getState(), ".score.unvoted");

    public RedditPage(TestState state, UrlParameter...parameters) {
        super(state, parameters);
    }

    /**
     * Get all posts on the current page of a reddit sub.
     *
     * @return A list of {@link RedditPost} for each post on the page
     *
     * @exception PageException If no posts are found on the page
     */
    public List<RedditPost> getPostsOnPage() {
        List<RedditPost> posts = new ArrayList<>();

        for (WebElement aRow : postRow.getMultiple()) {

            String name = aRow.findElement(postTitle.getBy()).getText();
            String karma = aRow.findElement(postKarma.getBy()).getText();

            posts.add(new RedditPost(karma, name));
        }

        if (posts.isEmpty()) {
            throw new PageException("Could not find anything on the page!  Did it finish loading?");
        }
        return posts;
    }
}
