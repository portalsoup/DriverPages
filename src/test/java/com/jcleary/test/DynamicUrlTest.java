package com.jcleary.test;

import com.jcleary.pageobjects.reddit.VariableSubReddit;
import com.jcleary.state.TestState;
import com.jcleary.webdriver.UrlParameter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * A test demonstrating using a single page implementation with multiple pages using dynamic content in the url.
 *
 * Created by julian on 10/3/2015.
 */
public class DynamicUrlTest {

    private TestState state;

    @BeforeMethod
    public void setupState() {
        state = TestState.getInstance();
    }

    @AfterMethod
    public void tearDownState() {
        state.close();
    }


    @Test(enabled = true, dataProvider = "subProvider")
    public void subredditTest(String subName, String formattedSubName) throws InterruptedException {

        VariableSubReddit sub = new VariableSubReddit(state, new UrlParameter("subreddit", subName));

        state.getDriver().get(sub.url());

        assertThat("Invalid subreddit name.", sub.getSubName(), is(formattedSubName));
    }

    @DataProvider(name = "subProvider")
    public Object[][] subProvider() {
        return new Object[][] {
                {"rocketleague",    "RocketLeague"},
                {"programming",     "programming"},
                {"gaming",          "Gaming"}};
    }
}
