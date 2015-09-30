package com.jcleary.webdriver;

/**
 * A component is a re-usable chunk of html that represents a component that exists inside a page.  A page can have
 * many components, and each component has knowledge of what page it belongs to.
 *
 * Created by julian on 9/29/2015.
 */
public interface Component<T extends Loadable> extends Loadable {

    /**
     * Get the owning page of this component.  A valid owner is a page object whose {@link Page#isLoaded()} method
     * returns true while this component is present on the page.
     *
     * @return The page object that was provided as the designated owner at component instantiation
     */
    T getOwner();

    /**
     * Leave this component and return control back to the owning page.  This method should perform necessary actions
     * to leave this component if necessary and return an instance of the owning page object.
     *
     * @return The page object that was provided as the designated owner at component instantiation
     */
    T leaveComponent();

    /**
     * Get the relative root node that acts as the container to this component.  This should be the nearest parent node
     * that encompasses the entirety of this component in it's inner HTML.
     *
     * @return A Selector that locates the nearest parent node
     */
    Selector getRelativeRootNode();

}
