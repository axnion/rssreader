import api.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Class apiTesting
 *
 * This is the test suite with integration tests for the api package.
 *
 * @author Axel Nilsson (axnion)
 * @version 1.0
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
     * Test Case: 37
     * Loads an existing file and checks all the values in the Feeds so they are what they should
     * be.
     */
    @Test
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
     * Test Case: 38
     * Loads an existing file and checks all the values in the ItemLists so they are what they
     * should be.
     */
    @Test
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
     * Test Case: 39
     * Tries to load a file that does not exist, this should produce a RuntimeExcpetion.
     */
    @Test(expected = RuntimeException.class)
    public void loadNonexistentConfiguration()
    {
        config.loadConfig("ThisFileShouldNotExistAndIfItDoesYouAreAnIdiot.json");
    }

    /**
     * Test Case: 40
     * Adds Feeds and creates ItemLists to create a configuration. Then it saves the Configuration
     * to a file. Then the test read the file and compares the content to what it should be that's
     * saved in a String variable.
     */
    @Test
    public void saveConfiguration()
    {
        // Adding Feeds to the Configuration
        config.addFeedToConfiguration(apiTesting.class
                .getResource("../../resources/test/xml/exampleFeed1.xml").getPath());
        config.addFeedToConfiguration(apiTesting.class
                .getResource("../../resources/test/xml/exampleFeed2.xml").getPath());

        // Adding ItemLists to the Configuration
        config.addItemListToConfiguration("Test1");
        config.addItemListToConfiguration("Test2");

        // Adding Feed to an ItemList
        config.addFeedToItemList("Test2", apiTesting.class
                .getResource("../../resources/test/xml/exampleFeed2.xml").getPath());

        // Saves configuration
        config.saveConfig(apiTesting.class
                .getResource("../../resources/test/configuration/savedConfig.json").getPath());

        String str = "";
        String comp = "{\"feeds\":[{\"title\":\"Example Feed 1\",\"link\":\"" +
                "http://examplefeed1.com/\",\"description\":\"This is a description for example" +
                " feed 1\",\"urlToXML\":\""+ apiTesting.class
                .getResource("../../resources/test/xml/exampleFeed1.xml").getPath() +"\",\"" +
                "items\":[{\"title\":\"Example Item 1\",\"link\":\"http://www.google.com\",\"" +
                "description\":\"This is an item description\",\"id\":\"example-id-1\",\"date\":\"Mon, 01 Jan 2016 12:00:00 +0000\"," +
                "\"visited\":true,\"starred\":false},{\"title\":\"Example Item 2\",\"link\":\"" +
                "http://www.google.com\",\"description\":\"This is an item description\",\"id\"" +
                ":\"example-id-2\",\"date\":\"Tue, 02 Jan 2016 20:30:15 +0000\",\"visited\":true,\"starred\":false}]},{\"title\":\"" +
                "Example Feed 2\",\"link\":\"http://examplefeed2.com/\",\"description\":\"" +
                "This is a description for example feed 2\",\"urlToXML\":\"" + apiTesting.class
                .getResource("../../resources/test/xml/exampleFeed2.xml").getPath() + "\",\"" +
                "items\":[{\"title\":\"Example Item 21\",\"link\":\"http://www.google.com\",\"" +
                "description\":\"This is an item description\",\"id\":\"example-id-21\",\"date\":\"Wen, 11 Feb 2016 21:15:15 +0000\",\"" +
                "visited\":true,\"starred\":false},{\"title\":\"Example Item 22\",\"link\":\"" +
                "http://www.google.com\",\"description\":\"This is an item description\",\"id\"" +
                ":\"example-id-22\",\"date\":\"Thu, 12 Feb 2016 23:45:20 +0000\",\"visited\":true,\"starred\":false}]}],\"itemLists\":[{\"" +
                "name\":\"Test1\",\"sorting\":\"DATE_DEC\",\"feedUrls\":null},{\"name\":\"Test2\",\"" +
                "sorting\":\"DATE_DEC\",\"feedUrls\":[\"" + apiTesting.class
                .getResource("../../resources/test/xml/exampleFeed2.xml").getPath() + "\"]}]}";

        try
        {
            File file = new File(apiTesting.class
                    .getResource("../../resources/test/configuration/savedConfig.json").getPath());
            Scanner scan = new Scanner(file);

            while(scan.hasNextLine())
                str += scan.nextLine();
        }
        catch(IOException err)
        {
            err.printStackTrace();
        }

        assertNotNull(str);
        assertEquals(str, comp);
    }

    /**
     * Test Case: 52
     * Tries to save the configuration to a directory. Should produce a RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void savingToADirectory()
    {
        config.saveConfig(apiTesting.class
                .getResource("../../resources/test/configuration/").getPath());
    }
}

// Created: 2016-05-05
