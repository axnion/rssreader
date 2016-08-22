package system.rss;

import org.junit.Test;
import system.rss.exceptions.ItemDoesNotExist;
import system.rss.exceptions.NoXMLFileFound;

import static org.junit.Assert.*;

/**
 * Class rssExceptionsTests
 *
 * @author Axel Nilsson (axnion)
 */
public class rssExceptionsTests {
    @Test
    public void ItemDoesNotExistTest() {
        try {
            throw new ItemDoesNotExist();
        }
        catch (ItemDoesNotExist expt) {
            assertEquals(null, expt.getMessage());
        }

        try {
            throw new ItemDoesNotExist("item1", "http://www.feed.com/rss.xml");
        }
        catch (ItemDoesNotExist expt) {
            assertEquals("There is no Feed with the ÍD \"item1\" in Feed " +
                    "\"http://www.feed.com/rss.xml\"", expt.getMessage());
        }
    }

    @Test
    public void NoXMLFileFoundTest() {
        try {
            throw new NoXMLFileFound();
        }
        catch (NoXMLFileFound expt) {
            assertEquals(null, expt.getMessage());
        }

        try {
            throw new NoXMLFileFound("http://www.feed.com/rss.xml");
        }
        catch (NoXMLFileFound expt) {
            assertEquals("No XML file found at \"http://www.feed.com/rss.xml\"", expt.getMessage());
        }
    }
}
