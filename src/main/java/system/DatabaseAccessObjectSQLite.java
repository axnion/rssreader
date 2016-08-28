package system;

import system.rss.Feed;
import system.rss.Item;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class DatabaseAccessObjectSQLite
 *
 * This is the SQLite implementation of the DatabaseAccessObject interface. Is used to save and load
 * data between RSSReader and SQLite databases.
 *
 * @author Axel Nilsson (axnion)
 */
class DatabaseAccessObjectSQLite implements DatabaseAccessObject{
    private String path;
    private Date lastSaved;

    /**
     * Constructor
     *
     * Sets path to the default temp.sqlite and calls the init method.
     */
    DatabaseAccessObjectSQLite() {
        path = "temp.sqlite";
        lastSaved = new Date(0);
    }

    /**
     * Constructor
     *
     * Takes the argument passed though pathParam and assigns it's value to path and calls the init
     * method.
     *
     * @param pathParam The path to the database this object will load from and save to.
     */
    DatabaseAccessObjectSQLite(String pathParam) {
        path = pathParam;
        lastSaved = new Date(0);
    }

    /*
    ------------------------------------ LOAD FROM DATABASE ----------------------------------------
    */

    /**
     * Uses loadPrep, loadFeedLists, loadFeeds, loadItems methods to create an ArrayList of FeedList
     * objects using the data from the database. The database that will be loaded is specified in
     * the path field.
     *
     * @return              An ArrayList of FeedList objects created from data from the database.
     * @throws Exception    If there is any exceptions thrown by the SQLite driver this exception is
     *                      passed to the caller of this method.
     */
    public ArrayList<FeedList> load() throws Exception {
        Connection connection = loadPrep();
        Statement statement = connection.createStatement();

        ArrayList<FeedList> feedLists = loadFeedLists(statement);

        for(FeedList feedList : feedLists) {
            loadFeeds(statement, feedList);
            loadItems(statement, feedList);
        }

        connection.commit();
        statement.close();
        connection.close();

        return feedLists;
    }

