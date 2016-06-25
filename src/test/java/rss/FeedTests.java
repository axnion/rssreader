package rss;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Class FeedTests
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedTests {
    private Feed feed;

    @Before
    public void createObject() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(Mocks.createItemMock("ItemTitle1", "http://link1.com", "Description1",
                "Mon, 01 Jan 2016 12:00:00 +0000", "item_id_1"));
        items.add(Mocks.createItemMock("ItemTitle2", "http://link2.com", "Description2",
                "Mon, 01 Jan 2016 12:00:00 +0000", "item_id_2"));

        feed = new Feed("FeedTitle", "http://www.link-to-feed-website.com", "Description",
                "https:link-to-feed-website.com/feed.xml", items);
    }

    @Test
    public void accessorsAndMutators() {
        // Checking the values set by the constructor using accessors
        assertEquals("FeedTitle", feed.getTitle());
        assertEquals("http://www.link-to-feed-website.com", feed.getLink());
        assertEquals("Description", feed.getDescription());
        assertEquals("https:link-to-feed-website.com/feed.xml", feed.getUrlToXML());
        assertEquals("ItemTitle1", feed.getItems().get(0).getTitle());
        assertEquals("ItemTitle2", feed.getItems().get(1).getTitle());

        //Change the values using mutators
        ArrayList<Item> items = new ArrayList<>();
        items.add(Mocks.createItemMock("ItemTitle3", "http://link3.com", "Description3",
                "Mon, 01 Jan 2016 12:00:00 +0000", "item_id_3"));
        items.add(Mocks.createItemMock("ItemTitle4", "http://link4.com", "Description4",
                "Mon, 01 Jan 2016 12:00:00 +0000", "item_id_4"));

        feed.setTitle("NewFeedTitle");
        feed.setLink("http://www.link-to-new-feed-website.com");
        feed.setDescription("New description");
        feed.setUrlToXML("http://www.link-to-new-feed-website.com/feed.xml");
        feed.setItems(items);

        // Checking the values set by the mutators using accessors
        assertEquals("NewFeedTitle", feed.getTitle());
        assertEquals("http://www.link-to-new-feed-website.com", feed.getLink());
        assertEquals("New description", feed.getDescription());
        assertEquals("http://www.link-to-new-feed-website.com/feed.xml", feed.getUrlToXML());
        assertEquals("ItemTitle3", feed.getItems().get(0).getTitle());
        assertEquals("ItemTitle4", feed.getItems().get(1).getTitle());
    }
}
