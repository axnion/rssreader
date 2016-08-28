package system.rss;

/**
 * Class FeedMinimal
 *
 * This is a minimal representation of a feed. It only contains the title of the feed and the url to
 * the XML file.
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedMinimal {
    public String urlToXml;
    public String title;

    /**
     * Constructor
     * Assigns the arguments to the appropriate field.
     *
     * @param urlToXml  The URL to the RSS file.
     * @param title     The title of the feed.
     */
    FeedMinimal(String urlToXml, String title) {
        this.urlToXml = urlToXml;
        this.title = title;
    }
}
