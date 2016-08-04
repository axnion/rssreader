package system;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Class DatabaseAccessObjectSQLiteTests
 *
 * @author Axel Nilsson (axnion)
 */
public class DatabaseAccessObjectSQLiteTests {
    private DatabaseAccessObjectSQLite dao;

    @Before
    public void createObject() {
        dao = new DatabaseAccessObjectSQLite();
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
}
