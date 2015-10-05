package com.jcleary.test;

import com.jcleary.state.TestState;
import com.jcleary.pageobjects.reddit.RocketLeagueSub;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Base page object for all reddit pages.
 *
 * Created by julian on 9/17/2015.
 */
public class RedditPageTest {

    private TestState state;

    @BeforeMethod
    public void setupState() {
        state = TestState.getInstance();
    }

    @AfterMethod
    public void tearDownState() {
        state.close();
    }

    /**
     * Navigates to the rocket league subreddit, verifies that the page is not loaded before, and that it is loaded
     * after.  Then retrieves and prints to console all the posts and their respective karma visible on the page.
     *
     * @throws InterruptedException
     */
    @Test
    public void simpleNavigationTest() throws InterruptedException {

        RocketLeagueSub league = new RocketLeagueSub(state);

        assertThat("Did the rocket league subreddit load?", !league.isLoaded());

        state.getDriver().get(league.url());

        league.waitUntilLoaded(5000);

        assertThat("Did the rocket league subreddit load?", league.isLoaded());

        league.getPostsOnPage().forEach(System.out::println);
    }
}
