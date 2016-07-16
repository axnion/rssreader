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
        assertEquals(0, Configuration.getFeedLists().size());

        ArrayList<FeedList> feedLists = new ArrayList<>();
        feedLists.add(Mocks.createFeedListMock("FeedList1"));
        feedLists.add(Mocks.createFeedListMock("FeedList2"));
        Configuration.setFeedLists(feedLists);

        DatabaseAccessObject dao = Mocks.createDatabaseAccessObjectMock();
        Configuration.setDao(dao);

        assertEquals("FeedList1", Configuration.getFeedLists().get(0).getName());
        assertEquals("FeedList2", Configuration.getFeedLists().get(1).getName());
        assertEquals(dao, Configuration.getDao());
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
        Configuration.getAllItemsFromFeedList("FeedList1");

        verify(feedLists.get(0), times(1)).getAllItems();
        verify(feedLists.get(1), never()).getAllItems();
    }
}
