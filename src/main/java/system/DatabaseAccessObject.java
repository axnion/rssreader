package system;

import java.util.ArrayList;

/**
 * Interface DatabaseAccessObject
 *
 * @author Axel Nilsson (axnion)
 */
interface DatabaseAccessObject {
    ArrayList<FeedList> load() throws Exception;
    void save() throws Exception;

    void setPath(String path);
    String getPath();
}
