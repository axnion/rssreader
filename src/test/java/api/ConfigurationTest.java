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
     * Test case: 22
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
     * Test case: 24
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
     * Test case: 25
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
     * Test case: 26
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
     * Test case: 27
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
     * Test case: 28
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
     * Test case: 29
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
     * Test case: 30
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
     * Test case: 43
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
     * Test case: 31
     */
    @Test
    public void addItemListInEmptyConfiguration()
    {
        config.addItemListToConfiguration("TestItemList");

        assertEquals(1, config.getItemLists().length);
        assertEquals("TestItemList", config.getItemLists()[0].getName());
    }

    /**
     * Test case: 32
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
     * Test case: 33
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
     * Test case: 50
     * Removes an ItemList that exists in the Configuration. The correct one should be removed and
     * there should be only one left afterwards.
     */
    @Test
    public void removeExistingItemList()
    {
        // Creates ItemList mocks
        ItemList[] itemLists = new ItemList[2];
        itemLists[0] = Mocks.createMockItemList("ItemList 1", "DATE_ASC");
        itemLists[1] = Mocks.createMockItemList("ItemList 2", "NAME_DEC");
        config.setItemLists(itemLists);

        // Removes an existing ItemList
        config.removeItemListFromConfiguration("ItemList 1");

        // Check the remaining ItemList so the correct one is removed
        assertEquals(1, config.getItemLists().length);
        assertEquals("ItemList 2", config.getItemLists()[0].getName());
    }

    /**
     * Test case: 51
     * Tries to remove an ItemList that already
     */
    @Test(expected = RuntimeException.class)
    public void removeNonExistingItemList()
    {
        // Creates ItemList mocks
        ItemList[] itemLists = new ItemList[2];
        itemLists[0] = Mocks.createMockItemList("ItemList 1", "DATE_ASC");
        itemLists[1] = Mocks.createMockItemList("ItemList 2", "NAME_DEC");
        config.setItemLists(itemLists);

        // Removes an existing ItemList
        config.removeItemListFromConfiguration("ItemList 3");

        // Check the remaining ItemList so nothing is removed
        assertEquals(2, config.getItemLists().length);
        assertEquals("ItemList 1", config.getItemLists()[0].getName());
        assertEquals("ItemList 2", config.getItemLists()[1].getName());
    }

    /**
     * Test case: 52
     */
    @Test(expected = RuntimeException.class)
    public void removeOnEmptyItemListArray()
    {
        config.removeItemListFromConfiguration("ItemList 1");
        assertNull(config.getItemLists());
    }

    /**
     * Test case: 53
     */
    @Test
    public void removeLastItemList()
    {
        // Creates ItemList mocks
        ItemList[] itemLists = new ItemList[1];
        itemLists[0] = Mocks.createMockItemList("ItemList 1", "DATE_ASC");
        config.setItemLists(itemLists);

        // Removes an existing ItemList
        config.removeItemListFromConfiguration("ItemList 1");

        // Checks the array, it should be null.
        assertNull(config.getItemLists());
    }

    /**
     * Test case: 44
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
     * Test case: 45
     * Tries to add a Feed to an ItemList that does not exist. This will result in a
     * RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void addFeedToNonExistingItemList()
    {
        // Creates ItemList mocks
        ItemList[] itemLists = new ItemList[1];
        itemLists[0] = Mocks.createMockItemList("ItemList 1", "DATE_ASC");
        config.setItemLists(itemLists);

        config.addFeedToItemList("ItemList 2", "http://examplefeed.net/feed.xml");
    }

    /**
     * Test case: 46
     * Tries to add Feed to an ItemList when the array of ItemLists is empty. Should throw
     * RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void addFeedToEmptyItemList()
    {
        config.addFeedToItemList("ItemList 1", "http://examplefeed.net/feed.xml");
    }

    /**
     * Test case: 47
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
     * Test case: 48
     * Tries to remove a Feed from an ItemList that does not exist. This should produce a
     * RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void removeFeedFromNonExistantItemList()
    {
        // Creates ItemList mocks
        ItemList[] itemLists = new ItemList[1];
        itemLists[0] = Mocks.createMockItemList("ItemList 1", "DATE_ASC");
        config.setItemLists(itemLists);

        config.removeFeedFromItemList("ItemList 2", "http://examplefeed.net/feed.xml");
    }

    /**
     * Test case: 49
     * Tries to remove a Feed from an ItemList that does not exist, because there are no ItemList at
     * all. Should produce a RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void removeFeedFromEmptyItemList()
    {
        config.removeFeedFromItemList("ItemList 1", "http://examplefeed.net/feed.xml");
    }

    /**
     * Test case: 62
     */
    @Test
    public void getExistingItemList()
    {
        // Creates ItemList mocks
        ItemList[] itemLists = new ItemList[3];
        itemLists[0] = Mocks.createMockItemList("ItemList 1", "DATE_ASC");
        itemLists[1] = Mocks.createMockItemList("ItemList 2", "NAME_DEC");
        itemLists[2] = Mocks.createMockItemList("ItemList 3", "NAME_ASC");
        config.setItemLists(itemLists);

        ItemList returnValue = config.getItemList("ItemList 2");

        assertEquals("ItemList 2", returnValue.getName());
        assertEquals("NAME_DEC", returnValue.getSorting());
    }

    /**
     * Test case: 63
     */
    @Test(expected = RuntimeException.class)
    public void getNonExistingItemList()
    {
        // Creates ItemList mocks
        ItemList[] itemLists = new ItemList[3];
        itemLists[0] = Mocks.createMockItemList("ItemList 1", "DATE_ASC");
        itemLists[1] = Mocks.createMockItemList("ItemList 2", "NAME_DEC");
        itemLists[2] = Mocks.createMockItemList("ItemList 3", "NAME_ASC");
        config.setItemLists(itemLists);

        config.getItemList("itemList 4");
    }

    /**
     * Test case: 64
     */
    @Test(expected = RuntimeException.class)
    public void getItemListFromEmptyList()
    {
        config.getItemList("itemList 4");
    }

    /**
     * Test case: 35
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
     * Test case: 36
     * Tries to update the the feeds and the item lists but both are null. Should just ignore and not
     * call update on the feeds or item lists and they should remain null.
     */
    @Test
    public void updateOnEmpty()
    {
        config.update();
        assertNull(config.getFeeds());
        assertNull(config.getItemLists());
    }

    /**
     * Test case: 94
     * Tests when the method is called to set the sorting to TITLE_ASC. Checks so the setSorting
     * method on the ItemList object is called and the sort method.
     */
    @Test
    public void setSortingToTitleAsc()
    {
        // Creates ItemList mocks
        ItemList[] itemList = new ItemList[1];
        itemList[0] = Mocks.createMockItemList("testList", "");
        config.setItemLists(itemList);

        config.setSorting("TITLE_ASC", "testList");

        verify(itemList[0]).setSorting("TITLE_ASC");
        verify(itemList[0]).sort();
    }

    /**
     * Test case: 95
     * Tests when the method is called to set the sorting to TITLE_DEC. Checks so the setSorting
     * method on the ItemList object is called and the sort method.
     */
    @Test
    public void setSortingToTitleDec()
    {
        // Creates ItemList mocks
        ItemList[] itemList = new ItemList[1];
        itemList[0] = Mocks.createMockItemList("testList", "");
        config.setItemLists(itemList);

        config.setSorting("TITLE_DEC", "testList");

        verify(itemList[0]).setSorting("TITLE_DEC");
        verify(itemList[0]).sort();
    }

    /**
     * Test case: 96
     * Tests when the method is called to set the sorting to DATE_ASC. Checks so the setSorting
     * method on the ItemList object is called and the sort method.
     */
    @Test
    public void setSortingToDateAsc()
    {
        // Creates ItemList mocks
        ItemList[] itemList = new ItemList[1];
        itemList[0] = Mocks.createMockItemList("testList", "");
        config.setItemLists(itemList);

        config.setSorting("DATE_ASC", "testList");

        verify(itemList[0]).setSorting("DATE_ASC");
        verify(itemList[0]).sort();
    }

    /**
     * Test case: 97
     * Tests when the method is called to set the sorting to DATE_DEC. Checks so the setSorting
     * method on the ItemList object is called and the sort method.
     */
    @Test
    public void setSortingToDateDec()
    {
        // Creates ItemList mocks
        ItemList[] itemList = new ItemList[1];
        itemList[0] = Mocks.createMockItemList("testList", "");
        config.setItemLists(itemList);

        config.setSorting("DATE_DEC", "testList");

        verify(itemList[0]).setSorting("DATE_DEC");
        verify(itemList[0]).sort();
    }

    /**
     * Test case: 98
     * Tests when the method is called to set the sorting to an unkown method. The tests expects a
     * RuntimeException to be thrown and the setSorting and sort methods on the itemList object
     * should not be called.
     */
    @Test(expected = RuntimeException.class)
    public void setSortingToUnknownMethod()
    {
        // Creates ItemList mocks
        ItemList[] itemList = new ItemList[1];
        itemList[0] = Mocks.createMockItemList("testList", "");
        config.setItemLists(itemList);

        config.setSorting("STARS_ASC", "testList");

        verify(itemList[0], never()).setSorting("STARS_ASC");
        verify(itemList[0], never()).sort();
    }

    /**
     * Test case: 99
     * Tests when the method is called to set the sorting to TITLE_ASC when the ItemList objects
     * sorting is already set to TITLE_ASC. We expect no exception but the setSorting and sort
     * methods should never be called.
     */
    @Test
    public void setSortingToSameSorting()
    {
        // Creates ItemList mocks
        ItemList[] itemList = new ItemList[1];
        itemList[0] = Mocks.createMockItemList("testList", "TITLE_ASC");
        config.setItemLists(itemList);

        config.setSorting("TITLE_ASC", "testList");

        verify(itemList[0], never()).setSorting(any());
        verify(itemList[0], never()).sort();
    }

    /**
     * Test case: 100
     */
    @Test
    public void setStarredWithExistingItem()
    {
        // Creates Item mocks
        Item[] items = new Item[3];
        items[0] = Mocks.createMockItem("Example Item 1", "http://examplefeed1.net/exampleItem",
                "This is an item description", "exItem1", false, false);
        items[1] = Mocks.createMockItem("Example Item 2", "http://examplefeed2.net/exampleItem",
                "This is an item description", "exItem2", false, false);
        items[2] = Mocks.createMockItem("Example Item 3", "http://examplefeed3.net/exampleItem",
                "This is an item description", "exItem3", false, false);

        Item[] otherItems = new Item[1];
        otherItems[0] = Mocks.createMockItem("Other item", "http://examplefeed1.net/exampleItem",
                "This is an item description", "otherItem", false, false);

        // Creates Feed mocks
        Feed[] feeds = new Feed[3];
        feeds[0] = Mocks.createMockFeed("Example Feed", "http://examplefeed.net",
                "This is a description for example feed", "http://examplefeed.net/feed.xml");
        feeds[1] = Mocks.createMockFeed("Example Feed", "http://examplefeed.net",
                "This is a description for example feed", "http://examplefeed.net/feed.xml");
        feeds[2] = Mocks.createMockFeed("Example Feed", "http://examplefeed.net",
                "This is a description for example feed", "http://examplefeed.net/feed.xml");
        when(feeds[0].getItems()).thenReturn(otherItems);
        when(feeds[1].getItems()).thenReturn(items);
        when(feeds[2].getItems()).thenReturn(otherItems);
        config.setFeeds(feeds);

        config.setStarred(true, "exItem2");

        verify(feeds[0], atLeastOnce()).getItems();
        verify(feeds[1], atLeastOnce()).getItems();
        verify(feeds[2], never()).getItems();

        verify(items[0], never()).setStarred(anyBoolean());
        verify(items[1], times(1)).setStarred(true);
        verify(items[2], never()).setStarred(anyBoolean());
        verify(otherItems[0], never()).setStarred(anyBoolean());
    }

    /**
     * Test case: 101
     */
    @Test(expected = RuntimeException.class)
    public void setStarredFeedsNull()
    {
        config.setStarred(true, "exItem");
    }

    /**
     * Test case: 102
     */
    @Test(expected = RuntimeException.class)
    public void setStarredItemsNull()
    {
        Feed[] feeds = new Feed[1];
        feeds[0] = Mocks.createMockFeed("Example Feed", "http://examplefeed.net",
                "This is a description for example feed", "http://examplefeed.net/feed.xml");
        when(feeds[0].getItems()).thenReturn(null);

        config.setStarred(true, "exItem");
        verify(feeds[0], times(1)).getItems();
    }

    /**
     * Test case: 103
     */
    @Test(expected = RuntimeException.class)
    public void setStarredNonExistentItem()
    {
        // Creates Item mocks
        Item[] items = new Item[3];
        items[0] = Mocks.createMockItem("Example Item 1", "http://examplefeed1.net/exampleItem",
                "This is an item description", "exItem1", false, false);
        items[1] = Mocks.createMockItem("Example Item 2", "http://examplefeed2.net/exampleItem",
                "This is an item description", "exItem2", false, false);
        items[2] = Mocks.createMockItem("Example Item 3", "http://examplefeed3.net/exampleItem",
                "This is an item description", "exItem3", false, false);

        Item[] otherItems = new Item[1];
        otherItems[0] = Mocks.createMockItem("Other item", "http://examplefeed1.net/exampleItem",
                "This is an item description", "otherItem", false, false);

        // Creates Feed mocks
        Feed[] feeds = new Feed[3];
        feeds[0] = Mocks.createMockFeed("Example Feed", "http://examplefeed.net",
                "This is a description for example feed", "http://examplefeed.net/feed.xml");
        feeds[1] = Mocks.createMockFeed("Example Feed", "http://examplefeed.net",
                "This is a description for example feed", "http://examplefeed.net/feed.xml");
        feeds[2] = Mocks.createMockFeed("Example Feed", "http://examplefeed.net",
                "This is a description for example feed", "http://examplefeed.net/feed.xml");
        when(feeds[0].getItems()).thenReturn(otherItems);
        when(feeds[1].getItems()).thenReturn(items);
        when(feeds[2].getItems()).thenReturn(otherItems);
        config.setFeeds(feeds);

        config.setStarred(true, "NonExistantID");
    }
}

// Created: 2016-05-01