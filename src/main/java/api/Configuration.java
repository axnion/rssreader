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
 * @version 0.2
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
    public void addFeedToConfiguration(String url)
    {
        Feed newFeed = new Feed(url);
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
    public void removeFeedFromConfiguration(String url)
    {
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
                if(feeds[i].getUrlToXML().equals(url) && !exists)
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
    public void addItemListToConfiguration(String name)
    {
        ItemList newItemList = new ItemList(name);
        ItemList[] newList;

        if(itemLists == null)
        {
            newList = new ItemList[1];
            newList[0] = newItemList;
        }
        else
        {
            newList = new ItemList[itemLists.length + 1];
            int i;

            for(i = 0; i < itemLists.length; i++)
            {
                if(itemLists[i].getName().equals(name))
                    throw new RuntimeException("A list with that name already exists");
                else
                    newList[i] = itemLists[i];
            }

            newList[i] = newItemList;
        }

        setItemLists(newList);
    }

    /**
     * Removes one ItemList whose name is the same String as the argument name. If there are no
     * ItemLists or the method can't find a match, then a RuntimeException is thrown. The method
     * goes though the array of ItemLists and copies all elements exept the first one that matches
     * the argument name.
     * @param name
     */
    public void removeItemListFromConfiguration(String name)
    {
        ItemList[] newItemLists = null;
        boolean exists = true;

        if(itemLists == null)
            throw new RuntimeException("There are no ItemLists to remove in this Configuration");
        else if(itemLists.length != 1)
        {
            newItemLists = new ItemList[itemLists.length - 1];
            exists = false;

            for(int i = 0; i < itemLists.length; i++)
            {
                if(itemLists[i].getName().equals(name))
                    exists = true;
                else if(i == newItemLists.length && !exists)
                    break;
                else if(!exists)
                    newItemLists[i] = itemLists[i];
                else
                    newItemLists[i - 1] = itemLists[i];
            }
        }

        if(exists)
            setItemLists(newItemLists);
        else
            throw new RuntimeException("There is no ItemList with name \"" + name + "\" to remove " +
                    "in this Configuration");
    }

    /**
     * Adds a Feed to an ItemList. The correct ItemList is identified by it's name. When the correct
     * itemList is found we call on the addFeed method on the itemList object which adds the url to
     * the itemLists url list.
     * @param name A String containing the name of the ItemList we want the url to be added to.
     * @param url   A String containing the URL to the XML file for the feed we want to add to the
     *              itemList
     * @throws RuntimeException If there is no ItemList with the correct name
     */
    public void addFeedToItemList(String name, String url)
    {
        for(ItemList itemList : itemLists)
        {
            if(itemList.getName().equals(name))
            {
                itemList.addFeed(url);
                return;
            }
        }

        throw new RuntimeException("There where no itemLists with that name");
    }

    /**
     * Removed the Feed with the argument URL from the ItemList with the argument name. When the
     * correct itemList is found it call the removeFeed method on that ItemList with the url as an
     * argument.
     * @param name  A String containing the name of the ItemList we want the Feed to be removed from
     * @param url   A String containing the URL to the XML file of the Feed we want removed from the
     *              ItemList
     * @throws RuntimeException If there is no ItemList with the correct name
     */
    public void removeFeedFromItemList(String name, String url)
    {
        for(ItemList itemList : itemLists)
        {
            if(itemList.getName().equals(name))
            {
                itemList.removeFeed(url);
                return;
            }
        }

        throw new RuntimeException("There where no itemLists with that name");
    }

    /**
     * This method returns an ItemList with a specific name. If it does exist it will be returned,
     * but if there is no ItemList that has that name a RuntimeExcpetion is thrown, and the same
     * will happen if there are no ItemLists at all.
     * @param name  The name of the itemList we are looking for
     * @return      An ItemList with an identical name as the name parameter.
     */
    public ItemList getItemList(String name)
    {
        if(itemLists == null)
            throw new RuntimeException("There are no ItemLists");

        for(ItemList itemList : itemLists)
        {
            if(itemList.getName().equals(name))
                return itemList;
        }

        throw new RuntimeException("There is no ItemList with that name");
    }

    /**
     * This is the main update method. It calls on the other update methods in both the Feed objects
     * and the itemList objects. So it will go though every Feed object and check for updates and
     * update them, and the go though every itemList and update them.
     */
    public void update()
    {
        if(feeds != null)
        {
            for(Feed feed : feeds)
                feed.update();
        }

        if(itemLists != null)
        {
            for(ItemList itemList : itemLists)
                itemList.update(feeds);
        }
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
            throw new RuntimeException("Could not save that file to " + path);
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
            throw new RuntimeException("Could not load that file from" + path);
        }
    }

    public void setSorting(String sorting, String itemListName)
    {
        if(!sorting.equals("TITLE_ASC") && !sorting.equals("TITLE_DEC") && !sorting.equals("DATE_ASC") && !sorting.equals("DATE_DEC"))
        {
            throw new RuntimeException( "\"" + sorting + "\"" + " is not a valid sorting method");
        }

        if(getItemList(itemListName).getSorting().equals(sorting))
            return;

        getItemList(itemListName).setSorting(sorting);
        getItemList(itemListName).sort();

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
