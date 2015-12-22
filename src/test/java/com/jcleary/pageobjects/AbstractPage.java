package com.jcleary.pageobjects;

import com.jcleary.core.BasicState;
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
    private BasicState state;

    private UrlParameter[] parameters;

    public AbstractPage(BasicState state) {
        this.state = state;
    }

    public AbstractPage(BasicState state, UrlParameter...parameters) {
        this(state);
        this.parameters = parameters;
    }

    @Override
    public UrlParameter[] getUrlParameters() {
        return parameters;
    }
}
