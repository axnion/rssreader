package system.rss;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class Item
 *
 * The Item class represents an item in a feed. Each item has a title, a link to the content, a
 * descriptive text and an id. They also have a field for if they have been visited or not and if
 * they are starred.
 *
 * @author Axel Nilsson (axnion)
 */
public class Item {
    private String title;           // The title of the item
    private String link;            // The link to the content of the item
    private String description;     // A descriptive text about the item
    private String id;              // A unique ID for this item
    private String feedIdentifier;  // The identifier of the feed this item belongs to.
    private Date date;              // The date the item was released
    private boolean visited;        // The visited status of the Item
    private boolean starred;        // The starred status of the Item

    /**
     * Constructor
     */
    public Item() {
        title = "";
        link = "";
        description = "";
        id = "";
        feedIdentifier = "";
        date = new Date(0);
        visited = true;
        starred = false;
    }

    /**
     * Compares this Items title to another Item objects title, if this Items title is
     * earlier in the alphabet than the other the value returned is lower than 0. If this items
     * title is later than others title the returned value is higher than 0. If they are the same 0
     * is returned
     *
     * @param other The Item object to compare this Item with
     * @return      An integer representing the comparison of the titles of these two Items
     */
    public int compareTitle(Item other) {
        String title1 = this.getTitle().toLowerCase();
        String title2 = other.getTitle().toLowerCase();
        return title1.compareTo(title2);
    }

    public int compareDate(Item other) {
        return this.getDate().compareTo(other.getDate());
    }

    /*
    ------------------------------- ACCESSORS AND MUTATORS -----------------------------------------
    */

    /**
     * Accessor method for title
     *
     * @return A String containing the title of the item
     */
    public String getTitle() {
        return title;
    }

    /**
     * Accessor method for link
     *
     * @return A String containing the URL to the content of the item
     */
    public String getLink() {
        return link;
    }

    /**
     * Accessor method for description
     *
     * @return A String containing a description of the item
     */
    public String getDescription() {
        return description;
    }

    /**
     * Accessor method for id
     *
     * @return A String containing the unique ID of the item
     */
    public String getId() {
        return id;
    }

    /**
     * Accessor method for date
     *
     * @return A String containing the date and time of this items release
     */
    public Date getDate() {
        return date;
    }

    /**
     * Accessor method for feedIdentifier
     *
     * @return A String containing the feed identifier for this Item
     */
    public String getFeedIdentifier() {
        return feedIdentifier;
    }

    /**
     * Accessor method for visited
     *
     * @return The boolean value of visited.
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Accessor method for starred
     *
     * @return The boolean value of starred.
     */
    public boolean isStarred() {
        return starred;
    }

    /**
     * Mutator method for title
     *
     * @param title The new String we want assigned to title
     */
    void setTitle(String title) {
        this.title = title;
    }

    /**
     * Mutator method for link
     *
     * @param link The new String we want assigned to link
     */
    void setLink(String link) {
        this.link = link;
    }

    /**
     * Mutator method for description
     *
     * @param description The new String we want assigned to description
     */
    void setDescription(String description) {
        this.description = description;
    }

    /**
     * Mutator method for id
     *
     * @param id The new String we want assigned to id
     */
    void setId(String id) {
        this.id = id;
    }

    /**
     * Mutator method for date
     *
     * @param date The new String we want assigned to date
     */
    void setDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
        try {
            this.date = format.parse(date);
            this.date.setTime(this.date.getTime());
        }
        catch(ParseException expt) {     //Mon Jul 25 2016 12:13:33 GMT+0000
            format = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z");
            try {
                this.date = format.parse(date);
                this.date.setTime(this.date.getTime());
            }
            catch(ParseException expt2) {
                this.date = new Date(0);
                expt.printStackTrace();
            }
        }
    }

    /**
     * Mutator method for feedIdentifier
     *
     * @param feedIdentifier The new String we want assigned to feedIdentifier
     */
    void setFeedIdentifier(String feedIdentifier) {
        this.feedIdentifier = feedIdentifier;
    }

    /**
     * Mutator method for visited
     *
     * @param visited The new boolean value of visited
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Mutator method for starred
     *
     * @param starred The new boolean value of starred
     */
    public void setStarred(boolean starred) {
        this.starred = starred;
    }
}