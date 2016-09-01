package system;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Class DatabaseAccessObjectJSONTest
 *
 * @author Axel Nilsson (axnion)
 */
public class DatabaseAccessObjectJSONTest {
    private DatabaseAccessObjectJSON dao;
    private String resources = DatabaseAccessObjectSQLite.class
            .getResource("../../../resources/test/DatabaseAccessObjectJSONTestResources/")
            .getPath();
    private ArrayList<String> urlsToCreatedFiles = new ArrayList<>();


    @Before
    public void createObject() {
        dao = new DatabaseAccessObjectJSON();
        createExampleFile();
    }

    @After
    public void deleteCreatedFiles()  throws IOException {
        for(String url : urlsToCreatedFiles) {
            new File(url).delete();
        }

        urlsToCreatedFiles.clear();
    }

    @Test
    public void accessorsAndMutators() {
        assertEquals("temp.json", dao.getPath());
        assertEquals(new Date(0), dao.getLastSaved());

        dao.setPath("path/to/database");
        Date newDate = new Date();
        dao.setLastSaved(newDate);

        assertEquals("path/to/database", dao.getPath());
        assertEquals(newDate, dao.getLastSaved());
    }

    @Test
    public void saveConfigurationToDefault() {
        ArrayList<FeedList> feedLists = Mocks.createFullConfigurationWithoutExistingFeeds();

        try {
            dao.save(feedLists, new Date());
            urlsToCreatedFiles.add(dao.getPath());
        }
        catch(Exception expt) {
            fail();
        }

        try {
            assertTrue(FileUtils.contentEquals(new File(dao.getPath()),
                    new File(resources + "exampleSaveWithoutExistingFeed.json")));
        }
        catch(IOException expt) {
            fail();
        }
    }

    @Test
    public void saveConfigurationToOther() {
        ArrayList<FeedList> feedLists = Mocks.createFullConfigurationWithoutExistingFeeds();

        try {
            dao.setPath(resources + "testSave.json");
            dao.save(feedLists, new Date());
            urlsToCreatedFiles.add(dao.getPath());
        }
        catch(Exception expt) {
            fail();
        }

        try {
            assertTrue(FileUtils.contentEquals(new File(dao.getPath()),
                    new File(resources + "exampleSaveWithoutExistingFeed.json")));
        }
        catch(IOException expt) {
            fail();
        }
    }

    @Test
    public void loadConfiguration() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        try {
            dao.setPath(resources + "exampleSaveWithExistingFeed.json");
            feedLists = dao.load();
        }
        catch(Exception expt) {
            expt.printStackTrace();
            fail();
        }

        assertEquals(2, feedLists.size());
        assertEquals("FeedList1", feedLists.get(0).getName());
        assertEquals("DATE_DEC", feedLists.get(0).getSortingRules());
        assertTrue(feedLists.get(0).getShowVisitedStatus());

        assertEquals("FeedList2", feedLists.get(1).getName());
        assertEquals("TITLE_ASC", feedLists.get(1).getSortingRules());
        assertFalse(feedLists.get(1).getShowVisitedStatus());

        assertEquals(resources + "feed1.xml", feedLists.get(0).getFeeds().get(0).getUrlToXML());
        assertEquals(resources + "feed2.xml", feedLists.get(0).getFeeds().get(1).getUrlToXML());
        assertEquals(resources + "feed3.xml", feedLists.get(1).getFeeds().get(0).getUrlToXML());
        assertEquals(resources + "feed4.xml", feedLists.get(1).getFeeds().get(1).getUrlToXML());

        assertEquals("item1", feedLists.get(0).getFeeds().get(0).getItems().get(0).getId());
        assertEquals(false, feedLists.get(0).getFeeds().get(0).getItems().get(0).isStarred());
        assertEquals(false, feedLists.get(0).getFeeds().get(0).getItems().get(0).isVisited());

        assertEquals("item2", feedLists.get(0).getFeeds().get(0).getItems().get(1).getId());
        assertEquals(true, feedLists.get(0).getFeeds().get(0).getItems().get(1).isStarred());
        assertEquals(false, feedLists.get(0).getFeeds().get(0).getItems().get(1).isVisited());

        assertEquals("item3", feedLists.get(0).getFeeds().get(1).getItems().get(0).getId());
        assertEquals(true, feedLists.get(0).getFeeds().get(1).getItems().get(0).isStarred());
        assertEquals(true, feedLists.get(0).getFeeds().get(1).getItems().get(0).isVisited());

