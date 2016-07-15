package rss;

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
    private String date;            // The date the item was released
    private String feedIdentifier;  // The identifier of the feed this item belongs to.
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
        date = "";
        feedIdentifier = "";
        visited = true;
        starred = false;
    }

    /**
     * Compares this Items title to another Item objects title, if this Items title is
     * earlier in the alphabet than the other the value returned is lower than 0. If this items
     * title is later than others title the returned value is higher than 0. If they are the same 0
     * is returned
     * @param other The Item object to compare this Item with
     * @return      An integer representing the comparison of the titles of these two Items
     */
    public int compareTitle(Item other) {
        String title1 = this.getTitle().toLowerCase();
        String title2 = other.getTitle().toLowerCase();
        return title1.compareTo(title2);
    }

    /**
     * Compares this Items date to another Items title. It will compare year, months, days, hours,
     * minutes, and seconds, in that order. If the other item is newer than this it will return an
     * integer smaller than 0. And if this is newer than the other a positive integer is returned.
     * If they are the same 0 is returned.
     * @param other The Item object to compare this Item with
     * @return      An integer representing the comparison of the titles of these two Items
     */
    public int compareDate(Item other) {
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
    private int monthStringToInt(String str) {
        if(str.equals("Jan"))
            return 1;
        else if(str.equals("Feb"))
            return 2;
        else if(str.equals("Mar"))
            return 3;
        else if(str.equals("Apr"))
            return 4;
        else if(str.equals("May"))
            return 5;
        else if(str.equals("Jun"))
            return 6;
        else if(str.equals("Jul"))
            return 7;
        else if(str.equals("Aug"))
            return 8;
        else if(str.equals("Sep"))
            return 9;
        else if(str.equals("Oct"))
            return 10;
        else if(str.equals("Nov"))
            return 11;
        else if(str.equals("Dec"))
            return 12;
        else
            return 0;
    }

    /**
     * Removes the first character if it is a 0.
     * @param str   A String containing numbers.
     * @return      An integer converted from the argument
     */
    private int removeFirstZero(String str) {
        if(str.charAt(0) == '0')
            str = "" + str.charAt(1);

        return Integer.parseInt(str);
    }

    /*
    ------------------------------- ACCESSORS AND MUTATORS -----------------------------------------
    */

    /**
     * Accessor method for title
     * @return A String containing the title of the item
     */
    public String getTitle() {
        return title;
    }

    /**
     * Accessor method for link
     * @return A String containing the URL to the content of the item
     */
    public String getLink() {
        return link;
    }

    /**
     * Accessor method for description
     * @return A String containing a description of the item
     */
    public String getDescription() {
        return description;
    }

    /**
     * Accessor method for id
     * @return A String containing the unique ID of the item
     */
    public String getId() {
        return id;
    }

    /**
     * Accessor method for date
     * @return A String containing the date and time of this items release
     */
    public String getDate() {
        return date;
    }

    /**
     * Accessor method for feedIdentifier
     * @return A String containing the feed identifier for this Item
     */
    public String getFeedIdentifier() {
        return feedIdentifier;
    }

    /**
     * Accessor method for visited
     * @return The boolean value of visited.
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Accessor method for starred
     * @return The boolean value of starred.
     */
    public boolean isStarred() {
        return starred;
    }

    /**
     * Mutator method for title
     * @param title The new String we want assigned to title
     */
    void setTitle(String title) {
        this.title = title;
    }

    /**
     * Mutator method for link
     * @param link The new String we want assigned to link
     */
    void setLink(String link) {
        this.link = link;
    }

    /**
     * Mutator method for description
     * @param description The new String we want assigned to description
     */
    void setDescription(String description) {
        this.description = description;
    }

    /**
     * Mutator method for id
     * @param id The new String we want assigned to id
     */
    void setId(String id) {
        this.id = id;
    }

    /**
     * Mutator method for date
     * @param date The new String we want assigned to date
     */
    void setDate(String date) {
        // VALIDATION NEEDED!!!!!
        this.date = date;
    }

    /**
     * Mutator method for feedIdentifier
     * @param feedIdentifier The new String we want assigned to feedIdentifier
     */
    void setFeedIdentifier(String feedIdentifier) {
        this.feedIdentifier = feedIdentifier;
    }

    /**
     * Mutator method for visited
     * @param visited The new boolean value of visited
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Mutator method for starred
     * @param starred The new boolean value of starred
     */
    public void setStarred(boolean starred) {
        this.starred = starred;
    }
}

// Created: 2016-04-17
