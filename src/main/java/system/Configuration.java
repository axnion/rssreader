package system;

import rss.Feed;
import system.exceptions.FeedListAlreadyExists;
import system.exceptions.FeedListDoesNotExist;
import rss.Item;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class Configuration
 *
 * @author Axel Nilsson (axnion)
 */
public class Configuration {
    private static ArrayList<FeedList> feedLists = new ArrayList<>();
    private static DatabaseAccessObject dao = new DatabaseAccessObject();
    private static Date lastUpdated = new Date();

    public static void addFeedList(String listName) {
        if(!feedListExists(listName)) {
            try {
                feedLists.add(new FeedList(listName, "DATE_DEC"));
                lastUpdated = new Date();
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
                lastUpdated = new Date();
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
            lastUpdated = new Date();
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }
    }

    public static void removeFeed(String url, String listName) {
        try {
            getFeedListByName(listName).remove(url);
            lastUpdated = new Date();
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }
    }

    public static void startUpdater() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new UpdaterThread(), 0, 5, TimeUnit.SECONDS);
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
        getFeedListByName(listName).setSortingRules(sorting);
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
        lastUpdated = new Date();
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
        lastUpdated = new Date();
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

    public static Date getLastUpdated() {
        return lastUpdated;
    }

    public static void setLastUpdated(Date newDate) {
        lastUpdated = newDate;
    }
}
