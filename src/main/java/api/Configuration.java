package api;

/**
 * Class Configuration
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
class Configuration
{
    private Feed[] feeds;
    private ItemList[] itemList;

    Configuration()
    {
        feeds = null;
        itemList = null;
    }

    /*
    ------------------------------- ACCESSORS AND MUTATORS -----------------------------------------
    */

    Feed[] getFeeds()
    {
        return feeds;
    }

    ItemList[] getItemList()
    {
        return itemList;
    }

    void setFeeds(Feed[] feeds)
    {
        this.feeds = feeds;
    }

    void setItemList(ItemList[] itemList)
    {
        this.itemList = itemList;
    }
}

// Created: 2016-04-19
