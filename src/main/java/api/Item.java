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

    /**
     * Compares this Items title to another Item objects title, if this Items title is
     * earlier in the alphabet than the other the value returned is lower than 0. If this items
     * title is later than others title the returned value is higher than 0. If they are the same 0
     * is returned
     * @param other The Item object to compare this Item with
     * @return      An integer representing the comparison of the titles of these two Items
     */
    int compareTitle(Item other)
    {
        return this.getTitle().toLowerCase().compareTo(other.getTitle().toLowerCase());
    }

    /**
     * Compares this Items date to another Items title. It will compare year, months, days, hours,
     * minutes, and seconds, in that order. If the other item is newer than this it will return an
     * integer smaller than 0. And if this is newer than the other a positive integer is returned.
     * If they are the same 0 is returned.
     * @param other The Item object to compare this Item with
     * @return      An integer representing the comparison of the titles of these two Items
     */
    int compareDate(Item other)
    {
        int comparison;

        // Comparing years
        comparison = Integer.parseInt(this.getDate().substring(12, 16)) -
                Integer.parseInt(other.getDate().substring(12, 16));
        if(comparison != 0)
            return comparison;

        // Comparing months
        comparison = monthStringToInt(this.getDate().substring(8, 11)) -
                monthStringToInt(other.getDate().substring(8, 11));
        if(comparison != 0)
            return comparison;

        // Comparing days
        comparison = removeFirstZero(this.getDate().substring(5, 7)) -
                removeFirstZero(other.getDate().substring(5, 7));
        if(comparison != 0)
            return comparison;

        // Comparing hours
        comparison = removeFirstZero(this.getDate().substring(17, 19)) -
                removeFirstZero(other.getDate().substring(17, 19));
        if(comparison != 0)
            return comparison;

        // Comparing minutes
        comparison = removeFirstZero(this.getDate().substring(20, 22)) -
                removeFirstZero(other.getDate().substring(20, 22));
        if(comparison != 0)
            return comparison;

        // Comparing seconds
        comparison = removeFirstZero(this.getDate().substring(23, 25)) -
                removeFirstZero(other.getDate().substring(23, 25));
        if(comparison != 0)
            return comparison;

        return 0;
    }

    /**
     * Takes a three character String representing a month. If the String matches one of the
     * alternatives an integer associated with that month.
     * @param str   A String representing a month.
     * @return      An integer representing a month.
     */
    private int monthStringToInt(String str)
    {
        switch (str)
        {
            case "Jan": return 1;
            case "Feb": return 2;
            case "Mar": return 3;
            case "Apr": return 4;
            case "May": return 5;
            case "Jun": return 6;
            case "Jul": return 7;
            case "Aug": return 8;
            case "Sep": return 9;
            case "Oct": return 10;
            case "Nov": return 11;
            case "Dec": return 12;
        }

        return 0;
    }

    /**
     * Removes the first character if it is a 0.
     * @param str   A String containing numbers.
     * @return      An integer converted from the argument
     */
    private int removeFirstZero(String str)
    {
        if(str.charAt(0) == '0')
            str = "" + str.charAt(1);

        return Integer.parseInt(str);
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
