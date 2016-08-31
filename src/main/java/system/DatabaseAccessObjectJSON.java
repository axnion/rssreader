package system;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class DatabaseAccessObjectJSON
 *
 * @author Axel Nilsson (axnion)
 */
class DatabaseAccessObjectJSON implements DatabaseAccessObject {
    private String path;
    private Date lastSaved;

    DatabaseAccessObjectJSON() {
        path = "temp.json";
        lastSaved = new Date(0);
    }

    DatabaseAccessObjectJSON(String path) {
        this.path = path;
        lastSaved = new Date(0);
    }

    public ArrayList<FeedList> load() {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        return feedLists;
    }

    public void save(ArrayList<FeedList> feedLists, Date configurationLastUpdated)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File(path), feedLists);
    }

    /*
    ----------------------------------- ACCESSORS AND MUTATORS -------------------------------------
    */

    /**
     * Accessor method for path.
     *
     * @return  A String containing the path currently used in this object.
     */
    public String getPath() {
        return path;
    }

    /**
     * Accessor method for lastSaved.
     *
     * @return  A Date object containing the last time this object saved to the current path.
     */
    Date getLastSaved() {
        return lastSaved;
    }

    /**
     * Mutator method for path.
     *
     * @param path  A String containing the new path to be set as to path.
     */
    public void setPath(String path) {
        this.path = path;
        setLastSaved(new Date(0));
    }

    /**
     * Mutator method for lastsaved.
     *
     * @param lastSavedParam    A Date object to be set as the last time this object last saved to
     *                          current path.
     */
    void setLastSaved(Date lastSavedParam) {
        lastSaved = lastSavedParam;
    }
}
