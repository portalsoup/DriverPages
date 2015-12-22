## DriverPages

A framework for writing Selenium page objects.


# Directory structure

*src/main*: Where the core framework lives.  This is mostly interfaces and WebDriver containing classes to implement,
extend and use.

*src/test*: Sample implementation of a page object structure and a few TestNG driven tests to perform actions.


# A base overview of the core objects and what they do:

*com.jcleary.webdriver.Selector*: Represents a mechanism to locate elements by css, xpath and other means.  A Selector
will, in a way, proxy Selenium's WebElement object, offering a very similar API that will instantiate and destroy
WebElement instances as required to perform actions.  It also provides much more advanced operations you can
perform to an element.  Instances of these are created by SelectorFactory for convenience, which provides more fluid
readability with it's flow.  And the ability to generate Selectors with dynamic string locators.

*com.jcleary.webdriver.Page*: Declares that an object represents a page, typically having a url that can be traveled
to (unless it's a single page app). Contains methods and annotations useful for declaring meta data; like the URL
that a page can be reached at and useful browser functions.  A page object will hold Selector references representing
it's useful html elements.  And methods that perform actions on them either individually or as groups.

*com.jcleary.webdriver.Loadable*: Declares that an object represents a loadable page or component in a page.  A Loadable
can have strategies to help dynamically determine when loading is complete, such as a page loading.

*com.jcleary.webdriver.Component*: Declares that an object represents a component within a page.  This will hold a
reference to it's 'owning' page and allow entry and exit with fluidity.  A page object may hold multiple component
references.

*com.jcleary.webdriver.core.State*: Page objects sometimes can benefit from having extra data accessible internally.
In order to allow page objects to be immutable if desired, this interface can be implemented in such a way that it
contains all data a page object may want to have always available.  Such as credentials of a user to be logged in.
If a page needs no extra data beyond a readily available WebDriver instance (which is the only requirement), then
BasicState.java can be used.