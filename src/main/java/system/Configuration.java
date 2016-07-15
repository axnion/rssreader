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
    private static DatabaseController databaseController = new DatabaseController();

    public static void addFeed(String url, String listName) {
        try {
            addFeedToDatabase(url, listName);
            getFeedListByName(listName).add(url, true);
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }
    }

    public static void removeFeed(String url, String listName) {
        try {
            removeFeedFromDatabase(url, listName);
            getFeedListByName(listName).remove(url);
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }
    }

    public static void addFeedList(String listName) {
        if(!feedListExists(listName)) {
            try {
                addFeedListToDatabase(listName);
                feedLists.add(new FeedList(listName));
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
                removeFeedListFromDatabase(listName);
                feedLists.remove(getFeedListByName(listName));
            }
            catch(Exception expt) {
                expt.printStackTrace();
            }
        }
        else
            throw new FeedListDoesNotExist(listName);
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

    static void reset() {
        feedLists = new ArrayList<>();
        databaseController = new DatabaseController();
    }

    static void addFeedListWithoutAddingToDatabase(String listName) {
        if(!feedListExists(listName)) {
            feedLists.add(new FeedList(listName));
        }
    }

    static void addFeedWithoutAddingToDatabase(String listName, String url) {
        getFeedListByName(listName).add(url, false);
    }

    private  static boolean feedListExists(String listName) {
        for(FeedList list : feedLists) {
            if(list.getName().equals(listName))
                return true;
        }
        return false;
    }

    /*
    -------------------------------------- DATABASE ACCESS -----------------------------------------
    */

//    public static String getSorting(String listName) throws Exception {
//        return databaseController.getSorting(listName);
//    }
//
//    public static boolean isVisited(String listName, String itemId) throws Exception {
//        return databaseController.getVisitedStatus(listName, itemId);
//    }
//
//    public static boolean isStarred(String listName, String itemId) throws Exception {
//        return databaseController.getStarredStatus(listName, itemId);
//    }
//
//    public static void setSorting(String listName, String sorting) throws Exception {
//        databaseController.setSorting(listName, sorting);
//    }
//
//    public static void setVisited(String listName, String itemId, boolean status) throws Exception {
//        databaseController.setVisitedStatus(listName, itemId, status);
//    }
//
//    public static void setStarred(String listName, String itemId, boolean status) throws Exception {
//        databaseController.setStarredStatus(listName, itemId, status);
//    }

    public static void newDatabase() throws Exception {
        reset();
        databaseController.newDatabase();
    }

    public static void saveDatabase(String path) throws Exception {
        databaseController.saveDatabase(path);
    }

    public static void loadDatabase(String path) throws Exception {
        reset();
        databaseController.loadDatabase(path);
    }

    private static void addFeedListToDatabase(String listName) throws Exception {
        databaseController.addFeedList(listName);
    }

    private static void removeFeedListFromDatabase(String listName) throws Exception {
        databaseController.removeFeedList(listName);
    }

    private static void addFeedToDatabase(String url, String listName) throws Exception {
        databaseController.addFeed(url, listName);
    }

    private static void removeFeedFromDatabase(String url, String listName) throws Exception {
        databaseController.removeFeed(url, listName);
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

    static DatabaseController getDatabaseController() {
        return databaseController;
    }

    static void setDatabaseController(DatabaseController newDatabaseController) {
        databaseController = newDatabaseController;
    }
}
