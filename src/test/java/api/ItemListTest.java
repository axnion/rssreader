package api;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

/**
 * Test Class ItemListTest
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1.1
 */
public class ItemListTest
{
    private ItemList itemList;
    private String feed1 = "feed1";
    private String feed2 = "feed2";
    private String feed3 = "feed3";

    @Before
    public void createObject()
    {
        itemList = new ItemList();
    }

    /**
     * Test Case: 10
     */
    @Test
    public void createUnnamedObject()
    {
        assertEquals("", itemList.getName());
        assertEquals("", itemList.getName());
        assertNull(itemList.getFeedUrls());
        assertNull(itemList.getItems());
    }

    /**
     * Test Case: 11
     */
    @Test
    public void createNamedObject()
    {
        itemList = new ItemList("TestList");
        assertEquals("TestList", itemList.getName());
        assertEquals("", itemList.getSorting());
        assertNull(itemList.getFeedUrls());
        assertNull(itemList.getItems());
    }

    /**
     * Test Case: 12
     */
    @Test
    public void accessorsAndMutators()
    {
        String[] strArr = new String[2];
        strArr[0] = "URL1";
        strArr[1] = "URL2";

        itemList.setName("TestName");
        itemList.setSorting("Date");
        itemList.setFeedUrls(strArr);

        assertEquals("TestName", itemList.getName());
        assertEquals("Date", itemList.getSorting());
        assertEquals(2, itemList.getFeedUrls().length);
        assertEquals("URL1", itemList.getFeedUrls()[0]);
        assertEquals("URL2", itemList.getFeedUrls()[1]);
    }

    /**
     * Test Case: 13
     */
    @Test
    public void addFeedTest()
    {
        itemList.addFeed(feed1);
        assertEquals(1, itemList.getFeedUrls().length);
        assertEquals(feed1, itemList.getFeedUrls()[0]);

        itemList.addFeed(feed2);
        assertEquals(2, itemList.getFeedUrls().length);
        assertEquals(feed1, itemList.getFeedUrls()[0]);
        assertEquals(feed2, itemList.getFeedUrls()[1]);
    }

    /**
     * Test Case: 14
     */
    @Test
    public void addExistingFeed()
    {
        // Add the feed to the ItemList
        itemList.addFeed(feed1);
        assertEquals(1, itemList.getFeedUrls().length);
        assertEquals(feed1, itemList.getFeedUrls()[0]);

        // Add it again, nothing should change
        itemList.addFeed(feed1);
        assertEquals(1, itemList.getFeedUrls().length);
        assertEquals(feed1, itemList.getFeedUrls()[0]);
    }

    /**
     * Test Case: 15
     */
    @Test
    public void removeFirstInFeedArray()
    {
        addDummyFeedsToItemList();

        itemList.removeFeed("feed1");
        assertEquals(2, itemList.getFeedUrls().length);
        assertEquals(feed2, itemList.getFeedUrls()[0]);
        assertEquals(feed3, itemList.getFeedUrls()[1]);
    }

    /**
     * Test Case: 16
     */
    @Test
    public void removeMiddleInFeedArray()
    {
        addDummyFeedsToItemList();

        itemList.removeFeed("feed2");
        assertEquals(2, itemList.getFeedUrls().length);
        assertEquals(feed1, itemList.getFeedUrls()[0]);
        assertEquals(feed3, itemList.getFeedUrls()[1]);
    }

    /**
     * Test Case: 17
     */
    @Test
    public void removeLastInFeedArray()
    {
        addDummyFeedsToItemList();

        itemList.removeFeed("feed3");
        assertEquals(2, itemList.getFeedUrls().length);
        assertEquals(feed1, itemList.getFeedUrls()[0]);
        assertEquals(feed2, itemList.getFeedUrls()[1]);
    }

    /**
     * Test Case: 18
     */
    @Test(expected = RuntimeException.class)
    public void removeOnEmptyFeedArray()
    {
        itemList.removeFeed(feed1);
    }

