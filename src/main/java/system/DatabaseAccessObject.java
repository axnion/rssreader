package system;

import java.util.ArrayList;
import java.util.Date;

/**
 * Interface DatabaseAccessObject
 *
 * This interface describes what fundamental methods a DatabaseAccessObject should have.
 *
 * @author Axel Nilsson (axnion)
 */
interface DatabaseAccessObject {

    /**
     * Loads from current database and returns the result as an ArrayList containing FeedLists.
     *
     * @return              An ArrayList containing FeedLists which houses the data loaded from the
     *                      database.
     * @throws Exception    If something goes wrong while loading data from database.
     */
    ArrayList<FeedList> load() throws Exception;

    /**
     * Takes data from the ArrayList of FeedLists and inserts it into a save file or database.
     *
     * @param feedLists                 An ArrayList of FeedLists from where the method will take
     *                                  data and insert it into a database or save file.
     * @param configurationLastUpdate   A Date object with the last time the ArrayList of FeedLists
     *                                  got updated.+
     * @throws Exception                If something goes wrong while saving to database.
     */
    void save(ArrayList<FeedList> feedLists, Date configurationLastUpdate) throws Exception;

    /**
     * Mutator method for a path to where we want to save/load the data.
     *
     * @param path  A String containing the path to the database/save file. Can be different
     *              depending on the implementation
     */
    void setPath(String path);

    /**
     * Access method for the path to the database/save file.
     *
     * @return  A String containing the path to the database/save file.
     */
    String getPath();
}
