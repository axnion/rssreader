package system.rss;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Class FeedSnifferTests
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedSnifferTests {
    private FeedSniffer feedSniffer;
    private String resources = FeedSniffer.class
            .getResource("../../../../resources/test/FeedSnifferTestResources/")
            .getPath();

    @Before
    public void createObject() {
        feedSniffer = new FeedSniffer();
    }

    @Test
    public void snifferTest() {
        ArrayList<FeedMinimal> feeds = feedSniffer.getFeeds(resources + "exampleFeed.xml");
        assertEquals(1, feeds.size());
        assertEquals("Feed", feeds.get(0).title);
        assertEquals(resources + "exampleFeed.xml", feeds.get(0).urlToXml);
    }
}
