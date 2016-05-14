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
     * Unit test case: 10
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
     * Unit test case: 11
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
     * Unit test case: 12
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
     * Unit test case: 13
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
     * Unit test case: 14
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
     * Unit test case: 15
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
     * Unit test case: 16
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
     * Unit test case: 17
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
     * Unit test case: 18
     */
    @Test(expected = RuntimeException.class)
    public void removeOnEmptyFeedArray()
    {
        itemList.removeFeed(feed1);
    }

    /**
     * Unit test case: 19
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
     * Unit test case: 20
     */
    @Test
    public void removingLastFeed()
    {
        itemList.addFeed(feed1);
        itemList.removeFeed(feed1);

        assertNull(itemList.getFeedUrls());
    }

    /**
     * Unit test case: 42
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
     * Unit test case: 21
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
     * Unit test case: 50
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
     * Unit test case: 51
     * Tries to update an ItemList with null as an argument. Should produce RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void updateTestNoFeeds()
    {
        itemList.addFeed(feed1);
        itemList.update(null);

        assertNull(itemList.getItems());
    }

    @Test
    public void sortByTitleAscending()
    {
        Feed[] feeds = createFeedArrayForSorting();
        itemList.addFeed(feed1);
        itemList.addFeed(feed2);
        itemList.update(feeds);

        itemList.setSorting("TITLE_ASC");
        itemList.sort();

        Item[] items = itemList.getItems();

        assertEquals("A Item", items[0].getTitle());
        assertEquals("B Item", items[1].getTitle());
        assertEquals("C Item", items[2].getTitle());
        assertEquals("D Item", items[3].getTitle());
        assertEquals("E Item", items[4].getTitle());
        assertEquals("F Item", items[5].getTitle());
        assertEquals("G Item", items[6].getTitle());
        assertEquals("H Item", items[7].getTitle());
        assertEquals("I Item", items[8].getTitle());
        assertEquals("J Item", items[9].getTitle());
    }

    @Test
    public void sortByTitleDecending()
    {
        Feed[] feeds = createFeedArrayForSorting();
        itemList.addFeed(feed1);
        itemList.addFeed(feed2);
        itemList.update(feeds);

        itemList.setSorting("TITLE_DEC");
        itemList.sort();

        Item[] items = itemList.getItems();

        assertEquals("J Item", items[0].getTitle());
        assertEquals("I Item", items[1].getTitle());
        assertEquals("H Item", items[2].getTitle());
        assertEquals("G Item", items[3].getTitle());
        assertEquals("F Item", items[4].getTitle());
        assertEquals("E Item", items[5].getTitle());
        assertEquals("D Item", items[6].getTitle());
        assertEquals("C Item", items[7].getTitle());
        assertEquals("B Item", items[8].getTitle());
        assertEquals("A Item", items[9].getTitle());
    }

    @Test
    public void sortByDateAscending()
    {
//        Feed[] feeds = createFeedArrayForSorting();
//        itemList.addFeed(feed1);
//        itemList.addFeed(feed2);
//        itemList.update(feeds);
//
//        itemList.setSorting("TITLE_ASC");
//        itemList.sort();
//
//        Item[] items = itemList.getItems();
//
//        assertEquals("A Item", items[9].getTitle());
//        assertEquals("B Item", items[8].getTitle());
//        assertEquals("C Item", items[7].getTitle());
//        assertEquals("D Item", items[6].getTitle());
//        assertEquals("E Item", items[5].getTitle());
//        assertEquals("F Item", items[4].getTitle());
//        assertEquals("G Item", items[3].getTitle());
//        assertEquals("H Item", items[2].getTitle());
//        assertEquals("I Item", items[1].getTitle());
//        assertEquals("J Item", items[0].getTitle());
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

    private Feed[] createFeedArrayForSorting()
    {
        Item[] itemArr0 = new Item[5];
        itemArr0[0] = mock(Item.class);
        itemArr0[1] = mock(Item.class);
        itemArr0[2] = mock(Item.class);
        itemArr0[3] = mock(Item.class);
        itemArr0[4] = mock(Item.class);
        when(itemArr0[0].getTitle()).thenReturn("C Item");
        when(itemArr0[1].getTitle()).thenReturn("J Item");
        when(itemArr0[2].getTitle()).thenReturn("A Item");
        when(itemArr0[3].getTitle()).thenReturn("H Item");
        when(itemArr0[4].getTitle()).thenReturn("E Item");

        when(itemArr0[0].getDate()).thenReturn("Mon, 01 Jan 2016 12:00:00 +0000");
        when(itemArr0[1].getDate()).thenReturn("Tus, 09 Jan 2016 12:00:00 +0000");
        when(itemArr0[2].getDate()).thenReturn("Mon, 01 Jan 2013 12:00:00 +0000");
        when(itemArr0[3].getDate()).thenReturn("Mon, 05 Sep 2016 12:00:00 +0000");
        when(itemArr0[4].getDate()).thenReturn("Wen, 24 Aug 2016 12:00:00 +0000");

        when(itemArr0[0].compareTitle(any())).thenCallRealMethod();
        when(itemArr0[1].compareTitle(any())).thenCallRealMethod();
        when(itemArr0[2].compareTitle(any())).thenCallRealMethod();
        when(itemArr0[3].compareTitle(any())).thenCallRealMethod();
        when(itemArr0[4].compareTitle(any())).thenCallRealMethod();

        when(itemArr0[0].compareDate(any())).thenCallRealMethod();
        when(itemArr0[1].compareDate(any())).thenCallRealMethod();
        when(itemArr0[2].compareDate(any())).thenCallRealMethod();
        when(itemArr0[3].compareDate(any())).thenCallRealMethod();
        when(itemArr0[4].compareDate(any())).thenCallRealMethod();

        Item[] itemArr1 = new Item[5];
        itemArr1[0] = mock(Item.class);
        itemArr1[1] = mock(Item.class);
        itemArr1[2] = mock(Item.class);
        itemArr1[3] = mock(Item.class);
        itemArr1[4] = mock(Item.class);
        when(itemArr1[0].getTitle()).thenReturn("D Item");
        when(itemArr1[1].getTitle()).thenReturn("B Item");
        when(itemArr1[2].getTitle()).thenReturn("G Item");
        when(itemArr1[3].getTitle()).thenReturn("I Item");
        when(itemArr1[4].getTitle()).thenReturn("F Item");

        when(itemArr1[0].getDate()).thenReturn("Mon, 01 Jan 2015 12:00:00 +0000");
        when(itemArr1[1].getDate()).thenReturn("Mon, 01 Mar 2016 12:00:00 +0000");
        when(itemArr1[2].getDate()).thenReturn("Fri, 01 Jan 2016 12:00:00 +0000");
        when(itemArr1[3].getDate()).thenReturn("Mon, 01 Jan 2014 12:00:00 +0000");
        when(itemArr1[4].getDate()).thenReturn("Sun, 01 Sep 2016 12:00:00 +0000");

        when(itemArr1[0].compareTitle(any())).thenCallRealMethod();
        when(itemArr1[1].compareTitle(any())).thenCallRealMethod();
        when(itemArr1[2].compareTitle(any())).thenCallRealMethod();
        when(itemArr1[3].compareTitle(any())).thenCallRealMethod();
        when(itemArr1[4].compareTitle(any())).thenCallRealMethod();

        when(itemArr1[0].compareDate(any())).thenCallRealMethod();
        when(itemArr1[1].compareDate(any())).thenCallRealMethod();
        when(itemArr1[2].compareDate(any())).thenCallRealMethod();
        when(itemArr1[3].compareDate(any())).thenCallRealMethod();
        when(itemArr1[4].compareDate(any())).thenCallRealMethod();

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