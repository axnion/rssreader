package system.rss;

import system.rss.exceptions.ItemDoesNotExist;

import java.util.ArrayList;

/**
 * Class Feed
 *
 * The Feed class represents an RSS feed. It has the title of the feed or channel, a link to the
 * "base" website, and it has a text describing the content of the feed. It also has an array of
 * Item objects represents all the items in the feed, and lastly it has a url to the XML file to be
 * able to update.
 *
 * @author Axel Nilsson (axnion)
 */
public class Feed {
    private String title;           // The title of the feed
    private String link;            // Link to the feeds website
    private String description;     // A description about the feed
    private String urlToXML;        // The URL to the XML file, used when updating the feed
    private ArrayList<Item> items;  // An array holding the items in the feed

    /**
     * Constructor
     *
     * Takes each arguemnts and assignes the value to the corresponding field.
     */
    Feed(String title, String link, String description, String urlToXml, ArrayList<Item> items) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.urlToXML = urlToXml;
        this.items = items;
    }

    /**
     * Uses getItemById to find a specific Item and then calls setVisited on the Item. Passes status
     * as argument.
     *
     * @param itemId    A String containing the identifier of a specific Item.
     * @param status    True if the user has visited the item, if not then false.
     */
    public void setVisited(String itemId, boolean status) {
        getItemById(itemId).setVisited(status);
    }

    /**
     * Uses getItemById to find a specific Item and then calls setStarred on the Item. Passes status
     * as argument.
     *
     * @param itemId    A String containing the identifier of a specific Item.
     * @param status    True if the user has visited the item, if not then false.
     */
    public void setStarred(String itemId, boolean status) {
        getItemById(itemId).setStarred(status);
    }

    /**
     * Returns the Item object from items with a matching id to the argument passed though id
     * parameter. If no Item is found then ItemDoesNotExist exception is thrown.
     *
     * @param id    A String containing the id of the Item the method should look for.
     * @return      An Item with an id matching the argument id.
     */
    Item getItemById(String id) {
        for(Item item : items) {
            if(item.getId().equals(id))
                return item;
        }
        throw new ItemDoesNotExist(id, urlToXML);
     }

    /*
    ------------------------------- ACCESSORS AND MUTATORS -----------------------------------------
    */

    /**
     * @return A String containing the title of the feed
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return A String containing the link to the base website of the feed
     */
    public String getLink() {
        return link;
    }

    /**
     * @return A String containing a descriptive text about the feed
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return An ArrayList of Item objects that represents the items in the feed
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * @return A String containging the url to the XML file
     */
    public String getUrlToXML() {
        return urlToXML;
    }

    /**
     * Sets the title of the feed to the content of title argument.
     * @param title A String containing the title we want to set the feeds title to.
     */
    void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the link of the feed to the content of the link argument
     * @param link A String containing a the link to the base website for this feed
     */
    void setLink(String link) {
        this.link = link;
    }

    /**
     * Sets the description of the feed to the content of the description argument.
     * @param description A String containing a descriptive text about the feed
     */
    void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets a new refrence to the items array from the items argument
     * @param items A ArrayListof Item objects
     */
    void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    /**
     * Sets the path to the XML file we got the feed to the new one in the argument
     * @param urlToXML A String containing the URL to the XML file.
     */
    void setUrlToXML(String urlToXML) {
        this.urlToXML = urlToXML;
    }
}