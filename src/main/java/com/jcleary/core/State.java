package com.jcleary.core;

import org.openqa.selenium.WebDriver;

/**
 * The state of the environment around pages.  An implementation that contains at least an instance of WebDriver is
 * required for all page objects.  This is the interface that allows you to pass data into your page objects.
 *
 * Created by julian on 10/3/2015.
 */
public interface State {

    /**
     * Get the instance of WebDriver that is controlling the browser for this session.
     *
     * @return An instance of an implementation of WebDriver
     */
    WebDriver getDriver();
}
