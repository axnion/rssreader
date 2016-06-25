package system;

import system.exceptions.DatabaseError;
import system.exceptions.FeedListAlreadyExists;
import system.exceptions.FeedListDoesNotExist;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import java.util.ArrayList;

/**
 * Class ConfigurationTest
 *
 * @author Axel Nilsson (axnion)
 */
public class ConfigurationTests {
    @Before
    public void resetObject() {
        Configuration.reset();
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
        assertEquals(0, Configuration.getFeedLists().size());

        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));
        Configuration.setFeedLists(feedLists);

        DatabaseController dbc = Mocks.createDatabaseControllerMock();
        Configuration.setDatabaseController(dbc);

        assertEquals("FeedList1", Configuration.getFeedLists().get(0).getName());
        assertEquals("FeedList2", Configuration.getFeedLists().get(1).getName());
        assertEquals(dbc, Configuration.getDatabaseController());
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
     * Name: Get all Items from FeedList with multiple Feed objects
     * Unit: getAllItemsFromFeedList()
     *
     * Tries to get all items from a specific FeedList. Should only call getAllItems on the correct
     * FeedList.
     */
    @Test
    public void getAllItemsFromFeedList() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));

        Configuration.setFeedLists(feedLists);
        Configuration.setDatabaseController(Mocks.createDatabaseControllerMock());
        Configuration.getAllItemsFromFeedList("FeedList1");

        verify(feedLists.get(0), times(1)).getAllItems(any());
        verify(feedLists.get(1), never()).getAllItems(any());
    }

    /**
     * Name: Get sorting
     * Unit: getSorting()
     *
     * Gets the sorting information from the database without DatabaseController throwing
     * exceptions.
     */
    @Test
    public void getSorting() {
        DatabaseController dbc = Mocks.createDatabaseControllerMock();
        Configuration.setDatabaseController(dbc);

        assertEquals("", Configuration.getSorting("FeedList1"));
        assertEquals("DATE_DEC", Configuration.getSorting("FeedList2"));

        try {
            verify(dbc, times(1)).getSorting("FeedList1");
        }
        catch(Exception err) {
            err.printStackTrace();
            fail();
        }
    }

    /**
     * Name: Get sorting database error
     * Unit getSorting()
     *
     * Gets the sorting information from a database where the DatabaseController throws an
     * exception. DatabaseError exception should be thrown.
     */
    @Test(expected = DatabaseError.class)
    public void getSortingDatabaseError() {
        DatabaseController dbc = Mocks.createDatabaseControllerMock();
        Configuration.setDatabaseController(dbc);
        Configuration.getSorting("FeedList3");

        try {
            verify(dbc, times(1)).getSorting("FeedList3");
        }
        catch(Exception err) {
            err.printStackTrace();
            fail();
        }
    }

    /**
     * Name: Is visited
     * Unit isVisited()
     *
     * Gets visited status from the database without DatabaseController throwing exceptions.
     */
    @Test
    public void isVisited() {
        DatabaseController dbc = Mocks.createDatabaseControllerMock();
        Configuration.setDatabaseController(dbc);

        assertEquals(true, Configuration.isVisited("FeedList1", "item_id_1"));
        assertEquals(false, Configuration.isVisited("FeedList1", "item_id_2"));

        try {
            verify(dbc, times(1)).getVisitedStatus("FeedList1", "item_id_1");
            verify(dbc, times(1)).getVisitedStatus("FeedList1", "item_id_2");
        }
        catch(Exception err) {
            err.printStackTrace();
            fail();
        }
    }

    /**
     * Name: Is visited database error
     * Unit isVisited()
     *
     * Gets the visited status from a database where the DatabaseController throws an
     * exception. DatabaseError exception should be thrown.
     */
    @Test(expected = DatabaseError.class)
    public void isVisitedDatabaseError() {
        DatabaseController dbc = Mocks.createDatabaseControllerMock();
        Configuration.setDatabaseController(dbc);
        Configuration.isVisited("FeedList2", "item_id_3");

        try {
            verify(dbc, times(1)).getVisitedStatus("FeedList2", "item_id_3");
        }
        catch(Exception err) {
            err.printStackTrace();
            fail();
        }
    }

    /**
     * Name: Is starred
     * Unit isStarred()
     *
     * Gets starred status from the database without DatabaseController throwing exceptions.
     */
    @Test
    public void isStarred() {
        DatabaseController dbc = Mocks.createDatabaseControllerMock();
        Configuration.setDatabaseController(dbc);

        assertEquals(true, Configuration.isStarred("FeedList1", "item_id_1"));
        assertEquals(false, Configuration.isStarred("FeedList1", "item_id_2"));

        try {
            verify(dbc, times(1)).getStarredStatus("FeedList1", "item_id_1");
            verify(dbc, times(1)).getStarredStatus("FeedList1", "item_id_2");
        }
        catch(Exception err) {
            err.printStackTrace();
            fail();
        }
    }

    /**
     * Name: Is starred database error
     * Unit isStarred()
     *
     * Gets the starred status from a database where the DatabaseController throws an
     * exception. DatabaseError exception should be thrown.
     */
    @Test(expected = DatabaseError.class)
    public void isStarredDatabaseError() {
        DatabaseController dbc = Mocks.createDatabaseControllerMock();
        Configuration.setDatabaseController(dbc);
        Configuration.isStarred("FeedList2", "item_id_3");

        try {
            verify(dbc, times(1)).getStarredStatus("FeedList2", "item_id_3");
        }
        catch(Exception err) {
            err.printStackTrace();
            fail();
        }
    }

    /**
     * Name: Set sorting
     * Unit setSorting()
     *
     * Check so the setSorting method on DatabaseController is called when setSorting is called on
     * Configuration.
     */
    @Test
    public void setSorting() {
        DatabaseController dbc = Mocks.createDatabaseControllerMock();
        Configuration.setDatabaseController(dbc);
        Configuration.setSorting("FeedList1", "DATE_DEC");

        try {
            verify(dbc, times(1)).setSorting("FeedList1", "DATE_DEC");
        }
        catch(Exception err) {
            err.printStackTrace();
            fail();
        }
    }

    /**
     * Name: Set sorting database error
     * Unit setSorting()
     *
     * Tries to set the sorting and the DatabaseController throws an exception. DatabaseError should
     * be thrown.
     */
    @Test(expected = DatabaseError.class)
    public void setSortingDatabaseError() {
        DatabaseController dbc = Mocks.createDatabaseControllerMock();
        Configuration.setDatabaseController(dbc);
        Configuration.setSorting("FeedList2", "DATE_DEC");

        try {
            verify(dbc, times(1)).setSorting("FeedList2", "DATE_DEC");
        }
        catch(Exception err) {
            err.printStackTrace();
            fail();
        }
    }

    /**
     * Name: Set visited
     * Unit setVisited()
     *
     * Check so the setVisited method on DatabaseController is called when setVisitedStatus is
     * called on Configuration.
     */
    @Test
    public void setVisited() {
        DatabaseController dbc = Mocks.createDatabaseControllerMock();
        Configuration.setDatabaseController(dbc);
        Configuration.setVisited("FeedList1", "item_id_1", false);

        try {
            verify(dbc, times(1)).setVisitedStatus("FeedList1", "item_id_1", false);
        }
        catch(Exception err) {
            err.printStackTrace();
            fail();
        }
    }

    /**
     * Name: Set visited database error
     * Unit setVisited()
     *
     * Tries to set the visited status and the DatabaseController throws an exception.
     * DatabaseError should be thrown.
     */
    @Test(expected = DatabaseError.class)
    public void setVisitedDatabaseError() {
        DatabaseController dbc = Mocks.createDatabaseControllerMock();
        Configuration.setDatabaseController(dbc);
        Configuration.setVisited("FeedList2", "item_id_1", false);

        try {
            verify(dbc, times(1)).setVisitedStatus("FeedList2", "item_id_1", false);
        }
        catch(Exception err) {
            err.printStackTrace();
            fail();
        }
    }

    /**
     * Name: Set starred
     * Unit setStarred()
     *
     * Check so the setStarred method on DatabaseController is called when setStarredStatus is
     * called on Configuration.
     */
    @Test
    public void setStarred() {
        DatabaseController dbc = Mocks.createDatabaseControllerMock();
        Configuration.setDatabaseController(dbc);
        Configuration.setStarred("FeedList1", "item_id_1", false);

        try {
            verify(dbc, times(1)).setStarredStatus("FeedList1", "item_id_1", false);
        }
        catch(Exception err) {
            err.printStackTrace();
            fail();
        }
    }

    /**
     * Name: Set starred database error
     * Unit setStarred()
     *
     * Tries to set the starred status and the DatabaseController throws an exception.
     * DatabaseError should be thrown.
     */
    @Test(expected = DatabaseError.class)
    public void setStarredDatabaseError() {
        DatabaseController dbc = Mocks.createDatabaseControllerMock();
        Configuration.setDatabaseController(dbc);
        Configuration.setStarred("FeedList2", "item_id_1", false);

        try {
            verify(dbc, times(1)).setStarredStatus("FeedList2", "item_id_1", false);
        }
        catch(Exception err) {
            err.printStackTrace();
            fail();
        }
    }
}
