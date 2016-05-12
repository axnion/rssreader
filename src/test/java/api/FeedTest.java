package api;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

/**
 * Test Class FeedTest
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1.1
 */
public class FeedTest
{
    private Feed feed;

    @Before
    public void createObject()
    {
        feed = new Feed();
    }

    /**
     * Unit test case: 4
     */
    @Test
    public void createObjectWithoutUrl()
    {
        assertEquals("", feed.getTitle());
        assertEquals("", feed.getLink());
        assertEquals("", feed.getDescription());
        assertEquals("", feed.getUrlToXML());
        assertNull(feed.getItems());
    }

    /**
     * Unit test case: 5
     */
    @Test
    public void createObjectWithUrl()
    {
        feed = new Feed(FeedTest.class.getResource("../../../resources/test/xml/FeedBasic.xml")
                .getPath());

        // Checking the value of each field in the Feed object.
        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.google.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());
        assertEquals(FeedTest.class.getResource("../../../resources/test/xml/FeedBasic.xml")
                .getPath(), feed.getUrlToXML());
        assertEquals("Test title", feed.getTitle());

        // Check the Item object inside the Feed object so we know the read works.
        Item[] items = feed.getItems();
        assertEquals(2, items.length);
        assertEquals("Item title", items[0].getTitle());
        assertEquals("http://www.google.com", items[0].getLink());
        assertEquals("This is an item description", items[0].getDescription());
        assertFalse(items[0].isVisited());
        assertFalse(items[0].isStarred());
    }

    /**
     * Unit test case: 6
     */
    @Test
    public void accessorsAndMutators()
    {
        // Using mutators to change values of each field
        feed.setTitle("TestValue");
        feed.setLink("TestValue");
        feed.setDescription("TestValue");
        feed.setUrlToXML("TestValue");

        Item[] testItemArray = new Item[1];
        testItemArray[0] = mock(Item.class);
        when(testItemArray[0].getId()).thenReturn("TestID");
        feed.setItems(testItemArray);

        // Checking modified Feed
        assertEquals("TestValue", feed.getTitle());
        assertEquals("TestValue", feed.getLink());
        assertEquals("TestValue", feed.getDescription());
        assertEquals("TestValue", feed.getUrlToXML());
        assertEquals("TestID", feed.getItems()[0].getId());
    }

    /**
     * Unit test case: 7
     */
    @Test(expected = RuntimeException.class)
    public void readingNonexistingFile()
    {
        feed.setUrlToXML("/test.xml");
        feed.update();
    }

    @Test
    public void readingFeedWithItemWithEverything()
    {
        String url = "../../../resources/test/xml/FeedBrokenItems.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();
        Item[] items = feed.getItems();
        assertEquals(5, items.length);

        assertEquals("Item title 1", items[0].getTitle());
        assertEquals("http://www.test-link-1.net", items[0].getLink());
        assertEquals("This is an item 1 description", items[0].getDescription());
        assertEquals("Mon, 01 Jan 2016 00:00:00 +0000", items[0].getDate());
        assertEquals("test-id-1", items[0].getId());
    }

    @Test
    public void readingFeedWithItemWithoutTitle()
    {
        String url = "../../../resources/test/xml/FeedBrokenItems.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();
        Item[] items = feed.getItems();
        assertEquals(5, items.length);

        assertEquals("Item title 1", items[0].getTitle());
        assertEquals("http://www.test-link-1.net", items[0].getLink());
        assertEquals("This is an item 1 description", items[0].getDescription());
        assertEquals("Mon, 01 Jan 2016 00:00:00 +0000", items[0].getDate());
        assertEquals("test-id-1", items[0].getId());

        assertEquals("Untitled", items[1].getTitle());
        assertEquals("http://www.test-link-2.net", items[1].getLink());
        assertEquals("This is an item 2 description", items[1].getDescription());
        assertEquals("Tue, 02 Jan 2016 00:00:00 +0000", items[1].getDate());
        assertEquals("test-id-2", items[1].getId());
    }

    @Test
    public void readingFeedWithItemWithoutDescription()
    {
        String url = "../../../resources/test/xml/FeedBrokenItems.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();
        Item[] items = feed.getItems();
        assertEquals(5, items.length);

        assertEquals("Item title 3", items[2].getTitle());
        assertEquals("http://www.test-link-3.net", items[2].getLink());
        assertEquals("", items[2].getDescription());
        assertEquals("Sat, 01 Jan 0001 00:00:00 +0000", items[2].getDate());
        assertEquals("test-id-3", items[2].getId());
    }

    @Test
    public void readingFeedWithItemWithoutLink()
    {
        String url = "../../../resources/test/xml/FeedBrokenItems.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();
        Item[] items = feed.getItems();
        assertEquals(5, items.length);

        for(Item item : items)
            assertNotEquals("Item title 4", item.getTitle());
    }

    @Test
    public void readingFeedWithItemWithoutDate()
    {
        String url = "../../../resources/test/xml/FeedBrokenItems.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());
        assertEquals(FeedTest.class.getResource(url).getPath(), feed.getUrlToXML());

        Item[] items = feed.getItems();
        assertEquals(5, items.length);

        assertEquals("Item title 5", items[3].getTitle());
        assertEquals("http://www.test-link-5.net", items[3].getLink());
        assertEquals("This is an item 5 description", items[3].getDescription());
        assertEquals("Mon, 01 Jan 2016 00:00:00 +0000", items[3].getDate());
        assertEquals("test-id-5", items[3].getId());
    }

    @Test
    public void readingFeedWithItemWithoutId()
    {
        String url = "../../../resources/test/xml/FeedBrokenItems.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();
        Item[] items = feed.getItems();
        assertEquals(5, items.length);

        for(Item item : items)
            assertNotEquals("Item title 6", item.getTitle());
    }

    /**
     * Unit test case: 8
     */
    @Test
    public void syncingWithXmlAddedItem()
    {
        String url = "../../../resources/test/xml/FeedBasic.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();

        //Change URL to new XML to simulate an update to the Feed XML file
        url = "../../../resources/test/xml/FeedItemAdded.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();

        // Checking the Feed data
        assertEquals("Test title", feed.getTitle());
        assertEquals("This is a feed description", feed.getDescription());
        assertEquals("http://www.google.com", feed.getLink());

        // Checking the items
        Item[] feedItems = feed.getItems();
        assertEquals("Item title", feedItems[0].getTitle());
        assertEquals("http://www.google.com", feedItems[0].getLink());
        assertEquals("This is an item description", feedItems[0].getDescription());
        assertEquals("test-id-1", feedItems[0].getId());

        assertEquals("Item title 2", feedItems[1].getTitle());
        assertEquals("http://www.google.se", feedItems[1].getLink());
        assertEquals("This is a second item description", feedItems[1].getDescription());
        assertEquals("test-id-2", feedItems[1].getId());

        assertEquals("Item title 3", feedItems[2].getTitle());
        assertEquals("http://www.google.org", feedItems[2].getLink());
        assertEquals("This is a third item description", feedItems[2].getDescription());
        assertEquals("test-id-3", feedItems[2].getId());
    }

    /**
     * Unit test case: 9
     */
    @Test
    public void syncingWithXmlRemovedItem()
    {
        String url = "../../../resources/test/xml/FeedBasic.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();

        //Change URL to new XML to simulate an update to the Feed XML file
        url = "../../../resources/test/xml/FeedItemRemoved.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();

        // Checking the Feed data
        assertEquals("Test title", feed.getTitle());
        assertEquals("This is a feed description", feed.getDescription());
        assertEquals("http://www.google.com", feed.getLink());

        // Checking the items
        Item[] feedItems = feed.getItems();
        assertEquals("Item title", feedItems[0].getTitle());
        assertEquals("http://www.google.com", feedItems[0].getLink());
        assertEquals("This is an item description", feedItems[0].getDescription());
        assertEquals("test-id-1", feedItems[0].getId());
    }

    /**
     * Unit test case: 41
     */
    @Test
    public void syncingWithXmlRemovedLastItem()
    {
        String url = "../../../resources/test/xml/FeedBasic.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();

        //Change URL to new XML to simulate an update to the Feed XML file
        url = "../../../resources/test/xml/FeedLastItemRemoved.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();

        // Checking the Feed data
        assertEquals("Test title", feed.getTitle());
        assertEquals("This is a feed description", feed.getDescription());
        assertEquals("http://www.google.com", feed.getLink());
        assertNull(feed.getItems());
    }
}

// Created: 2016-04-17