    /**
     * loadPrep prepares the system for loading a database. It makes the connection with the
     * database which is returned at the end of the method. It also creates important tables if they
     * do not exist in the database.
     *
     * @return              A Connection object which is connected to a database at the location
     *                      stored in the path field.
     * @throws Exception    If there is any exceptions thrown by the SQLite driver this exception is
     *                      passed to the caller of this method.
     */
    private Connection loadPrep() throws Exception {
        if(path.equals("temp.sqlite")) {
            File tempDatabase = new File("temp.sqlite");
            tempDatabase.delete();
        }

        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();

        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS save_data_feed_lists " +
                        "(FEEDLISTNAME TEXT PRIMARY KEY UNIQUE NOT NULL, " +
                        "SORTING VARCHAR(16) DEFAULT DATE_DEC NOT NULL, " +
                        "SHOWVISITED BOOLEAN DEFAULT TRUE NOT NULL);"
        );
        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS save_data_feeds " +
                        "(URLTOXML TEXT NOT NULL, FEEDLISTNAME TEXT NOT NULL);"
        );
        statement.close();

        return connection;
    }

    /**
     * Get's all the saved information on every FeedList from the database. For each row in the
     * database the method creates a new FeedList object and puts it into an ArrayList. The
     * ArrayList is then returned.
     *
     * @param statement     A Statement object which is connected to a database which can be used to
     *                      send queries to the database and receive the result
     * @return              An ArrayList of FeedList objects created with information from the
     *                      database connected to the Statement object.
     * @throws Exception    If there is any exceptions thrown by the SQLite driver this exception is
     *                      passed to the caller of this method.
     */
    private ArrayList<FeedList> loadFeedLists(Statement statement) throws Exception {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        String query = "SELECT * FROM save_data_feed_lists;";

        ResultSet results = statement.executeQuery(query);
        while(results.next()) {
            feedLists.add(new FeedList(results.getString("feedlistname"),
                    results.getString("sorting"), results.getString("SHOWVISITED").equals("true")));
        }

        return feedLists;
    }

    /**
     * Finds all rows in the save_data-feeds table which has the name of the FeedList passed though
     * the feedList parameter. Then it goes though all of the rows and creates Feed objects and
     * calls the add method on feedList.
     *
     * @param statement     A Statement object which is connected to a database which can be used to
     *                      send queries to the database and receive the result
     * @param feedList      A FeedList object to which we want to add Feeds which is saved in the
     *                      database.
     * @throws Exception    If there is any exceptions thrown by the SQLite driver this exception is
     *                      passed to the caller of this method.
     */
    private void loadFeeds(Statement statement, FeedList feedList) throws Exception {
        String query = "SELECT * FROM save_data_feeds WHERE FEEDLISTNAME='" +
                feedList.getName() + "';";

        ResultSet results = statement.executeQuery(query);
        while(results.next()) {
            feedList.add(results.getString("urltoxml"));
        }
    }

    /**
     * Loads userdata about Items from the database. For each Item in a FeedList we get visited and
     * starred status of the Item. If no match is found the method sees the Item as new and sets
     * both statuses as false.
     *
     * @param statement     A Statement object which is connected to a database which can be used to
     *                      send queries to the database and receive the result
     * @param feedList      The FeedList which holds the Feeds which holds the Items which statuses
     *                      are to be altered.
     * @throws Exception    If there is any exceptions thrown by the SQLite driver this exception is
     *                      passed to the caller of this method.
     */
    private void loadItems(Statement statement, FeedList feedList) throws Exception  {
        String query;
        for(Item item : feedList.getAllItems()) {
            query = "SELECT * FROM '" + feedList.getName() + "' WHERE ID='" +
                    item.getId() + "';";

            ResultSet results = statement.executeQuery(query);
            results.next();

            if(!results.isClosed()) {
                item.setVisited(results.getString("visited").equals("true"));
                item.setStarred(results.getString("starred").equals("true"));
            }
            else {
                item.setVisited(false);
                item.setStarred(false);
            }
        }
    }

    /*
    ------------------------------------ SAVE TO DATABASE ------------------------------------------
    */

    /**
     * This is the save method called to save all user generated data from an ArrayList of FeedList
     * objects. It calls savePrep, saveFeedLists, saveFeeds, saveItems which togheter saves user
     * data to the database.
     *
     * @param feedLists                 An ArrayList of FeedLists from where the method will take
     *                                  data and insert it into a database or save file.
     * @param lastUpdateConfiguration   A Date object describing the last time the ArrayList was
     *                                  updated
     * @throws Exception                If there is any exceptions thrown by the SQLite driver this
     *                                  exception is passed to the caller of this method.
     */
    public void save(ArrayList<FeedList> feedLists, Date lastUpdateConfiguration) throws Exception {
        if(getLastSaved().after(lastUpdateConfiguration))
            return;

        Connection connection = savePrep();
        Statement statement = connection.createStatement();

        for(FeedList feedList : feedLists) {
            saveFeedLists(statement, feedList);
            saveFeeds(statement, feedList);
            saveItems(statement, feedList);
        }

        connection.commit();
        statement.close();
        connection.close();

        setLastSaved(new Date());
    }

    /**
     * Connects to and prepares the database for saving.
     *
     * @return              A Connection object with a connection to the database at path.
     * @throws Exception    If there is any exceptions thrown by the SQLite driver this exception is
     *                      passed to the caller of this method.
     */
    private Connection savePrep() throws Exception {
        new File(path).delete();

        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();

        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS save_data_feed_lists " +
                        "(FEEDLISTNAME TEXT PRIMARY KEY UNIQUE NOT NULL, " +
                        "SORTING VARCHAR(16) DEFAULT DATE_DEC NOT NULL, " +
                        "SHOWVISITED BOOLEAN DEFAULT TRUE NOT NULL);"
        );
        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS save_data_feeds " +
                        "(URLTOXML TEXT NOT NULL, FEEDLISTNAME TEXT NOT NULL);"
        );

        statement.close();
        return connection;
    }

    /**
     *
     * @param statement     A Statement object which is connected to a database which can be used to
     *                      send queries to the database and receive the result
     * @param feedList      A FeedList object containing the data to be saved to the database.
     * @throws Exception    If there is any exceptions thrown by the SQLite driver this exception is
     *                      passed to the caller of this method.
     */
    private void saveFeedLists(Statement statement, FeedList feedList) throws Exception {
        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " + feedList.getName() + " " +
                        "(ID TEXT PRIMARY KEY UNIQUE NOT NULL, " +
                        "VISITED BOOLEAN NOT NULL, " +
                        "STARRED BOOLEAN NOT NULL);"
        );
        statement.executeUpdate(
                "INSERT INTO save_data_feed_lists (FEEDLISTNAME, SORTING, SHOWVISITED) " +
                        "VALUES ('" + feedList.getName() + "','" + feedList.getSortingRules() +
                        "','" + feedList.getShowVisitedStatus() + "');"
        );
    }

    /**
     *
     * @param statement     A Statement object which is connected to a database which can be used to
     *                      send queries to the database and receive the result
     * @param feedList      A FeedList object containing the data to be saved to the database.
     * @throws Exception    If there is any exceptions thrown by the SQLite driver this exception is
     *                      passed to the caller of this method.
     */
    private void saveFeeds(Statement statement, FeedList feedList) throws Exception {
        for(Feed feed : feedList.getFeeds()) {
            statement.executeUpdate(
                    "INSERT INTO save_data_feeds (URLTOXML,FEEDLISTNAME) " +
                            "VALUES ('" + feed.getUrlToXML() + "','" + feedList.getName() + "');"
            );
        }
    }

    /**
     *
     * @param statement     A Statement object which is connected to a database which can be used to
     *                      send queries to the database and receive the result
     * @param feedList      A FeedList object containing the data to be saved to the database.
     * @throws Exception    If there is any exceptions thrown by the SQLite driver this exception is
     *                      passed to the caller of this method.
     */
    private void saveItems(Statement statement, FeedList feedList) throws Exception {
        for(Item item : feedList.getAllItems()) {
            statement.executeUpdate(
                    "INSERT INTO " + feedList.getName() + " (ID,VISITED,STARRED) " +
                            "VALUES ('" + item.getId() + "','" +
                            item.isVisited() + "','" + item.isStarred() + "');"
            );
        }
    }

    /*
    ----------------------------------- ACCESSORS AND MUTATORS -------------------------------------
    */

    /**
     * Accessor method for path.
     *
     * @return  A String containing the path currently used in this object.
     */
    public String getPath() {
        return path;
    }

    /**
     * Accessor method for lastSaved.
     *
     * @return  A Date object containing the last time this object saved to the current path.
     */
    Date getLastSaved() {
        return lastSaved;
    }

    /**
     * Mutator method for path.
     *
     * @param path  A String containing the new path to be set as to path.
     */
    public void setPath(String path) {
        this.path = path;
        setLastSaved(new Date(0));
    }

    /**
     * Mutator method for lastsaved.
     *
     * @param lastSavedParam    A Date object to be set as the last time this object last saved to
     *                          current path.
     */
    void setLastSaved(Date lastSavedParam) {
        lastSaved = lastSavedParam;
    }
}
