package com.jcleary.util;

/**
 * A slightly different minimal interface to {@link java.util.function.Predicate}.  Predicate returns a boolean value,
 * this class returns an object.  Non-null equals true, null equals false.
 *
 * Created by julian on 12/21/2015.
 */
@FunctionalInterface
public interface ExpectedPredicate<T> {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     * @return A non-null return value equals true and a null return value equals false
     */
    Object test(T t);
}
