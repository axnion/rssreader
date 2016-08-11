package system;

import app.App;
import rss.Feed;
import rss.Item;

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
 * This class is used to load and save user data in the RSSReader application.
 *
 * @author Axel Nilsson (axnion)
 */
class DatabaseAccessObjectSQLite {
    private String path;
    private Date lastSaved;

    /**
     * Constructor
     *
     * Sets path to the default temp.db and calls the init method.
     */
    DatabaseAccessObjectSQLite() {
        path = "temp.db";
        init();
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
        init();
    }

    /**
     * Initializes the object by trying to load the database at the currently set path. If the
     * loading of the database fails the stack trace is printed out and RuntimeException is thrown
     * to terminate the session.
     */
    private void init() {
        try {
            load();
        }
        catch(Exception expt) {
            expt.printStackTrace();
            throw new RuntimeException("Failed due to default database not loading");
        }
        lastSaved = new Date();
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
    ArrayList<FeedList> load() throws Exception {
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

        App.showMessage("Loaded " + path);
        System.out.println("Loaded " + path);

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
        if(path.equals("temp.db")) {
            File tempDatabase = new File("temp.db");
            tempDatabase.delete();
        }

        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();

        String createFeedListSaveDataTable = "CREATE TABLE IF NOT EXISTS save_data_feed_lists(" +
                "FEEDLISTNAME TEXT          PRIMARY KEY         UNIQUE      NOT NULL, " +
                "SORTING      VARCHAR(16)   DEFAULT DATE_DEC    NOT NULL, " +
                "SHOWVISITED  BOOLEAN       DEFAULT TRUE        NOT NULL);";

        String createFeedSaveDataTable = "CREATE TABLE IF NOT EXISTS save_data_feeds" +
                " (URLTOXML     TEXT    NOT NULL," +
                " FEEDLISTNAME  TEXT    NOT NULL);";

        statement.executeUpdate(createFeedListSaveDataTable);
        statement.executeUpdate(createFeedSaveDataTable);
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
     * @param feedList
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
     *
     * @param statement     A Statement object which is connected to a database which can be used to
     *                      send queries to the database and receive the result
     * @param feedList      
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

    void save() throws Exception {
        if(getLastSaved().after(Configuration.getLastUpdated())) {
            return;
        }

        Connection connection = savePrep();
        Statement statement = connection.createStatement();

        for(FeedList feedList : Configuration.getFeedLists()) {
            saveFeedLists(statement, feedList);
            saveFeeds(statement, feedList);
            saveItems(statement, feedList);
        }

        connection.commit();
        statement.close();
        connection.close();

        setLastSaved(new Date());
        App.showMessage("Saved to " + path);
        System.out.println("Saved to " + path);
    }

    private Connection savePrep() throws Exception {
        new File(path).delete();

        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        connection.setAutoCommit(false);

        Statement statement = connection.createStatement();

        String createFeedListSaveDataTable = "CREATE TABLE IF NOT EXISTS save_data_feed_lists(" +
                "FEEDLISTNAME TEXT          PRIMARY KEY         UNIQUE      NOT NULL, " +
                "SORTING      VARCHAR(16)   DEFAULT DATE_DEC    NOT NULL, " +
                "SHOWVISITED  BOOLEAN       DEFAULT TRUE        NOT NULL);";

        String createFeedSaveDataTable = "CREATE TABLE IF NOT EXISTS save_data_feeds" +
                " (URLTOXML     TEXT    NOT NULL," +
                " FEEDLISTNAME  TEXT    NOT NULL);";

        statement.executeUpdate(createFeedListSaveDataTable);
        statement.executeUpdate(createFeedSaveDataTable);
        statement.close();

        return connection;
    }

    private void saveFeedLists(Statement statement, FeedList feedList) throws Exception {
        String createFeedListTableQuery =  "CREATE TABLE IF NOT EXISTS " + feedList.getName() +
                " (ID           TEXT        PRIMARY KEY     UNIQUE     NOT NULL," +
                " VISITED       BOOLEAN     NOT NULL," +
                " STARRED       BOOLEAN     NOT NULL);";

        String addFeedListToSortTable = "INSERT INTO save_data_feed_lists " +
                "(FEEDLISTNAME, SORTING, SHOWVISITED) " +
                "VALUES ('" + feedList.getName() + "','" + feedList.getSortingRules() + "','" +
                feedList.getShowVisitedStatus() + "');";

        statement.executeUpdate(createFeedListTableQuery);
        statement.executeUpdate(addFeedListToSortTable);
    }

    private void saveFeeds(Statement statement, FeedList feedList) throws Exception {
        for(Feed feed : feedList.getFeeds()) {
            String addFeedQuery = "INSERT INTO save_data_feeds (URLTOXML,FEEDLISTNAME) " +
                    "VALUES ('" + feed.getUrlToXML() + "','" + feedList.getName() + "');";

            statement.executeUpdate(addFeedQuery);
        }
    }

    private void saveItems(Statement statement, FeedList feedList) throws Exception {
        for(Item item : feedList.getAllItems()) {
            String addItemQuery = "INSERT OR IGNORE INTO " + feedList.getName() +
                    " (ID,VISITED,STARRED) VALUES ('" + item.getId() + "','" +
                    item.isVisited() + "','" + item.isStarred() + "');";

            statement.executeUpdate(addItemQuery);
        }
    }

    /*
    ----------------------------------- ACCESSORS AND MUTATORS -------------------------------------
    */

    String getPath() {
        return path;
    }

    Date getLastSaved() {
        return lastSaved;
    }

    void setPath(String path) {
        this.path = path;
        setLastSaved(new Date(0));
    }

    void setLastSaved(Date lastSaved) {
        this.lastSaved = lastSaved;
    }
}
