package system;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import system.rss.Feed;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Class DatabaseAccessObjectSQLiteTests
 *
 * @author Axel Nilsson (axnion)
 */
public class DatabaseAccessObjectSQLiteTests {
    private DatabaseAccessObjectSQLite dao;
    private String path = "";

    @Before
    public void createObject() {
        dao = new DatabaseAccessObjectSQLite();
    }

    @After
    public void removeDatabase() {
        if(!path.equals("")) {
            // Remove database
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
    public void saveDefault() {
        ArrayList<FeedList> feedLists = Mocks.createFullConfiguration();

        try {
            dao.save(feedLists, new Date());
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }
    }

    @Test
    public void saveToOtherLocation() {
        ArrayList<FeedList> feedLists = Mocks.createFullConfiguration();
        String url = "../../../resources/test/db";
        path = DatabaseAccessObjectSQLite.class.getResource(url).getPath() + "saveDatabaseTest.db";

        try {
            dao.setPath(path);
            dao.save(feedLists, new Date());
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }
    }

}
