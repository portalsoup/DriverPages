package com.jcleary.sample.page;

import com.jcleary.core.State;
import com.jcleary.webdriver.Page;
import com.jcleary.webdriver.Selector;

import java.io.File;

import lombok.Getter;
import org.openqa.selenium.WebElement;

/**
 * Created by julian on 6/4/2016.
 */
@Page.Info(host = "http://${html.host}")
public class BasicSelectorHtmlPage extends AbstractPage {

    public final Selector curtainButton = new Selector(getState(), "curtain");

    @Getter
    private final Selector delayDiv = new Selector(getState(), "delay-div");

    public BasicSelectorHtmlPage(State state) {
        super(state);
    }

    public String getCurtainText() {
        return curtainButton.getAttribute("value");
    }

    public boolean isDelayDivVisible() {
        return delayDiv.isDisplayed();
    }

    public boolean waitUntilDivVisible() {
        if (delayDiv.waitUntil(WebElement::isDisplayed) == null) {
            return false;
        }
        return true;
    }


    @Override
    public BasicSelectorHtmlPage go() {
        String path = "file:///" + new File("src/test/resources/BasicSelectorHtml.html").getAbsolutePath();

        getState().getDriver().get(path);

        return this;
    }
}
