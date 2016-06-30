package system.exceptions;

/**
 * Class ItemDoesNotExist
 *
 * @author Axel Nilsson (axnion)
 */
public class ItemDoesNotExist extends RuntimeException {
    public ItemDoesNotExist() {
        super();
    }

    public ItemDoesNotExist(String itemId, String feedUrl) {
        super("There is no Feed with the ÍD \"" + itemId + "\" in Feed \"" + itemId + "\"");
    }
}
