package system;

import system.rss.Feed;
import system.rss.Item;

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;

/**
 * Class Mocks
 *
 * @author Axel Nilsson (axnion)
 */
class Mocks {
    static FeedList createFeedListMock(String name) {
        FeedList feedList = mock(FeedList.class);
        doReturn(name).when(feedList).getName();
        return feedList;
    }

    static FeedList createFeedListMock(String name, String sortingRules, boolean showVisitedStatus,
                                       ArrayList<Feed> feeds) {
        ArrayList<Item> items = new ArrayList<>();
        for(Feed feed : feeds) {
            items.addAll(feed.getItems());
        }

        FeedList feedList = mock(FeedList.class);
        doReturn(name).when(feedList).getName();
        doNothing().when(feedList).add(anyString());
        doNothing().when(feedList).remove(anyString());
        doReturn(feeds.size()).when(feedList).size();
        doReturn(true).when(feedList).update();
        doReturn(items).when(feedList).getAllItems();
        doReturn(feeds).when(feedList).getFeeds();
        doReturn(sortingRules).when(feedList).getSortingRules();
        doReturn(showVisitedStatus).when(feedList).getShowVisitedStatus();

        return feedList;
    }

    static ArrayList<FeedList> createFullConfiguration() {
        // Create Items
        ArrayList<Item> items1 = new ArrayList<>();
        items1.add(system.rss.Mocks.createItemMock("item1", "http://feed1.com/item1", "item1desc",
                "1451649600", "item1", false, false));
        items1.add(system.rss.Mocks.createItemMock("item2", "http://feed1.com/item2", "item2desc",
                "1451649600", "item2", false, true));

        ArrayList<Item> items2 = new ArrayList<>();
        items2.add(system.rss.Mocks.createItemMock("item3", "http://feed2.com/item3", "item3desc",
                "1451649600", "item3", true, true));
        items2.add(system.rss.Mocks.createItemMock("item4", "http://feed2.com/item4", "item4desc",
                "1451649600", "item4", true, false));

        ArrayList<Item> items3 = new ArrayList<>();
        items3.add(system.rss.Mocks.createItemMock("item5", "http://feed3.com/item5", "item5desc",
                "1451649600", "item5", true, false));
        items3.add(system.rss.Mocks.createItemMock("item6", "http://feed3.com/item6", "item6desc",
                "1451649600", "item6", true, true));

        ArrayList<Item> items4 = new ArrayList<>();
        items4.add(system.rss.Mocks.createItemMock("item7", "http://feed4.com/item7", "item7desc",
                "1451649600", "item7", false, true));
        items4.add(system.rss.Mocks.createItemMock("item8", "http://feed4.com/item8", "item8desc",
                "1451649600", "item8", false, false));

        // Create Feeds
        String path = DatabaseAccessObjectSQLite.class.getResource("../../../resources/test/db/").
                getPath();

        ArrayList<Feed> feeds1 = new ArrayList<>();
        Feed feed1 = system.rss.Mocks.createFeedMock("feed1", "http://feed1.com", "feed1desc",
                path + "feed1.xml", items1);
        Feed feed2 = system.rss.Mocks.createFeedMock("feed2", "http://feed2.com", "feed2desc",
                path + "feed2.xml", items2);
        feeds1.add(feed1);
        feeds1.add(feed2);

        ArrayList<Feed> feeds2 = new ArrayList<>();
        Feed feed3 = system.rss.Mocks.createFeedMock("feed3", "http://feed3.com", "feed3desc",
                path + "feed3.xml", items3);
        Feed feed4 = system.rss.Mocks.createFeedMock("feed4", "http://feed4.com", "feed4desc",
                path + "feed4.xml", items4);
        feeds2.add(feed3);
        feeds2.add(feed4);

        // Create FeedLists
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1", "DATE_DEC", true, feeds1));
        feedLists.add(Mocks.createFeedListMock("FeedList2", "TITLE_ASC", false, feeds2));

        return feedLists;
    }

    static DatabaseAccessObjectSQLite createDatabaseAccessObjectMock() {
        DatabaseAccessObjectSQLite dao = mock(DatabaseAccessObjectSQLite.class);

        try {
            doNothing().when(dao).setPath(anyString());
            doNothing().when(dao).save(any(), any(Date.class));
            doReturn(new ArrayList<FeedList>()).when(dao).load();
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        return dao;
    }
}
