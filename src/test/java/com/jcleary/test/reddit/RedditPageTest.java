package com.jcleary.test.reddit;

import com.jcleary.exceptions.PageException;
import com.jcleary.pageobjects.reddit.VariableSubReddit;
import com.jcleary.state.TestState;
import com.jcleary.pageobjects.reddit.RocketLeagueSub;
import com.jcleary.webdriver.UrlParameter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Base page object for all reddit pages.
 *
 * Created by julian on 9/17/2015.
 */
public class RedditPageTest {

    /**
     * Navigates to the rocket league subreddit, verifies that the page is not loaded before, and that it is loaded
     * after.  Then retrieves and prints to console all the posts and their respective karma visible on the page.
     *
     * @throws InterruptedException
     */
    //@Test
    public void simpleRedditNavigationTest() throws InterruptedException {

        try (TestState state = TestState.getInstance()) {
            RocketLeagueSub league = new RocketLeagueSub(state);

            assertThat("Did the rocket league subreddit load?", !league.isLoaded());

            state.getDriver().get(league.url());

            league.waitUntilLoaded(5000);

            assertThat("Did the rocket league subreddit load?", league.isLoaded());

            league.getPostsOnPage().forEach(System.out::println);
        }
    }

    //@Test(enabled = true, dataProvider = "subProvider")
    public void subredditTest(String subName, String formatted) {

        try (TestState state = TestState.getInstance()) {
            VariableSubReddit sub = new VariableSubReddit(state, new UrlParameter("subreddit", subName));

            state.go(sub.url());

            assertThat("Invalid subreddit name.", sub.getSubName(), is(formatted));
        }
    }

    @DataProvider(name = "subProvider")
    public Object[][] subProvider() {
        return new Object[][] {
                //url section       Correctly displayed
                {"rocketleague",    "RocketLeague" },
                {"programming",     "programming" },
                {"gaming",          "Gaming" }};
    }
}
