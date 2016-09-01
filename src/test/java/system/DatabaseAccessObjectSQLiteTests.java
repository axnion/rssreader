package system;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import system.rss.Feed;
import system.rss.Item;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

/**
 * Class DatabaseAccessObjectSQLiteTests
 *
 * This is the test class for the DatabaseAccessObjectSQLite class.
 *
 * @author Axel Nilsson (axnion)
 */
public class DatabaseAccessObjectSQLiteTests {
    private DatabaseAccessObjectSQLite dao;
    private String resources = DatabaseAccessObjectSQLite.class
            .getResource("../../../resources/test/DatabaseAccessObjectSQLiteTestResources/")
            .getPath();
    private String path = "";

    /**
     * Test preparations. Creates a new DatabaseAccessObjectSQLite object and calls
     * createExampleDatabase method before every test.
     */
    @Before
    public void createObject() {
        dao = new DatabaseAccessObjectSQLite();
        createExampleDatabase();
    }

    /**
     * Test cleanup. Removes any databases created during a test.
     */
    @After
    public void removeDatabase() {
        File tempDatabase = new File("temp.sqlite");
        tempDatabase.delete();

        if(!path.equals("")) {
            File createdDatabase = new File(path);
            createdDatabase.delete();
            path = "";
        }
    }

    /**
     * Name: Create DAO without path
     * Unit: DatabaseAccessObjectSQLite()
     *
     * Creates a DatabaseAccessObjectSQLite object without giving constructor an argument. The path
     * of the object should be the default "temp.sqlite".
     */
    @Test
    public void createDAOWithoutPath() {
        dao = new DatabaseAccessObjectSQLite();
        assertEquals("temp.sqlite", dao.getPath());
    }

    /**
     * Name: Create DAO with path
     * Unit: DatabaseAccessObjectSQLite(String)
     *
     * Creates a DatabaseAccessObjectSQLite object while giving constructor an argument. The path of
     * the object should be set to the same as the argument.
     */
    @Test
    public void createDAOWithPath() {
        dao = new DatabaseAccessObjectSQLite("path/to/database");
        assertEquals("path/to/database", dao.getPath());
    }

    /**
     * Name: Accessors and mutators
     * Unit: getPath(), getLastSaved(), setPath(String), setLastSaved(Date)
     *
     * Checks the values created by the constructor and then uses the mutator methods and then
     * checks the fields again to see if the mutators had the correct effect.
     */
    @Test
    public void accessorsAndMutators() {
        assertEquals("temp.sqlite", dao.getPath());
        assertEquals(new Date(0), dao.getLastSaved());

        dao.setPath("path/to/database");
        Date newDate = new Date();
        dao.setLastSaved(newDate);

        assertEquals("path/to/database", dao.getPath());
        assertEquals(newDate, dao.getLastSaved());
    }

    /**
     * Name: Load default
     * Unit: load()
     *
     * Tries to load the default database which is empty. So the returned ArrayList should be empty.
     */
    @Test
    public void loadDefault() {
        try {
            ArrayList<FeedList> feedLists = dao.load();
            assertEquals(0, feedLists.size());
        }
        catch(Exception expt) {
            expt.printStackTrace();
            fail();
        }
    }

