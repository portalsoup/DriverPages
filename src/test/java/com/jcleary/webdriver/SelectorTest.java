package com.jcleary.webdriver;

import com.jcleary.core.State;
import com.jcleary.page.BasicSelectorHtmlPage;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.SystemClock;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by julian on 6/4/2016.
 */
public class SelectorTest {

    @Test
    public void loadHtmlTest() throws InterruptedException {

        try (State state = State.getInstance()) {
            BasicSelectorHtmlPage page = new BasicSelectorHtmlPage(state);

            page.go();

            Thread.sleep(1000);
            assertThat(page.getCurtainText(), not(equalTo("")));
        }
    }

    @Test
    public void clickTest() throws InterruptedException {

        try (State state = State.getInstance()) {
            BasicSelectorHtmlPage page = new BasicSelectorHtmlPage(state);

            page.go();
            Thread.sleep(1000);

            assertThat("Does the curtains button say open curtain before click?",
                    page.getCurtainText(), equalTo("Open Curtain"));

            //page.clickCurtain();
            assertThat("Does the curtains button say closed curtain after click?",
                    page.getCurtainText(), equalTo("Close Curtain"));
        }
    }

    @Test
    public void testWaitUntilVisible() throws InterruptedException {
            try (State state = State.getInstance()) {
                BasicSelectorHtmlPage page = new BasicSelectorHtmlPage(state);

                Thread.sleep(1000);
                Clock clock = new SystemClock();
                long before = clock.now();

                page.go();

                assertThat(page.isDelayDivVisible(), equalTo(false));

                page.waitUntilDivVisible();

                assertThat(clock.now() - before, greaterThanOrEqualTo(5000L));
                System.out.println(clock.laterBy(before));

                assertThat(page.isDelayDivVisible(), equalTo(true));
            }
    }
}