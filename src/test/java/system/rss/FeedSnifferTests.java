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

    @Before
    public void createObject() {
        feedSniffer = new FeedSniffer();
    }

    @Test
    public void snifferTest() {
        ArrayList<Feed> feeds = feedSniffer.getFeeds("http://www.axelnilsson.tech/");
        assertEquals(0, feeds.size());
    }
}
