package system;

import system.exceptions.FeedListAlreadyExists;
import system.exceptions.FeedListDoesNotExist;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class ConfigurationTest
 *
 * @author Axel Nilsson (axnion)
 */
public class ConfigurationTests {
    @Before
    public void resetObject() {
        Configuration.setFeedLists(new ArrayList<>());
    }

    @Test
    public void createObject() {
        new Configuration();
    }

    /**
     * Name: Accessors and Mutators
     * Unit: getFeedLists(), setFeedLists()
     *
     * Tests the accessor and mutator methods of the Configuration class. Uses mutators to assign
     * an object and then uses accessors to return the objects and compare the returned objects to
     * the originals.
     */
    @Test
    public void accessorsAndMutators() {
        assertNotNull(Configuration.getFeedLists());
        assertNotNull(Configuration.getDao());
        assertEquals(0, Configuration.getFeedLists().size());
        assertEquals(5, Configuration.getUpdatePeriod());

        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));
        Configuration.setFeedLists(feedLists);

        DatabaseAccessObjectSQLite dao = Mocks.createDatabaseAccessObjectMock();
        Configuration.setDao(dao);

        Configuration.setUpdatePeriod(10);

        Date currentDate = new Date();
        Configuration.setLastUpdated(currentDate);

        assertEquals("FeedList1", Configuration.getFeedLists().get(0).getName());
        assertEquals("FeedList2", Configuration.getFeedLists().get(1).getName());
        assertEquals(dao, Configuration.getDao());
        assertEquals(currentDate, Configuration.getLastUpdated());
    }

    /**
     * Name: Add FeedList to empty Configuration
     * Unit: addFeedList()
     *
     * Adds a FeedList to a Configuration not containing any other FeedLists.
     */
    @Test
    public void addFeedListToEmptyConfiguration() {
        Configuration.addFeedList("FeedList1");
        assertEquals(1, Configuration.getFeedLists().size());
    }

    /**
     * Name: Add FeedList to Configuration with existing FeedList
     * Unit: addFeedList()
     *
     * Adds a FeedList to a Configuration that already contains other FeedLists.
     */
    @Test
    public void addFeedListToConfigurationWithExistingFeedLists() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));
        Configuration.setFeedLists(feedLists);

        Configuration.addFeedList("FeedList3");

        assertEquals(3, Configuration.getFeedLists().size());
    }

    /**
     * Name: Add identical FeedList to Configuration
     * Unit: addFeedList()
     *
     * Adds a FeedList to a Configuration that already contains a FeedList with the same identifier.
     */
    @Test(expected = FeedListAlreadyExists.class)
    public void addIdenticalFeedListToConfiguration() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));
        Configuration.setFeedLists(feedLists);

        Configuration.addFeedList("FeedList1");

        assertEquals(2, Configuration.getFeedLists().size());
    }

    /**
     * Name: Remove existing FeedList
     * Unit: removeFeedList()
     *
     * Tries to remove a FeedList that does exist in the Configuration.
     */
    @Test
    public void removeExistingFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));
        Configuration.setFeedLists(feedLists);

        Configuration.removeFeedList("FeedList1");

        assertEquals(1, Configuration.getFeedLists().size());
        assertEquals("FeedList2", Configuration.getFeedLists().get(0).getName());
    }

    /**
     * Name: Remove nonexistent FeedList
     * Unit: removeFeedList()
     *
     * Tries to remove a FeedList from the Configuration that does not exist. Should throw
     * FeedListDoesNotExist exception and none of the existing feeds should ge removed.
     */
    @Test(expected = FeedListDoesNotExist.class)
    public void removeNonexistingFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));
        Configuration.setFeedLists(feedLists);

        Configuration.removeFeedList("FeedList3");

        assertEquals(2, Configuration.getFeedLists().size());
        assertEquals("FeedList1", Configuration.getFeedLists().get(0).getName());
        assertEquals("FeedList2", Configuration.getFeedLists().get(1).getName());
    }

    /**
     * Name: Remove FeedList from empty Configuration
     * Unit: removeFeedList()
     *
     * Tries to remove a FeedList from a Configuration without any FeedLists. Should throw a
     * FeedListDoesNotExist exception.
     */
    @Test(expected = FeedListDoesNotExist.class)
    public void removeFeedListFromEmptyConfiguration() {
        Configuration.removeFeedList("FeedList1");
    }

    /**
     * Name: Add Feed to FeedList
     * Unit: addFeed()
     *
     * Tests to add a Feed to an existing FeedList. Checks so the add method is called on the
     * correct FeedList.
     */
    @Test
    public void addFeedToFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));
        Configuration.setFeedLists(feedLists);

        String url = ConfigurationTests.class
                .getResource("../../../resources/test/xml/exampleFeed1.xml").getPath();

        Configuration.addFeed(url, "FeedList1");

        verify(feedLists.get(0), times(1)).add(any());
        verify(feedLists.get(1), never()).add(any());
    }

    /**
     * Name: Add Feed to nonexistent FeedList
     * Unit: addFeed()
     *
     * Tests trying to add a Feed to a FeedList that does not exist in the Configuration. A
     * FeedListDoesNotExist exception should be thrown and add method should never be called on
     * any of the FeedLists
     */
    @Test(expected = FeedListDoesNotExist.class)
    public void addFeedToNonexistentFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));
        Configuration.setFeedLists(feedLists);

        String url = ConfigurationTests.class
                .getResource("../../../resources/test/xml/exampleFeed1.xml").getPath();

        Configuration.addFeed(url, "FeedList3");

        verify(feedLists.get(0), never()).add(any());
        verify(feedLists.get(1), never()).add(any());
    }

    /**
     * Name: Remove Feed from FeedList
     * Unit: removeFeed()
     *
     * Tries to remove an existing Feed from existing FeedList.
     */
    @Test
    public void removeFeedFromFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));
        Configuration.setFeedLists(feedLists);

        String url = ConfigurationTests.class
                .getResource("../../../resources/test/xml/exampleFeed1.xml").getPath();

        Configuration.removeFeed(url, "FeedList1");

        verify(feedLists.get(0), times(1)).remove(any());
        verify(feedLists.get(1), never()).remove(any());
    }

    /**
     * Name: Remove Feed from nonexistent FeedList
     * Unit: removeFeed()
     *
     * Tries to remove a Feed From a FeedList that does not exist. Should throw a
     * FeedListDoesNotExist exception.
     */
    @Test(expected = FeedListDoesNotExist.class)
    public void removeFeedFromNonexistentFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));
        Configuration.setFeedLists(feedLists);

        String url = ConfigurationTests.class
                .getResource("../../../resources/test/xml/exampleFeed1.xml").getPath();

        Configuration.addFeed(url, "FeedList3");

        verify(feedLists.get(0), never()).remove(any());
        verify(feedLists.get(1), never()).remove(any());
    }


    @Test
    public void updaterTest() {

    }


    /**
     * Name: Get existing FeedList by name
     * Unit: getFeedListByName()
     *
     * Tries to get an existing FeedList from the Configuration by name.
     */
    @Test
    public void getExistingFeedListByName() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));
        Configuration.setFeedLists(feedLists);

        Configuration.getFeedListByName("FeedList1");
    }

    /**
     * Name: Get nonexistent FeedList by name
     * Unit: getFeedListByName()
     *
     * Tries to get a FeedList that does not exist in the Configuration by name. Should result in a
     * FeedListDoesNotExist exception being thrown.
     */
    @Test(expected = FeedListDoesNotExist.class)
    public void getNonexistentFeedListByName() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));
        Configuration.setFeedLists(feedLists);

        Configuration.getFeedListByName("FeedList3");
    }

    /**
     * Name: Get FeedList by name from empty Configuration
     * Unit: getFeedListByName()
     *
     * Tries to get a FeedList from an empty Configuration by name. Should result in
     * FeedListDoesNotExist being thrown.
     */
    @Test(expected = FeedListDoesNotExist.class)
    public void getFeedListByNameFromEmptyConfiguration() {
        Configuration.getFeedListByName("FeedList3");
    }

    /**
     * Name: Get all Items from existing FeedList
     * Unit: getAllItemsFromFeedList()
     *
     * Tries to get all Item objects from a specific existing FeedList. Should only call getAllItems
     * on the correct FeedList.
     */
    @Test
    public void getAllItemsFromExistingFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));

        Configuration.setFeedLists(feedLists);
        Configuration.getAllItemsFromFeedList("FeedList1");

        verify(feedLists.get(0), times(1)).getAllItems();
        verify(feedLists.get(1), never()).getAllItems();
    }

    /**
     * Name: Get all Items from nonexistent FeedList
     * Unit: getAllItemsFromFeedList()
     *
     * Tries to get all Item objects from a specific FeedList which does not exist in the
     * Configuration. getAllItems should not be called on any of the FeedLists.
     */
    @Test(expected = FeedListDoesNotExist.class)
    public void getAllItemsFromNonexistentFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));

        Configuration.setFeedLists(feedLists);
        Configuration.getAllItemsFromFeedList("FeedList3");

        verify(feedLists.get(0), never()).getAllItems();
        verify(feedLists.get(1), never()).getAllItems();
    }

    /**
     * Name: Get all Feeds from existing FeedList
     * Unit: getAllFeedsFromFeedList()
     *
     * Tries to get all Feed objects from a specified FeedList. Should only call getAllItems on the
     * correct FeedList.
     */
    @Test
    public void getAllFeedsFromExistingFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));

        Configuration.setFeedLists(feedLists);
        Configuration.getAllFeedsFromFeedList("FeedList2");

        verify(feedLists.get(0), never()).getFeeds();
        verify(feedLists.get(1), times(1)).getFeeds();
    }

    /**
     * Name: Get all Feeds from FeedList
     * Unit: getAllFeedsFromFeedList()
     *
     * Tries to get all Feed objects from a specific FeedList which does not exist in the
     * Configuration. getFeeds should not be called on any of the FeedLists.
     */
    @Test(expected = FeedListDoesNotExist.class)
    public void getAllFeedsFromNonexistentFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));

        Configuration.setFeedLists(feedLists);
        Configuration.getAllFeedsFromFeedList("FeedList3");

        verify(feedLists.get(0), never()).getFeeds();
        verify(feedLists.get(1), never()).getFeeds();
    }

    /**
     * Name: Set sorting rules on existing FeedList
     * Unit: setSortingRules()
     *
     * Tries to set the sorting rules of an existing FeedList. Should call setSortingRules with
     * correct argument on the correct FeedList.
     */
    @Test
    public void setSortingRulesOnExistingFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));

        Configuration.setFeedLists(feedLists);
        Configuration.setSortingRules("FeedList1", "TITLE_DEC");

        verify(feedLists.get(0), times(1)).setSortingRules("TITLE_DEC");
        verify(feedLists.get(1), never()).setSortingRules(anyString());
    }

    /**
     * Name: Set sorting rules on nonexistent FeedList
     * Unit: setSortingRules()
     *
     * Tries to set sorting rules on a FeedList which does not exist. setSortingRules should not be
     * called on any FeedLists.
     */
    @Test(expected = FeedListDoesNotExist.class)
    public void setSortingRulesOnNonexistentFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));

        Configuration.setFeedLists(feedLists);
        Configuration.setSortingRules("FeedList3", "TITLE_DEC");

        verify(feedLists.get(0), never()).setSortingRules(anyString());
        verify(feedLists.get(1), never()).setSortingRules(anyString());
    }

    /**
     * Name: Set visited status on Item from nonexistent FeedList
     * Unit: setVisited()
     *
     * Tries to call setVisited on a specific title and sent specific argument to the method.
     * setVisited should only be called on the correct FeedList with the correct arguments.
     */
    @Test
    public void setVisitedStatusOnItemFromExistingFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));

        Configuration.setFeedLists(feedLists);
        Configuration.setVisited("FeedList1", "http://feedsId.com/system.rss.xml", "itemId", true);

        verify(feedLists.get(0), times(1)).setVisited("http://feedsId.com/system.rss.xml", "itemId", true);
        verify(feedLists.get(1), never()).setVisited(anyString(), anyString(), anyBoolean());
    }

    /**
     * Name: Set visited status on Item from nonexistent FeedList
     * Unit: setVisited()
     *
     *
     */
    @Test(expected = FeedListDoesNotExist.class)
    public void setVisitedStatusOnItemFromNonexistentFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));

        Configuration.setFeedLists(feedLists);
        Configuration.setVisited("FeedList3", "http://feedsId.com/system.rss.xml", "itemId", true);

        verify(feedLists.get(0), never()).setVisited(anyString(), anyString(), anyBoolean());
        verify(feedLists.get(1), never()).setVisited(anyString(), anyString(), anyBoolean());
    }

    /**
     * Name: Set starred status on Item from existing FeedList
     * Unit: setStarred()
     */
    @Test
    public void setStarredStatusOnItemFromExistingFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));

        Configuration.setFeedLists(feedLists);
        Configuration.setStarred("FeedList1", "http://feedsId.com/system.rss.xml", "itemId", true);

        verify(feedLists.get(0), times(1)).setStarred("http://feedsId.com/system.rss.xml", "itemId", true);
        verify(feedLists.get(1), never()).setStarred(anyString(), anyString(), anyBoolean());
    }

    /**
     * Name: Set starred status on Item from nonexistent FeedList
     * Unit: setStarred()
     */
    @Test(expected = FeedListDoesNotExist.class)
    public void setStarredStatusOnItemFromNonexistentFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));

        Configuration.setFeedLists(feedLists);
        Configuration.setVisited("FeedList3", "http://feedsId.com/system.rss.xml", "itemId", true);

        verify(feedLists.get(0), never()).setStarred(anyString(), anyString(), anyBoolean());
        verify(feedLists.get(1), never()).setStarred(anyString(), anyString(), anyBoolean());
    }

    @Test
    public void newDatabaseTest() {
        Date currentDate = new Date();

        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));
        Configuration.setFeedLists(feedLists);

        DatabaseAccessObjectSQLite dao = Mocks.createDatabaseAccessObjectMock();
        Configuration.setDao(dao);

        assertEquals(2, Configuration.getFeedLists().size());

        try {
            Configuration.reset();
            assertTrue(currentDate.before(Configuration.getLastUpdated()));
            verify(dao, times(1)).setPath("temp.db");
            verify(dao, times(1)).load();
        }
        catch(Exception expt) {
            expt.printStackTrace();
            fail();
        }
    }

    @Test
    public void saveDatabaseTest() {
        DatabaseAccessObjectSQLite dao = Mocks.createDatabaseAccessObjectMock();
        Configuration.setDao(dao);

        try {
            Configuration.save();
            verify(dao, times(1)).save();
        }
        catch(Exception expt) {
            expt.printStackTrace();
            fail();
        }
    }

    @Test
    public void saveAsDatabaseTest() {
        DatabaseAccessObjectSQLite dao = Mocks.createDatabaseAccessObjectMock();
        Configuration.setDao(dao);

        try {
            Configuration.save("new/path/to/save/database");
            verify(dao, times(1)).save();
            verify(dao, times(1)).setPath("new/path/to/save/database");
        }
        catch(Exception expt) {
            expt.printStackTrace();
            fail();
        }
    }

    @Test
    public void loadDatabaseTest() {
        Date currentDate = new Date();

        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));
        Configuration.setFeedLists(feedLists);

        DatabaseAccessObjectSQLite dao = Mocks.createDatabaseAccessObjectMock();
        Configuration.setDao(dao);

        assertEquals(2, Configuration.getFeedLists().size());

        try {
            Configuration.load("path/to/database");
            assertTrue(currentDate.before(Configuration.getLastUpdated()));
            verify(dao, times(1)).setPath("path/to/database");
            verify(dao, times(1)).load();
        }
        catch(Exception expt) {
            expt.printStackTrace();
            fail();
        }
    }
}