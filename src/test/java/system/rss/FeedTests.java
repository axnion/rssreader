package system.rss;

import org.junit.Before;
import org.junit.Test;
import system.rss.exceptions.ItemDoesNotExist;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import java.util.ArrayList;

/**
 * Class FeedTests
 *
 * This is the test class for the Feed class.
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedTests {
    private Feed feed;
    private ArrayList<Item> items;

    /**
     * Test preparation. Creates a new Feed object and adds two mock Items to the Feed.
     */
    @Before
    public void createObject() {
        items = new ArrayList<>();
        items.add(Mocks.createItemMock("ItemTitle1", "http://link1.com", "Description1",
                "1451649600000", "item_id_1", false, false));
        items.add(Mocks.createItemMock("ItemTitle2", "http://link2.com", "Description2",
                "1451649600000", "item_id_2", false, false));

        feed = new Feed("FeedTitle", "http://www.link-to-feed-website.com", "Description",
                "https:link-to-feed-website.com/feed.xml", items);
    }

    /**
     * Name: Set visited on existing Item
     * Unit: setVisited(String, boolean)
     */
    @Test
    public void setVisitedOnExistingItem() {
        feed.setVisited("item_id_1", true);
        verify(items.get(0), times(1)).setVisited(true);
        verify(items.get(0), never()).setVisited(false);
        verify(items.get(1), never()).setVisited(anyBoolean());
    }

    /**
     * Name: Set visited on nonexistent Item
     * Unit: setVisited(String, boolean)
     */
    @Test(expected = ItemDoesNotExist.class)
    public void setVisitedOnNonexistantItem() {
        feed.setVisited("item_id_3", true);
        verify(items.get(0), never()).setVisited(anyBoolean());
        verify(items.get(1), never()).setVisited(anyBoolean());
    }

    /**
     * Name: Set starred on existing Item
     * Unit: setStarred(String, boolean)
     */
    @Test
    public void setStarredOnExistingItem() {
        feed.setStarred("item_id_1", true);
        verify(items.get(0), times(1)).setStarred(true);
        verify(items.get(0), never()).setStarred(false);
        verify(items.get(1), never()).setStarred(anyBoolean());
    }

    /**
     * Name: Set starred on nonexistent Item
     * Unit: setStarred(String, boolean)
     */
    @Test(expected = ItemDoesNotExist.class)
    public void setStarredOnNonexistantItem() {
        feed.setStarred("item_id_3", true);
        verify(items.get(0), never()).setStarred(anyBoolean());
        verify(items.get(1), never()).setStarred(anyBoolean());
    }

    /**
     * Name: Accessors and mutators
     * Unit: getTitle(), getLink(), getDescription(), getItems(), getUrlToXML(), setTitle(String),
     *       setLink(String), setDescription(String), setItems(ArrayList<Item>), setUrlToXML(String)
     *
     * Tests the accessor and mutator methods of the Feed class. Uses mutators to assign an object
     * and then uses accessors to return the objects and compare the returned objects to the
     * originals.
     */
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
                "1451649600000", "item_id_3", false, false));
        items.add(Mocks.createItemMock("ItemTitle4", "http://link4.com", "Description4",
                "1451649600000", "item_id_4", false, false));

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
