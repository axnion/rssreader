package system.rss;

import org.junit.Before;
import org.junit.Test;
import system.rss.exceptions.NoXMLFileFound;

import static org.junit.Assert.*;

/**
 * Class RssParserTests
 *
 * @author Axel Nilsson (axnion)
 */
public class RssParserTests {
    private RssParser rssParser;
    private String resources = FeedSniffer.class
            .getResource("../../../../resources/test/RssParserTestResources/")
            .getPath();

    @Before
    public void createObject() {
        rssParser = new RssParser();
    }

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

    @Test(expected = NoXMLFileFound.class)
    public void readNonexistentFeed() {
        rssParser.getFeed("/ThisFileShouldNotExist.xml");
    }

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

    @Test
    public void readFeedWithoutDescription() {
        Feed feed = rssParser.getFeed(resources + "FeedWithoutDescription.xml");

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("", feed.getDescription());

        assertEquals(1, feed.getItems().size());
        assertEquals("Item title", feed.getItems().get(0).getTitle());
        assertEquals("http://www.test-link.net", feed.getItems().get(0).getLink());
        assertEquals("This is an item description", feed.getItems().get(0).getDescription());
        assertEquals(1451606400, feed.getItems().get(0).getDate().getTime() / 1000);
        assertEquals("test-id", feed.getItems().get(0).getId());
    }

    @Test
    public void readFeedWithoutItems() {
        Feed feed = rssParser.getFeed(resources + "FeedWithoutItems.xml");

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());
    }

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

    @Test
    public void readFeedWithItemWithoutLink() {
        Feed feed = rssParser.getFeed(resources + "FeedItemWithoutLink.xml");

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());

        assertTrue(feed.getItems().isEmpty());
    }

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

    @Test
    public void readFeedWithItemWithoutId() {
        Feed feed = rssParser.getFeed(resources + "FeedItemWithoutId.xml");

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());

        assertTrue(feed.getItems().isEmpty());
    }

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
