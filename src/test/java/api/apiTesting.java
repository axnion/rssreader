package api;

import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Class apiTesting
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
public class apiTesting
{
    private Configuration config;

    @Before
    public void createObject()
    {
        config = new Configuration();
    }

    /**
     * OBS! SHOULD BE MOVED TO INTEGRATION TESTING
     *
     * Test Case: 37
     */
    //@Test
    public void loadExistingConfiguration_Feeds()
    {
        config.loadConfig(apiTesting.class
                .getResource("../../resources/test/configuration/exampleConfig1.json")
                .getPath());

        Feed[] feeds = config.getFeeds();

        assertEquals(2, feeds.length);
        assertEquals(2, feeds[0].getItems().length);
        assertEquals(2, feeds[1].getItems().length);

        assertEquals("Example Feed 1", feeds[0].getTitle());
        assertEquals("http://examplefeed1.com/", feeds[0].getLink());
        assertEquals("This is a description for example feed 1", feeds[0].getDescription());
        assertEquals("http://examplefeed1.com/feed.xml", feeds[0].getUrlToXML());

        assertEquals("Example Feed 2", feeds[1].getTitle());
        assertEquals("http://examplefeed2.com/", feeds[1].getLink());
        assertEquals("This is a description for example feed 2", feeds[1].getDescription());
        assertEquals("http://examplefeed2.com/feed.xml", feeds[1].getUrlToXML());

        assertEquals("Example Item 1", feeds[0].getItems()[0].getTitle());
        assertEquals("http://www.google.com", feeds[0].getItems()[0].getLink());
        assertEquals("This is an item description", feeds[0].getItems()[0].getDescription());
        assertEquals("example-id-1", feeds[0].getItems()[0].getId());
        assertFalse(feeds[0].getItems()[0].isVisited());
        assertFalse(feeds[0].getItems()[0].isStarred());

        assertEquals("Example Item 22", feeds[1].getItems()[1].getTitle());
        assertEquals("http://www.google.com", feeds[1].getItems()[1].getLink());
        assertEquals("This is an item description", feeds[1].getItems()[1].getDescription());
        assertEquals("example-id-22", feeds[1].getItems()[1].getId());
        assertFalse(feeds[1].getItems()[1].isVisited());
        assertTrue(feeds[1].getItems()[1].isStarred());
    }

    /**
     * OBS! SHOULD BE MOVED TO INTEGRATION TESTING
     *
     * Test Case: 38
     */
    //@Test
    public void loadExistingConfiguration_ItemLists()
    {
        config.loadConfig(apiTesting.class
                .getResource("../../resources/test/configuration/exampleConfig1.json")
                .getPath());

        ItemList[] itemLists = config.getItemLists();

        assertEquals(2, itemLists.length);
        assertEquals(3, itemLists[0].getFeedUrls().length);
        assertEquals(3, itemLists[1].getFeedUrls().length);

        assertEquals("ItemList 1", itemLists[0].getName());
        assertEquals("DATE_ASC", itemLists[0].getSorting());
        assertEquals("URL11", itemLists[0].getFeedUrls()[0]);
        assertEquals("URL12", itemLists[0].getFeedUrls()[1]);
        assertEquals("URL13", itemLists[0].getFeedUrls()[2]);

        assertEquals("ItemList 2", itemLists[1].getName());
        assertEquals("NAME_DEC", itemLists[1].getSorting());
        assertEquals("URL21", itemLists[1].getFeedUrls()[0]);
        assertEquals("URL22", itemLists[1].getFeedUrls()[1]);
        assertEquals("URL23", itemLists[1].getFeedUrls()[2]);
    }

    /**
     * OBS! SHOULD BE MOVED TO INTEGRATION TESTING
     *
     * Test Case: 39
     */
    //@Test(expected = RuntimeException.class)
    public void loadNonexistentConfiguration()
    {
        config.loadConfig("ThisFileShouldNotExistAndIfItDoesYouAreAnIdiot.json");
    }

    /**
     * OBS! SHOULD BE MOVED TO INTEGRATION TESTING
     *
     * Test Case: 40
     */
    //@Test
    public void saveConfiguration()
    {
        // Creates Item mocks
        Item[] items = new Item[1];
        items[0] = Mocks.createMockItem("Example Item", "http://examplefeed.net/exampleItem",
                "This is an item description", "exItem", false, true);

        // Creates Feed mocks
        Feed[] feeds = new Feed[1];
        feeds[0] = Mocks.createMockFeed("Example Feed", "http://examplefeed.net",
                "This is a description for example feed", "http://examplefeed.net/feed.xml");
        when(feeds[0].getItems()).thenReturn(items);
        config.setFeeds(feeds);

        String[] urls = new String[1];
        urls[0] = "http://examplefeed.net/feed.xml";

        // Creates ItemList mocks
        ItemList[] itemList = new ItemList[1];
        itemList[0] = Mocks.createMockItemList("Example ItemList", "DATE_DEC");
        when(itemList[0].getFeedUrls()).thenReturn(urls);
        config.setItemLists(itemList);

        config.saveConfig(apiTesting.class
                .getResource("../../resources/test/configuration/savedConfig.json").getPath());
        String str = null;
        String comp = null;

        try
        {
            File file = new File(apiTesting.class
                    .getResource("../../resources/test/configuration/savedConfig.json")
                    .getPath());
            Scanner scan = new Scanner(file);

            while(scan.hasNext())
                str += scan.next();

            file = new File(apiTesting.class
                    .getResource("../../resources/test/configuration/savedConfigCompare.json")
                    .getPath());
            scan = new Scanner(file);

            while(scan.hasNext())
                comp += scan.next();
        }
        catch(IOException err)
        {
            err.printStackTrace();
        }

        assertNotNull(str);
        assertNotNull(comp);
        assertEquals(0, str.compareTo(comp));
    }
}

// Created: 2016-05-05
