package rss;

import org.junit.Before;
import org.junit.Test;
import rss.exceptions.NoXMLFileFound;

import static org.junit.Assert.*;

/**
 * Class RssParserTests
 *
 * @author Axel Nilsson (axnion)
 */
public class RssParserTests {
    private RssParser rssParser;

    @Before
    public void createObject() {
        rssParser = new RssParser();
    }

    @Test
    public void readFeed() {
        String url = "../../../resources/test/xml/FeedItemComplete.xml";
        Feed feed = rssParser.getFeed(RssParser.class.getResource(url).getPath());

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
        String url = "../../../resources/test/xml/FeedWithoutTitle.xml";
        Feed feed = rssParser.getFeed(RssParser.class.getResource(url).getPath());

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
        String url = "../../../resources/test/xml/FeedWithoutLink.xml";
        Feed feed = rssParser.getFeed(RssParser.class.getResource(url).getPath());

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
        String url = "../../../resources/test/xml/FeedWithoutDescription.xml";
        Feed feed = rssParser.getFeed(RssParser.class.getResource(url).getPath());

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
        String url = "../../../resources/test/xml/FeedWithoutItems.xml";
        Feed feed = rssParser.getFeed(RssParser.class.getResource(url).getPath());

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());
    }

    @Test
    public void readFeedWithItemWithoutTitle() {
        String url = "../../../resources/test/xml/FeedItemWithoutTitle.xml";
        Feed feed = rssParser.getFeed(RssParser.class.getResource(url).getPath());

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
        String url = "../../../resources/test/xml/FeedItemWithoutLink.xml";
        Feed feed = rssParser.getFeed(RssParser.class.getResource(url).getPath());

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());

        assertTrue(feed.getItems().isEmpty());
    }

    @Test
    public void readFeedWithItemWithoutDescription() {
        String url = "../../../resources/test/xml/FeedItemWithoutDescription.xml";
        Feed feed = rssParser.getFeed(RssParser.class.getResource(url).getPath());

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
        String url = "../../../resources/test/xml/FeedItemWithoutDate.xml";
        Feed feed = rssParser.getFeed(RssParser.class.getResource(url).getPath());

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
        String url = "../../../resources/test/xml/FeedItemWithoutId.xml";
        Feed feed = rssParser.getFeed(RssParser.class.getResource(url).getPath());

        assertEquals("Test title", feed.getTitle());
        assertEquals("http://www.feed-link.com", feed.getLink());
        assertEquals("This is a feed description", feed.getDescription());

        assertTrue(feed.getItems().isEmpty());
    }

    @Test
    public void readFeedWithBrokenItems() {
        String url = "../../../resources/test/xml/FeedPartiallyBroken.xml";
        Feed feed = rssParser.getFeed(RssParser.class.getResource(url).getPath());

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
}