    /**
     * Name: Load database with content
     * Unit: load()
     *
     * Tries to load a database which does contain content. The test then goes though the created
     * objects and checks the values.
     */
    @Test
    public void loadDatabaseWithContent() {
        ArrayList<FeedList> feedLists = new ArrayList<>();

        System.out.println(resources + "exampleDatabase.sqlite");

        try {
            dao.setPath(resources + "exampleDatabase.sqlite");
            feedLists = dao.load();
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        assertEquals(2, feedLists.size());

        // Checking FeedLists
        FeedList feedList1 = feedLists.get(0);
        FeedList feedList2 = feedLists.get(1);
        assertEquals("FeedList1", feedList1.getName());
        assertEquals("FeedList2", feedList2.getName());

        // Checking Feeds
        Feed feed1 = feedList1.getFeeds().get(0);
        Feed feed2 = feedList1.getFeeds().get(1);
        Feed feed3 = feedList2.getFeeds().get(0);
        Feed feed4 = feedList2.getFeeds().get(1);
        assertEquals("feed1", feed1.getTitle());
        assertEquals("feed2", feed2.getTitle());
        assertEquals("feed3", feed3.getTitle());
        assertEquals("feed4", feed4.getTitle());

        // Checking Items
        Item item1 = feed1.getItems().get(0);
        Item item2 = feed1.getItems().get(1);
        Item item3 = feed2.getItems().get(0);
        Item item4 = feed2.getItems().get(1);
        Item item5 = feed3.getItems().get(0);
        Item item6 = feed3.getItems().get(1);
        Item item7 = feed4.getItems().get(0);
        Item item8 = feed4.getItems().get(1);

        assertEquals("item1", item1.getId());
        assertEquals("item2", item2.getId());
        assertEquals("item3", item3.getId());
        assertEquals("item4", item4.getId());
        assertEquals("item5", item5.getId());
        assertEquals("item6", item6.getId());
        assertEquals("item7", item7.getId());
        assertEquals("item8", item8.getId());


        // Checking Item which did not exist in the database but should be loaded from the xml
        Item item9 = feed4.getItems().get(2);

        assertEquals("item9", item9.getId());
        assertFalse(item9.isVisited());
        assertFalse(item9.isStarred());
    }

    /**
     * Name: Save default
     * Unit: save()
     *
     * Tries to save an ArrayList of FeedList objects to the default database. It then compares the
     * created database with the example database and the content has to match.
     */
    @Test
    public void saveDefault() {
        ArrayList<FeedList> feedLists = Mocks.createFullConfigurationWithExistingFeeds();

        try {
            dao.save(feedLists, new Date());
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        assertTrue(compareFiles(new File(path), new File(resources + "exampleDatabase.sqlite")));
    }

    /**
     * Name: Save to other location
     * Unit: save()
     *
     * Tries to save an ArrayList of FeedList objects to a database at path. It then compares the
     * created database with the example database and the content has to match.
     */
    @Test
    public void saveToOtherLocation() {
        ArrayList<FeedList> feedLists = Mocks.createFullConfigurationWithExistingFeeds();
        path = resources + "saveDatabaseCreated.sqlite";
        System.out.println(path);

        try {
            dao.setPath(path);
            dao.save(feedLists, new Date());
        }
        catch(Exception expt) {
            fail();
        }

        assertTrue(compareFiles(new File(path), new File(resources + "exampleDatabase.sqlite")));
    }

    /**
     * Name: Save last update later than Configuration
     * Unit: save()
     *
     * Tries to save a Configuration but the Configurations last updated stamp is earlier than the
     * DatabaseAccessObjectSQLite objects last saved stamp.
     */
    @Test
    public void saveLastUpdateLaterThanConfiguration() {
        ArrayList<FeedList> feedLists = Mocks.createFullConfigurationWithExistingFeeds();
        path = resources + "saveDatabaseCreated.sqlite";

        try {
            dao.setPath(path);
            dao.setLastSaved(new Date());
            dao.save(feedLists, new Date(0));
        }
        catch(Exception expt) {
            fail();
        }

        assertFalse(new File(path).exists());
        verify(feedLists.get(0), never()).getName();
        verify(feedLists.get(1), never()).getName();
    }

    /**
     * Creates the example database used in some tests. The reason for generating the database
     * instead of having a database saved is because the database will contain computer specific
     * paths.
     */
    private static void createExampleDatabase() {
        String resources = DatabaseAccessObjectSQLite.class
                .getResource("../../../resources/test/DatabaseAccessObjectSQLiteTestResources/")
                .getPath();

        try {
            String pathToDatabase = resources + "exampleDatabase.sqlite";
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + pathToDatabase);
            Statement statement = connection.createStatement();

            // Creating base tables
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

            // Adding FeedLists
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS FeedList1 " +
                            "(ID TEXT PRIMARY KEY UNIQUE NOT NULL, " +
                            "VISITED BOOLEAN NOT NULL, " +
                            "STARRED BOOLEAN NOT NULL);"
            );
            statement.executeUpdate(
                    "INSERT INTO save_data_feed_lists (FEEDLISTNAME, SORTING, SHOWVISITED) " +
                            "VALUES ('FeedList1','DATE_DEC','true');"
            );

            statement.executeUpdate(
                    "CREATE TABLE FeedList2 " +
                            "(ID TEXT PRIMARY KEY UNIQUE NOT NULL, " +
                            "VISITED BOOLEAN NOT NULL, " +
                            "STARRED BOOLEAN NOT NULL);"
            );
            statement.executeUpdate(
                    "INSERT INTO save_data_feed_lists (FEEDLISTNAME, SORTING, SHOWVISITED) " +
                            "VALUES ('FeedList2','TITLE_ASC','false');"
            );

            // Adding Feeds
            statement.executeUpdate(
                    "INSERT INTO save_data_feeds (URLTOXML,FEEDLISTNAME) " +
                            "VALUES ('" + resources + "feed1.xml','FeedList1');"
            );
            statement.executeUpdate(
                    "INSERT INTO save_data_feeds (URLTOXML,FEEDLISTNAME) " +
                            "VALUES ('" + resources + "feed2.xml','FeedList1');"
            );
            statement.executeUpdate(
                    "INSERT INTO save_data_feeds (URLTOXML,FEEDLISTNAME) " +
                            "VALUES ('" + resources + "feed3.xml','FeedList2');"
            );
            statement.executeUpdate(
                    "INSERT INTO save_data_feeds (URLTOXML,FEEDLISTNAME) " +
                            "VALUES ('" + resources + "feed4.xml','FeedList2');"
            );

            // Adding Items
            statement.executeUpdate(
                    "INSERT INTO FeedList1 (ID,VISITED,STARRED) " +
                            "VALUES ('item1','false','false');"
            );
            statement.executeUpdate(
                    "INSERT INTO FeedList1 (ID,VISITED,STARRED) " +
                            "VALUES ('item2','false','true');"
            );
            statement.executeUpdate(
                    "INSERT INTO FeedList1 (ID,VISITED,STARRED) " +
                            "VALUES ('item3','true','true');"
            );
            statement.executeUpdate(
                    "INSERT INTO FeedList1 (ID,VISITED,STARRED) " +
                            "VALUES ('item4','true','false');"
            );
            statement.executeUpdate(
                    "INSERT INTO FeedList2 (ID,VISITED,STARRED) " +
                            "VALUES ('item5','true','false');"
            );
            statement.executeUpdate(
                    "INSERT INTO FeedList2 (ID,VISITED,STARRED) " +
                            "VALUES ('item6','true','true');"
            );
            statement.executeUpdate(
                    "INSERT INTO FeedList2 (ID,VISITED,STARRED) " +
                            "VALUES ('item7','false','true');"
            );
            statement.executeUpdate(
                    "INSERT INTO FeedList2 (ID,VISITED,STARRED) " +
                            "VALUES ('item8','false','false');"
            );

            statement.close();
            connection.close();
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }
    }

    /**
     * Is used to compare two files.
     *
     * @param file1 The file we want to compare to file2
     * @param file2 The file we want to compare to file1
     * @return      True if file1 and file2 has the same content.
     */
    private boolean compareFiles(File file1, File file2) {
        try {
            Scanner createdDatabaseScanner = new Scanner(file1);
            Scanner compDatabaseScanner = new Scanner(file2);

            while(createdDatabaseScanner.hasNextLine()) {
                compDatabaseScanner.hasNextLine();

                if(!createdDatabaseScanner.nextLine().equals(compDatabaseScanner.nextLine())) {
                    return false;
                }
            }
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        return true;
    }
}
