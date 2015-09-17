package com.jcleary.exceptions;

import org.openqa.selenium.WebDriverException;

/**
 * Created by julian on 9/17/2015.
 */
public class PageException extends WebDriverException {

    public PageException(String message) {
        super(message);
    }

    public PageException(Throwable reason) {
        super(reason);
    }

    public PageException(String message, Throwable reason) {
        super(message, reason);
    }
}
