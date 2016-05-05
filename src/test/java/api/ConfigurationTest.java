package api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

/**
 * Test Class ConfigurationTest
 *
 * @author Axel Nilsson (axnion)
 * @version 0.2
 */
public class ConfigurationTest
{
    private Configuration config;

    @Before
    public void createObject()
    {
        config = new Configuration();
    }

    /**
     * Test Case: 22
     * Testing the accessor and mutator methods in the Configuration class. Use the setters to set
     * the fields to mocked objects and then use the getters and check the validity of the returned
     * objects
     */
    @Test
    public void accessorsAndMutators()
    {
        // Check the values assigned by the constuctor
        assertNull(config.getFeeds());
        assertNull(config.getItemLists());

        // Creates Feed mocks and assign them using setter
        Feed[] feeds = new Feed[2];
        feeds[0] = Mocks.createMockFeed("Feed 1", "", "", "");
        feeds[1] = Mocks.createMockFeed("Feed 2", "", "", "");
        config.setFeeds(feeds);

        // Creates ItemList mocks and assign them using setter
        ItemList[] itemList = new ItemList[2];
        itemList[0] = Mocks.createMockItemList("ItemList 1", "");
        itemList[1] = Mocks.createMockItemList("ItemList 2", "");
        config.setItemLists(itemList);

        // Checks the values of the fields using getters
        assertEquals("Feed 1", config.getFeeds()[0].getTitle());
        assertEquals("Feed 2", config.getFeeds()[1].getTitle());
        assertEquals("ItemList 1", config.getItemLists()[0].getName());
        assertEquals("ItemList 2", config.getItemLists()[1].getName());
    }

    /**
     * Test Case: 24
     * Adds one Feed to the Configuration when it contains no Feeds. Then checks so there is only
     * that one Feed in the Configuration.
     */
    @Test
    public void addFeedToEmptyConfig()
    {
        // Adding Feed to Configuration
        String url = ConfigurationTest.class
                .getResource("../../../resources/test/xml/exampleFeed1.xml").getPath();
        config.addFeedToConfiguration(url);

        // Checks the results
        assertEquals(1, config.getFeeds().length);
        assertEquals(url, config.getFeeds()[0].getUrlToXML());
    }

    /**
     * Test Case: 25
     * Adds a Feed to a Configuration already containing Feeds. Then checks so there is now three
     * Feeds instead of the previous two and also checks the order and the validity.
     */
    @Test
    public void addFeedToNonEmptyConfig()
    {
        String url = ConfigurationTest.class
                .getResource("../../../resources/test/xml/exampleFeed1.xml").getPath();

        // Creates Feed mocks
        Feed[] feeds = new Feed[2];
        feeds[0] = Mocks.createMockFeed("Example Feed 1", "http://examplefeed1.com/",
                "This is a description for example feed 1", "http://examplefeed1.com/feed.xml");
        feeds[1] = Mocks.createMockFeed("Example Feed 2", "http://examplefeed2.com/",
                "This is a description for example feed 2", "http://examplefeed2.com/feed.xml");
        config.setFeeds(feeds);

        // Adds a new Feed to Configuration
        config.addFeedToConfiguration(url);

        // Checks if the Feed array has the correct values.
        assertEquals(3, config.getFeeds().length);
        assertEquals("http://examplefeed1.com/feed.xml", config.getFeeds()[0].getUrlToXML());
        assertEquals("http://examplefeed2.com/feed.xml", config.getFeeds()[1].getUrlToXML());
        assertEquals(url, config.getFeeds()[2].getUrlToXML());
    }

