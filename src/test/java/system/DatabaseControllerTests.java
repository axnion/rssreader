package system;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Class DatabaseControllerTests
 *
 * @author Axel Nilsson (axnion)
 */
public class DatabaseControllerTests {
    private DatabaseController dc;

    @Before
    public void resetDatabaseController() {
        try {
            dc = new DatabaseController();
        }
        catch(Exception err) {
           err.printStackTrace();
        }
    }

    /**
     * Name: Accessors and mutators
     * Unit: getPath(), setPath()
     *
     * Tests the methods used to access and mutate the path field in the DatabaseController.
     */
    @Test
    public void accessorsAndMutators() {
        assertEquals("temp.db", dc.getPath());
        dc.setPath("a/test/path/db.db");
        assertEquals("a/test/path/db.db", dc.getPath());
    }

    /**
     * Name: Connect to database without setting path
     * Unit: DatabaseController()
     *
     * Tries to connect to the database without a path being set to the DatabaseController. The
     * DatabaseController should create and connect to temp.db.
     */
    @Test
    public void connectToDatabaseWithoutSettingPath() {
        try {
            dc.connectToDatabase();
        }
        catch(Exception err) {
            fail("connectToDatabase() threw an exception");
        }

        File f = new File("temp.db");
        assertTrue(f.exists());
        assertTrue(f.isFile());
        assertTrue(f.delete());
    }

    /**
     * Name: Connect to database with a set path
     * Unit: DatabaseController()
     *
     * Sets the path to a database and tries to connect to it. Since the database does not exist it
     * should be created and then connected to.
     */
    @Test
    public void connectToDatabaseWithASetPath() {
        String path = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        path += "/createDatabase.db";

        dc.setPath(path);

        try {
            dc.connectToDatabase();
        }
        catch(Exception err) {
            fail("connectToDatabase() threw an exception");
        }

        File f = new File(path);
        assertTrue(f.exists());
        assertTrue(f.isFile());
        assertTrue(f.delete());
    }

    /**
     * Name: Get visited status of Item
     * Unit: getVisitedStatus()
     *
     * Tries to get the visited status from a couple of items in a database.
     */
    @Test
    public void getVisitedStatusOfItem() {
        String path = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        path += "/exampleDatabase.db";

        dc.setPath(path);

        try {
            assertTrue(dc.getVisitedStatus("FeedList1", "item_id_1"));
            assertFalse(dc.getVisitedStatus("FeedList1", "item_id_2"));
            assertTrue(dc.getVisitedStatus("FeedList1", "item_id_3"));
        }
        catch(Exception err) {
            fail("getVisitedStatus() threw an exception");
        }
    }

    /**
     * Name: Get starred status of Item
     * Unit: getStarredStatus()
     *
     * Tries to get the starred status from a couple of items in a database.
     */
    @Test
    public void getStarredStatusOfItem() {
        String path = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        path += "/exampleDatabase.db";

        dc.setPath(path);

        try {
            assertTrue(dc.getStarredStatus("FeedList1", "item_id_1"));
            assertTrue(dc.getStarredStatus("FeedList1", "item_id_2"));
            assertFalse(dc.getStarredStatus("FeedList1", "item_id_3"));
        }
        catch(Exception err) {
            fail("getStarredStatus() threw an exception");
        }
    }

    /**
     * Name: Get sorting of FeedList
     * Unit: getSorting()
     *
     * Tries to get the sorting from a couple of items in a database.
     */
    @Test
    public void getSortingOfFeedList() {
        String path = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        path += "/exampleDatabase.db";

        try {
            dc.setPath(path);
            assertEquals("", dc.getSorting("FeedList1"));
            assertEquals("DATE_DEC", dc.getSorting("FeedList2"));
        }
        catch(Exception err) {
            fail("getSorting() threw an exception");
        }
    }

