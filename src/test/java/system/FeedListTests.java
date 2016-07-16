package system;

import system.exceptions.FeedAlreadyExists;
import system.exceptions.FeedDoesNotExist;
import org.junit.Before;
import org.junit.Test;
import rss.*;
import rss.Mocks;
import rss.exceptions.NoXMLFileFound;

import static org.junit.Assert.*;

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
        feedList = new FeedList("MyFeedList", "DATA_DEC");
        feedList.setRssParser(Mocks.createRssParser());
    }

    @Test
    public void accessorsAndMutators() {
        // Checking values set by constructor
        assertEquals("MyFeedList", feedList.getName());

        // Change the values using mutators
        ArrayList<Feed> feeds = new ArrayList<>();
        feeds.add(rss.Mocks.createFeedMock("FeedTitle1", "http://feed-website-1.com",
                "Description1", "http://feed-website-1.com/feed.xml", new ArrayList<>()));
        feeds.add(rss.Mocks.createFeedMock("FeedTitle2", "http://feed-website-2.com",
                "Description2", "http://feed-website-2.com/feed.xml", new ArrayList<>()));

        RssParser rssParser = Mocks.createRssParser();

        feedList.setName("MyNewFeedList");
        feedList.setFeeds(feeds);
        feedList.setRssParser(rssParser);

        // Checking values set by mutators
        assertEquals("MyNewFeedList", feedList.getName());
        assertEquals(feeds, feedList.getFeeds());
        assertEquals(rssParser, feedList.getRssParser());
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
    public void clearFeedList() {
        addFeedMocks(false);

        assertEquals(2, feedList.size());
        feedList.clear();
        assertEquals(0, feedList.size());
    }

    @Test
    public void getAllItemsWithoutSorting() {
        addFeedMocks(true);

        ArrayList<Item> items = feedList.getAllItems();

        assertEquals("item-id-1", items.get(0).getId());
        assertEquals("item-id-2", items.get(1).getId());
        assertEquals("item-id-3", items.get(2).getId());
        assertEquals("item-id-4", items.get(3).getId());
        assertEquals("item-id-5", items.get(4).getId());
        assertEquals("item-id-6", items.get(5).getId());
        assertEquals("item-id-7", items.get(6).getId());
        assertEquals("item-id-8", items.get(7).getId());
    }

    @Test
    public void getAllItemsSortedByTitleAscending() {
        addFeedMocks(true);

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

        ArrayList<Item> items = feedList.getAllItems();

        assertEquals("Wen, 01 Jan 2015 12:00:00 +0000", items.get(0).getDate());
        assertEquals("Mon, 01 Jan 2016 11:00:00 +0000", items.get(1).getDate());
        assertEquals("Mon, 01 Jan 2016 12:00:00 +0000", items.get(2).getDate());
        assertEquals("Mon, 01 Jan 2016 12:00:00 +0000", items.get(3).getDate());
        assertEquals("Mon, 01 Jan 2016 12:30:00 +0000", items.get(4).getDate());
        assertEquals("Mon, 01 Jan 2016 12:30:01 +0000", items.get(5).getDate());
        assertEquals("Tis, 02 Jan 2016 12:00:00 +0000", items.get(6).getDate());
        assertEquals("Thu, 01 Feb 2016 12:00:00 +0000", items.get(7).getDate());
    }

    @Test
    public void getAllItemsSortedByDateDescending() {
        addFeedMocks(true);

        ArrayList<Item> items = feedList.getAllItems();

        assertEquals("Thu, 01 Feb 2016 12:00:00 +0000", items.get(0).getDate());
        assertEquals("Tis, 02 Jan 2016 12:00:00 +0000", items.get(1).getDate());
        assertEquals("Mon, 01 Jan 2016 12:30:01 +0000", items.get(2).getDate());
        assertEquals("Mon, 01 Jan 2016 12:30:00 +0000", items.get(3).getDate());
        assertEquals("Mon, 01 Jan 2016 12:00:00 +0000", items.get(4).getDate());
        assertEquals("Mon, 01 Jan 2016 12:00:00 +0000", items.get(5).getDate());
        assertEquals("Mon, 01 Jan 2016 11:00:00 +0000", items.get(6).getDate());
        assertEquals("Wen, 01 Jan 2015 12:00:00 +0000", items.get(7).getDate());
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

    private void addFeedMocks(boolean addItems) {
        ArrayList<Feed> feeds = new ArrayList<>();
        ArrayList<Item> items1 = new ArrayList<>();
        ArrayList<Item> items2 = new ArrayList<>();

        if(addItems) {
            items1.add(Mocks.createItemMock("Ambient", "http://feed-website.com/Item1",
                    "Description1", "Mon, 01 Jan 2016 12:00:00 +0000", "item-id-1"));
            items1.add(Mocks.createItemMock("Ceremony", "http://feed-website.com/Item2",
                    "Description2", "Tis, 02 Jan 2016 12:00:00 +0000", "item-id-2"));
            items1.add(Mocks.createItemMock("Alignment", "http://feed-website.com/Item3",
                    "Description3", "Wen, 01 Jan 2015 12:00:00 +0000", "item-id-3"));
            items1.add(Mocks.createItemMock("Paralysis", "http://feed-website.com/Item4",
                    "Description4", "Thu, 01 Feb 2016 12:00:00 +0000", "item-id-4"));

            items2.add(Mocks.createItemMock("Treason", "http://feed-website.com/Item5",
                    "Description5", "Mon, 01 Jan 2016 11:00:00 +0000", "item-id-5"));
            items2.add(Mocks.createItemMock("Homeland", "http://feed-website.com/Item6",
                    "Description6", "Mon, 01 Jan 2016 12:30:00 +0000", "item-id-6"));
            items2.add(Mocks.createItemMock("Evacuation", "http://feed-website.com/Item7",
                    "Description7", "Mon, 01 Jan 2016 12:00:00 +0000", "item-id-7"));
            items2.add(Mocks.createItemMock("Featherweight", "http://feed-website.com/Item8",
                    "Description8", "Mon, 01 Jan 2016 12:30:01 +0000", "item-id-8"));
        }

        feeds.add(rss.Mocks.createFeedMock("FeedTitle1", "http://feed-website-1.com",
                "Description1", "http://feed-website-1.com/feed.xml", items1));
        feeds.add(rss.Mocks.createFeedMock("FeedTitle2", "http://feed-website-2.com",
                "Description2", "http://feed-website-2.com/feed.xml", items2));

        feedList.setFeeds(feeds);
    }
}
