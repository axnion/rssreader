package api;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class Feed
 *
 * The Feed class represents an rss feed. It has the title of the feed or channel, a link to the
 * "base" website, and it has a text describing the content of the feed. It also has an array of
 * Item objects represents all the items in the feed, and lastly it has a url to the XML file to be
 * able to update.
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1.1
 */
public class Feed
{
    private String title;           // The title of the feed
    private String link;            // Link to the feeds website
    private String description;     // A description about the feed
    private String urlToXML;        // The URL to the XML file, used when updating the feed
    private Item[] items;           // An array holding the items in the feed

    /**
     * Constructor
     */
    Feed()
    {
        title = "";
        link = "";
        description = "";
        items = null;
        urlToXML = "";
    }

    /**
     * Constructor
     * @param url The URL to the XML file
     */
    Feed(String url)
    {
        title = "";
        link = "";
        description = "";
        urlToXML = url;
        update();
    }

    /**
     * Updates the object by checking the XML file for new items and syncing them to the object
     */
    void update()
    {
        ArrayList<Item> upToDateItems = getXml();
        syncItems(upToDateItems);
    }

    /**
     * Takes the URL to the XML file and downloads the document. Then the title, link and
     * description is updated. Then an array of Item objects are created from the data in the XML
     * file.
     * @return An ArrayList of Item objects from the XML file
     */
    private ArrayList<Item> getXml()
    {
        ArrayList<Item> itemsArray = new ArrayList<>();
        Document document;
        NodeList channel;

        String newTitle = "Untitled";
        String newDescription = "";
        String newLink = "";

        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(urlToXML);
            document.getDocumentElement().normalize();

            channel = document.getElementsByTagName("channel").item(0).getChildNodes();
        }
        catch(Exception err)
        {
            throw new RuntimeException("Could not read file at " + urlToXML);
        }

        for(int i = 0; i < channel.getLength(); i++)
        {
            if(channel.item(i).getNodeName().equals("title"))
            {
                newTitle = channel.item(i).getTextContent();
            }
            else if(channel.item(i).getNodeName().equals("description"))
            {
                newDescription = channel.item(i).getTextContent();
            }
            else if(channel.item(i).getNodeName().equals("link"))
            {
                newLink = channel.item(i).getTextContent();
            }
            else if(channel.item(i).getNodeName().equals("item"))
            {
                Element itemElement = (Element) channel.item(i);
                Item item = new Item();

                try
                {
                    item.setId(itemElement.getElementsByTagName("guid").item(0).getFirstChild()
                            .getNodeValue());
                }
                catch(RuntimeException err)
                {
                    continue;
                }

                try
                {
                    item.setTitle(itemElement.getElementsByTagName("title").item(0).getFirstChild()
                            .getNodeValue());
                }
                catch(RuntimeException err)
                {
                    item.setTitle("Untitled");
                }

                try
                {
                    item.setLink(itemElement.getElementsByTagName("link").item(0).getFirstChild()
                            .getNodeValue());
                }
                catch(RuntimeException err)
                {
                    continue;
                }

                try
                {
                    item.setDate(itemElement.getElementsByTagName("pubDate").item(0).getFirstChild()
                            .getNodeValue());
                }
                catch(RuntimeException err)
                {
                    item.setDate("Mon, 01 Jan 0001 00:00:00 +0000");
                }

                try
                {
                    item.setDescription(itemElement.getElementsByTagName("description").item(0)
                            .getFirstChild().getNodeValue());
                }
                catch(RuntimeException err)
                {
                    item.setDescription("");
                }

                itemsArray.add(item);
            }
        }

        setTitle(newTitle);
        setLink(newLink);
        setDescription(newDescription);

        return itemsArray;
    }

    /**
     * Takes an array of Item object and compares it to the objects array of Item objects. We want
     * all new object in the correct order in the beginning, but still maintain the old objects with
     * their fields untouched.
     * @param upToDateItems An ArrayList containing an up to date list of Item object
     */
    private void syncItems(ArrayList<Item> upToDateItems)
    {
        ArrayList<Item> newItemList = new ArrayList<>();
        Item[] newItems;

        if(items == null)
        {
            newItemList = upToDateItems;
            for(Item item : newItemList)
            {
                item.setVisited(true);
            }
        }
        else
        {
            ArrayList<Item> oldItems = new ArrayList<>(Arrays.asList(items));
            boolean match;

            for(Item upToDateItem : upToDateItems)
            {
                match = false;

                for(Item oldItem : oldItems)
                {
                    if(oldItem.getId().equals(upToDateItem.getId()))
                    {
                        newItemList.add(oldItem);
                        match = true;
                        break;
                    }
                }

                if(!match)
                    newItemList.add(upToDateItem);
            }
        }

        if(upToDateItems.size() == 0)
            newItems = null;
        else
        {
            newItems = new Item[newItemList.size()];
            for(int i = 0; i < newItems.length; i++)
                newItems[i] = newItemList.get(i);
        }

        setItems(newItems);
    }

    /*
    ------------------------------- ACCESSORS AND MUTATORS -----------------------------------------
    */

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
     * @return An ArrayList of Item objects that represents the items in the feed
     */
    public Item[] getItems()
    {
        return items;
    }

    /**
     * @return A String containging the url to the XML file
     */
    public String getUrlToXML()
    {
        return urlToXML;
    }

    /**
     * Sets the title of the feed to the content of title argument.
     * @param title A String containing the title we want to set the feeds title to.
     */
    void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Sets the link of the feed to the content of the link argument
     * @param link A String containing a the link to the base website for this feed
     */
    void setLink(String link)
    {
        this.link = link;
    }

    /**
     * Sets the description of the feed to the content of the description argument.
     * @param description A String containing a descriptive text about the feed
     */
    void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Sets a new refrence to the items array from the items argument
     * @param items A ArrayListof Item objects
     */
    void setItems(Item[]items)
    {
        this.items = items;
    }

    /**
     * Sets the path to the XML file we got the feed to the new one in the argument
     * @param urlToXML A String containing the URL to the XML file.
     */
    void setUrlToXML(String urlToXML)
    {
        this.urlToXML = urlToXML;
    }

}

// Created: 2016-04-17
