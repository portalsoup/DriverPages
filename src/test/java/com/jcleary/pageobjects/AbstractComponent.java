package com.jcleary.pageobjects;

import com.jcleary.webdriver.Component;
import com.jcleary.webdriver.Loadable;
import com.jcleary.webdriver.Selector;

/**
 * Abstract base implementation for all components.
 *
 * @param <T> The owning page to this component.  A valid owner is a page whose {@link IsLoaded} method returns true
 *           when this component's {@link Component#isLoaded()} method also returns true
 */
public abstract class AbstractComponent<T extends AbstractPage> implements Component<T>, Loadable {

    private final T owner;
    private final Selector relativeRootNode;
    public AbstractComponent(T owner, Selector relativeRootNode) {
        this.owner = owner;
        this.relativeRootNode = relativeRootNode;
    }


    @Override
    public T getOwner() {
        return owner;
    }

    @Override
    public Selector getRelativeRootNode() {
        return relativeRootNode;
    }
}
