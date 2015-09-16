package com.jcleary.test;

import org.openqa.selenium.Keys;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
    public void test() {

        state.go("http://www.google.com");

        Selector searchField = SelectorFactory.byCss(state, "input");

        searchField.getWhere((e) -> e.getAttribute("title").equals("Search")).sendKeys("Automation" + Keys.ENTER);
    }
}
