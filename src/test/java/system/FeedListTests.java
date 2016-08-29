package system;

import system.exceptions.FeedAlreadyExists;
import system.exceptions.FeedDoesNotExist;
import org.junit.Before;
import org.junit.Test;
import system.rss.*;
import system.rss.Mocks;

import system.rss.exceptions.NoXMLFileFound;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import java.util.ArrayList;

/**
 * Class FeedListTests
 *
 * This is the test class for the FeedList class.
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedListTests {
    private FeedList feedList;

    /**
     * Test preparations. Creates a new FeedList object and sets the RssParser in the FeedList
     * object to a mock.
     */
    @Before
    public void createObject() {
        feedList = new FeedList("MyFeedList", "DATE_DEC", true);
        feedList.setRssParser(Mocks.createRssParser());
    }

    /**
     * Name: Accessors and mutators
     * Unit: getName(), getSortingRules(), getShowVisitedStatus(), getRssParser(), getFeeds(),
     *       setName(String), setSortingRules(String), setShowVisitedStatus(boolean),
     *       setRssParser(RssParser), setFeeds(ArrayList<Feed>)
     *
     * Checks the values created by the constructor and then uses the mutator methods and then
     * checks the fields again to see if the mutators had the correct effect.
     */
    @Test
    public void accessorsAndMutators() {
        // Checking values set by constructor
        assertEquals("MyFeedList", feedList.getName());
        assertEquals("DATE_DEC", feedList.getSortingRules());
        assertEquals(true, feedList.getShowVisitedStatus());
        assertEquals(0, feedList.getFeeds().size());

        // Change the values using mutators
        ArrayList<Feed> feeds = new ArrayList<>();
        feeds.add(system.rss.Mocks.createFeedMock("FeedTitle1", "http://feed-website-1.com",
                "Description1", "http://feed-website-1.com/feed.xml", new ArrayList<>()));
        feeds.add(system.rss.Mocks.createFeedMock("FeedTitle2", "http://feed-website-2.com",
                "Description2", "http://feed-website-2.com/feed.xml", new ArrayList<>()));
        RssParser rssParser = Mocks.createRssParser();

        feedList.setName("MyNewFeedList");
        feedList.setSortingRules("TITLE_ASC");
        feedList.setShowVisitedStatus(false);
        feedList.setRssParser(rssParser);
        feedList.setFeeds(feeds);

        // Checking values set by mutators
        assertEquals("MyNewFeedList", feedList.getName());
        assertEquals("TITLE_ASC", feedList.getSortingRules());
        assertEquals(false, feedList.getShowVisitedStatus());
        assertEquals(rssParser, feedList.getRssParser());
        assertEquals(feeds, feedList.getFeeds());
    }


    @Test
    public void setShowVisitedStatus() {
        addFeedMocks(true);

        feedList.setShowVisitedStatus(false);

        ArrayList<Item> items = feedList.getAllItems();

        for(Item item : items) {
            verify(item, times(1)).setVisited(true);
        }
    }
    /**
     * Name: Get Feed within bounds
     * Unit: get(int)
     *
     * Tries to get a Feed from the FeedList using the get method where the index is within the
     * bounds of the FeedList.
     */
    @Test
    public void getFeedWithinBounds() {
        addFeedMocks(false);

        assertEquals("FeedTitle1", feedList.get(0).getTitle());
        assertEquals("http://feed-website-1.com/feed.xml", feedList.get(0).getUrlToXML());

        assertEquals("FeedTitle2", feedList.get(1).getTitle());
        assertEquals("http://feed-website-2.com/feed.xml", feedList.get(1).getUrlToXML());
    }

    /**
     * Name: Get Feed outside bounds
     * Unit: get(int)
     *
     * Tries to get a Feed from the FeedList using the get method where the index is outside the
     * bounds of the FeedList.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getFeedOutsideBounds() {
        addFeedMocks(false);
        feedList.get(3);
    }

    /**
     * Name: Get Feed from empty FeedList
     * Unit: get(int)
     *
     * Tries to get a Feed from a FeedList which does not contain any Feeds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getFeedFromEmptyFeedList() {
        feedList.get(0);
    }

    /**
     * Name: Add Feed to empty FeedList
     * Unit: add(String)
     *
     * Tries to add a Feed to an empty FeedList.
     */
    @Test
    public void addFeedToEmptyFeedList() {
        feedList.add("http://feed-website-1.com/feed.xml");

        assertEquals(1, feedList.size());
        assertEquals("FeedTitle1", feedList.get(0).getTitle());
        assertEquals("http://feed-website-1.com/feed.xml", feedList.get(0).getUrlToXML());
    }

    /**
     * Name: Add Feed to FeedList
     * Unit: add(String)
     *
     * Adds a Feed to a FeedList containing other Feeds but none are the same.
     */
    @Test
    public void addFeedToFeedList() {
        addFeedMocks(false);
        feedList.add("http://feed-website-3.com/feed.xml");

        assertEquals(3, feedList.size());
        assertEquals("FeedTitle3", feedList.get(2).getTitle());
        assertEquals("http://feed-website-3.com/feed.xml", feedList.get(2).getUrlToXML());
    }

    /**
     * Name: Add Feed to FeedList which already has same Feed
     * Unit: add(String)
     *
     * Adds a Feed to a FeedList which already contains a Feed with the same identifier.
     */
    @Test(expected = FeedAlreadyExists.class)
    public void addFeedToFeedListWhichAlreadyHasSameFeed() {
        addFeedMocks(false);
        feedList.add("http://feed-website-1.com/feed.xml");

        assertEquals(2, feedList.size());
        assertEquals("FeedTitle1", feedList.get(0).getTitle());
        assertEquals("http://feed-website-1.com/feed.xml", feedList.get(0).getUrlToXML());
        assertEquals("FeedTitle2", feedList.get(1).getTitle());
        assertEquals("http://feed-website-2.com/feed.xml", feedList.get(1).getUrlToXML());
    }

    /**
     * Name: Add Feed where Feed where not found
     * Unit: add(String)
     *
     * Adds a Feed to a FeedList, but the url to the xml does not lead to an xml file.
     */
    @Test(expected = NoXMLFileFound.class)
    public void addFeedWhereFeedWhereNotFound() {
        feedList.add("http://feed-website-4.com/feed.xml");
    }

    /**
     * Name: Remove existing Feed
     * Unit: remove(String)
     *
     * Tries to remove an existing Feed from FeedList.
     */
    @Test
    public void removeExistingFeed() {
        addFeedMocks(false);

        feedList.remove("http://feed-website-1.com/feed.xml");

        assertEquals(1, feedList.size());
        assertEquals("FeedTitle2", feedList.get(0).getTitle());
        assertEquals("http://feed-website-2.com/feed.xml", feedList.get(0).getUrlToXML());
    }

    /**
     * Name: Remove nonexistent Feed
     * Unit: remove(String)
     *
     * Tries to remove a nonexisten Feed from a FeedList.
     */
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

    /**
     * Name: Remove Feed from empty FeedList
     * Unit: remove(String)
     *
     * Tries to remove a Feed from an empty FeedList.
     */
    @Test(expected = FeedDoesNotExist.class)
    public void removeFeedFromEmptyFeedList() {
        feedList.remove("http://feed-website-1.com/feed.xml");
        assertEquals(0, feedList.size());
    }

    /**
     * Name: Update FeedList without Feeds
     * Unit: update()
     *
     * Tries to update a FeedList which does not contain any Feed objects.
     */
    @Test
    public void updateFeedListWithoutFeeds() {
        boolean updateStatus = feedList.update();
        assertFalse(updateStatus);
    }

    /**
     * Name: Update FeedList with nothing to update
     * Unit: update()
     *
     * Tires to update a FeedList where none of the Feeds have anything to update.
     */
    @Test
    public void updateFeedListWithNothingToUpdate() {
        ArrayList<Feed> feedMocks = addFeedMocks(false);
        RssParser rssParserMock = Mocks.createRssParser();

        doReturn(false).when(rssParserMock).updateFeed(feedMocks.get(0));
        doReturn(false).when(rssParserMock).updateFeed(feedMocks.get(1));

        feedList.setRssParser(rssParserMock);
        boolean updateStatus = feedList.update();

        assertFalse(updateStatus);
    }

    /**
     * Name: Update FeedList single update
     * Unit: update()
     *
     * Tries to update a FeedList where one Feed has something to update and another one does not.
     */
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

    /**
     * Name: Update FeedList multiple updates
     * Unit: update()
     *
     * Tries to update a FeedList where two Feeds has something to update.
     */
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

    /**
     * Name: Get all Items default sorting rules
     * Unit: getAllItems()
     *
     * Gets all Items from FeedList without setting the sortingRules so it's the default which is
     * the same as date descending.
     */
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

    /**
     * Name: Get all Items sorted by title ascending
     * Unit: setSortingRules(String), getAllItems()
     *
     * Get all Items after setting the sorting to title ascending.
     */
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

    /**
     * Name: Get all Items sorted by title descending
     * Unit: setSortingRules(String), getAllItems()
     *
     * Get all Items after setting the sorting to title descending.
     */
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

    /**
     * Name: Get all Items sorted by date ascending
     * Unit: setSortingRules(String), getAllItems()
     *
     * Get all Items after setting the sorting to date ascending.
     */
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

    /**
     * Name: Get all Items sorted by date descending
     * Unit: setSortingRules(String), getAllItems()
     *
     * Get all Items after setting the sorting to date descending.
     */
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

    /**
     * Name: Get all Items from empty FeedList
     * Unit: getAllItems()
     *
     * Tires to get all Items from an empty FeedList.
     */
    @Test
    public void getAllItemsFromEmptyFeedList() {
        ArrayList<Item> items = feedList.getAllItems();
        assertEquals(0, items.size());
    }

    /**
     * Name: Get all Items from empty Feeds in FeedList
     * Unit: getAllItems()
     *
     * Tries to get all Items from a FeedList where none of the Feeds have any Items.
     */
    @Test
    public void getAllItemsFromEmptyFeedsInFeedList() {
        addFeedMocks(false);
        ArrayList<Item> items = feedList.getAllItems();
        assertEquals(0, items.size());
    }

    /**
     * Name: Set visited on existing Feed
     * Unit: setVisited()
     *
     * Tries to set the visited status of an existing Feed.
     */
    @Test
    public void setVisitedOnExistingFeed() {
        addFeedMocks(true);
        feedList.setVisited("http://feed-website-1.com/feed.xml", "item_id_1", true);
        verify(feedList.getFeeds().get(0), times(1)).setVisited(eq("item_id_1"), eq(true));
        verify(feedList.getFeeds().get(0), never()).setVisited(eq("item_id_1"), eq(false));
        verify(feedList.getFeeds().get(1), never()).setVisited(eq("item_id_1"), anyBoolean());
    }

    /**
     * Name: Set visited on nonexistent Feed
     * Unit: setVisited()
     *
     * Tries to set the visited status of an nonexistent Feed.
     */
    @Test(expected = FeedDoesNotExist.class)
    public void setVisitedOnNonexistentFeed() {
        addFeedMocks(true);
        feedList.setVisited("http://feed-website-3.com/feed.xml", "item_id_3", true);
        verify(feedList.getFeeds().get(0), never()).setVisited(eq("item_id_1"), anyBoolean());
        verify(feedList.getFeeds().get(1), never()).setVisited(eq("item_id_1"), anyBoolean());
    }

    /**
     * Name: Set starred on existing Feed
     * Unit: setStarred()
     *
     * Tries to set the starred status of an existing Feed.
     */
    @Test
    public void setStarredOnExistingFeed() {
        addFeedMocks(true);
        feedList.setStarred("http://feed-website-1.com/feed.xml", "item_id_1", true);
        verify(feedList.getFeeds().get(0), times(1)).setStarred(eq("item_id_1"), eq(true));
        verify(feedList.getFeeds().get(0), never()).setStarred(eq("item_id_1"), eq(false));
        verify(feedList.getFeeds().get(1), never()).setStarred(eq("item_id_1"), anyBoolean());
    }

    /**
     * Name: Set starred on nonexistent Feed
     * Unit: setStarred()
     *
     * Tries to set the starred status of an nonexistent Feed.
     */
    @Test(expected = FeedDoesNotExist.class)
    public void setStarredOnNonexistentFeed() {
        addFeedMocks(true);
        feedList.setStarred("http://feed-website-3.com/feed.xml", "item_id_3", true);
        verify(feedList.getFeeds().get(0), never()).setStarred(eq("item_id_1"), anyBoolean());
        verify(feedList.getFeeds().get(1), never()).setStarred(eq("item_id_1"), anyBoolean());
    }

    /**
     * Adds Feed mocks to the FeedList
     *
     * @param addItems  True if the method should add Item mocks to the FeedList.
     * @return          An ArrayList of Feed mocks.
     */
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
