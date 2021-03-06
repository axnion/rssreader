
import org.junit.Test;
import system.Configuration;

/**
 * Class systemTests
 *
 * @author Axel Nilsson (axnion)
 */
public class systemTests {
    @Test
    public void startup() {
        try {
            Configuration.load("temp.db");
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        Configuration.addFeedList("MyFeedList");
        Configuration.addFeed("http://feeds.feedburner.com/sakerhetspodcasten", "MyFeedList");

        Configuration.removeFeedList("MyFeedList");
    }

    @Test
    public void saveDatabase() {
        Configuration.addFeedList("TestList");
        Configuration.addFeed("http://feeds.feedburner.com/sakerhetspodcasten", "TestList");

        try {
            Configuration.save("");
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }
    }
}
