package system;

import org.junit.Before;
import org.junit.Test;
import system.rss.Feed;
import system.rss.RssParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Class DatabaseAccessObjectJSONTest
 *
 * @author Axel Nilsson (axnion)
 */
public class DatabaseAccessObjectJSONTest {
    DatabaseAccessObject dao;
    private String resources = DatabaseAccessObjectSQLite.class
            .getResource("../../../resources/test/DatabaseAccessObjectSQLiteTestResources/")
            .getPath();

    @Before
    public void createObject() {
        dao = new DatabaseAccessObjectJSON();
    }

    @Test
    public void saveConfiguration() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        RssParser rssParser = new RssParser();

        feedLists.add(new FeedList("FeedList1", "DATE_DEC", true));
        feedLists.add(new FeedList("FeedList2", "TITLE_ASC", true));

        feedLists.get(0).add(resources + "feed1.xml");
        feedLists.get(0).add(resources + "feed2.xml");
        feedLists.get(1).add(resources + "feed3.xml");
        feedLists.get(1).add(resources + "feed4.xml");

        try {
            dao.save(feedLists, new Date());
        }
        catch(Exception expt) {
            fail();
        }

        File file = new File(dao.getPath());
        assertTrue(file.exists());
    }
}