    /**
     * Test Case: 26
     * Tries to add a Feed with an identical urlToXML. Since that url is the identifier of Feed a
     * RuntimeException is thrown because we can't have duplicates.
     */
    @Test(expected = RuntimeException.class)
    public void addExistingFeedToConfig()
    {
        String url = ConfigurationTest.class
                .getResource("../../../resources/test/xml/exampleFeed2.xml").getPath();

        // Creates Feed mocks
        Feed[] feeds = new Feed[2];
        feeds[0] = Mocks.createMockFeed("Example Feed 1", "http://examplefeed1.com/",
                "This is a description for example feed 1", "http://examplefeed1.com/feed.xml");
        feeds[1] = Mocks.createMockFeed("Example Feed 2", "http://examplefeed2.com/",
                "This is a description for example feed 2", url);
        config.setFeeds(feeds);

        // Adds the identical Feed to the Configuration
        config.addFeedToConfiguration(url);
    }

    /**
     * Test Case: 27
     * Removes a Feed that exists in the Configuration. This Feed should then not be present in the
     * Feed array afterwards.
     */
    @Test
    public void removeExistingFeed()
    {
        // Creates Feed mocks
        Feed[] feeds = new Feed[2];
        feeds[0] = Mocks.createMockFeed("Example Feed 1", "http://examplefeed1.com/",
                "This is a description for example feed 1", "http://examplefeed1.com/feed.xml");
        feeds[1] = Mocks.createMockFeed("Example Feed 2", "http://examplefeed2.com/",
                "This is a description for example feed 2", "http://examplefeed2.com/feed.xml");
        config.setFeeds(feeds);

        // Removes an existing Feed
        config.removeFeedFromConfiguration("http://examplefeed1.com/feed.xml");

        // Check the remaining feed so the correct one is removed
        assertEquals(1, config.getFeeds().length);
        assertEquals("http://examplefeed2.com/feed.xml", config.getFeeds()[0].getUrlToXML());
    }

    /**
     * Test Case: 28
     * Tries to remove a Feed from the Configuration that does not exist. This should produce
     * RuntimeException. The tests also checks so the Feed array was untouched.
     */
    @Test(expected = RuntimeException.class)
    public void removeNonExistingFeed()
    {
        // Creates Feed mocks
        Feed[] feeds = new Feed[2];
        feeds[0] = Mocks.createMockFeed("Example Feed 1", "http://examplefeed1.com/",
                "This is a description for example feed 1", "http://examplefeed1.com/feed.xml");
        feeds[1] = Mocks.createMockFeed("Example Feed 2", "http://examplefeed2.com/",
                "This is a description for example feed 2", "http://examplefeed2.com/feed.xml");
        config.setFeeds(feeds);

        // Removes an nonexistent Feed
        config.removeFeedFromConfiguration("http://examplefeed404.com/feed.xml");

        // Checks the Feed array
        assertEquals(2, config.getFeeds().length);
        assertNotEquals("http://examplefeed1.com/feed.xml", config.getFeeds()[0].getUrlToXML());
        assertNotEquals("http://examplefeed2.com/feed.xml", config.getFeeds()[1].getUrlToXML());
    }

    /**
     * Test Case: 29
     * Tries to remove a Feed from an empty Feed array. This should produce a RuntimeException. The
     * Feed array should also be pointing to null.
     */
    @Test(expected = RuntimeException.class)
    public void removeOnEmptyFeedArray()
    {
        config.removeFeedFromConfiguration("http://examplefeed1.com/feed.xml");
        assertNull(config.getFeeds());
    }

    /**
     * Test Case: 30
     * Removes the last remaining Feed in the Feed array. The Feed array should be pointing at null
     * afterwards.
     */
    @Test
    public void removeLastFeed()
    {
        // Creates Feed mocks
        Feed[] feeds = new Feed[1];
        feeds[0] = Mocks.createMockFeed("Example Feed 1", "http://examplefeed1.com/",
                "This is a description for example feed 1", "http://examplefeed1.com/feed.xml");
        config.setFeeds(feeds);

        // Removes the last Feed
        config.removeFeedFromConfiguration("http://examplefeed1.com/feed.xml");

        // Checks the array, it should be null.
        assertNull(config.getFeeds());
    }

