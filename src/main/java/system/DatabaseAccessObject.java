package system;

import java.util.ArrayList;
import java.util.Date;

/**
 * Interface DatabaseAccessObject
 *
 * @author Axel Nilsson (axnion)
 */
interface DatabaseAccessObject {
    ArrayList<FeedList> load() throws Exception;
    void save(ArrayList<FeedList> feedLists, Date configurationLastUpdate) throws Exception;

    void setPath(String path);
    String getPath();
}
