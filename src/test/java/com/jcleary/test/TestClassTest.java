package com.jcleary.test;

import com.jcleary.core.TestState;
import com.jcleary.webdriver.Selector;
import com.jcleary.webdriver.SelectorFactory;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.function.Predicate;

/**
 * Created by julian on 9/15/2015.
 */
public class TestClassTest {

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
    public void test() throws InterruptedException {

        state.go("http://www.google.com");

        Selector searchField = SelectorFactory.byCss(state, "input");

        searchField.waitForFirstOccurrenceWhere(e -> e.getAttribute("title").equals("Google Search"));

        searchField.getWhere(e -> e.getAttribute("title").equals("Google Search")).sendKeys("Automation" + Keys.ENTER);

        Selector stats = SelectorFactory.byId(state, "resultStats");

        Thread.sleep(1000);
        stats.waitUntil(WebElement::isDisplayed);
        System.out.println("The number of results are: " + stats.getText());
    }
}
