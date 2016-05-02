package api;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

/**
 * Test Class ControllerTest
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
public class ControllerTest
{
    private Controller controller;

    @Before
    public void createObject()
    {
        controller = new Controller();
    }

    @Test
    public void accessorsAndMutators()
    {
        Configuration configuration = createMockConfiguration();

        controller.setConfiguration(configuration);

        assertEquals("Example Feed 1", controller.getFeeds()[0].getTitle());
        assertEquals("http://examplefeed1.com/", controller.getFeeds()[0].getLink());
        assertEquals("This is a description for example feed 1", controller.getFeeds()[0]
                .getDescription());
        assertEquals("http://examplefeed1.com/feed.xml", controller.getFeeds()[0].getUrlToXML());
        assertEquals("Example Item 1", controller.getFeeds()[0].getItems()[0].getTitle());
        assertEquals("http://www.google.com", controller.getFeeds()[0].getItems()[0].getLink());
        assertEquals("This is an item description", controller.getFeeds()[0].getItems()[0]
                .getDescription());
        assertEquals("example-id-1", controller.getFeeds()[0].getItems()[0].getId());

        assertEquals("ItemList 1", controller.getItemLists()[0].getName());
        assertEquals("DATE_ASC", controller.getItemLists()[0].getSorting());
        assertEquals("URL11", controller.getItemLists()[0].getFeedUrls()[0]);

        assertTrue(configuration.equals(controller.getConfiguration()));
    }

    @Test
    public void addFeedToEmptyConfig()
    {
        String url = ControllerTest.class.getResource("../../../resources/test/xml/exampleFeed1.xml")
                .getPath();
        controller.addFeed(url);
        Configuration config = controller.getConfiguration();

        assertEquals(1, config.getFeeds().length);
        assertEquals(url, config.getFeeds()[0].getUrlToXML());
    }

    @Test
    public void addFeedToNonEmptyConfig()
    {
        String url = ControllerTest.class.getResource("../../../resources/test/xml/exampleFeed1.xml")
                .getPath();
        controller.setConfiguration(createMockConfiguration());
        controller.addFeed(url);
        Configuration config = controller.getConfiguration();

        assertEquals(3, config.getFeeds().length);
        assertEquals(url, config.getFeeds()[2].getUrlToXML());
    }

    @Test(expected = RuntimeException.class)
    public void addExistingFeedToConfig()
    {
        String url = ControllerTest.class.getResource("../../../resources/test/xml/exampleFeed2.xml")
                .getPath();
        controller.setConfiguration(createMockConfiguration());
        controller.addFeed(url);
    }

    @Test
    public void removeExistingFeed()
    {
        controller.setConfiguration(createMockConfiguration());
        controller.removeFeed("http://examplefeed1.com/feed.xml");
        Configuration config = controller.getConfiguration();

        assertEquals(1, config.getFeeds().length);
        assertNotEquals("http://examplefeed1.com/feed.xml", config.getFeeds()[0].getUrlToXML());
    }

    @Test
    public void removeNonExistingFeed()
    {
        controller.setConfiguration(createMockConfiguration());
        controller.removeFeed("http://examplefeed400.com/feed.xml");
        Configuration config = controller.getConfiguration();

        assertEquals(2, config.getFeeds().length);
        assertNotEquals("http://examplefeed400.com/feed.xml", config.getFeeds()[0].getUrlToXML());
        assertNotEquals("http://examplefeed400.com/feed.xml", config.getFeeds()[1].getUrlToXML());
    }

    @Test(expected = RuntimeException.class)
    public void removeOnEmptyFeedArray()
    {
        controller.removeFeed("http://examplefeed1.com/feed.xml");
    }

    @Test
    public void removeLastFeed()
    {
        controller.setConfiguration(createMockConfiguration());
        controller.removeFeed("http://examplefeed1.com/feed.xml");
        controller.removeFeed(ControllerTest.class
                .getResource("../../../resources/test/xml/exampleFeed2.xml").getPath());

        assertNull(controller.getFeeds());
    }

    @Test
    public void createItemListInEmptyArray()
    {
        controller.createItemList("TestItemList");
        Configuration config = controller.getConfiguration();

        assertEquals(1, config.getItemLists().length);
        assertEquals("TestItemList", config.getItemLists()[0].getName());
    }

    @Test
    public void createItemListInNonEmptyArray()
    {
        controller.setConfiguration(createMockConfiguration());
        controller.createItemList("TestItemList");
        Configuration config = controller.getConfiguration();

        assertEquals(3, config.getItemLists().length);
        assertEquals("TestItemList", config.getItemLists()[2].getName());
    }

    @Test(expected = RuntimeException.class)
    public void createItemListWithExistingName()
    {
        controller.setConfiguration(createMockConfiguration());
        controller.createItemList("ItemList 1");
    }

    @Test(expected = RuntimeException.class)
    public void removeItemList()
    {
        controller.removeItemList("");
    }

    @Test
    public void updateTest()
    {
        Configuration config = createMockConfiguration();

        controller.setConfiguration(config);
        controller.update();

        verify(config.getFeeds()[0]).update();
        verify(config.getFeeds()[1]).update();
        verify(config.getItemLists()[0]).update(any());
        verify(config.getItemLists()[1]).update(any());
    }

    @Test(expected = RuntimeException.class)
    public void updateOnEmpty()
    {
        controller.update();
    }

    @Test
    public void loadExistingConfiguration_Feeds()
    {
        controller.loadConfig(ControllerTest.class
                .getResource("../../../resources/test/configuration/exampleConfig1.json")
                .getPath());

        Configuration config = controller.getConfiguration();
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

    @Test
    public void loadExistingConfiguration_ItemLists()
    {
        controller.loadConfig(ControllerTest.class
                .getResource("../../../resources/test/configuration/exampleConfig1.json")
                .getPath());

        Configuration config = controller.getConfiguration();
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

    @Test(expected = RuntimeException.class)
    public void loadNonexistentConfiguration()
    {
        controller.loadConfig("ThisFileShouldNotExistAndIfItDoesYouAreAnIdiot.json");
    }

    @Test
    public void saveConfiguration()
    {
        controller.setConfiguration(createConfiguration());
        controller.saveConfig(ControllerTest.class
                .getResource("../../../resources/test/configuration/savedConfig.json").getPath());
        String str = null;
        String comp = null;

        try
        {
            File file = new File(ControllerTest.class
                    .getResource("../../../resources/test/configuration/savedConfig.json")
                    .getPath());
            Scanner scan = new Scanner(file);

            while(scan.hasNext())
                str += scan.next();

            file = new File(ControllerTest.class
                    .getResource("../../../resources/test/configuration/savedConfigCompare.json")
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

    private Configuration createConfiguration()
    {
        Configuration config = new Configuration();

        Feed[] feeds = new Feed[1];
        ItemList[] itemLists = new ItemList[1];
        Item[] items = new Item[1];
        String[] urls = new String[1];

        items[0] = new Item();
        items[0].setTitle("Example Item");
        items[0].setLink("http://examplefeed.net/exampleItem");
        items[0].setDescription("This is an item description");
        items[0].setId("exItem");
        items[0].setVisited(false);
        items[0].setStarred(true);

        feeds[0] = new Feed();
        feeds[0].setTitle("Example Feed");
        feeds[0].setLink("http://examplefeed.net");
        feeds[0].setDescription("This is a description for example feed");
        feeds[0].setUrlToXML("http://examplefeed.net/feed.xml");
        feeds[0].setItems(items);

        urls[0] = "http://examplefeed.net/feed.xml";

        itemLists[0] = new ItemList();
        itemLists[0].setName("Example ItemList");
        itemLists[0].setSorting("DATE_DEC");
        itemLists[0].setFeedUrls(urls);

        config.setFeeds(feeds);
        config.setItemLists(itemLists);

        return config;
    }

    private Configuration createMockConfiguration()
    {
        Configuration config = mock(Configuration.class);
        Feed[] feeds = createMockFeeds();
        ItemList[] itemList = createMockItemLists();

        when(config.getFeeds()).thenReturn(feeds);
        when(config.getItemLists()).thenReturn(itemList);

        return config;
    }

    private Feed[] createMockFeeds()
    {
        Item[] items1 = new Item[2];
        Item[] items2 = new Item[2];
        items1[0] = mock(Item.class);
        items1[1] = mock(Item.class);
        items2[0] = mock(Item.class);
        items2[1] = mock(Item.class);

        when(items1[0].getTitle()).thenReturn("Example Item 1");
        when(items1[0].getLink()).thenReturn("http://www.google.com");
        when(items1[0].getDescription()).thenReturn("This is an item description");
        when(items1[0].getId()).thenReturn("example-id-1");
        when(items1[0].isVisited()).thenReturn(false);
        when(items1[0].isStarred()).thenReturn(false);

        when(items1[1].getTitle()).thenReturn("Example Item 2");
        when(items1[1].getLink()).thenReturn("http://www.google.com");
        when(items1[1].getDescription()).thenReturn("This is an item description");
        when(items1[1].getId()).thenReturn("example-id-2");
        when(items1[1].isVisited()).thenReturn(true);
        when(items1[1].isStarred()).thenReturn(true);

        when(items2[0].getTitle()).thenReturn("Example Item 21");
        when(items2[0].getLink()).thenReturn("http://www.google.com");
        when(items2[0].getDescription()).thenReturn("This is an item description");
        when(items2[0].getId()).thenReturn("example-id-21");
        when(items2[0].isVisited()).thenReturn(true);
        when(items2[0].isStarred()).thenReturn(false);

        when(items2[1].getTitle()).thenReturn("Example Item 22");
        when(items2[1].getLink()).thenReturn("http://www.google.com");
        when(items2[1].getDescription()).thenReturn("This is an item description");
        when(items2[1].getId()).thenReturn("example-id-22");
        when(items2[1].isVisited()).thenReturn(false);
        when(items2[1].isStarred()).thenReturn(true);

        Feed[] feeds = new Feed[2];
        feeds[0] = mock(Feed.class);
        feeds[1] = mock(Feed.class);

        when(feeds[0].getTitle()).thenReturn("Example Feed 1");
        when(feeds[0].getLink()).thenReturn("http://examplefeed1.com/");
        when(feeds[0].getDescription()).thenReturn("This is a description for example feed 1");
        when(feeds[0].getUrlToXML()).thenReturn("http://examplefeed1.com/feed.xml");
        when(feeds[0].getItems()).thenReturn(items1);

        when(feeds[1].getTitle()).thenReturn("Example Feed 2");
        when(feeds[1].getLink()).thenReturn("http://examplefeed2.com/");
        when(feeds[1].getDescription()).thenReturn("This is a description for example feed 2");
        when(feeds[1].getUrlToXML()).thenReturn(ControllerTest.class
                .getResource("../../../resources/test/xml/exampleFeed2.xml").getPath());
        when(feeds[1].getItems()).thenReturn(items2);

        return feeds;
    }

    private ItemList[] createMockItemLists()
    {
        String[] urls1 = new String[3];
        String[] urls2 = new String[3];
        urls1[0] = "URL11";
        urls1[1] = "URL12";
        urls1[2] = "URL13";
        urls2[0] = "URL21";
        urls2[1] = "URL22";
        urls2[2] = "URL23";

        ItemList[] itemLists = new ItemList[2];
        itemLists[0] = mock(ItemList.class);
        itemLists[1] = mock(ItemList.class);

        when(itemLists[0].getName()).thenReturn("ItemList 1");
        when(itemLists[0].getSorting()).thenReturn("DATE_ASC");
        when(itemLists[0].getFeedUrls()).thenReturn(urls1);

        when(itemLists[1].getName()).thenReturn("ItemList 2");
        when(itemLists[1].getSorting()).thenReturn("NAME_DEC");
        when(itemLists[1].getFeedUrls()).thenReturn(urls2);

        return itemLists;
    }
}

// Created: 2016-05-01