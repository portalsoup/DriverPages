package com.jcleary.test;

import com.jcleary.exceptions.PageException;
import com.jcleary.pageobjects.reddit.VariableSubReddit;
import com.jcleary.state.TestState;
import com.jcleary.webdriver.UrlParameter;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * A test demonstrating using a single page implementation with multiple pages using dynamic content in the url.
 *
 * TODO notes:
 *
 * Create a runtime tool that can be run to scan and find mismatches
 *
 * Created by julian on 10/3/2015.
 */
public class DynamicUrlTest {

    @Test(expectedExceptions = PageException.class)
    public void badParameterKey() {
        try (TestState state = mock(TestState.class)) {
            new VariableSubReddit(state, new UrlParameter("herpderp", "SampleData")).url();
        }
    }

    @Test
    public void goodParameterKey() {
        try (TestState state = mock(TestState.class)) {
            VariableSubReddit sub = new VariableSubReddit(state, new UrlParameter("subreddit", "SampleData"));

            String url = sub.url();

            assertThat("The url's parameter wasn't substituted properly!",
                    url, Matchers.containsString("/r/SampleData"));
        }
    }
}
