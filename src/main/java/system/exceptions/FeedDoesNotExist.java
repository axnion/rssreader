package system.exceptions;

/**
 * Class FeedDoesNotExist
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedDoesNotExist extends RuntimeException {
    public FeedDoesNotExist() {
        super();
    }

    public FeedDoesNotExist(String feedUrl, String feedListName) {
        super("There is no Feed with the URL \"" + feedUrl + "\" in \"" + feedListName + "\"");
    }
}