    /**
     * Test Case: 19
     */
    @Test
    public void removeNonexistentFeed()
    {
        addDummyFeedsToItemList();

        itemList.removeFeed("feed4");
        assertEquals(3, itemList.getFeedUrls().length);
        assertEquals(feed1, itemList.getFeedUrls()[0]);
        assertEquals(feed2, itemList.getFeedUrls()[1]);
        assertEquals(feed3, itemList.getFeedUrls()[2]);
    }

    /**
     * Test Case: 20
     */
    @Test
    public void removingLastFeed()
    {
        itemList.addFeed(feed1);
        itemList.removeFeed(feed1);

        assertNull(itemList.getFeedUrls());
    }

    /**
     * Test Case: 42
     */
    @Test
    public void removingFeedWhenMoreThanOneInItemList()
    {
        String[] feeds = new String[5];
        feeds[0] = feed1;
        feeds[1] = feed1;
        feeds[2] = feed2;
        feeds[3] = feed3;
        feeds[4] = feed1;

        itemList.setFeedUrls(feeds);
        itemList.removeFeed(feed1);

        feeds = itemList.getFeedUrls();

        assertEquals(4, feeds.length);
        assertEquals(feed1, feeds[0]);
        assertEquals(feed2, feeds[1]);
        assertEquals(feed3, feeds[2]);
        assertEquals(feed1, feeds[3]);
    }

    /**
     * Test Case: 21
     * Adds a feed url to the ItemList, then it updates the ItemList with a Feed containing the feed
     * url. Then adds another feed and updates again. Checks results both times.
     */
    @Test
    public void updateTest()
    {
        Feed[] feeds = createFeedArrayMock();
        itemList.addFeed(feed1);
        itemList.update(feeds);

        Item[] items = itemList.getItems();
        assertEquals(2, items.length);
        assertEquals("item00", items[0].getTitle());
        assertEquals("item01", items[1].getTitle());

        itemList.addFeed(feed2);
        itemList.update(feeds);

        items = itemList.getItems();
        assertEquals(4, items.length);
        assertEquals("item00", items[0].getTitle());
        assertEquals("item01", items[1].getTitle());
        assertEquals("item10", items[2].getTitle());
        assertEquals("item11", items[3].getTitle());
    }

    /**
     * Test Case: 50
     * Tries to update an ItemList with no feed urls. Should produce RuntimeException.
     */
    @Test
    public void updateTestNoFeedUrls()
    {
        Feed[] feeds = createFeedArrayMock();
        itemList.update(feeds);

        assertNull(itemList.getItems());
    }

    /**
     * Test Case: 51
     * Tries to update an ItemList with null as an argument. Should produce RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void updateTestNoFeeds()
    {
        Feed[] feeds = createFeedArrayMock();
        itemList.addFeed(feed1);
        itemList.update(null);

        assertNull(itemList.getItems());
    }

    private Feed[] createFeedArrayMock()
    {
        Item[] itemArr0 = new Item[2];
        itemArr0[0] = mock(Item.class);
        itemArr0[1] = mock(Item.class);
        when(itemArr0[0].getTitle()).thenReturn("item00");
        when(itemArr0[1].getTitle()).thenReturn("item01");

        Item[] itemArr1 = new Item[2];
        itemArr1[0] = mock(Item.class);
        itemArr1[1] = mock(Item.class);
        when(itemArr1[0].getTitle()).thenReturn("item10");
        when(itemArr1[1].getTitle()).thenReturn("item11");

        Feed[] feedArr = new Feed[2];
        feedArr[0] = mock(Feed.class);
        feedArr[1] = mock(Feed.class);
        when(feedArr[0].getUrlToXML()).thenReturn("feed1");
        when(feedArr[1].getUrlToXML()).thenReturn("feed2");
        when(feedArr[0].getItems()).thenReturn(itemArr0);
        when(feedArr[1].getItems()).thenReturn(itemArr1);

        return feedArr;
    }

    private void addDummyFeedsToItemList()
    {
        itemList.addFeed(feed1);
        itemList.addFeed(feed2);
        itemList.addFeed(feed3);
    }
}

// Created: 2016-04-30