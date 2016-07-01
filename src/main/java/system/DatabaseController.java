package system;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.sql.*;

/**
 * Class DatabaseController
 *
 * @author Axel Nilsson (axnion)
 */
class DatabaseController {
    private static String path;

    DatabaseController() {
        path = "temp.db";
    }

    Connection connectToDatabase() throws Exception {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:" + path);
    }
    
    boolean getVisitedStatus(String feedListName, String itemId) throws Exception {
        Connection connection = connectToDatabase();
        String query = "SELECT VISITED FROM " + feedListName + " WHERE ID='" + itemId + "';";

        Boolean status = true;
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query);

        String resultString = result.getString("visited");

        if(resultString.toLowerCase().equals("true"))
            status = true;
        else
            status = false;

        result.close();
        statement.close();
        connection.close();

        return status;
    }

    boolean getStarredStatus(String feedListName, String itemId) throws Exception {
        Connection connection = connectToDatabase();
        String query = "SELECT STARRED FROM " + feedListName + " WHERE ID='" + itemId + "';";

        Boolean status;
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query);

        String resultString = result.getString("starred");

        if(resultString.toLowerCase().equals("false"))
            status = false;
        else
            status = true;

        result.close();
        statement.close();
        connection.close();

        return status;
    }

    String getSorting(String feedListName) throws Exception {
        Connection connection = connectToDatabase();
        String query = "SELECT SORTING FROM save_data_feed_lists WHERE FEEDLISTNAME = '" +
                feedListName + "';";

        String sorting;
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query);

        sorting = result.getString("sorting");

        result.close();
        statement.close();
        connection.close();

        return sorting;
    }

    void setVisitedStatus(String feedListName, String itemId, boolean status)
            throws Exception {
        Connection connection = connectToDatabase();

        String query = "UPDATE " + feedListName + " SET VISITED='" + status + "' WHERE ID='" +
                itemId + "';";

        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        connection.commit();

        statement.close();
        connection.close();
    }

    void setStarredStatus(String feedListName, String itemId, boolean status)
            throws Exception {
        Connection connection = connectToDatabase();

        String query = "UPDATE " + feedListName + " SET STARRED='" + status + "' WHERE ID='" +
                itemId + "';";

        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        connection.commit();

        statement.close();
        connection.close();
    }

    void setSorting(String feedListName, String sorting) throws Exception {
        Connection connection = connectToDatabase();
        String query = "UPDATE save_data_feed_lists SET SORTING='" + sorting +
                "' WHERE FEEDLISTNAME='" + feedListName + "';";

        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        connection.commit();

        statement.close();
        connection.close();
    }

    void addFeedList(String feedListName) throws Exception {
        Connection connection = connectToDatabase();

        String createFeedListTableQuery =  "CREATE TABLE " + feedListName +
                " (ID           TEXT        PRIMARY KEY     UNIQUE     NOT NULL," +
                " VISITED       BOOLEAN     NOT NULL," +
                " STARRED       BOOLEAN     NOT NULL);";

        String createFeedListSaveDataTable = "CREATE TABLE IF NOT EXISTS save_data_feed_lists" +
                " (FEEDLISTNAME     TEXT    PRIMARY KEY     UNIQUE       NOT NULL," +
                " SORTING VARCHAR(16));";

        String addFeddListToSortTable = "INSERT INTO save_data_feed_lists (FEEDLISTNAME,SORTING) " +
                "VALUES ('" + feedListName + "'," + "null" + ")";

        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();

        statement.executeUpdate(createFeedListTableQuery);
        statement.executeUpdate(createFeedListSaveDataTable);
        statement.executeUpdate(addFeddListToSortTable);

        connection.commit();
        statement.close();
        connection.close();
    }

    void removeFeedList(String feedListName) throws Exception {
        Connection connection = connectToDatabase();

        String dropFeedListTableQuery = "DROP TABLE " + feedListName + ";";

        String deleteFeedListSaveData = "DELETE FROM save_data_feed_lists WHERE FEEDLISTNAME='" +
                feedListName + "';";

        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();

        statement.executeUpdate(dropFeedListTableQuery);
        statement.executeUpdate(deleteFeedListSaveData);

        connection.commit();
        statement.close();
        connection.close();
    }

    void addFeed(String feedUrlToXml, String feedListName) throws Exception {
        Connection connection = connectToDatabase();

        String createFeedSaveDataTable = "CREATE TABLE IF NOT EXISTS save_data_feeds" +
                " (URLTOXML     TEXT    NOT NULL," +
                " FEEDLISTNAME  TEXT    NOT NULL);";

        String addFeedToSortTable = "INSERT INTO save_data_feeds (URLTOXML,FEEDLISTNAME) " +
                "VALUES ('" + feedUrlToXml + "','" + feedListName + "')";

        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();

        statement.executeUpdate(createFeedSaveDataTable);
        statement.executeUpdate(addFeedToSortTable);

        connection.commit();
        statement.close();
        connection.close();
    }

    void removeFeed(String feedUrlToXml, String feedListName) throws Exception {
        Connection connection = connectToDatabase();

        String deleteFeedSaveData = "DELETE FROM save_data_feeds WHERE URLTOXML='" + feedUrlToXml +
                "' AND FEEDLISTNAME='" + feedListName + "'";

        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();

        statement.executeUpdate(deleteFeedSaveData);

        connection.commit();
        statement.close();
        connection.close();
    }

    void addItem(String feedListName, String itemId, boolean visited) throws Exception {
        Connection connection = connectToDatabase();
        String query = "INSERT INTO " + feedListName + " (ID,VISITED,STARRED) " +
                "VALUES ('" + itemId + "','" + visited + "','false');";

        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        connection.commit();

        statement.close();
        connection.close();
    }

    void deleteItem(String feedListName, String itemId) throws Exception {
        Connection connection = connectToDatabase();
        String query = "DELETE FROM " + feedListName + " WHERE ID='" + itemId + "';";

        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        connection.commit();

        statement.close();
        connection.close();
    }

    void loadDatabase() throws Exception {
        if(path.equals("temp.db")) {
            File tempDatabase = new File("temp.db");
            tempDatabase.delete();
        }

        Connection connection = connectToDatabase();
        String getFeedLists = "SELECT * FROM save_data_feed_lists;";
        String getFeeds = "SELECT * FROM save_data_feeds;";
        Statement statement = connection.createStatement();

        String createFeedListSaveDataTable = "CREATE TABLE IF NOT EXISTS save_data_feed_lists" +
                " (FEEDLISTNAME     TEXT    PRIMARY KEY     UNIQUE       NOT NULL," +
                " SORTING VARCHAR(16));";

        String createFeedSaveDataTable = "CREATE TABLE IF NOT EXISTS save_data_feeds" +
                " (URLTOXML     TEXT    NOT NULL," +
                " FEEDLISTNAME  TEXT    NOT NULL);";

        statement.executeUpdate(createFeedListSaveDataTable);
        statement.executeUpdate(createFeedSaveDataTable);

        ResultSet results = statement.executeQuery(getFeedLists);
        while(results.next()) {
            String listName = results.getString("feedlistname");

            Configuration.addFeedListWithoutAddingToDatabase(listName);
        }

        results = statement.executeQuery(getFeeds);
        while(results.next()) {
            String listName = results.getString("feedlistname");
            String urlToXml = results.getString("urltoxml");

            Configuration.addFeedWithoutAddingToDatabase(listName, urlToXml);
        }
    }

    String getPath() {
        return path;
    }

    void setPath(String newPath) {
        path = newPath;
    }
}
