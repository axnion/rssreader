import htmlParser.HtmlParser;
import javafx.scene.text.TextFlow;
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
            Configuration.loadDatabase("temp.db");
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        Configuration.addFeedList("MyFeedList");
        Configuration.addFeed("http://feeds.feedburner.com/sakerhetspodcasten", "MyFeedList");

        Configuration.removeFeedList("MyFeedList");
    }
}
