package api;

/**
 * Class Feed
 *
 * The Feed class represents an rss feed. It has the title of the feed or channel, a link to the
 * "base" website, and it has a text describing the content of the feed. It also has an array of
 * Item objects represents all the items in the feed, and lastly it has a url to the XML file to be
 * able to update.
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
public class Feed
{
    private String title;
    private String link;
    private String description;
    private Item[] items;
    private String pathToXML;

    /**
     * Constructor
     */
    public Feed()
    {
        title = "";
        link = "";
        description = "";
        items = null;
        pathToXML = "";
    }

    /**
     * @return A String containing the title of the feed
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @return A String containing the link to the base website of the feed
     */
    public String getLink()
    {
        return link;
    }

    /**
     * @return A String containing a descriptive text about the feed
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @return An array of Item objects that represents the items in the feed
     */
    public Item[] getItems()
    {
        return items;
    }

    /**
     * @return A String containging the url to the XML file
     */
    public String getPathToXML()
    {
        return pathToXML;
    }

    /**
     * Sets the title of the feed to the content of title argument.
     * @param title A String containing the title we want to set the feeds title to.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Sets the link of the feed to the content of the link argument
     * @param link A String containing a the link to the base website for this feed
     */
    public void setLink(String link)
    {
        this.link = link;
    }

    /**
     * Sets the description of the feed to the content of the description argument.
     * @param description A String containing a descriptive text about the feed
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Sets a new refrence to the items array from the items argument
     * @param items A refrence to an array of Item objects
     */
    public void setItems(Item[] items)
    {
        this.items = items;
    }

    /**
     * Sets the path to the XML file we got the feed to the new one in the argument
     * @param pathToXML A String containing the URL to the XML file.
     */
    public void setPathToXML(String pathToXML)
    {
        this.pathToXML = pathToXML;
    }

    public void update()
    {
        throw new RuntimeException("update() not impemented");
    }

    private void getXml()
    {
        throw new RuntimeException("getXml() not impemented");
    }
}

// Created: 2016-04-17
