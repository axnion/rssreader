package api;

/**
 * Class Configuration
 *
 * The Configuration class is used to store the configuration of all feeds included in this
 * configuration and all lists in this configuration. An Configuration object is created when
 * loding a configuration and is also converted to JSON when we save a configuration. So this is the
 * Java object version of the configuration json file.
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
class Configuration
{
    private Feed[] feeds;
    private ItemList[] itemLists;

    /**
     * Constructor
     */
    Configuration()
    {
        feeds = null;
        itemLists = null;
    }

    /*
    ------------------------------- ACCESSORS AND MUTATORS -----------------------------------------
    */

    /**
     * Returns the feeds array.
     * @return An array of Feed objects containing all Feeds of this Configuration
     */
    Feed[] getFeeds()
    {
        return feeds;
    }

    /**
     * Returns the array of ItemList objects in this object.
     * @return An array of ItemList objects contained in this Configuration
     */
    ItemList[] getItemLists()
    {
        return itemLists;
    }

    /**
     * Sets the Feed array of this Configuration
     * @param feeds An array of Feed objects we want to be used in this Configuration
     */
    void setFeeds(Feed[] feeds)
    {
        this.feeds = feeds;
    }

    /**
     * Sets the ItemList array of this Configuration.
     * @param itemList An array of ItemList objects to be used in this Configuration
     */
    void setItemLists(ItemList[] itemList)
    {
        this.itemLists = itemList;
    }
}

// Created: 2016-04-19
