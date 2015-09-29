package com.jcleary.webdriver;

/**
 * Created by julian on 9/28/2015.
 */
@FunctionalInterface
public interface UrlParameter {

    String get(Page page);
}
