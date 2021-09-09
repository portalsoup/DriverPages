# DriverPages

Generic lightweight page object model framework for web app automation.  

One example of building page objects:

The use of Selectors and the state store are completely optional

    // This abstract class is really only demonstrating how urls build with class inheritance
    // in these annotations
    @Page.Info(host = "https://www.reddit.com")
    abstract class RedditPage extends Page {

        // Stored for convenient access from the state
        protected final StateStore store;

        public RedditPage(State state) {
            super(state);
            this.store = getState().store();
        }

        public RedditPage(Page page) {
            super(page);
            this.store = getState().store();
        }
    }


    // Uses the same url as it's parent
    @Page.Info
    class FrontPage extends RedditPage {


        private Selector searchForm = new Selector(getState(), "#search");
        private Selector searchField = new Selector(searchForm, "[type='text']");
        private Selector searchButton = new Selector(searchForm, "[type='submit']");

        public FrontPage(State state) {
            super(state);
        }

        public FrontPage(Page page) {
            super(page);
        }

        /**
         * Perform a search on the reddit front page.  The stored value in the state with the key "reddit.search" is used.
         * 
         * @return A page object that maps the reddit search page.
         */
        public SearchPage search() {
            searchField.waitUntil(WebElement::isDisplayed)
                    // Need to cast type because sendKeys's parameter is a CharSequence.  Not a problem
                    // when the types properly match.
                    .sendKeys(store.<String>getItem("reddit.search")); 

            searchButton.waitUntil(WebElement::isDisplayed)
                    .click();
            return new SearchPage(this);
        }
    } 

    // Adds /search to the end of the url of it's parent
    @Page.Info(relativePath = "/search")
    class SearchPage extends RedditPage {
    

        public SearchPage(State state) {
            super(state);
        }

        public SearchPage(Page page) {
            super(page);
        }
        
         /**
         * Attempt to wait until a previously searched term successfully navigated to the search page.
         * @return
         */
        public boolean verifySearch() {
            this.waitUntil(p ->
                    p.getState()
                            .getDriver()
                            .getCurrentUrl()
                            .contains("https://www.reddit.com/search")
            );
            return getState().getDriver().getCurrentUrl().contains("?q=" + store.<String>getItem("reddit.search"));
        }

    }
 

Using these page objects could be done like this:

        try (State state = new State()) {
            state.store().update("reddit.search", "Pikachu");
            FrontPage page = new FrontPage(state);
            page.go();
            SearchPage searchPage = page.search();
            assertTrue(page.verifySearch());
        }
