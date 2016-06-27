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
        Configuration.loadDatabase();
    }
}
