package system.rss;

import org.junit.Before;
import org.junit.Test;
import system.rss.exceptions.NoXMLFileFound;

import static org.junit.Assert.*;

/**
 * Class RssParserTests
 *
 * This is the test class for the RssParser class.
 *
 * @author Axel Nilsson (axnion)
 */
public class RssParserTests {
    private RssParser rssParser;
    private String resources = FeedSniffer.class
            .getResource("../../../../resources/test/RssParserTestResources/")
            .getPath();

    /**
     * Test preparation. Create a new RssParser object before each test.
     */
    @Before
    public void createObject() {
        rssParser = new RssParser();
    }

    /**
     * Name: Read feed
     * Unit: getFeed(String)
     *
     * Reads an XML file containing an RSS feed without any defects.
     */
    @Test
    public void readFeed() {
        Feed feed = rssParser.getFeed(resources + "FeedItemComplete.xml");

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());

        assertEquals(1, feed.getItems().size());
        assertEquals("Item title", feed.getItems().get(0).getTitle());
        assertEquals("http://www.test-link.net", feed.getItems().get(0).getLink());
        assertEquals("This is an item description", feed.getItems().get(0).getDescription());
        assertEquals(1451606400, feed.getItems().get(0).getDate().getTime() / 1000);
        assertEquals("test-id", feed.getItems().get(0).getId());
    }

    /**
     * Name: Read nonexistent Feed
     * Unit: getFeed(String)
     *
     * Reads an XML file which does not exist.
     */
    @Test(expected = NoXMLFileFound.class)
    public void readNonexistentFeed() {
        rssParser.getFeed("/ThisFileShouldNotExist.xml");
    }

    /**
     * Name: Read Feed without title
     * Unit: getFeed(String)
     *
     * Reads an XML file containing an RSS feed without title.
     */
    @Test
    public void readFeedWithoutTitle() {
        Feed feed = rssParser.getFeed(resources + "FeedWithoutTitle.xml");

        assertEquals("Untitled", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());

        assertEquals(1, feed.getItems().size());
        assertEquals("Item title", feed.getItems().get(0).getTitle());
        assertEquals("http://www.test-link.net", feed.getItems().get(0).getLink());
        assertEquals("This is an item description", feed.getItems().get(0).getDescription());
        assertEquals(1451606400, feed.getItems().get(0).getDate().getTime() / 1000);
        assertEquals("test-id", feed.getItems().get(0).getId());
    }

    /**
     * Name: Read feed without link
     * Unit: getFeed(String)
     *
     * Reads an XML file containing an RSS feed without link.
     */
    @Test
    public void readFeedWithoutLink() {
        Feed feed = rssParser.getFeed(resources + "FeedWithoutLink.xml");

        assertEquals("Test title", feed.getTitle());
        assertEquals("", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());

        assertEquals(1, feed.getItems().size());
        assertEquals("Item title", feed.getItems().get(0).getTitle());
        assertEquals("http://www.test-link.net", feed.getItems().get(0).getLink());
        assertEquals("This is an item description", feed.getItems().get(0).getDescription());
        assertEquals(1451606400, feed.getItems().get(0).getDate().getTime() / 1000);
        assertEquals("test-id", feed.getItems().get(0).getId());
    }

    /**
     * Name: Read feed without description
     * Unit: getFeed(String)
     *
     * Reads an XML file containing an RSS feed without description.
     */
    @Test
    public void readFeedWithoutDescription() {
        Feed feed = rssParser.getFeed(resources + "FeedWithoutDescription.xml");

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("No description", feed.getDescription());

        assertEquals(1, feed.getItems().size());
        assertEquals("Item title", feed.getItems().get(0).getTitle());
        assertEquals("http://www.test-link.net", feed.getItems().get(0).getLink());
        assertEquals("This is an item description", feed.getItems().get(0).getDescription());
        assertEquals(1451606400, feed.getItems().get(0).getDate().getTime() / 1000);
        assertEquals("test-id", feed.getItems().get(0).getId());
    }

    /**
     * Name: Read feed without Items
     * Unit: getFeed(String)
     *
     * Reads an XML file containing an RSS feed without Items.
     */
    @Test
    public void readFeedWithoutItems() {
        Feed feed = rssParser.getFeed(resources + "FeedWithoutItems.xml");

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());
    }

    /**
     * Name: Read feed with Item without title
     * Unit: getFeed(String)
     *
     * Reads an XML file containing an RSS feed with an Item without title.
     */
    @Test
    public void readFeedWithItemWithoutTitle() {
        Feed feed = rssParser.getFeed(resources + "FeedItemWithoutTitle.xml");

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());

        assertEquals(1, feed.getItems().size());
        assertEquals("Untitled", feed.getItems().get(0).getTitle());
        assertEquals("http://www.test-link.net", feed.getItems().get(0).getLink());
        assertEquals("This is an item description", feed.getItems().get(0).getDescription());
        assertEquals(1451606400, feed.getItems().get(0).getDate().getTime() / 1000);
        assertEquals("test-id", feed.getItems().get(0).getId());
    }

    /**
     * Name: Read feed with Item without link
     * Unit: getFeed(String)
     *
     * Reads an XML file containing an RSS feed with an Item without link.
     */
    @Test
    public void readFeedWithItemWithoutLink() {
        Feed feed = rssParser.getFeed(resources + "FeedItemWithoutLink.xml");

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());

        assertTrue(feed.getItems().isEmpty());
    }

    /**
     * Name: Read feed with Item without description
     * Unit: getFeed(String)
     *
     * Reads an XML file containing an RSS feed with an Item without description.
     */
    @Test
    public void readFeedWithItemWithoutDescription() {
        Feed feed = rssParser.getFeed(resources + "FeedItemWithoutDescription.xml");

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());

        assertEquals(1, feed.getItems().size());
        assertEquals("Item title", feed.getItems().get(0).getTitle());
        assertEquals("http://www.test-link.net", feed.getItems().get(0).getLink());
        assertEquals("", feed.getItems().get(0).getDescription());
        assertEquals(1451606400, feed.getItems().get(0).getDate().getTime() / 1000);
        assertEquals("test-id", feed.getItems().get(0).getId());
    }

    /**
     * Name: Read feed with item without date
     * Unit: getFeed(String)
     *
     * Reads an XML file containing an RSS feed with an Item without date.
     */
    @Test
    public void readFeedWithItemWithoutDate() {
        Feed feed = rssParser.getFeed(resources + "FeedItemWithoutDate.xml");

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());

        assertEquals(1, feed.getItems().size());
        assertEquals("Item title", feed.getItems().get(0).getTitle());
        assertEquals("http://www.test-link.net", feed.getItems().get(0).getLink());
        assertEquals("This is an item description", feed.getItems().get(0).getDescription());
        assertEquals(0, feed.getItems().get(0).getDate().getTime() / 1000);
        assertEquals("test-id", feed.getItems().get(0).getId());
    }

    /**
     * Name: Read feed with Item without id
     * Unit: getFeed(String)
     *
     * Reads an XML file containing an RSS feed with an Item without id.
     */
    @Test
    public void readFeedWithItemWithoutId() {
        Feed feed = rssParser.getFeed(resources + "FeedItemWithoutId.xml");

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());

        assertTrue(feed.getItems().isEmpty());
    }

    /**
     * Name: Read feed with broken Items
     * Unit: getFeed(String)
     *
     * Reads an XML file containing an RSS feed with several broken Items
     */
    @Test
    public void readFeedWithBrokenItems() {
        Feed feed = rssParser.getFeed(resources + "FeedPartiallyBroken.xml");

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());

        assertEquals(2, feed.getItems().size());

        assertEquals("Item title 1", feed.getItems().get(0).getTitle());
        assertEquals("http://www.test-link-1.net", feed.getItems().get(0).getLink());
        assertEquals("This is an item 1 description", feed.getItems().get(0).getDescription());
        assertEquals(1451606400, feed.getItems().get(0).getDate().getTime() / 1000);
        assertEquals("test-id-1", feed.getItems().get(0).getId());

        assertEquals("Untitled", feed.getItems().get(1).getTitle());
        assertEquals("http://www.test-link-3.net", feed.getItems().get(1).getLink());
        assertEquals("This is an item 3 description", feed.getItems().get(1).getDescription());
        assertEquals(1451779200, feed.getItems().get(1).getDate().getTime() / 1000);
        assertEquals("test-id-3", feed.getItems().get(1).getId());
    }

    /**
     * Name: Update Feed nothing to update
     * Unit: updateFeed(Feed)
     *
     * Tries to update a Feed where nothing needs to be updated.
     */
    @Test
    public void updateFeedNothingToUpdate() {
        Feed feed = rssParser.getFeed(resources + "update/original.xml");

        assertEquals(4, feed.getItems().size());
        assertEquals("item1", feed.getItems().get(0).getId());
        assertEquals("item2", feed.getItems().get(1).getId());
        assertEquals("item3", feed.getItems().get(2).getId());
        assertEquals("item4", feed.getItems().get(3).getId());

        rssParser.updateFeed(feed);

        assertEquals(4, feed.getItems().size());
        assertEquals("item1", feed.getItems().get(0).getId());
        assertEquals("item2", feed.getItems().get(1).getId());
        assertEquals("item3", feed.getItems().get(2).getId());
        assertEquals("item4", feed.getItems().get(3).getId());
    }

    /**
     * Name: Update Feed added Item
     * Unit: updateFeed(Feed)
     *
     * Tries to update a Feed where an Item has been added.
     */
    @Test
    public void updateFeedAddedItem() {
        Feed feed = rssParser.getFeed(resources + "update/original.xml");

        assertEquals(4, feed.getItems().size());
        assertEquals("item1", feed.getItems().get(0).getId());
        assertEquals("item2", feed.getItems().get(1).getId());
        assertEquals("item3", feed.getItems().get(2).getId());
        assertEquals("item4", feed.getItems().get(3).getId());

        feed.setUrlToXML(resources + "update/itemAdded.xml");
        rssParser.updateFeed(feed);

        assertEquals(5, feed.getItems().size());
        assertEquals("item1", feed.getItems().get(0).getId());
        assertEquals("item2", feed.getItems().get(1).getId());
        assertEquals("item3", feed.getItems().get(2).getId());
        assertEquals("item4", feed.getItems().get(3).getId());
        assertEquals("item5", feed.getItems().get(4).getId());
    }


    /**
     * Name: Update Feed removed Item
     * Unit: updateFeed(Feed)
     *
     * Tries to update a Feed where an Item has been removed.
     */
    @Test
    public void updateFeedRemovedItem() {
        Feed feed = rssParser.getFeed(resources + "update/original.xml");

        assertEquals(4, feed.getItems().size());
        assertEquals("item1", feed.getItems().get(0).getId());
        assertEquals("item2", feed.getItems().get(1).getId());
        assertEquals("item3", feed.getItems().get(2).getId());
        assertEquals("item4", feed.getItems().get(3).getId());

        feed.setUrlToXML(resources + "update/itemRemoved.xml");
        rssParser.updateFeed(feed);

        assertEquals(3, feed.getItems().size());
        assertEquals("item1", feed.getItems().get(0).getId());
        assertEquals("item2", feed.getItems().get(1).getId());
        assertEquals("item4", feed.getItems().get(2).getId());
    }
}
