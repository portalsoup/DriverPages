package com.jcleary.test.reddit;

import com.jcleary.beans.RedditPost;
import com.jcleary.core.State;
import com.jcleary.pageobjects.reddit.VariableSubReddit;
import com.jcleary.webdriver.UrlParameter;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by julian on 12/22/2015.
 */
public class NavigationTest {

    @Test
    public void navigateThroughPages() {
        try (State state = State.getInstance()) {
            VariableSubReddit league = new VariableSubReddit(state, new UrlParameter("subreddit", "rocketleague"));

            assertThat("Did the rocket league subreddit load?", !league.isLoaded());

            state.getDriver().get(league.url());

            league.waitUntilLoaded(5000);

            assertThat("Did the rocket league subreddit load?", league.isLoaded());

            List<RedditPost> posts = league.getPostsOnPage();
            posts.forEach(System.out::println);

            RedditPost aPost = posts.get(ThreadLocalRandom.current().nextInt(posts.size()));

            league.goToComments(aPost);
        }
    }
}
