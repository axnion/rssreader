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

    static FeedList createFeedListMock(String name, ArrayList<Feed> feeds) {
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

        return feedList;
    }

    static ArrayList<FeedList> createFullConfiguration() {
        // Create Items
        ArrayList<Item> items1 = new ArrayList<>();
        items1.add(system.rss.Mocks.createItemMock("item1", "http://feed1.com/item1", "item1desc",
                "0001", "item1"));
        items1.add(system.rss.Mocks.createItemMock("item2", "http://feed1.com/item2", "item2desc",
                "0001", "item2"));

        ArrayList<Item> items2 = new ArrayList<>();
        items1.add(system.rss.Mocks.createItemMock("item3", "http://feed2.com/item3", "item3desc",
                "0001", "item3"));
        items1.add(system.rss.Mocks.createItemMock("item4", "http://feed2.com/item4", "item4desc",
                "0001", "item4"));

        ArrayList<Item> items3 = new ArrayList<>();
        items1.add(system.rss.Mocks.createItemMock("item5", "http://feed3.com/item5", "item5desc",
                "0001", "item5"));
        items1.add(system.rss.Mocks.createItemMock("item6", "http://feed3.com/item6", "item6desc",
                "0001", "item6"));

        ArrayList<Item> items4 = new ArrayList<>();
        items1.add(system.rss.Mocks.createItemMock("item7", "http://feed4.com/item7", "item7desc",
                "0001", "item7"));
        items1.add(system.rss.Mocks.createItemMock("item8", "http://feed4.com/item8", "item8desc",
                "0001", "item8"));

        // Create Feeds
        ArrayList<Feed> feeds1 = new ArrayList<>();
        Feed feed1 = system.rss.Mocks.createFeedMock("feed1", "http://feed1.com", "feed1desc",
                "http://feed1.com/rss.xml", items1);
        Feed feed2 = system.rss.Mocks.createFeedMock("feed2", "http://feed2.com", "feed2desc",
                "http://feed2.com/rss.xml", items2);
        feeds1.add(feed1);
        feeds1.add(feed2);

        ArrayList<Feed> feeds2 = new ArrayList<>();
        Feed feed3 = system.rss.Mocks.createFeedMock("feed3", "http://feed3.com", "feed3desc",
                "http://feed3.com/rss.xml", items3);
        Feed feed4 = system.rss.Mocks.createFeedMock("feed4", "http://feed4.com", "feed4desc",
                "http://feed4.com/rss.xml", items4);
        feeds1.add(feed3);
        feeds1.add(feed4);

        // Create FeedLists
        ArrayList<FeedList> feedLists = new ArrayList<>();
        FeedList feedList1 = Mocks.createFeedListMock("FeedList1", feeds1);
        FeedList feedList2 = Mocks.createFeedListMock("FeedList1", feeds2);

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
