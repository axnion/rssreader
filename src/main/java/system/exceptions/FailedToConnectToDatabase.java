package system.exceptions;

/**
 * Class FailedToConnectToDatabase
 *
 * @author Axel Nilsson (axnion)
 */
public class FailedToConnectToDatabase extends RuntimeException {
    public FailedToConnectToDatabase() {
        super();
    }

    public FailedToConnectToDatabase(String path) {
        super("Can't find database at \"" + path + "\"");
    }
}
