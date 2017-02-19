package com.jcleary.core;

import org.openqa.selenium.WebDriver;

import java.util.Map;

/**
 * A store for the active state
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
