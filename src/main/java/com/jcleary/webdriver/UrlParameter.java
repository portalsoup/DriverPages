package com.jcleary.webdriver;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * An immutable data structure representing a single url parameter that
 * is identified by a name and contains a value.
 *
 * Created by julian on 9/28/2015.
 */
public final class UrlParameter {

    @Getter(AccessLevel.PUBLIC)
    private final String key;

    @Getter(AccessLevel.PUBLIC)
    private final String value;

    /**
     * Defines a value to replace all instances of defined variables with the same name as the key.
     *
     * @param key The name of the defined variable
     * @param value The value all instances of the defined variable will be replaced with
     */
    public UrlParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
