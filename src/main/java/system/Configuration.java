package system;

import rss.Feed;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import system.exceptions.FeedListAlreadyExists;
import system.exceptions.FeedListDoesNotExist;
import rss.Item;

import java.util.ArrayList;

/**
 * Class Configuration
 *
 * @author Axel Nilsson (axnion)
 */
public class Configuration {
    private static ArrayList<FeedList> feedLists = new ArrayList<>();
    private static DatabaseAccessObject dao = new DatabaseAccessObject();

    public static void addFeedList(String listName) {
        if(!feedListExists(listName)) {
            try {
                feedLists.add(new FeedList(listName, "DATE_DEC"));
            }
            catch(Exception expt) {
                expt.printStackTrace();
            }
        }
        else
            throw new FeedListAlreadyExists(listName);
    }

    public static void removeFeedList(String listName) {
        if(feedListExists(listName)) {
            try {
                feedLists.remove(getFeedListByName(listName));
            }
            catch(Exception expt) {
                expt.printStackTrace();
            }
        }
        else
            throw new FeedListDoesNotExist(listName);
    }

    public static void addFeed(String url, String listName) {
        try {
            getFeedListByName(listName).add(url);
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }
    }

    public static void removeFeed(String url, String listName) {
        try {
            getFeedListByName(listName).remove(url);
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }
    }

    public static void update() {
        for(FeedList feedList : feedLists) {
            feedList.update();
        }
    }

    public static FeedList getFeedListByName(String listName) {
        for(FeedList list : feedLists) {
            if(list.getName().equals(listName))
                return list;
        }
        throw new FeedListDoesNotExist(listName);
    }

    public static ArrayList<Item> getAllItemsFromFeedList(String listName) {
        ArrayList<Item> items = new ArrayList<>();

        try {
            items = getFeedListByName(listName).getAllItems();
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        return items;
    }

    public static ArrayList<Feed> getAllFeedsFromFeedList(String listName) {
        return getFeedListByName(listName).getFeeds();
    }

    public static ArrayList<FeedList> getAllFeedLists() {
        return feedLists;
    }

    public static void setSorting(String listName, String sorting) {
        throw new NotImplementedException();
    }

    public static void setVisited(String listName, String feedIdentifier, String itemId,
                                  boolean status) {
        getFeedListByName(listName).getFeedByUrl(feedIdentifier).getItemById(itemId)
                .setVisited(status);
    }

    public static void setStarred(String listName, String feedIdentifier, String itemId,
                                  boolean status) {
        getFeedListByName(listName).getFeedByUrl(feedIdentifier).getItemById(itemId)
                .setStarred(status);
    }

    private  static boolean feedListExists(String listName) {
        for(FeedList list : feedLists) {
            if(list.getName().equals(listName))
                return true;
        }
        return false;
    }

    /*
    ----------------------------------- SAVING AND LOADING -----------------------------------------
    */

    public static void newDatabase() throws Exception {
        dao.setPath("temp.db");
        feedLists = dao.load();
    }

    public static void saveDatabase() throws Exception {
        dao.save(feedLists);
    }

    public static void saveDatabase(String path) throws Exception {
        dao.copy(path);
        dao.save(feedLists);
    }

    public static void loadDatabase(String path) throws Exception {
        dao.setPath(path);
        feedLists = dao.load();
    }

    /*
    ----------------------------------- ACCESSORS AND MUTATORS -------------------------------------
    */

    static ArrayList<FeedList> getFeedLists() {
        return feedLists;
    }

    static void setFeedLists(ArrayList<FeedList> newFeedLists) {
        feedLists = newFeedLists;
    }

    static DatabaseAccessObject getDao() {
        return dao;
    }

    static void setDao(DatabaseAccessObject newDao) {
        dao = newDao;
    }
}
