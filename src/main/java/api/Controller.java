package api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Class Controller
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
public class Controller
{
    private Configuration configuration;

    public Controller()
    {
        configuration = new Configuration();
    }

    public void addFeed(String url)
    {
        Feed newFeed = new Feed(url);
        Feed[] feeds = configuration.getFeeds();
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
                newFeedList[i] = feeds[i];

            newFeedList[i] = newFeed;
        }

        configuration.setFeeds(newFeedList);
    }

    public void removeFeed(String url)
    {
        Feed[] feeds = configuration.getFeeds();
        Feed[] newFeedList = new Feed[feeds.length - 1];
        boolean exists = false;

        if(feeds == null)
            return;

        for(int i = 0; i < feeds.length; i++)
        {
            if(!feeds[i].getUrlToXML().equals(url) && !exists)
                newFeedList[i] = feeds[i];
            else if(!feeds[i].getUrlToXML().equals(url) && exists)
                newFeedList[i - 1] = feeds[i];
            else
                exists = true;
        }

        if(exists)
            configuration.setFeeds(newFeedList);
    }

    public void update()
    {
        Feed[] feeds = configuration.getFeeds();
        ItemList[] itemLists = configuration.getItemList();

        if(feeds == null)
            return;

        for(Feed feed : feeds)
            feed.update();
        for(ItemList itemList : itemLists)
            itemList.update(feeds);
    }

    public void saveConfig(String path)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            mapper.writeValue(new File(path), configuration);
        }
        catch(IOException err)
        {
            err.printStackTrace();
        }
    }

    public void loadConfig(String path)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            Configuration configuration = mapper.readValue(new File(path), Configuration.class);
        }
        catch(IOException err)
        {
            err.printStackTrace();
        }
    }

    public Feed[] getFeeds()
    {
        return configuration.getFeeds();
    }

    public ItemList[] getItemList()
    {
        return configuration.getItemList();
    }
}

// Created: 2016-04-19