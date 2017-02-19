package com.jcleary.util;

/**
 * Created by portalsoup on 2/18/17.
 */
public enum RegexUtil {

    INTERPOLATE_SINGLE("\\$\\{(\\w+)}");

    private String pattern;

    RegexUtil(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
