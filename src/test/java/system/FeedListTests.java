package system;

import system.exceptions.FeedAlreadyExists;
import system.exceptions.FeedDoesNotExist;
import org.junit.Before;
import org.junit.Test;
import system.rss.*;
import system.rss.Mocks;
import system.rss.exceptions.ItemDoesNotExist;
import system.rss.exceptions.NoXMLFileFound;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import java.util.ArrayList;

/**
 * Class FeedListTests
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedListTests {
    private FeedList feedList;

    @Before
    public void createObject() {
        feedList = new FeedList("MyFeedList", "DATE_DEC", true);
        feedList.setRssParser(Mocks.createRssParser());
    }

    @Test
    public void accessorsAndMutators() {
        // Checking values set by constructor
        assertEquals("MyFeedList", feedList.getName());
        assertEquals("DATE_DEC", feedList.getSortingRules());
        assertEquals(true, feedList.getShowVisitedStatus());

        // Change the values using mutators
        ArrayList<Feed> feeds = new ArrayList<>();
        feeds.add(system.rss.Mocks.createFeedMock("FeedTitle1", "http://feed-website-1.com",
                "Description1", "http://feed-website-1.com/feed.xml", new ArrayList<>()));
        feeds.add(system.rss.Mocks.createFeedMock("FeedTitle2", "http://feed-website-2.com",
                "Description2", "http://feed-website-2.com/feed.xml", new ArrayList<>()));

        RssParser rssParser = Mocks.createRssParser();

        feedList.setName("MyNewFeedList");
        feedList.setSortingRules("TITLE_ASC");
        feedList.setFeeds(feeds);
        feedList.setRssParser(rssParser);
        feedList.setShowVisitedStatus(false);

        // Checking values set by mutators
        assertEquals("MyNewFeedList", feedList.getName());
        assertEquals("TITLE_ASC", feedList.getSortingRules());
        assertEquals(feeds, feedList.getFeeds());
        assertEquals(rssParser, feedList.getRssParser());
        assertEquals(false, feedList.getShowVisitedStatus());
    }

    @Test
    public void getFeedWithinBounds() {
        addFeedMocks(false);

        assertEquals("FeedTitle1", feedList.get(0).getTitle());
        assertEquals("http://feed-website-1.com/feed.xml", feedList.get(0).getUrlToXML());

        assertEquals("FeedTitle2", feedList.get(1).getTitle());
        assertEquals("http://feed-website-2.com/feed.xml", feedList.get(1).getUrlToXML());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getFeedOutsideBounds() {
        addFeedMocks(false);
        feedList.get(3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getFeedFromEmptyFeedList() {
        feedList.get(0);
    }

    @Test
    public void addFeedToEmptyFeedList() {
        feedList.add("http://feed-website-1.com/feed.xml");

        assertEquals("FeedTitle1", feedList.get(0).getTitle());
        assertEquals("http://feed-website-1.com/feed.xml", feedList.get(0).getUrlToXML());
    }

    @Test
    public void addNonexistentFeed() {
        feedList.add("http://feed-website-3.com/feed.xml");

        assertEquals("FeedTitle3", feedList.get(0).getTitle());
        assertEquals("http://feed-website-3.com/feed.xml", feedList.get(0).getUrlToXML());
    }

    @Test(expected = FeedAlreadyExists.class)
    public void addExistingFeed() {
        addFeedMocks(false);
        feedList.add("http://feed-website-1.com/feed.xml");

        assertEquals(2, feedList.size());
        assertEquals("FeedTitle1", feedList.get(0).getTitle());
        assertEquals("http://feed-website-1.com/feed.xml", feedList.get(0).getUrlToXML());
        assertEquals("FeedTitle2", feedList.get(1).getTitle());
        assertEquals("http://feed-website-2.com/feed.xml", feedList.get(1).getUrlToXML());
    }

    @Test(expected = NoXMLFileFound.class)
    public void addFeedWhereFeedWhereNotFound() {
        feedList.add("http://feed-website-4.com/feed.xml");
    }

    @Test
    public void removeExistingFeed() {
        addFeedMocks(false);

        feedList.remove("http://feed-website-1.com/feed.xml");

        assertEquals(1, feedList.size());
        assertEquals("FeedTitle2", feedList.get(0).getTitle());
        assertEquals("http://feed-website-2.com/feed.xml", feedList.get(0).getUrlToXML());
    }

    @Test(expected = FeedDoesNotExist.class)
    public void removeNonexistentFeed() {
        addFeedMocks(false);

        feedList.remove("http://feed-website-3.com/feed.xml");

        assertEquals(2, feedList.size());
        assertEquals("FeedTitle1", feedList.get(0).getTitle());
        assertEquals("http://feed-website-1.com/feed.xml", feedList.get(0).getUrlToXML());
        assertEquals("FeedTitle2", feedList.get(1).getTitle());
        assertEquals("http://feed-website-2.com/feed.xml", feedList.get(1).getUrlToXML());
    }

    @Test(expected = FeedDoesNotExist.class)
    public void removeFeedFromEmptyFeedList() {
        feedList.remove("http://feed-website-1.com/feed.xml");
        assertEquals(0, feedList.size());
    }

    @Test
    public void updateFeedListNoFeeds() {
        boolean updateStatus = feedList.update();
        assertFalse(updateStatus);
    }

    @Test
    public void updateFeedListNoUpdate() {
        ArrayList<Feed> feedMocks = addFeedMocks(false);
        RssParser rssParserMock = Mocks.createRssParser();

        doReturn(false).when(rssParserMock).updateFeed(feedMocks.get(0));
        doReturn(false).when(rssParserMock).updateFeed(feedMocks.get(1));

        feedList.setRssParser(rssParserMock);
        boolean updateStatus = feedList.update();

        assertFalse(updateStatus);
    }

    @Test
    public void updateFeedListSingleUpdate() {
        ArrayList<Feed> feedMocks = addFeedMocks(false);
        RssParser rssParserMock = Mocks.createRssParser();

        doReturn(true).when(rssParserMock).updateFeed(feedMocks.get(0));
        doReturn(false).when(rssParserMock).updateFeed(feedMocks.get(1));

        feedList.setRssParser(rssParserMock);
        boolean updateStatus = feedList.update();

        assertTrue(updateStatus);
    }

    @Test
    public void updateFeedListMultipleUpdates() {
        ArrayList<Feed> feedMocks = addFeedMocks(false);
        RssParser rssParserMock = Mocks.createRssParser();

        doReturn(true).when(rssParserMock).updateFeed(feedMocks.get(0));
        doReturn(true).when(rssParserMock).updateFeed(feedMocks.get(1));

        feedList.setRssParser(rssParserMock);
        boolean updateStatus = feedList.update();

        assertTrue(updateStatus);
    }

    @Test
    public void getAllItemsDefaultSortingRules() {
        addFeedMocks(true);

        ArrayList<Item> items = feedList.getAllItems();

        assertEquals("1454328000000", Long.toString(items.get(0).getDate().getTime()));
        assertEquals("1451736000000", Long.toString(items.get(1).getDate().getTime()));
        assertEquals("1451651401000", Long.toString(items.get(2).getDate().getTime()));
        assertEquals("1451651400000", Long.toString(items.get(3).getDate().getTime()));
        assertEquals("1451649600000", Long.toString(items.get(4).getDate().getTime()));
        assertEquals("1451649600000", Long.toString(items.get(5).getDate().getTime()));
        assertEquals("1451646000000", Long.toString(items.get(6).getDate().getTime()));
        assertEquals("1420113600000", Long.toString(items.get(7).getDate().getTime()));
    }

    @Test
    public void getAllItemsSortedByTitleAscending() {
        addFeedMocks(true);

        feedList.setSortingRules("TITLE_ASC");
        ArrayList<Item> items = feedList.getAllItems();

        assertEquals("Alignment", items.get(0).getTitle());
        assertEquals("Ambient", items.get(1).getTitle());
        assertEquals("Ceremony", items.get(2).getTitle());
        assertEquals("Evacuation", items.get(3).getTitle());
        assertEquals("Featherweight", items.get(4).getTitle());
        assertEquals("Homeland", items.get(5).getTitle());
        assertEquals("Paralysis", items.get(6).getTitle());
        assertEquals("Treason", items.get(7).getTitle());
    }

    @Test
    public void getAllItemsSortedByTitleDescending() {
        addFeedMocks(true);

        feedList.setSortingRules("TITLE_DEC");
        ArrayList<Item> items = feedList.getAllItems();

        assertEquals("Treason", items.get(0).getTitle());
        assertEquals("Paralysis", items.get(1).getTitle());
        assertEquals("Homeland", items.get(2).getTitle());
        assertEquals("Featherweight", items.get(3).getTitle());
        assertEquals("Evacuation", items.get(4).getTitle());
        assertEquals("Ceremony", items.get(5).getTitle());
        assertEquals("Ambient", items.get(6).getTitle());
        assertEquals("Alignment", items.get(7).getTitle());
    }

    @Test
    public void getAllItemsSortedByDateAscending() {
        addFeedMocks(true);

        feedList.setSortingRules("DATE_ASC");
        ArrayList<Item> items = feedList.getAllItems();

        assertEquals("1420113600000", Long.toString(items.get(0).getDate().getTime()));
        assertEquals("1451646000000", Long.toString(items.get(1).getDate().getTime()));
        assertEquals("1451649600000", Long.toString(items.get(2).getDate().getTime()));
        assertEquals("1451649600000", Long.toString(items.get(3).getDate().getTime()));
        assertEquals("1451651400000", Long.toString(items.get(4).getDate().getTime()));
        assertEquals("1451651401000", Long.toString(items.get(5).getDate().getTime()));
        assertEquals("1451736000000", Long.toString(items.get(6).getDate().getTime()));
        assertEquals("1454328000000", Long.toString(items.get(7).getDate().getTime()));
    }

    @Test
    public void getAllItemsSortedByDateDescending() {
        addFeedMocks(true);

        feedList.setSortingRules("DATE_DEC");
        ArrayList<Item> items = feedList.getAllItems();

        assertEquals("1454328000000", Long.toString(items.get(0).getDate().getTime()));
        assertEquals("1451736000000", Long.toString(items.get(1).getDate().getTime()));
        assertEquals("1451651401000", Long.toString(items.get(2).getDate().getTime()));
        assertEquals("1451651400000", Long.toString(items.get(3).getDate().getTime()));
        assertEquals("1451649600000", Long.toString(items.get(4).getDate().getTime()));
        assertEquals("1451649600000", Long.toString(items.get(5).getDate().getTime()));
        assertEquals("1451646000000", Long.toString(items.get(6).getDate().getTime()));
        assertEquals("1420113600000", Long.toString(items.get(7).getDate().getTime()));
    }

    @Test
    public void getAllItemsFromEmptyFeedList() {
        ArrayList<Item> items = feedList.getAllItems();
        assertEquals(0, items.size());
    }

    @Test
    public void getAllItemsFromEmptyFeedsInFeedList() {
        addFeedMocks(false);
        ArrayList<Item> items = feedList.getAllItems();
        assertEquals(0, items.size());
    }

    @Test
    public void setVisitedOnExistingFeed() {
        addFeedMocks(true);
        feedList.setVisited("http://feed-website-1.com/feed.xml", "item_id_1", true);
        verify(feedList.getFeeds().get(0), times(1)).setVisited(eq("item_id_1"), eq(true));
        verify(feedList.getFeeds().get(0), never()).setVisited(eq("item_id_1"), eq(false));
        verify(feedList.getFeeds().get(1), never()).setVisited(eq("item_id_1"), anyBoolean());
    }

    @Test(expected = FeedDoesNotExist.class)
    public void setVisitedOnNonexistentFeed() {
        addFeedMocks(true);
        feedList.setVisited("http://feed-website-3.com/feed.xml", "item_id_3", true);
        verify(feedList.getFeeds().get(0), never()).setVisited(eq("item_id_1"), anyBoolean());
        verify(feedList.getFeeds().get(1), never()).setVisited(eq("item_id_1"), anyBoolean());
    }

    @Test
    public void setStarredOnExistingFeed() {
        addFeedMocks(true);
        feedList.setStarred("http://feed-website-1.com/feed.xml", "item_id_1", true);
        verify(feedList.getFeeds().get(0), times(1)).setStarred(eq("item_id_1"), eq(true));
        verify(feedList.getFeeds().get(0), never()).setStarred(eq("item_id_1"), eq(false));
        verify(feedList.getFeeds().get(1), never()).setStarred(eq("item_id_1"), anyBoolean());
    }

    @Test(expected = FeedDoesNotExist.class)
    public void setStarredOnNonexistentFeed() {
        addFeedMocks(true);
        feedList.setStarred("http://feed-website-3.com/feed.xml", "item_id_3", true);
        verify(feedList.getFeeds().get(0), never()).setStarred(eq("item_id_1"), anyBoolean());
        verify(feedList.getFeeds().get(1), never()).setStarred(eq("item_id_1"), anyBoolean());
    }

    private ArrayList<Feed> addFeedMocks(boolean addItems) {
        ArrayList<Feed> feeds = new ArrayList<>();
        ArrayList<Item> items1 = new ArrayList<>();
        ArrayList<Item> items2 = new ArrayList<>();

        if(addItems) {
            items1.add(Mocks.createItemMock("Ambient", "http://feed-website.com/Item1",
                    "Description1", "1451649600000", "item-id-1", false, false));
            items1.add(Mocks.createItemMock("Ceremony", "http://feed-website.com/Item2",
                    "Description2", "1451736000000", "item-id-2", false, false));
            items1.add(Mocks.createItemMock("Alignment", "http://feed-website.com/Item3",
                    "Description3", "1420113600000", "item-id-3", false, false));
            items1.add(Mocks.createItemMock("Paralysis", "http://feed-website.com/Item4",
                    "Description4", "1454328000000", "item-id-4", false, false));

            items2.add(Mocks.createItemMock("Treason", "http://feed-website.com/Item5",
                    "Description5", "1451646000000", "item-id-5", false, false));
            items2.add(Mocks.createItemMock("Homeland", "http://feed-website.com/Item6",
                    "Description6", "1451651400000", "item-id-6", false, false));
            items2.add(Mocks.createItemMock("Evacuation", "http://feed-website.com/Item7",
                    "Description7", "1451649600000", "item-id-7", false, false));
            items2.add(Mocks.createItemMock("Featherweight", "http://feed-website.com/Item8",
                    "Description8","1451651401000", "item-id-8", false, false));
        }

        feeds.add(system.rss.Mocks.createFeedMock("FeedTitle1", "http://feed-website-1.com",
                "Description1", "http://feed-website-1.com/feed.xml", items1));
        feeds.add(system.rss.Mocks.createFeedMock("FeedTitle2", "http://feed-website-2.com",
                "Description2", "http://feed-website-2.com/feed.xml", items2));

        feedList.setFeeds(feeds);
        return feeds;
    }
}
