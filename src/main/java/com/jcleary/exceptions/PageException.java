package com.jcleary.exceptions;

import com.jcleary.webdriver.Page;
import lombok.Getter;
import org.openqa.selenium.WebDriverException;

/**
 * General exception for page related errors.
 *
 * Created by julian on 9/17/2015.
 */
public class PageException extends WebDriverException {

    @Getter
    private final Page occuredOn;

    public PageException(String message) {
        this(message, null);
    }

    public PageException(Throwable reason) {
        this(reason, null);
    }

    public PageException(String message, Page occuredOn) {
        super(message);
        this.occuredOn = occuredOn;
    }

    public PageException(Throwable reason, Page occuredOn) {
        super(reason);
        this.occuredOn = occuredOn;
    }

    public PageException(String message, Throwable reason, Page occuredOn) {
        super(message, reason);
        this.occuredOn = occuredOn;
    }

    public String toString() {
        return super.toString() + "\nOccured on: " + occuredOn.getClass().getSimpleName();
    }
}