        assertEquals("item4", feedLists.get(0).getFeeds().get(1).getItems().get(1).getId());
        assertEquals(false, feedLists.get(0).getFeeds().get(1).getItems().get(1).isStarred());
        assertEquals(true, feedLists.get(0).getFeeds().get(1).getItems().get(1).isVisited());

        assertEquals("item5", feedLists.get(1).getFeeds().get(0).getItems().get(0).getId());
        assertEquals(false, feedLists.get(1).getFeeds().get(0).getItems().get(0).isStarred());
        assertEquals(true, feedLists.get(1).getFeeds().get(0).getItems().get(0).isVisited());

        assertEquals("item6", feedLists.get(1).getFeeds().get(0).getItems().get(1).getId());
        assertEquals(true, feedLists.get(1).getFeeds().get(0).getItems().get(1).isStarred());
        assertEquals(true, feedLists.get(1).getFeeds().get(0).getItems().get(1).isVisited());

        assertEquals("item7", feedLists.get(1).getFeeds().get(1).getItems().get(0).getId());
        assertEquals(true, feedLists.get(1).getFeeds().get(1).getItems().get(0).isStarred());
        assertEquals(false, feedLists.get(1).getFeeds().get(1).getItems().get(0).isVisited());

        assertEquals("item8", feedLists.get(1).getFeeds().get(1).getItems().get(1).getId());
        assertEquals(false, feedLists.get(1).getFeeds().get(1).getItems().get(1).isStarred());
        assertEquals(false, feedLists.get(1).getFeeds().get(1).getItems().get(1).isVisited());
    }

    @Test
    public void createExampleFile() {
        String content = "[ {\n" +
                "  \"sortingRules\" : \"DATE_DEC\",\n" +
                "  \"name\" : \"FeedList1\",\n" +
                "  \"feeds\" : [ {\n" +
                "    \"urlToXML\" : \"" + resources + "feed1.xml" + "\",\n" +
                "    \"items\" : [ {\n" +
                "      \"starred\" : false,\n" +
                "      \"visited\" : false,\n" +
                "      \"id\" : \"item1\"\n" +
                "    }, {\n" +
                "      \"starred\" : true,\n" +
                "      \"visited\" : false,\n" +
                "      \"id\" : \"item2\"\n" +
                "    } ]\n" +
                "  }, {\n" +
                "    \"urlToXML\" : \"" + resources + "feed2.xml" + "\",\n" +
                "    \"items\" : [ {\n" +
                "      \"starred\" : true,\n" +
                "      \"visited\" : true,\n" +
                "      \"id\" : \"item3\"\n" +
                "    }, {\n" +
                "      \"starred\" : false,\n" +
                "      \"visited\" : true,\n" +
                "      \"id\" : \"item4\"\n" +
                "    } ]\n" +
                "  } ],\n" +
                "  \"showVisitedStatus\" : true\n" +
                "}, {\n" +
                "  \"sortingRules\" : \"TITLE_ASC\",\n" +
                "  \"name\" : \"FeedList2\",\n" +
                "  \"feeds\" : [ {\n" +
                "    \"urlToXML\" : \"" + resources + "feed3.xml" + "\",\n" +
                "    \"items\" : [ {\n" +
                "      \"starred\" : false,\n" +
                "      \"visited\" : true,\n" +
                "      \"id\" : \"item5\"\n" +
                "    }, {\n" +
                "      \"starred\" : true,\n" +
                "      \"visited\" : true,\n" +
                "      \"id\" : \"item6\"\n" +
                "    } ]\n" +
                "  }, {\n" +
                "    \"urlToXML\" : \"" + resources + "feed4.xml" + "\",\n" +
                "    \"items\" : [ {\n" +
                "      \"starred\" : true,\n" +
                "      \"visited\" : false,\n" +
                "      \"id\" : \"item7\"\n" +
                "    }, {\n" +
                "      \"starred\" : false,\n" +
                "      \"visited\" : false,\n" +
                "      \"id\" : \"item8\"\n" +
                "    } ]\n" +
                "  } ],\n" +
                "  \"showVisitedStatus\" : false\n" +
                "} ]";

        try {
            PrintWriter writer = new PrintWriter(resources +
                    "exampleSaveWithExistingFeed.json", "UTF-8");
            writer.print(content);
            writer.close();
        }
        catch(IOException expt) {
            expt.printStackTrace();
        }

        urlsToCreatedFiles.add(dao.getPath());
    }
}