    /**
     * Name: Set visited status of Item
     * Unit: setVisitedStatus()
     *
     * Tries to change the visited status of an item. Checks the status before, then modify the
     * value and then checks if it has been changed.
     */
    @Test
    public void setVisitedStatusOfItem() {
        String source = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        source += "/exampleDatabase.db";
        String target = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        target += "/setVisitedStatusTest.db";
        copyDatabase(source, target);
        dc.setPath(target);

        try {
            assertTrue(dc.getVisitedStatus("FeedList1", "item_id_1"));
            dc.setVisitedStatus("FeedList1", "item_id_1", false);
            assertFalse(dc.getVisitedStatus("FeedList1", "item_id_1"));
        }
        catch(Exception err) {
            fail("Getting or setting visited status threw an exception");
        }

        File f = new File(target);
        assertTrue(f.delete());
    }

    /**
     * Name: Set visited status of Item from nonexistent FeedList
     * Unit: setVisitedStatus()
     *
     * Tries to change the visited status of an Item in a FeedList that does not exist. An exception
     * should be thrown.
     */
    @Test
    public void setVisitedStatusOfItemFromNonexistentFeedList() {
        String source = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        source += "/exampleDatabase.db";
        String target = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        target += "/setVisitedStatusTest.db";
        copyDatabase(source, target);
        dc.setPath(target);

        try {
            dc.setVisitedStatus("FeedList3", "item_id_1", false);
            fail("Setting visited status of Item in nonexistent FeedList didn't produce exception");
        }
        catch(Exception err) {
            
        }

        File f = new File(target);
        assertTrue(f.delete());
    }

    /**
     * Name: Set starred status of Item
     * Unit: setStarredStatus()
     *
     * Tries to change the starred status of an item. Checks the status before, then modify the
     * value and then checks if it has been changed.
     */
    @Test
    public void setStarredStatusOfItem() {
        String source = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        source += "/exampleDatabase.db";
        String target = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        target += "/setStarredStatusTest.db";
        copyDatabase(source, target);

        dc.setPath(target);

        try {
            assertTrue(dc.getStarredStatus("FeedList1", "item_id_1"));
            dc.setStarredStatus("FeedList1", "item_id_1", false);
            assertFalse(dc.getStarredStatus("FeedList1", "item_id_1"));
        }
        catch(Exception err) {
            fail("Getting or setting starred status threw an exception");
        }

        File f = new File(target);
        assertTrue(f.delete());
    }

    /**
     * Name: Set starred status of Item from nonexistent FeedList
     * Unit: setStarredStatus()
     *
     * Tries to change the starred status of an Item in a FeedList that does not exist. An exception
     * should be thrown.
     */
    @Test
    public void setStarredStatusOfItemFromNonexistentFeedList() {
        String source = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        source += "/exampleDatabase.db";
        String target = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        target += "/setStarredStatusTest.db";
        copyDatabase(source, target);
        dc.setPath(target);

        try {
            dc.setStarredStatus("FeedList3", "item_id_1", false);
            fail("Setting starred status of Item in nonexistent FeedList didn't produce exception");
        }
        catch(Exception err) {
           
        }

        File f = new File(target);
        assertTrue(f.delete());
    }

    /**
     * Name: Set sorting of FeedList
     * Unit: setSorting()
     *
     * Tries to change the sorting of a FeedList. Checks the sorting before, then modify the value
     * and then checks if it has been changed.
     */
    @Test
    public void setSortingOfFeedList() {
        String source = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        source += "/exampleDatabase.db";
        String target = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        target += "/setSortingTest.db";
        copyDatabase(source, target);

        dc.setPath(target);

        try {
            assertEquals("", dc.getSorting("FeedList1"));
            dc.setSorting("FeedList1", "DATE_DEC");
            assertEquals("DATE_DEC", dc.getSorting("FeedList1"));
        }
        catch(Exception err) {
            fail("Getting or setting sorting threw an exception");
        }

        File f = new File(target);
        assertTrue(f.delete());
    }

