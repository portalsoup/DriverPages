package com.jcleary.test;

import com.jcleary.core.TestState;
import com.jcleary.pageobjects.RocketLeagueSub;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by julian on 9/17/2015.
 */
public class RedditPageTest {

    private TestState state;

    @BeforeMethod
    public void setupState() {
        state = new TestState();
    }

    @AfterMethod
    public void tearDownState() {
        state.close();
    }

    @Test
    public void simpleNavigationTest() throws InterruptedException {

        String url = new RocketLeagueSub(state).url();

        state.getDriver().get(url);

        Thread.sleep(5000);
    }
}
