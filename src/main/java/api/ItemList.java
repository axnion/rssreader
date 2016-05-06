package api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class ItemList
 *
 * The ItemList class is used to house all the Item objects for each list we can see in the client.
 * It has a name, a String describing how the items are sorted, an array of Strings with the URLs to
 * the feeds we want include in this list. You can add and removed feeds and also update the items
 * array.
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1.1
 */
@JsonIgnoreProperties({"items"})
public class ItemList
{
    private String name;
    private String sorting;
    private String[] feedUrls;
    private Item[] items;

    /**
     * Constructor
     */
    ItemList()
    {
        name = "";
        sorting = "";
        feedUrls = null;
        items = null;
    }

    /**
     * Constructor
     * @param name The unique name of the list
     */
    ItemList(String name)
    {
        this.name = name;
        sorting = "";
        feedUrls = null;
        items = null;
    }

    /**
     * Adds the feed URL to the feedUrls array. Takes the argument URL and checks if it exists in
     * the list already. If it does it is just ignored. If it does not exist it is added to the feed
     * URL list.
     * @param feedUrl A String containing the URL to the feeds XML file.
     */
    void addFeed(String feedUrl)
    {
        String[] newFeedUrlList;
        int i = 0;
        if(feedUrls == null)
        {
            newFeedUrlList = new String[1];
        }
        else
        {
            newFeedUrlList = new String[feedUrls.length + 1];

            for(i = 0; i < feedUrls.length; i++)
            {
                if(feedUrls[i].equals(feedUrl))
                    return;

                newFeedUrlList[i] = feedUrls[i];
            }
        }

        newFeedUrlList[i] = feedUrl;
        setFeedUrls(newFeedUrlList);
    }

    /**
     * Removes the feed URL that is the same as the argument. It copies everything from one array to
     * another exept the URL with the same content as feedUrl
     * @param feedUrl A String containing the identical URL to the one to be removed.
     */
    void removeFeed(String feedUrl)
    {
        String[] newFeedUrlList = null;
        boolean exists = true;

        if(feedUrls == null)
            throw new RuntimeException("There are no feeds to remove in this ItemList");
        else if(feedUrls.length != 1)
        {
            newFeedUrlList = new String[feedUrls.length - 1];
            exists = false;

            for(int i = 0; i < feedUrls.length; i++)
            {
                if(feedUrls[i].equals(feedUrl) && !exists)
                    exists = true;
                else if(i == newFeedUrlList.length && !exists)
                        break;
                else if(!exists)
                    newFeedUrlList[i] = feedUrls[i];
                else
                    newFeedUrlList[i - 1] = feedUrls[i];
            }
        }

        if(exists)
            setFeedUrls(newFeedUrlList);
    }

    /**
     * Updates the items by gathering all the items from every feed included in this itemList.
     * @param feeds An array of Feed objects from the
     */
    void update(Feed[] feeds)
    {
        Item[] newList = null;

        if(feeds == null)
            throw new RuntimeException("No Feeds to update the ItemList");

        if(feedUrls != null)
        {
            for(int i = 0; i < feeds.length; i++)
            {
                for(int j = 0; j < feedUrls.length; j++)
                {
                    if(feeds[i].getUrlToXML().equals(feedUrls[j]))
                    {
                        if(newList == null)
                            newList = feeds[i].getItems();
                        else
                            newList = combineItemArrays(newList, feeds[i].getItems());
                        break;
                    }
                }
            }
        }

        items = newList;
    }

    /**
     * Used to concatenate two arrays into one. So all Item objects from a and b is copied over to
     * an array that is the same length as the combined of a and b. The new array is then returned.
     * @param a An array of Item objects to be added to the new array
     * @param b The other array of Item objects to be added to the new array
     * @return An array containing all elements of both a and b
     */
    private Item[] combineItemArrays(Item[] a, Item[] b)
    {
        Item[] c = new Item[a.length + b.length];

        for(int i = 0; i < a.length; i++)
            c[i] = a[i];
        for(int i = 0; i < b.length; i++)
            c[i + a.length] = b[i];

        return c;
    }

     /*
    ------------------------------- ACCESSORS AND MUTATORS -----------------------------------------
    */

    /**
     * Returns the name of this list
     * @return A String containing the name of the list
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns a string describing how this list is sorted
     * @return A String containing text describing the way the list is sorted
     */
    public String getSorting()
    {
        return sorting;
    }

    /**
     * Return an array of Strings with the URLs of the feeds included in this list
     * @return An array of Strings containing URLs to Feed XML files
     */
    public String[] getFeedUrls()
    {
        return feedUrls;
    }

    /**
     * Returns the array of Item objects
     * @return An array of Item objects in this list
     */
    public Item[] getItems()
    {
        return items;
    }

    /**
     * Sets the name of the ItemList. The argument name is set to the new name of the list
     * @param name A String containing a new name for the list
     */
    void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets the new method of sorting for this list.
     * @param sorting A String containing text describing the way the list should be sorted
     */
    void setSorting(String sorting)
    {
        this.sorting = sorting;
    }

    /**
     * Sets a new array of URL to feeds. This method should be avoided and should only be used by
     * Jackson when reading the configuration file.
     * @param feedUrls An array of Strings containing URLs to feed XML files
     */
    void setFeedUrls(String[] feedUrls)
    {
        this.feedUrls = feedUrls;
    }
}

// Created: 2016-04-20
