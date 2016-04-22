package api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class ItemList
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
@JsonIgnoreProperties({"items"})
public class ItemList
{
    private String name;
    private String sorting;
    private String[] feedUrls;

    private Item[] items;

    public ItemList()
    {
        name = "";
        sorting = "";
        feedUrls = null;
        items = null;
    }

    public ItemList(String name)
    {
        this.name = name;
        sorting = "";
        feedUrls = null;
        items = null;
    }

    public void addFeed(String feedUrl)
    {
        String[] newFeedUrlList = new String[feedUrls.length + 1];
        int i;

        for(i = 0; i < items.length; i++)
        {
            if(feedUrls[i].equals(feedUrl))
                return;

            newFeedUrlList[i] = feedUrls[i];
        }


        newFeedUrlList[i] = feedUrl;
        setFeedUrls(newFeedUrlList);
    }

    public void removeFeed(String feedUrl)
    {
        String[] newFeedUrlList = new String[feedUrls.length - 1];
        boolean exists = false;

        for(int i = 0; i < feedUrls.length; i++)
        {
            if(!feedUrls[i].equals(feedUrl) && !exists)
                newFeedUrlList[i] = feedUrls[i];
            else if(!feedUrls[i].equals(feedUrl) && exists)
                newFeedUrlList[i - 1] = feedUrls[i];
            else
                exists = true;
        }

        if(exists)
            setFeedUrls(newFeedUrlList);
    }

    public void update(Feed[] feeds)
    {
        Item[] newList = null;

        for(int i = 0; i < feeds.length; i++)
        {
            for(int j = 0; j < feedUrls.length; i++)
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
        items = newList;
    }

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

    public String getName()
    {
        return name;
    }

    public String getSorting()
    {
        return sorting;
    }

    public String[] getFeedUrls()
    {
        return feedUrls;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSorting(String sorting)
    {
        this.sorting = sorting;
    }

    public void setFeedUrls(String[] feedUrls)
    {
        this.feedUrls = feedUrls;
    }
}

// Created: 2016-04-20