    /**
     * Name: Add FeedList to empty database
     * Unit: addFeedList()
     *
     * Adds a FeedList to a database that does not already have any FeedLists in it.
     */
    @Test
    public void addFeedListToEmptyDatabase() {
        String path = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        path += "/addFeedListToDatabase.db";

        try {
            dc.setPath(path);
            dc.addFeedList("FeedListTest");
        }
        catch(Exception err) {
            fail("addFeedList() threw an exception");
        }

        File f = new File(path);
        assertTrue(f.delete());
    }

    /**
     * Name: Add FeedList to database with content
     * Unit: addFeedList()
     *
     * Adds a FeedList to a database that already contains FeedLists.
     */
    @Test
    public void addFeedListToDatabaseWithContent() {
        String source = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        source += "/exampleDatabase.db";
        String target = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        target += "/addFeedListDatabaseWithContent.db";
        copyDatabase(source, target);

        try {
            dc.setPath(target);
            dc.addFeedList("FeedListTest");
        }
        catch(Exception err) {
            fail("addFeedList() threw an exception");
        }

        File f = new File(target);
        assertTrue(f.delete());
    }

    /**
     * Name: Add FeedList to database containing same FeedList
     * Unit: addFeedList()
     *
     * Tries to add a FeedList to a database that already contains a FeedList with the same
     * identifier. The DatabaseController should throw an exception.
     */
    @Test
    public void addFeedListToDatabaseContainingSameFeedList() {
        String source = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        source += "/exampleDatabase.db";
        String target = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        target += "/addFeedListDatabaseWithContent.db";
        copyDatabase(source, target);

        try {
            dc.setPath(target);
            dc.addFeedList("FeedList1");
            fail("addFeedList() threw an exception");
        }
        catch(Exception err) {
           
        }

        File f = new File(target);
        assertTrue(f.delete());
    }

    /**
     * Name: Remove existing FeedList
     * Unit: removeFeedList()
     *
     * Removes an existing FeedList from a database.
     */
    @Test
    public void removeExistingFeedList() {
        String source = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        source += "/exampleDatabase.db";
        String target = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        target += "/removeFeedList.db";
        copyDatabase(source, target);

        dc.setPath(target);

        try {
            dc.removeFeedList("FeedList1");
        }
        catch(Exception err) {
            fail("removeFeedList() threw an exception");
        }

        File f = new File(target);
        assertTrue(f.delete());
    }

    /**
     * Name: Remove nonexistent FeedList
     * Unit: removeFeedList()
     *
     * Tries to remove a FeedList from a database that does not contain a FeedList with the same
     * identifier.
     */
    @Test
    public void removeNonexistentFeedList() {
        String source = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        source += "/exampleDatabase.db";
        String target = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        target += "/removeFeedList.db";
        copyDatabase(source, target);

        dc.setPath(target);

        try {
            dc.removeFeedList("FeedList3");
            fail("removeFeedList() was not thrown");
        }
        catch(Exception err) {
           
        }

        File f = new File(target);
        assertTrue(f.delete());
    }

    /**
     * Name: Add Item to existing FeedList
     * Unit: addItem()
     *
     * Adds an Item to a FeedList in a database. The FeedList exists and there is no Item in this
     * FeedList with the same id as the one we want added.
     */
    @Test
    public void addItemToExistingFeedList() {
        String source = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        source += "/exampleDatabase.db";
        String target = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        target += "/addItem.db";
        copyDatabase(source, target);
        dc.setPath(target);

        try {
            dc.addItem("FeedList1", "item_id_4", false);
            assertFalse(dc.getVisitedStatus("FeedList1", "item_id_4"));
            assertFalse(dc.getStarredStatus("FeedList1", "item_id_4"));
        }
        catch(Exception err) {
            fail("addItem() threw an exception");
        }

        File f = new File(target);
        assertTrue(f.delete());
    }

