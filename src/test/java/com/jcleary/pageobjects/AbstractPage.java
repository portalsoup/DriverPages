package com.jcleary.pageobjects;

import com.jcleary.state.TestState;
import com.jcleary.webdriver.Page;
import com.jcleary.webdriver.UrlParameter;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Abstract implementation to store the page state.
 *
 * Created by julian on 9/18/2015.
 */
public class AbstractPage implements Page {

    @Getter(AccessLevel.PUBLIC)
    private TestState state;

    private UrlParameter[] parameters;

    public AbstractPage(TestState state) {
        this.state = state;
    }

    public AbstractPage(TestState state, UrlParameter...parameters) {
        this(state);
        this.parameters = parameters;
    }

    @Override
    public UrlParameter[] getUrlParameters() {
        return parameters;
    }
}
