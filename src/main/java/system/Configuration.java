package system;

import system.rss.Feed;
import system.exceptions.FeedListAlreadyExists;
import system.exceptions.FeedListDoesNotExist;
import system.rss.Item;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class Configuration
 *
 * The Configuration is the central class in the backend system of RSSReader. It's completely static
 * to make the methods accessible from anywhere in the project.
 *
 * Configuration holds all FeedList objects used in the application which in turn holds all Feeds
 * and in turn all Items. All modifications done to the FeedList/Feed/Item objects are done though
 * here.
 *
 * Configuration also holds the DatabaseAccessObjectSQLite which is used to load and save user data to
 * a database.
 *
 * @author Axel Nilsson (axnion)
 */
public class Configuration {
    private static ArrayList<FeedList> feedLists = new ArrayList<>();
    private static DatabaseAccessObject dao = new DatabaseAccessObjectSQLite();
    private static Date lastUpdated = new Date();
    private static int updatePeriod = 5;
    private static int autoSavePeriod = 60;
    static ScheduledExecutorService executorService;

    /**
     * Creates and adds a new FeedList object with the name specified though the listName parameter.
     * Checks if a FeedList object already exists in feedLists by using the feedListExists method.
     *
     * If a FeedList with the name does not exists a new FeedList object is created and added to the
     * feedLists and lastUpdated is updated to current time. If a FeedList does already exists then
     * FeedListAlreadyExists exception is thrown.
     *
     * @param listName The name of the new FeedList object.
     */
    public static void addFeedList(String listName) {
        if(!feedListExists(listName)) {
            feedLists.add(new FeedList(listName, "DATE_DEC", true));
            lastUpdated = new Date();
        }
        else
            throw new FeedListAlreadyExists(listName);
    }

    /**
     * Removes a FeedList with the name specified tough the listName parameter. Checks if a
     * FeedList with the correct name exists in feedLists by using feedListExists method.
     *
     * If a FeedList with the correct name exists it is removed from feedList and lastUpdated is
     * updated to current time. If the FeedList does not exists FeedListDoesNotExist.
     *
     * @param listName The name of the FeedList to be removed.
     */
    public static void removeFeedList(String listName) {
        if(feedListExists(listName)) {
            feedLists.remove(getFeedListByName(listName));
            lastUpdated = new Date();
        }
        else
            throw new FeedListDoesNotExist(listName);
    }

    /**
     * Calls the add method of the correct FeedList in feedLists. Updates lastUpdated.
     *
     * @param url       The URL to the Feed to be added.
     * @param listName  The name of the FeedList we want to call add method on.
     */
    public static void addFeed(String url, String listName) {
        getFeedListByName(listName).add(url);
        lastUpdated = new Date();
    }

    /**
     * Calls the remove method of the correct FeedList in feedLists. Updates lastUpdated.
     *
     * @param url       The URL of the Feed to be removed.
     * @param listName  The name of the FeedList we want to call remove method on.
     */
    public static void removeFeed(String url, String listName) {
        getFeedListByName(listName).remove(url);
        lastUpdated = new Date();
    }

