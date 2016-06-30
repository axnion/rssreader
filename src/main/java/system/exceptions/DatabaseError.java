package system.exceptions;

/**
 * Class DatabaseError
 *
 * @author Axel Nilsson (axnion)
 */
public class DatabaseError extends RuntimeException {
    public DatabaseError() {
        super();
    }

    public DatabaseError(String message) {
        super(message);
    }
}