    /**
     * Test Case: 43
     * Tries to remove a Feed that exist in several elements in the array. It should only remove the
     * first one and the rest of the array should be intact.
     */
    @Test
    public void removingFeedWhenMoreThanOneInItemList()
    {
        // Creates Feed mocks
        Feed[] feeds = new Feed[5];
        feeds[0] = Mocks.createMockFeed("Example Feed 1", "http://examplefeed.com/",
                "This is a description for example feed 1", "http://examplefeed1.com/feed.xml");
        feeds[1] = Mocks.createMockFeed("Example Feed 2", "http://examplefeed2.com/",
                "This is a description for example feed 2", "http://examplefeed2.com/feed.xml");
        feeds[2] = Mocks.createMockFeed("Example Feed 3", "http://examplefeed3.com/",
                "This is a description for example feed 3", "http://examplefeed3.com/feed.xml");
        feeds[3] = Mocks.createMockFeed("Example Feed 1", "http://examplefeed.com/",
                "This is a description for example feed 1", "http://examplefeed1.com/feed.xml");
        feeds[4] = Mocks.createMockFeed("Example Feed 1", "http://examplefeed.com/",
                "This is a description for example feed 1", "http://examplefeed1.com/feed.xml");
        config.setFeeds(feeds);

        // Removes the last Feed
        config.removeFeedFromConfiguration("http://examplefeed1.com/feed.xml");

        assertEquals(4, config.getFeeds().length);
        assertEquals("http://examplefeed2.com/feed.xml", config.getFeeds()[0].getUrlToXML());
        assertEquals("http://examplefeed3.com/feed.xml", config.getFeeds()[1].getUrlToXML());
        assertEquals("http://examplefeed1.com/feed.xml", config.getFeeds()[2].getUrlToXML());
        assertEquals("http://examplefeed1.com/feed.xml", config.getFeeds()[3].getUrlToXML());
    }

    /**
     * Test Case: 31
     */
    @Test
    public void addItemListInEmptyConfiguration()
    {
        config.addItemListToConfiguration("TestItemList");

        assertEquals(1, config.getItemLists().length);
        assertEquals("TestItemList", config.getItemLists()[0].getName());
    }

    /**
     * Test Case: 32
     */
    @Test
    public void addItemListInNonEmptyConfiguration()
    {
        // Creates ItemList mocks
        ItemList[] itemLists = new ItemList[2];
        itemLists[0] = Mocks.createMockItemList("ItemList 1", "DATE_ASC");
        itemLists[1] = Mocks.createMockItemList("ItemList 2", "NAME_DEC");
        config.setItemLists(itemLists);

        // Adds a new ItemList
        config.addItemListToConfiguration("ItemList 3");

        // Validates the objects in the array
        assertEquals(3, config.getItemLists().length);
        assertEquals("ItemList 1", config.getItemLists()[0].getName());
        assertEquals("ItemList 2", config.getItemLists()[1].getName());
        assertEquals("ItemList 3", config.getItemLists()[2].getName());
    }

    /**
     * Test Case: 33
     */
    @Test(expected = RuntimeException.class)
    public void addItemListWithExistingName()
    {
        // Creates ItemList mocks
        ItemList[] itemLists = new ItemList[2];
        itemLists[0] = Mocks.createMockItemList("ItemList 1", "DATE_ASC");
        itemLists[1] = Mocks.createMockItemList("ItemList 2", "NAME_DEC");
        config.setItemLists(itemLists);

        // Adds a new ItemList
        config.addItemListToConfiguration("ItemList 1");

        // Validates the objects in the array
        assertEquals(2, config.getItemLists().length);
        assertEquals("ItemList 1", config.getItemLists()[0].getName());
        assertEquals("ItemList 2", config.getItemLists()[1].getName());
    }

    /**
     * Test Case: 34
     */
    @Test(expected = RuntimeException.class)
    public void removeItemList()
    {
        config.removeItemListFromConfiguration("");
    }

