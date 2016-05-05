package api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

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
public class Configuration
{
    private Feed[] feeds;
    private ItemList[] itemLists;

    /**
     * Constructor
     */
    public Configuration()
    {
        feeds = null;
        itemLists = null;
    }

    /**
     * Adds a Feed object to the feeds array of the currently set configuration. If the feed already
     * exist a RuntimeException is thrown.
     * @param url A String containing the URL to the new feeds XML file
     * @throws RuntimeException Is thrown if a Feed with an identical URL already exists
     */
    public void addFeed(String url)
    {
        Feed newFeed = new Feed(url);
        Feed[] feeds = getFeeds();
        Feed[] newFeedList;

        if(feeds == null)
        {
            newFeedList = new Feed[1];
            newFeedList[0] = newFeed;
        }
        else
        {
            newFeedList = new Feed[feeds.length + 1];
            int i;

            for(i = 0; i < feeds.length; i++)
            {
                if(feeds[i].getUrlToXML().equals(url))
                    throw new RuntimeException("Feed already exists");
                else
                    newFeedList[i] = feeds[i];
            }

            newFeedList[i] = newFeed;
        }

        setFeeds(newFeedList);
    }

    /**
     * Removes a Feed with an identical URL as the one specified in the url argument.
     * @param url A String containing a URL identical to the URL of the feed to be removed.
     */
    public void removeFeed(String url)
    {
        Feed[] feeds = getFeeds();
        Feed[] newFeedList = null;
        boolean exists = true;

        if(feeds == null)
            throw new RuntimeException("There are no feeds to remove in this Configuration");
        else if(feeds.length != 1)
        {
            newFeedList = new Feed[feeds.length - 1];
            exists = false;

            for(int i = 0; i < feeds.length; i++)
            {
                if(feeds[i].getUrlToXML().equals(url))
                    exists = true;
                else if(i == newFeedList.length && !exists)
                    break;
                else if(!exists)
                    newFeedList[i] = feeds[i];
                else
                    newFeedList[i - 1] = feeds[i];
            }
        }

        if(exists)
            setFeeds(newFeedList);
        else
            throw new RuntimeException("There is no Feed with URL \"" + url + "\" to remove in " +
                    "this Configuration");
    }

    /**
     * Creates an ItemList object with the name of the name argument. The object is created and then
     * added to the itemList field of the current Configuration. If there already is an ItemList
     * with the same name then a RuntimeException is thrown.
     * @param name A String containing a unique name for the ItemList to be created.
     * @throws RuntimeException Is thrown if an ItemList with an identical name already exists
     */
    public void addItemList(String name)
    {
        ItemList newItemList = new ItemList(name);
        ItemList[] oldList = getItemLists();
        ItemList[] newList;

        if(oldList == null)
        {
            newList = new ItemList[1];
            newList[0] = newItemList;
        }
        else
        {
            newList = new ItemList[oldList.length + 1];
            int i;

            for(i = 0; i < oldList.length; i++)
            {
                if(oldList[i].getName().equals(name))
                    throw new RuntimeException("A list with that name already exists");
                else
                    newList[i] = oldList[i];
            }

            newList[i] = newItemList;
        }

        setItemLists(newList);
    }

    /**
     * NOT IMPLEMENTED! NOT IMPLEMENTED! NOT IMPLEMENTED! NOT IMPLEMENTED! NOT IMPLEMENTED!
     * @param name A String containing an identical name to the ItemList to be removed
     */
    public void removeItemList(String name)
    {
        throw new RuntimeException("removeItemList() is not implemented");
    }

    /**
     * Adds a Feed to an ItemList. The correct ItemList is identified by it's index in the itemList
     * in the current Configuration. When the correct itemList is found we call on the addFeed
     * method on the itemList object which adds the url to the itemLists url list.
     * @param index An integer containing the index of the itemList object we are looking for in the
     *              itemList array in the Configuration
     * @param url   A String containing the URL to the XML file for the feed we want to add to the
     *              itemList
     */
    public void addFeedToItemList(int index, String url)
    {
        ItemList[] list = getItemLists();
        list[index].addFeed(url);
        setItemLists(list);
    }

    /**
     * This is the main update method. It calls on the other update methods in both the Feed objects
     * and the itemList objects. So it will go though every Feed object and check for updates and
     * update them, and the go though every itemList and update them.
     */
    public void update()
    {
        Feed[] feeds = getFeeds();
        ItemList[] itemLists = getItemLists();

        if(feeds == null)
            throw new RuntimeException("There is nothing to be updated");

        for(Feed feed : feeds)
            feed.update();
        for(ItemList itemList : itemLists)
            itemList.update(feeds);
    }

    /**
     * Saves the current configuration to a .json file at the path specified in the argument path.
     * @param path A String containing the path to where the user wants the configuration to be
     *             saved.
     */
    public void saveConfig(String path)
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try
        {
            mapper.writeValue(new File(path), this);
        }
        catch(IOException err)
        {
            err.printStackTrace();
        }
    }

    /**
     * Load a configuration from a .json file specified by the path argument.
     * @param path A String containing the path to where the file we want to load should be.
     */
    public void loadConfig(String path)
    {
        Configuration loadedFile;
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            loadedFile = mapper.readValue(new File(path), Configuration.class);
            this.setFeeds(loadedFile.getFeeds());
            this.setItemLists(loadedFile.getItemLists());
        }
        catch(IOException err)
        {
            throw new RuntimeException();
        }
    }

    /*
    ------------------------------- ACCESSORS AND MUTATORS -----------------------------------------
    */

    /**
     * Returns the feeds array.
     * @return An array of Feed objects containing all Feeds of this Configuration
     */
    public Feed[] getFeeds()
    {
        return feeds;
    }

    /**
     * Returns the array of ItemList objects in this object.
     * @return An array of ItemList objects contained in this Configuration
     */
    public ItemList[] getItemLists()
    {
        return itemLists;
    }

    /**
     * Sets the Feed array of this Configuration
     * @param feeds An array of Feed objects we want to be used in this Configuration
     */
    public void setFeeds(Feed[] feeds)
    {
        this.feeds = feeds;
    }

    /**
     * Sets the ItemList array of this Configuration.
     * @param itemList An array of ItemList objects to be used in this Configuration
     */
    public void setItemLists(ItemList[] itemList)
    {
        this.itemLists = itemList;
    }
}

// Created: 2016-04-19
