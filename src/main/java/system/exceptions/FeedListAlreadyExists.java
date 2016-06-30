package system.exceptions;

/**
 * Class FeedListAlreadyExists
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedListAlreadyExists extends RuntimeException {
    public FeedListAlreadyExists() {
        super();
    }

    public FeedListAlreadyExists(String feedListName) {
        super("A FeedList with the name \"" + feedListName + "\" already exists");
    }
}
