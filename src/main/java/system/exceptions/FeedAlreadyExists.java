package system.exceptions;

/**
 * Class FeedAlreadyExists
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedAlreadyExists extends RuntimeException {
    public FeedAlreadyExists() {
        super();
    }

    public FeedAlreadyExists(String feedUrl, String feedListName) {
        super("A Feed with the name \"" + feedUrl + "\" already exists in \"" + feedListName + "\"");
    }
}
