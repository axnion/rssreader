package system;

import org.junit.Test;
import system.exceptions.*;

import static org.junit.Assert.*;

/**
 * Class systemExceptionsTests
 *
 * This is the test class for the all exceptions in system package class.
 *
 * @author Axel Nilsson (axnion)
 */
public class systemExceptionsTests {

    /**
     * Name: Database error test
     * Unit: DatabaseError()
     */
    @Test
    public void DatabaseErrorTest() {
        try {
            throw new DatabaseError();
        }
        catch (DatabaseError expt) {
            assertEquals(null, expt.getMessage());
        }

        try {
            throw new DatabaseError("This is a message from the DatabaseError exception");
        }
        catch (DatabaseError expt) {
            assertEquals("This is a message from the DatabaseError exception", expt.getMessage());
        }
    }

    /**
     * Name: Failed to connect to database test
     * Unit: FailedToConnectToDatabase()
     */
    @Test
    public void FailedToConnectToDatabaseTest() {
        try {
            throw new FailedToConnectToDatabase();
        }
        catch (FailedToConnectToDatabase expt) {
            assertEquals(null, expt.getMessage());
        }

        try {
            throw new FailedToConnectToDatabase("path/to/database");
        }
        catch (FailedToConnectToDatabase expt) {
            assertEquals("Can't find database at \"path/to/database\"", expt.getMessage());
        }
    }

    /**
     * Name: Feed already exists test
     * Unit: FeedAlreadyExists()
     */
    @Test
    public void FeedAlreadyExistsTest() {
        try {
            throw new FeedAlreadyExists();
        }
        catch (FeedAlreadyExists expt) {
            assertEquals(null, expt.getMessage());
        }

        try {
            throw new FeedAlreadyExists("http://www.feed.com/rss.xml", "FeedListName");
        }
        catch (FeedAlreadyExists expt) {
            assertEquals("A Feed with the name \"http://www.feed.com/rss.xml\" " +
                    "already exists in \"FeedListName\"", expt.getMessage());
        }
    }

    /**
     * Name: Feed does not exist test
     * Unit: FeedDoesNotExist()
     */
    @Test
    public void FeedDoesNotExistTest() {
        try {
            throw new FeedDoesNotExist();
        }
        catch (FeedDoesNotExist expt) {
            assertEquals(null, expt.getMessage());
        }

        try {
            throw new FeedDoesNotExist("http://www.feed.com/rss.xml", "FeedListName");
        }
        catch (FeedDoesNotExist expt) {
            assertEquals("There is no Feed with the URL \"http://www.feed.com/rss.xml\" " +
                    "in \"FeedListName\"", expt.getMessage());
        }
    }

    /**
     * Name: FeedList already exists test
     * Unit: FeedListAlreadyExists()
     */
    @Test
    public void FeedListAlreadyExistsTest() {
        try {
            throw new FeedListAlreadyExists();
        }
        catch (FeedListAlreadyExists expt) {
            assertEquals(null, expt.getMessage());
        }

        try {
            throw new FeedListAlreadyExists("FeedListName");
        }
        catch (FeedListAlreadyExists expt) {
            assertEquals("A FeedList with the name \"FeedListName\" already exists",
                    expt.getMessage());
        }
    }

    /**
     * Name: FeedList does not exist
     * Unit: FeedListDoesNotExist()
     */
    @Test
    public void FeedListDoesNotExistTest() {
        try {
            throw new FeedListDoesNotExist();
        }
        catch (FeedListDoesNotExist expt) {
            assertEquals(null, expt.getMessage());
        }

        try {
            throw new FeedListDoesNotExist("FeedListName");
        }
        catch (FeedListDoesNotExist expt) {
            assertEquals("There is no FeedList named \"FeedListName\" " +
                    "in the current Configuration", expt.getMessage());
        }
    }
}
