package api;

/**
 * Class Item
 *
 * The Item class represents an item in a feed. Each item has a title, a link to the content, a
 * descriptive text and an id. They also have a field for if they have been visited or not and if
 * they are starred.
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
public class Item
{
    private String title;           // The title of the item
    private String link;            // The link to the content of the item
    private String description;     // A descriptive text about the item
    private String id;              // A unique ID for this item
    private String date;            // The date the item was released
    private boolean visited;        // True if user has visited the item
    private boolean starred;        // True if user has starred the item

    /**
     * Constructor
     */
    Item()
    {
        title = "";
        link = "";
        description = "";
        id = "";
        date = "";
        visited = false;
        starred = false;
    }

    /**
     * Constructor
     *
     * @param title         The visible title of the item
     * @param link          The URL to the items content
     * @param description   A descriptive text about the item
     * @param id            A unique ID to easily identify the item
     * @param date          A String containing the date and time of the items release
     * @param visited       True if the user has visited the item, if not then false
     * @param starred       True if the user has starred this item, if not then false.
     */
    Item(String title, String link, String description, String id, String date, boolean visited, boolean starred)
    {
        this.title = title;
        this.link = link;
        this.description = description;
        this.id = id;
        this.date = date;
        this.visited = visited;
        this.starred = starred;
    }

    /*
    ------------------------------- ACCESSORS AND MUTATORS -----------------------------------------
    */

    /**
     * @return A String containing the title of the item
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @return A String containing the URL to the content of the item
     */
    public String getLink()
    {
        return link;
    }

    /**
     * @return A String containing a description of the item
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @return A String containing the unique ID of the item
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return A Date object containing the date and time of this items release
     */
    public String getDate()
    {
        return date;
    }

    /**
     * @return True if the user has visited the item, if not then false.
     */
    public boolean isVisited()
    {
        return visited;
    }

    /**
     * @return True if the user has starred this item, if not then false.
     */
    public boolean isStarred()
    {
        return starred;
    }

    void setTitle(String title)
    {
        this.title = title;
    }

    void setLink(String link)
    {
        this.link = link;
    }

    void setDescription(String description)
    {
        this.description = description;
    }

    void setId(String id)
    {
        this.id = id;
    }

    void setDate(String date)
    {
        this.date = date;
    }

    void setVisited(boolean visited)
    {
        this.visited = visited;
    }

    void setStarred(boolean starred)
    {
        this.starred = starred;
    }
}

// Created: 2016-04-17