    /**
     * Test Case: 44
     * Tries to add a Feed to an existing ItemList. Uses verify method to verify that the addFeed
     * method is called.
     */
    @Test
    public void addFeedToExistingItemList()
    {
        // Creates ItemList mocks
        ItemList[] itemLists = new ItemList[3];
        itemLists[0] = Mocks.createMockItemList("ItemList 1", "DATE_ASC");
        itemLists[1] = Mocks.createMockItemList("ItemList 2", "NAME_DEC");
        itemLists[2] = Mocks.createMockItemList("ItemList 3", "NAME_ASC");
        config.setItemLists(itemLists);

        config.addFeedToItemList("ItemList 2", "http://examplefeed.net/feed.xml");

        verify(itemLists[0], never()).addFeed("http://examplefeed.net/feed.xml");
        verify(itemLists[1], times(1)).addFeed("http://examplefeed.net/feed.xml");
        verify(itemLists[2], never()).addFeed("http://examplefeed.net/feed.xml");
    }

    /**
     * Test Case: 45
     * Tries to add a Feed to an ItemList that does not exist. This will result in a
     * RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void addFeedToNonExistingItemList()
    {
        // Creates ItemList mocks
        ItemList[] itemLists = new ItemList[2];
        itemLists[0] = Mocks.createMockItemList("ItemList 1", "DATE_ASC");
        config.setItemLists(itemLists);

        config.addFeedToItemList("ItemList 2", "http://examplefeed.net/feed.xml");
    }

    /**
     * Test Case: 46
     * Tries to add Feed to an ItemList when the array of ItemLists is empty. Should throw
     * RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void addFeedToEmptyItemList()
    {
        config.addFeedToItemList("ItemList 1", "http://examplefeed.net/feed.xml");
    }

    /**
     * Test Case: 47
     * Removes a Feed that exist in the ItemList. The test then verifies that the removeFeed was
     * called on the correct ItemList.
     */
    @Test
    public void removeFeedFromExistingItemList()
    {
        // Creates ItemList mocks
        ItemList[] itemLists = new ItemList[3];
        itemLists[0] = Mocks.createMockItemList("ItemList 1", "DATE_ASC");
        itemLists[1] = Mocks.createMockItemList("ItemList 2", "NAME_DEC");
        itemLists[2] = Mocks.createMockItemList("ItemList 3", "NAME_ASC");
        config.setItemLists(itemLists);

        config.removeFeedFromItemList("ItemList 2", "http://examplefeed.net/feed.xml");

        verify(itemLists[0], never()).removeFeed("http://examplefeed.net/feed.xml");
        verify(itemLists[1], times(1)).removeFeed("http://examplefeed.net/feed.xml");
        verify(itemLists[2], never()).removeFeed("http://examplefeed.net/feed.xml");
    }

    /**
     * Test Case: 48
     */
    @Test(expected = RuntimeException.class)
    public void removeFeedFromNonExistantItemList()
    {
        // Creates ItemList mocks
        ItemList[] itemLists = new ItemList[2];
        itemLists[0] = Mocks.createMockItemList("ItemList 1", "DATE_ASC");
        config.setItemLists(itemLists);

        config.removeFeedFromItemList("ItemList 2", "http://examplefeed.net/feed.xml");
    }

    /**
     * Test Case: 49
     */
    @Test(expected = RuntimeException.class)
    public void removeFeedFromEmptyItemList()
    {
        config.removeFeedFromItemList("ItemList 1", "http://examplefeed.net/feed.xml");
    }

    /**
     * Test Case: 35
     */
    @Test
    public void updateTest()
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

        config.update();

        verify(config.getFeeds()[0]).update();
        verify(config.getItemLists()[0]).update(any());
    }

    /**
     * Test Case: 36
     */
    @Test(expected = RuntimeException.class)
    public void updateOnEmpty()
    {
        config.update();
    }
}

// Created: 2016-05-01