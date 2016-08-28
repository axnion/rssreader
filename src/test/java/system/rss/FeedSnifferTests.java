package system.rss;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Class FeedSnifferTests
 *
 * This is the test class for the FeedSniffer class.
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedSnifferTests {
    private FeedSniffer feedSniffer;
    private String resources = FeedSniffer.class
            .getResource("../../../../resources/test/FeedSnifferTestResources/")
            .getPath();

    /**
     * Test preparation. Creates a new FeedSniffer object before each test.
     */
    @Before
    public void createObject() {
        feedSniffer = new FeedSniffer();
    }

    /**
     * Name: Sniffer test
     * Unit: getFeeds(String)
     *
     * Tries to give a url to an xml file containing an RSS feed as an argument when calling
     * getFeeds.
     */
    @Test
    public void snifferTest() {
        ArrayList<FeedMinimal> feeds = feedSniffer.getFeeds(resources + "exampleFeed.xml");
        assertEquals(1, feeds.size());
        assertEquals("Feed", feeds.get(0).title);
        assertEquals(resources + "exampleFeed.xml", feeds.get(0).urlToXml);
    }
}
