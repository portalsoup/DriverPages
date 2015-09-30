package com.jcleary.pageobjects;

import com.jcleary.core.TestState;
import com.jcleary.webdriver.Loadable;
import com.jcleary.webdriver.Page;
import com.jcleary.webdriver.UrlParameter;
import lombok.Getter;

import java.util.Arrays;

/**
 * Abstract implementation to store the page state.
 *
 * Created by julian on 9/18/2015.
 */
public class AbstractPage implements Page, Loadable {

    @Getter
    private TestState state;

    public AbstractPage(TestState state) {
        this.state = state;
    }

    public AbstractPage(TestState state, UrlParameter...parameters) {
        this(state);

        String relativePath = getRelativePath();

        /* TODO Untested.
         * The idea here is that a user can create their own re-usable implementations for retrieving dynamic url
         * variables.  Or simple pass in a hard value if necessary.  This slightly changes the documented requirements
         * on the Page.Info annotation.
         *
         * The way variables should be used here are sorta bash style such as: www.google.com?{$1}={$2}
         *
         * This does need to be changed so that multiple of the same variable are substituted with the same value.
         * Right now it's just 1:1 find a variable, replace it.  Move on to the next variable value pair
         */
        Arrays.asList(parameters)
                .stream()
                .forEach(parameter->relativePath.replaceAll("\\{\\$\\d+\\}", parameter.get(this)));

    }

}
