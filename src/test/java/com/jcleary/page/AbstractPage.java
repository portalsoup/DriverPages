package com.jcleary.page;

import com.jcleary.core.State;
import com.jcleary.webdriver.Page;

/**
 * Created by julian on 6/5/2016.
 */
public abstract class AbstractPage extends Page {

    public AbstractPage(State state) {
        super(state);
    }
}