    /**
     * Starts a new Thread called UpdaterThread that checks for updates to the Feed objects. This
     * thread is run periodically and the wait is specified in updatePeriod field. The thread calls
     * the update method on each FeedList in feedLists and if any updates are found the lastUpdated
     * is updated to current time.
     */
    public static void startFeedUpdater() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Thread() {
            public void run() {
                setDaemon(true);
                Thread.currentThread().setName("UpdaterThread");
                boolean updated = false;
                for(FeedList feedList : feedLists) {
                    if(feedList.update())
                        updated = true;
                }

                if(updated) {
                    Configuration.setLastUpdated(new Date());
                    System.out.println("Update: Configuration");
                }
            }
        }, 0, updatePeriod, TimeUnit.SECONDS);
    }

    public static void stopFeedUpdater() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        executorService.shutdownNow();
    }

    /**
     * Takes a String and checks the ArrayList feedLists if any of the FeedList objects has a name
     * that is the same as the listName argument and returns that FeedList.
     *
     * @param listName  The name of the FeedList to be returned
     * @return          A FeedList object with the name that is the same as listName argument.
     */
    public static FeedList getFeedListByName(String listName) {
        for(FeedList list : feedLists) {
            if(list.getName().equals(listName))
                return list;
        }
        throw new FeedListDoesNotExist(listName);
    }

    /**
     * Returns all Item objects from every Feed in the FeedList with the name specified in listName.
     *
     * @param listName  A String containing the name of the FeedList we want all Items from.
     * @return          An ArrayList of all Item objects from the FeedList with a name same as
     *                  listName.
     */
    public static ArrayList<Item> getAllItemsFromFeedList(String listName) {
        return getFeedListByName(listName).getAllItems();
    }

    /**
     * Returns all Feed objects from the FeedList with a name equals to listName.
     *
     * @param listName  A String containing the name of the FeedList we want all Feeds from.
     * @return          An ArrayList of all Feed objects from the FeedList with a name equals to
     *                  listName.
     */
    public static ArrayList<Feed> getAllFeedsFromFeedList(String listName) {
        return getFeedListByName(listName).getFeeds();
    }

    /**
     * Calls the setSortingRules on the FeedList with a name specified though the listName
     * parameter.
     *
     * @param listName  The name of the FeedList setSortingRules will be called on.
     * @param sorting   The sorting rules that will be passed to the setSortingRules on the
     *                  FeedList.
     */
    public static void setSortingRules(String listName, String sorting) {
        getFeedListByName(listName).setSortingRules(sorting);
        setLastUpdated(new Date());
    }

    /**
     * Calls the setShowVisitedStatus on a Feedlist with a name corresponding to the value of
     * listName.,
     *
     * @param listName  The name of the Feedlist to be altered
     * @param status    The new show visited status of the FeedList
     */
    public static void setShowVisitedStatus(String listName, boolean status) {
        getFeedListByName(listName).setShowVisitedStatus(status);
        setLastUpdated(new Date());
    }

    /**
     * Finds the correct Item object using listName, feedIdentifier, and itemId. The setVisited
     * method is then called on the found Item object and passes the status as an argument.
     *
     * @param listName          Name of the FeedList where we can find the Item.
     * @param feedIdentifier    Identifier (URL) of the Feed object where the Item object is held.
     * @param itemId            The id of the Item object to be modified.
     * @param status            The new boolean value to be the new visited status of the Item.
     */
    public static void setVisited(String listName, String feedIdentifier, String itemId,
                                  boolean status) {
        getFeedListByName(listName).setVisited(feedIdentifier, itemId, status);
        setLastUpdated(new Date());
    }

    /**
     * Finds the correct Item object using listName, feedIdentifier, and itemId. The setStarred
     * method is then called on the found Item object and passes the status as an argument.
     *
     * @param listName          Name of the FeedList where we can find the Item.
     * @param feedIdentifier    Identifier (URL) of the Feed object where the Item object is held.
     * @param itemId            The id of the Item object to be modified.
     * @param status            The new boolean value to be the new starred status of the Item.
     */
    public static void setStarred(String listName, String feedIdentifier, String itemId,
                                  boolean status) {
        getFeedListByName(listName).setStarred(feedIdentifier, itemId, status);
        setLastUpdated(new Date());
    }

    /**
     * Iterates though feedLists and returns true if any of the FeedList objects has a name that is
     * equals to listName.
     *
     * @param listName  The name of the FeedList the method is looking for.
     * @return          True if a FeedList with the correct name is found, if not then false.
     */
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

    /**
     * Resets the Configuration by changing the path in DatabaseAccessObjectSQLite to default 'temp.db'
     * and loads the empty default database. Updates lastUpdated.
     *
     * @throws Exception Problems with database.
     */
    public static void reset() throws Exception {
        dao.setPath("temp.sqlite");
        feedLists = dao.load();
        lastUpdated = new Date();
    }

    /**
     * Calls the save method on DatabaseAccessObjectSQLite.
     *
     * @throws Exception Problems with database.
     */
    public static void save() throws Exception {
        dao.save(getFeedLists(), getLastUpdated());
    }

    /**
     * Changes the path in DatabaseAccessObjectSQLite and calls the save method.
     *
     * @param path A String containing the path we want the database to be saved.
     * @throws Exception Problems with database.
     */
    public static void save(String path) throws Exception {
        dao.setPath(path);
        dao.save(getFeedLists(), getLastUpdated());
    }

    /**
     * Changes the path in DatabaseAccessObjectSQLite and then calls the load method. Updates lastUpdated.
     *
     * @param path A String containing the path to the database to be loaded.
     * @throws Exception Problems with database.
     */
    public static void load(String path) throws Exception {
        dao.setPath(path);
        feedLists = dao.load();
        lastUpdated = new Date();
    }

    /*
    ----------------------------------- ACCESSORS AND MUTATORS -------------------------------------
    */

    /**
     * Access method for feedLists.
     *
     * @return An ArrayList of FeedList objects currently in Configuration.
     */
    public static ArrayList<FeedList> getFeedLists() {
        return feedLists;
    }

    /**
     * Access method for DatabaseAccessObjectSQLite.
     *
     * @return The DatabaseAccessObjectSQLite currently used in Configuration.
     */
    static DatabaseAccessObject getDao() {
        return dao;
    }

    /**
     * Access method for lastUpdated.
     *
     * @return The Date object currently in Configuration.
     */
    public static Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Access method for updatePeriod
     *
     * @return Number of seconds the program will currently wait between updates
     */
    public static int getUpdatePeriod() {
        return updatePeriod;
    }

    /**
     * Access method for autosavePeriod
     *
     * @return Number of seconds the program will currently wait between autosaves
     */
    public static int getAutoSavePeriod() {
        return autoSavePeriod;
    }

    /**
     * Mutator method for feedLists.
     *
     * @param newFeedLists An ArrayList of FeedList objects to be set as the new feedLists.
     */
    static void setFeedLists(ArrayList<FeedList> newFeedLists) {
        feedLists = newFeedLists;
    }

    /**
     * Mutator method for DatabaseAccessObjectSQLite.
     *
     * @param newDao A DatabaseAccessObjectSQLite to be set as the new dao.
     */
    static void setDao(DatabaseAccessObject newDao) {
        dao = newDao;
    }

    /**
     * Mutator method for lastUpdated.
     *
     * @param newDate A new Date object to be set as the new lastUpdated.
     */
    static void setLastUpdated(Date newDate) {
        lastUpdated = newDate;
    }

    /**database
     * Mutator method for updatePeriod
     *
     * @param seconds Number of seconds the program will wait between checking the feeds for updates
     */
    static void setUpdatePeriod(int seconds) {
        updatePeriod = seconds;
    }

    /**
     * Mutator method for autosavePeriod
     *
     * @param seconds Number of seconds the program will wait between autosaving.
     */
    static void setAutoSavePeriod(int seconds) {
        autoSavePeriod = seconds;
    }
}