    /**
     * Name: Add Item to nonexistent FeedList
     * Unit: addItem()
     *
     * Tries to add an Item to a FeedList that does not exist. The DatabaseController should throw
     * an exception.
     */
    @Test
    public void addItemToNonexistentFeedList() {
        String source = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        source += "/exampleDatabase.db";
        String target = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        target += "/addItem.db";
        copyDatabase(source, target);

        dc.setPath(target);

        try {
            dc.addItem("FeedList3", "item_id_4", false);
            fail("addItem() threw an exception");
        }
        catch(Exception err) {
           
        }

        File f = new File(target);
        assertTrue(f.delete());
    }

    /**
     * Name: Add existing Item to FeedList
     * Unit: addItem()
     *
     * Tries to add an Item to a FeedList that already contains an Item with identical identifier.
     * DatabaseController should throw an exception.
     */
    @Test
    public void addExistingItemToFeedList() {
        String source = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        source += "/exampleDatabase.db";
        String target = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        target += "/addItem.db";
        copyDatabase(source, target);

        dc.setPath(target);

        try {
            dc.addItem("FeedList1", "item_id_1", false);
            fail("addItem() threw an exception");
        }
        catch(Exception err) {
           
        }

        File f = new File(target);
        assertTrue(f.delete());
    }

    /**
     * Name: Delete Item from FeedList
     * Unit: deleteItem()
     *
     * Deletes an Item that exists in a FeedList that also exists in the database.
     */
    @Test
    public void deleteItemFromFeedList() {
        String source = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        source += "/exampleDatabase.db";
        String target = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        target += "/removeItem.db";
        copyDatabase(source, target);

        dc.setPath(target);

        try {
            dc.deleteItem("FeedList1", "item_id_1");
        }
        catch(Exception err) {
            fail("deleteItem() threw an exception");
        }

        try {
            dc.getVisitedStatus("FeedList1", "item_id_1");
            fail("Item can still be found in the database");
        }
        catch(Exception err) {
            System.out.println("Deleted item is gone");
        }

        File f = new File(target);
        assertTrue(f.delete());
    }

    /**
     * Name: Delete Item from nonexistent FeedList
     * Unit: deleteItem()
     *
     * Tries to delete an Item from a FeedList that does not exist. DatabaseController should
     * produce an exception.
     */
    @Test
    public void deleteItemFromNonexistentFeedList() {
        String source = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        source += "/exampleDatabase.db";
        String target = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        target += "/removeItem.db";
        copyDatabase(source, target);

        dc.setPath(target);

        try {
            dc.deleteItem("FeedList4", "item_id_1");
            fail("deleteItem() did not throw an exception");
        }
        catch(Exception err) {
           
        }

        File f = new File(target);
        assertTrue(f.delete());
    }

    /**
     * Name: Delete nonexistent Item
     * Unit: deleteItem()
     *
     * Tries to remove an Item that does not exist in an existing FeedList. No exception should be
     * thrown.
     */
    @Test
    public void deleteNonexistentItem() {
        String source = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        source += "/exampleDatabase.db";
        String target = DatabaseControllerTests.class.getResource("../../../resources/test/db")
                .getPath();
        target += "/removeItem.db";
        copyDatabase(source, target);

        dc.setPath(target);

        try {
            dc.getVisitedStatus("FeedList1", "item_id_4");
            fail("Nonexistant item found in database");
        }
        catch(Exception err) {
           
        }

        try {
            dc.deleteItem("FeedList1", "item_id_4");

        }
        catch(Exception err) {
            fail("deleteItem() threw an exception");
            err.printStackTrace();
        }

        try {
            dc.getVisitedStatus("FeedList1", "item_id_4");
            fail("Nonexistant item found in database");
        }
        catch(Exception err) {
           
        }

        File f = new File(target);
        assertTrue(f.delete());
    }

    /**
     * Copies the file from the source path to the target path.
     * @param source    Path to the file to be copied
     * @param target    Path to the destination of the copying.
     */
    private void copyDatabase(String source, String target) {
        try {
            FileUtils.copyFile(new File(source), new File(target));
        }
        catch(IOException err) {
            err.printStackTrace();
        }
    }
}
