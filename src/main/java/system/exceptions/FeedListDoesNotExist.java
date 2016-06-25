package system.exceptions;

/**
 * Class FeedListDoesNotExist
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedListDoesNotExist extends RuntimeException {
    public FeedListDoesNotExist() {
        super();
    }

    public FeedListDoesNotExist(String feedListName) {
        super("There is no FeedList named \"" + feedListName + "\" in the current Configuration");
    }
}
