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
 * Class DatabaseAccessObject
 *
 * @author Axel Nilsson (axnion)
 */
class DatabaseAccessObject {
    private String path;
    private Date lastSaved;

    DatabaseAccessObject() {
        path = "temp.db";
        init();
    }

    DatabaseAccessObject(String path) {
        this.path = path;
        init();
    }

    private void init() {
        try {
            load();
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }
        lastSaved = new Date();
    }

    /*
    ------------------------------------ LOAD FROM DATABASE ----------------------------------------
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

        return feedLists;
    }

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
                "FEEDLISTNAME TEXT PRIMARY KEY UNIQUE NOT NULL, " +
                "SORTING VARCHAR(16)  DEFAULT DATE_DEC NOT NULL);";

        String createFeedSaveDataTable = "CREATE TABLE IF NOT EXISTS save_data_feeds" +
                " (URLTOXML     TEXT    NOT NULL," +
                " FEEDLISTNAME  TEXT    NOT NULL);";

        statement.executeUpdate(createFeedListSaveDataTable);
        statement.executeUpdate(createFeedSaveDataTable);
        statement.close();

        return connection;
    }

    private ArrayList<FeedList> loadFeedLists(Statement statement) throws Exception {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        String query = "SELECT * FROM save_data_feed_lists;";

        ResultSet results = statement.executeQuery(query);
        while(results.next()) {
            feedLists.add(new FeedList(results.getString("feedlistname"),
                    results.getString("sorting")));
        }

        return feedLists;
    }

    private void loadFeeds(Statement statement, FeedList feedList) throws Exception {
        String query = "SELECT * FROM save_data_feeds WHERE FEEDLISTNAME='" +
                feedList.getName() + "';";

        ResultSet results = statement.executeQuery(query);
        while(results.next()) {
            feedList.add(results.getString("urltoxml"));
        }
    }

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
    }

    private Connection savePrep() throws Exception {
        new File(path).delete();

        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        connection.setAutoCommit(false);

        Statement statement = connection.createStatement();

        String createFeedListSaveDataTable = "CREATE TABLE IF NOT EXISTS save_data_feed_lists(" +
                "FEEDLISTNAME TEXT PRIMARY KEY UNIQUE NOT NULL, " +
                "SORTING VARCHAR(16)  DEFAULT DATE_DEC NOT NULL);";

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

        String addFeedListToSortTable = "INSERT INTO save_data_feed_lists (FEEDLISTNAME) " +
                "VALUES ('" + feedList.getName() + "')";

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
    }

    void setLastSaved(Date lastSaved) {
        this.lastSaved = lastSaved;
    }
}
