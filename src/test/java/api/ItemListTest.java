package api;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

/**
 * Test Class ItemListTest
 *
 * This is the test suite with tests for the ItemList class.
 *
 * @author Axel Nilsson (axnion)
 * @version 1.0
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
     * Test case: 10
     * Tries to create an ItemList without giving it a name. Then checks the field of the ItemList.
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
     * Test case: 11
     * Tries to create an ItemList with a name. Then checks the fields of the ItemList.
     */
    @Test
    public void createNamedObject()
    {
        itemList = new ItemList("TestList");
        assertEquals("TestList", itemList.getName());
        assertEquals("DATE_DEC", itemList.getSorting());
        assertNull(itemList.getFeedUrls());
        assertNull(itemList.getItems());
    }

    /**
     * Test case: 12
     * Tests the accessors and mutators of the ItemList class.
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
     * Test case: 13
     * Tries to add a Feed to the ItemList.
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
     * Test case: 14
     * Tries to add a Feed to an ItemList which already has a Feed with the same identifyer.
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
     * Test case: 15
     * Tries to remove the first Feed in the array from the ItemList.
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
     * Test case: 16
     * Tries to remove the middle Feed in the array from the ItemList.
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
     * Test case: 17
     * Tries to remove the last Feed in the array from the ItemList.
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
     * Test case: 18
     * Tries to remove a Feed from an empty ItemList.
     */
    @Test(expected = RuntimeException.class)
    public void removeOnEmptyFeedArray()
    {
        itemList.removeFeed(feed1);
    }

    /**
     * Test case: 19
     * Tries to remove non existent Feed from an ItemList
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
     * Test case: 20
     * Tries to remove the last Feed in an ItemList. The array should be null after.
     */
    @Test
    public void removingLastFeed()
    {
        itemList.addFeed(feed1);
        itemList.removeFeed(feed1);

        assertNull(itemList.getFeedUrls());
    }

    /**
     * Test case: 42
     * Tries to remove a Feed from an itemList when there is more than one of them in the ItemList
     * with the same identifier.
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
     * Test case: 21
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
        assertEquals("item01", items[0].getTitle());
        assertEquals("item00", items[1].getTitle());

        itemList.addFeed(feed2);
        itemList.update(feeds);

        items = itemList.getItems();
        assertEquals(4, items.length);
        assertEquals("item11", items[0].getTitle());
        assertEquals("item10", items[1].getTitle());
        assertEquals("item01", items[2].getTitle());
        assertEquals("item00", items[3].getTitle());
    }

    /**
     * Test case: 50
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
     * Test case: 51
     * Tries to update an ItemList with null as an argument. Should produce RuntimeException.
     */
    @Test
    public void updateTestNoFeeds()
    {
        itemList.addFeed(feed1);
        itemList.update(null);

        assertNull(itemList.getItems());
    }

    /**
     * Test case: 88
     * Tests what happens when the sorting method is called but how the items should be sorted has
     * not been specified. The ItemList should be sorted by date descending.
     */
    @Test
    public void sortWithoutSettingSorting()
    {
        Feed[] feeds = createFeedArrayForSorting();
        itemList.addFeed(feed1);
        itemList.addFeed(feed2);
        itemList.update(feeds);

        Item[] items = itemList.getItems();

        assertEquals("Mon, 05 Sep 2016 12:00:00 +0000", items[0].getDate());
        assertEquals("Sun, 01 Sep 2016 12:00:00 +0000", items[1].getDate());
        assertEquals("Wen, 24 Aug 2016 12:00:00 +0000", items[2].getDate());
        assertEquals("Mon, 01 Mar 2016 12:00:00 +0000", items[3].getDate());
        assertEquals("Tus, 09 Jan 2016 12:00:00 +0000", items[4].getDate());
        assertEquals("Fri, 02 Jan 2016 12:00:00 +0000", items[5].getDate());
        assertEquals("Mon, 01 Jan 2016 12:00:00 +0000", items[6].getDate());
        assertEquals("Mon, 01 Jan 2015 12:00:00 +0000", items[7].getDate());
        assertEquals("Mon, 01 Jan 2014 12:00:00 +0000", items[8].getDate());
        assertEquals("Mon, 01 Jan 2013 12:00:00 +0000", items[9].getDate());
    }

    /**
     * Test case: 89
     * Tries to sort the itemList by title in an ascending order. After being sorted the array of
     * Item objects should be in alphabetical order.
     */
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

    /**
     * Test case: 90
     * Tries to sort the itemList by title in a descending order. After being sorted the array of
     * Item objects should be in reversed alphabetical order.
     */
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

    /**
     * Test case: 91
     * Tries to sort the itemList by date in an ascending order. After being sorted the array of
     * Item objects should have the oldest object first and newest last, and all other objects
     * should follow the same pattern.
     */
    @Test
    public void sortByDateAscending()
    {
        Feed[] feeds = createFeedArrayForSorting();
        itemList.addFeed(feed1);
        itemList.addFeed(feed2);
        itemList.update(feeds);

        itemList.setSorting("DATE_ASC");
        itemList.sort();

        Item[] items = itemList.getItems();

        assertEquals("Mon, 01 Jan 2013 12:00:00 +0000", items[0].getDate());
        assertEquals("Mon, 01 Jan 2014 12:00:00 +0000", items[1].getDate());
        assertEquals("Mon, 01 Jan 2015 12:00:00 +0000", items[2].getDate());
        assertEquals("Mon, 01 Jan 2016 12:00:00 +0000", items[3].getDate());
        assertEquals("Fri, 02 Jan 2016 12:00:00 +0000", items[4].getDate());
        assertEquals("Tus, 09 Jan 2016 12:00:00 +0000", items[5].getDate());
        assertEquals("Mon, 01 Mar 2016 12:00:00 +0000", items[6].getDate());
        assertEquals("Wen, 24 Aug 2016 12:00:00 +0000", items[7].getDate());
        assertEquals("Sun, 01 Sep 2016 12:00:00 +0000", items[8].getDate());
        assertEquals("Mon, 05 Sep 2016 12:00:00 +0000", items[9].getDate());
    }

    /**
     * Test case: 92
     * Tries to sort the itemList by date in a descending order. After being sorted the array of
     * Item objects should have the newest object first and oldest last, and all other objects
     * should follow the same pattern.
     */
    @Test
    public void sortByDateDecending()
    {
        Feed[] feeds = createFeedArrayForSorting();
        itemList.addFeed(feed1);
        itemList.addFeed(feed2);
        itemList.update(feeds);

        itemList.setSorting("DATE_DEC");
        itemList.sort();

        Item[] items = itemList.getItems();

        assertEquals("Mon, 05 Sep 2016 12:00:00 +0000", items[0].getDate());
        assertEquals("Sun, 01 Sep 2016 12:00:00 +0000", items[1].getDate());
        assertEquals("Wen, 24 Aug 2016 12:00:00 +0000", items[2].getDate());
        assertEquals("Mon, 01 Mar 2016 12:00:00 +0000", items[3].getDate());
        assertEquals("Tus, 09 Jan 2016 12:00:00 +0000", items[4].getDate());
        assertEquals("Fri, 02 Jan 2016 12:00:00 +0000", items[5].getDate());
        assertEquals("Mon, 01 Jan 2016 12:00:00 +0000", items[6].getDate());
        assertEquals("Mon, 01 Jan 2015 12:00:00 +0000", items[7].getDate());
        assertEquals("Mon, 01 Jan 2014 12:00:00 +0000", items[8].getDate());
        assertEquals("Mon, 01 Jan 2013 12:00:00 +0000", items[9].getDate());
    }

    /**
     * This method creates and returns an array of mocked Feed objects containing mocked Item
     * objects.
     * @return  An array of Feed objects to be used in some itemList tests.
     */
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

    /**
     * This method creates two Feed objects containing Item objects that will be used in the testing
     * of the sorting method of the ItemList class.
     * @return  An array of Feed objects to be used in testing sorting.
     */
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
        when(itemArr1[2].getDate()).thenReturn("Fri, 02 Jan 2016 12:00:00 +0000");
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

    /**
     * Adds dummy feeds to the itemList field.
     */
    private void addDummyFeedsToItemList()
    {
        itemList.addFeed(feed1);
        itemList.addFeed(feed2);
        itemList.addFeed(feed3);
    }
}

// Created: 2016-04-30