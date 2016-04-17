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
    private String title;
    private String link;
    private String description;
    private String id;
    private boolean visited;
    private boolean starred;

    /**
     * Constructor
     */
    public Item()
    {
        title = "";
        link = "";
        description = "";
        id = "";
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
     * @param visited       True if the user has visited the item, if not then false
     * @param starred       True if the user has starred this item, if not then false.
     */
    public Item(String title, String link, String description, String id, boolean visited, boolean starred)
    {
        this.title = title;
        this.link = link;
        this.description = description;
        this.id = id;
        this.visited = visited;
        this.starred = starred;
    }

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
}

// Created: 2016-04-17
