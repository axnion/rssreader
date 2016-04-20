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

    public void add(String feedTitle)
    {

    }

    public void remove(String feedTitle)
    {

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
