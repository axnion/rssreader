package system;

import org.apache.commons.io.FileUtils;
import rss.Feed;
import rss.Item;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Class DatabaseAccessObject
 *
 * @author Axel Nilsson (axnion)
 */
public class DatabaseAccessObject {
    private String path;

    DatabaseAccessObject() {
        path = "temp.db";
        try {
            load();
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }
    }

    DatabaseAccessObject(String path) {
        this.path = path;
        try {
            load();
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }
    }

    ArrayList<FeedList> load() throws Exception {
        Statement statement = loadPreparation();
        ArrayList<FeedList> feedLists = loadFeedLists(statement);
        loadFeeds(statement, feedLists);
        loadItems(statement, feedLists);

        return feedLists;
    }

    private Statement loadPreparation() throws Exception {
        if(path.equals("temp.db")) {
            File tempDatabase = new File("temp.db");
            tempDatabase.delete();
        }

        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        Statement statement = connection.createStatement();

        String createFeedListSaveDataTable = "CREATE TABLE IF NOT EXISTS save_data_feed_lists(" +
                "FEEDLISTNAME TEXT PRIMARY KEY UNIQUE NOT NULL, " +
                "SORTING VARCHAR(16)  DEFAULT DATE_DEC NOT NULL);";

        String createFeedSaveDataTable = "CREATE TABLE IF NOT EXISTS save_data_feeds" +
                " (URLTOXML     TEXT    NOT NULL," +
                " FEEDLISTNAME  TEXT    NOT NULL);";

        statement.executeUpdate(createFeedListSaveDataTable);
        statement.executeUpdate(createFeedSaveDataTable);

        return statement;
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

    private void loadFeeds(Statement statement, ArrayList<FeedList> feedLists) throws Exception {
        for(FeedList feedList : feedLists) {
            String query = "SELECT * FROM save_data_feeds WHERE FEEDLISTNAME='" +
                    feedList.getName() + "';";

            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                feedList.add(results.getString("urltoxml"));
            }
        }
    }

    private void loadItems(Statement statement, ArrayList<FeedList> feedLists) throws Exception  {
        String query;

        for(FeedList feedList : feedLists) {
            for(Item item : feedList.getAllItems()) {
                query = "SELECT * FROM '" + feedList.getName() + "' WHERE ID='" +
                        item.getId() + "';";

                ResultSet results = statement.executeQuery(query);
                results.next();
                item.setVisited(results.getBoolean("VISITED"));
                item.setStarred(results.getBoolean("STARRED"));
            }
        }
    }

    void save() {

    }

    void copy(String destination) throws Exception {
        FileUtils.copyFile(new File(path), new File(destination));
        path = destination;
    }

    String getPath() {
        return path;
    }

    void setPath(String newPath) {
        path = newPath;
    }
}
