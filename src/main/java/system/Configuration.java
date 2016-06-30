package system;

import rss.Feed;
import system.exceptions.DatabaseError;
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
        addFeedToDatabase(url, listName);
        getFeedListByName(listName).add(url);
    }

    public static void removeFeed(String url, String listName) {
        removeFeedFromDatabase(url, listName);
        getFeedListByName(listName).remove(url);
    }

    public static void addFeedList(String listName) {
        if(!feedListExists(listName)) {
            addFeedListToDatabase(listName);
            feedLists.add(new FeedList(listName));
        }
        else
            throw new FeedListAlreadyExists(listName);
    }

    public static void removeFeedList(String listName) {
        if(feedListExists(listName)) {
            removeFeedListFromDatabase(listName);
            feedLists.remove(getFeedListByName(listName));
        }
        else
            throw new FeedListDoesNotExist(listName);
    }

    public static FeedList getFeedListByName(String listName) {
        for(FeedList list : feedLists) {
            if(list.getName().equals(listName))
                return list;
        }
        throw new FeedListDoesNotExist(listName);
    }

    public static ArrayList<Item> getAllItemsFromFeedList(String listName) {
        return getFeedListByName(listName).getAllItems(getSorting(listName));
    }

    public static ArrayList<Feed> getAllFeedsFromFeedList(String listName) {
        return getFeedListByName(listName).getFeeds();
    }

    public static void reset() {
        feedLists = new ArrayList<>();
        databaseController = new DatabaseController();
    }

    public  static boolean feedListExists(String listName) {
        for(FeedList list : feedLists) {
            if(list.getName().equals(listName))
                return true;
        }
        return false;
    }

    static void addFeedListWithoutAddingToDatabase(String listName) {
        if(!feedListExists(listName)) {
            feedLists.add(new FeedList(listName));
        }
        else
            throw new FeedListAlreadyExists(listName);
    }

    static void addFeedWithoutAddingToDatabase(String listName, String url) {
        getFeedListByName(listName).add(url);
    }

    /*
    -------------------------------------- DATABASE ACCESS -----------------------------------------
    */

    public static String getSorting(String listName) {
        try {
            return databaseController.getSorting(listName);
        }
        catch(Exception err) {
            throw new DatabaseError("Something went wrong when getting sorting information about " +
                    listName + ".");
        }
    }

    public static boolean isVisited(String listName, String itemId) {
        try {
            return databaseController.getVisitedStatus(listName, itemId);
        }
        catch(Exception err) {
            throw new DatabaseError("Something went wrong when getting visited status of " +
                    listName + "-> " + itemId + ".");
        }
    }

    public static boolean isStarred(String listName, String itemId) {
        try {
            return databaseController.getStarredStatus(listName, itemId);
        }
        catch(Exception err) {
            throw new DatabaseError("Something went wrong when getting starred status of " +
                    listName + "-> " + itemId + ".");
        }
    }

    public static void setSorting(String listName, String sorting) {
        try {
            databaseController.setSorting(listName, sorting);
        }
        catch(Exception err) {
            throw new DatabaseError("Something went wrong when modifying starred status of " +
                    listName + ".");
        }
    }

    public static void setVisited(String listName, String itemId, boolean status) {
        try {
            databaseController.setVisitedStatus(listName, itemId, status);
        }
        catch(Exception err) {
            throw new DatabaseError("Something went wrong when modifying visited status of " +
                    listName + "-> " + itemId + ".");
        }
    }

    public static void setStarred(String listName, String itemId, boolean status) {
        try {
            databaseController.setStarredStatus(listName, itemId, status);
        }
        catch(Exception err) {
            throw new DatabaseError("Something went wrong when modifying starred status of " +
                    listName + "-> " + itemId + ".");
        }
    }

    public static void loadDatabase() {
        try {
            databaseController.loadDatabase();
        }
        catch(Exception err) {
            throw new DatabaseError("Something went wrong when loading a database");
        }
    }

    private static void addFeedListToDatabase(String listName) {
        try {
            databaseController.addFeedList(listName);
        }
        catch(Exception err) {
            throw new DatabaseError("Something went wrong when trying to add a FeedList with" +
                    " the name \"" + listName + "\" to database");
        }
    }

    private static void removeFeedListFromDatabase(String listName) {
        try {
            databaseController.addFeedList(listName);
        }
        catch(Exception err) {
            throw new DatabaseError("Something went wrong when removing a FeedList with" +
                    " the name \"" + listName + "\" to database");
        }
    }

    private static void addFeedToDatabase(String url, String listName) {
        try {
            databaseController.addFeed(url, listName);
        }
        catch(Exception err) {
            throw new DatabaseError("Something went wrong when trying to add a Feed with the " +
                    "url \"" + url + "\" in FeedList \"" + listName + "\" to database");
        }
    }

    private static void removeFeedFromDatabase(String url, String listName) {
        try {
            databaseController.removeFeed(url, listName);
        }
        catch(Exception err) {
            throw new DatabaseError("Something went wrong when trying to remove a Feed with the " +
                    "url \"" + url + "\" in FeedList \"" + listName + "\" to database");
        }
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
