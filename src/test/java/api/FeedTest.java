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
     * Test Case: 4
     * Create an object without a url argument. All Strings in the object should be empty Strings
     * and the Item array should be null.
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
     * Test Case: 5
     * Create an object with a url argument. Then checks the values of the Feed so they have the
     * expected values of the loaded Feed.
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
     * Test Case: 6
     * Use the mutators to change the values of the Feed and then uses the accessors to check the
     * values.
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
     * Test Case: 7
     * Tries to read a feed XML file that does not exist. Should result in a RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void readingNonexistingFile()
    {
        feed.setUrlToXML("/test.xml");
        feed.update();
    }

    /**
     * Test case: 65
     * Tries to read a Feed XML file that contains an item that has title, link, description, date,
     * and id. This should all be loaded into the Item object in the Feed object.
     */
    @Test
    public void readingFeedWithItemWithEverything()
    {
        String url = "../../../resources/test/xml/FeedItemComplete.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();
        Item[] items = feed.getItems();
        assertEquals(1, items.length);

        assertEquals("Item title", items[0].getTitle());
        assertEquals("http://www.test-link.net", items[0].getLink());
        assertEquals("This is an item description", items[0].getDescription());
        assertEquals("Mon, 01 Jan 2016 00:00:00 +0000", items[0].getDate());
        assertEquals("test-id", items[0].getId());
    }

    /**
     * Test case: 66
     * Tries to read a Feed XML file that contains an item without a title. Everything should still
     * be loaded and the title should be set to Untitled.
     */
    @Test
    public void readingFeedWithItemWithoutTitle()
    {
        String url = "../../../resources/test/xml/FeedItemWithoutTitle.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();
        Item[] items = feed.getItems();
        assertEquals(1, items.length);

        assertEquals("Untitled", items[0].getTitle());
        assertEquals("http://www.test-link.net", items[0].getLink());
        assertEquals("This is an item description", items[0].getDescription());
        assertEquals("Mon, 01 Jan 2016 00:00:00 +0000", items[0].getDate());
        assertEquals("test-id", items[0].getId());
    }

    /**
     * Test case: 67
     * Tries to read a Feed XML file that contains an item without a description. Everything should
     * still be loaded and the description should be set to an empty String.
     */
    @Test
    public void readingFeedWithItemWithoutDescription()
    {
        String url = "../../../resources/test/xml/FeedItemWithoutDescription.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();
        Item[] items = feed.getItems();
        assertEquals(1, items.length);

        assertEquals("Item title", items[0].getTitle());
        assertEquals("http://www.test-link.net", items[0].getLink());
        assertEquals("", items[0].getDescription());
        assertEquals("Mon, 01 Jan 2016 00:00:00 +0000", items[0].getDate());
        assertEquals("test-id", items[0].getId());
    }

    /**
     * Test case: 68
     * Tries to read a Feed XML file that contains an item without a link. The item should be
     * ignored and a RuntimeException should be thrown.
     */
    @Test(expected = RuntimeException.class)
    public void readingFeedWithItemWithoutLink()
    {
        String url = "../../../resources/test/xml/FeedItemWithoutLink.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();
        Item[] items = feed.getItems();
        assertNull(items);
    }

    /**
     * Test case: 69
     * Tries to read a Feed XML file that contains an item without a pubDate. Everything should
     * still be loaded and the date should be set to the default "Mon, 01 Jan 0001 00:00:00 +0000"
     */
    @Test
    public void readingFeedWithItemWithoutDate()
    {
        String url = "../../../resources/test/xml/FeedItemWithoutDate.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());
        assertEquals(FeedTest.class.getResource(url).getPath(), feed.getUrlToXML());

        Item[] items = feed.getItems();
        assertEquals(1, items.length);

        assertEquals("Item title", items[0].getTitle());
        assertEquals("http://www.test-link.net", items[0].getLink());
        assertEquals("This is an item description", items[0].getDescription());
        assertEquals("Mon, 01 Jan 0001 00:00:00 +0000", items[0].getDate());
        assertEquals("test-id", items[0].getId());
    }

    /**
     * Test case: 70
     * Tries to read a Feed XML file that contains an item without a guid. The item should be
     * ignored and a RuntimeException should be thrown.
     */
    @Test(expected = RuntimeException.class)
    public void readingFeedWithItemWithoutId()
    {
        String url = "../../../resources/test/xml/FeedItemWithoutId.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();
        Item[] items = feed.getItems();
        assertNull(items);
    }

    /**
     * Test case: 71
     * Tries to read a Feed XML file that contains three items. The first one is fully working, the
     * second one is missing an id so is completely broken, the third one is missing a title so
     * should still be added. Only the first and third item should be added and the one missing an
     * id should be ignored.
     */
    @Test(expected = RuntimeException.class)
    public void readingFileWithPartiallyBrokenFeed()
    {
        String url = "../../../resources/test/xml/FeedPartiallyBroken.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());

        Item[] items = feed.getItems();
        assertEquals(2, items.length);

        assertEquals("Item title 1", items[0].getTitle());
        assertEquals("This is an item 1 description", items[0].getLink());
        assertEquals("http://www.test-link-1.net", items[0].getDescription());
        assertEquals("Mon, 01 Jan 2016 00:00:00 +0000", items[0].getDate());
        assertEquals("test-id-1", items[0].getId());

        assertEquals("Untitled", items[1].getTitle());
        assertEquals("This is an item 3 description", items[1].getLink());
        assertEquals("http://www.test-link-3.net", items[1].getDescription());
        assertEquals("Wen, 03 Jan 2016 00:00:00 +0000", items[1].getDate());
        assertEquals("test-id-3", items[1].getId());
    }

    /**
     * Test case: 72
     * Tries to read a Feed XML file that does not contain a title. The Feed should still be
     * created but the title should be set to Untitled.
     */
    @Test
    public void readingFeedWithoutTitle()
    {
        String url = "../../../resources/test/xml/FeedWithoutTitle.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();

        assertEquals("Untitled", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());
        assertNotNull(feed.getItems());
    }

    /**
     * Test case: 73
     * Tries to read a Feed XML file that does not contain a link. The Feed should still be
     * created but the link should be set to an empty String.
     */
    @Test
    public void readingFeedWithoutLink()
    {
        String url = "../../../resources/test/xml/FeedWithoutLink.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();

        assertEquals("Test title", feed.getTitle());
        assertEquals("", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());
        assertNotNull(feed.getItems());
    }

    /**
     * Test case: 74
     * Tries to read a Feed XML file that does not contain a description. The Feed should still be
     * created but the description should be set to an empty String.
     */
    @Test
    public void readingFeedWithoutDescription()
    {
        String url = "../../../resources/test/xml/FeedWithoutDescription.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("", feed.getDescription());
        assertNotNull(feed.getItems());
    }

    /**
     * Test case: 75
     * Tries to read a Feed XML file that does not contain any items. The Feed should still be
     * created but the items array should be null.
     */
    @Test
    public void readingFeedWithoutItems()
    {
        String url = "../../../resources/test/xml/FeedWithoutItems.xml";
        feed.setUrlToXML(FeedTest.class.getResource(url).getPath());
        feed.update();

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());
        assertNull(feed.getItems());
    }

    /**
     * Test Case: 8
     * Tries to sync a feed when an item has been added to the feed.
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
     * Test Case: 9
     * Tries to sync a feed when an item has been removed from the feed.
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
     * Test Case: 41
     * Tries to sync a feed where the last item in the feed has been removed.
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