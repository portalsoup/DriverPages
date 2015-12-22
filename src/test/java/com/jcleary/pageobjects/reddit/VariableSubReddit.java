package com.jcleary.pageobjects.reddit;

import com.jcleary.beans.RedditPost;
import com.jcleary.core.BasicState;
import com.jcleary.exceptions.PageException;
import com.jcleary.webdriver.Page;
import com.jcleary.webdriver.Selector;
import com.jcleary.webdriver.SelectorFactory;
import com.jcleary.webdriver.UrlParameter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jcleary.core.Ternary.*;

/**
 * A simple page implementation demonstrating a single implementation working with multiple pages
 * using dynamic content in the url.
 *
 * Created by julian on 10/4/2015.
 */
@Page.Info(relativePath = "/r/{@subreddit}")
public class VariableSubReddit extends RedditPage {

    public VariableSubReddit(BasicState state, UrlParameter... parameters) {
        super(state, parameters);
    }

    public String getSubName() {
        return subName.waitUntil(WebElement::isDisplayed).getText();
    }

    // This is our critera to know if we're anywhere on reddit.  (In addition to checking the url which we can do)
    @IsLoaded(visibility = TRUE, findExactly = 2)
    private final Selector subName = SelectorFactory.byCss(getState(), ".redditname");

    private final Selector postRow = SelectorFactory.byClassName(getState(), "thing");
    private final Selector postTitle = SelectorFactory.byCss(getState(), "a.title");
    private final Selector postKarma = SelectorFactory.byCss(getState(), ".score.unvoted");
    private final Selector commentsString = SelectorFactory.byCss(getState(), ".comments");


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

            Matcher matcher = Pattern.compile("\\d+").matcher(
                    aRow.findElement(commentsString.waitUntil(ExpectedConditions::visibilityOf).getBy()).getText());

            String comments = (matcher.find() ? matcher.group() : "-1");
            posts.add(new RedditPost(karma, name, Integer.parseInt(comments)));
        }

        if (posts.isEmpty()) {
            throw new PageException("Could not find anything on the page!  Did it finish loading?");
        }
        return posts;
    }

    public CommentsThreadPage goToComments(RedditPost post) {
        for (WebElement aPost : postRow.getMultiple()) {
            String name = aPost.findElement(postTitle.getBy()).getText();
            String karma = aPost.findElement(postKarma.getBy()).getText();

            Matcher matcher = Pattern.compile("\\d+").matcher(
                    aPost.findElement(commentsString.waitUntil(ExpectedConditions::visibilityOf).getBy()).getText());

            String comments = (matcher.find() ? matcher.group() : "-1");
            if (new RedditPost(karma, name, Integer.parseInt(comments)).equals(post)) {
                commentsString.click();
            }
        }

        CommentsThreadPage commentPage = new CommentsThreadPage(getState(), getUrlParameters());
        commentPage.waitUntilLoaded(10000L);
        return commentPage;
    }
}
