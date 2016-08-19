package system;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import system.rss.Feed;
import system.rss.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

/**
 * Class DatabaseAccessObjectSQLiteTests
 *
 * @author Axel Nilsson (axnion)
 */
public class DatabaseAccessObjectSQLiteTests {
    private DatabaseAccessObjectSQLite dao;
    private String resources = DatabaseAccessObjectSQLite.class
            .getResource("../../../resources/test/db/").getPath();
    private String path = "";

    @Before
    public void createObject() {
        dao = new DatabaseAccessObjectSQLite();
    }

    @After
    public void removeDatabase() {
        if(!path.equals("")) {
            File createdDatabase = new File(path);
            createdDatabase.delete();
            path = "";
        }
    }

    @Test
    public void createDAOWithoutPath() {
        dao = new DatabaseAccessObjectSQLite();
        assertEquals("temp.db", dao.getPath());
    }

    @Test
    public void createDAOWithPath() {
        dao = new DatabaseAccessObjectSQLite("path/to/database");
        assertEquals("path/to/database", dao.getPath());
    }

    @Test
    public void accessorsAndMutators() {
        assertEquals("temp.db", dao.getPath());
        dao.setPath("path/to/database");
        assertEquals("path/to/database", dao.getPath());
    }

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

    @Test
    public void loadDatabaseWithContent() {
        ArrayList<FeedList> feedLists = new ArrayList<>();

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

    @Test
    public void saveDefault() {
        ArrayList<FeedList> feedLists = Mocks.createFullConfiguration();

        try {
            dao.save(feedLists, new Date());
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        File createdDatabase = new File(dao.getPath());
        File compDatabase = new File(resources + "exampleDatabase.sqlite");

        try {
            assertTrue(FileUtils.contentEquals(createdDatabase, compDatabase));
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }
    }

    @Test
    public void saveToOtherLocation() {
        ArrayList<FeedList> feedLists = Mocks.createFullConfiguration();
        path = resources + "saveDatabaseCreated.sqlite";

        try {
            dao.setPath(path);
            dao.save(feedLists, new Date());
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        File createdDatabase = new File(path);
        File compDatabase = new File(resources + "exampleDatabase.sqlite");

        try {
            assertTrue(FileUtils.contentEquals(createdDatabase, compDatabase));
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }
    }

    @Test
    public void saveLastUpdateLaterThanConfiguration() {
        ArrayList<FeedList> feedLists = Mocks.createFullConfiguration();
        path = resources + "saveDatabaseCreated.sqlite";

        try {
            dao.setPath(path);
            dao.setLastSaved(new Date());
            dao.save(feedLists, new Date(0));
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        verify(feedLists.get(0), never()).getName();
        verify(feedLists.get(1), never()).getName();
    }
}
