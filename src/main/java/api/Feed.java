package api;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;

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
    private ArrayList<Item> items;
    private String urlToXML;

    /**
     * Constructor
     */
    public Feed()
    {
        title = "";
        link = "";
        description = "";
        items = new ArrayList<>();
        urlToXML = "";
    }

    /**
     * Constructor
     * @param url The URL to the XML file
     */
    public Feed(String url)
    {
        title = "";
        link = "";
        description = "";
        items = new ArrayList<>();
        urlToXML = url;
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
     * @return An ArrayList of Item objects that represents the items in the feed
     */
    public ArrayList<Item> getItems()
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
     * @param items A ArrayListof Item objects
     */
    public void setItems(ArrayList<Item> items)
    {
        this.items = items;
    }

    /**
     * Sets the path to the XML file we got the feed to the new one in the argument
     * @param urlToXML A String containing the URL to the XML file.
     */
    public void setUrlToXML(String urlToXML)
    {
        this.urlToXML = urlToXML;
    }

    /**
     * Updates the object by checking the XML file for new items and syncing them to the object
     */
    public void update()
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
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(urlToXML);
            document.getDocumentElement().normalize();

            setTitle(document.getElementsByTagName("title").item(0).getFirstChild().getNodeValue());
            setLink(document.getElementsByTagName("link").item(0).getFirstChild().getNodeValue());
            setDescription(document.getElementsByTagName("description").item(0).getFirstChild().getNodeValue());

            NodeList itemsList = document.getElementsByTagName("item");
            Element itemElement;
            for(int i = 0; i < itemsList.getLength(); i++)
            {
                itemElement = (Element) itemsList.item(i);
                Item item = new Item();

                try
                {
                    item.setTitle(itemElement.getElementsByTagName("title").item(0).getFirstChild().getNodeValue());
                }
                catch(NullPointerException err)
                {
                    err.printStackTrace();
                }

                try
                {
                    item.setDescription(itemElement.getElementsByTagName("description").item(0).getFirstChild().getNodeValue());
                }
                catch(NullPointerException err)
                {
                    err.printStackTrace();
                }

                try
                {
                    item.setLink(itemElement.getElementsByTagName("link").item(0).getFirstChild().getNodeValue());
                }
                catch(NullPointerException err)
                {
                    err.printStackTrace();
                }

                try
                {
                    item.setId(itemElement.getElementsByTagName("guid").item(0).getFirstChild().getNodeValue());
                }
                catch(NullPointerException err)
                {
                    err.printStackTrace();
                }

                itemsArray.add(item);
            }
        }
        catch(Exception err)
        {
            err.printStackTrace();
        }

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
        ArrayList<Item> itemList = new ArrayList<Item>();
        int counter = 0;

        if(items.size() == 0)
        {
            itemList = upToDateItems;
        }
        else
        {
            while(upToDateItems.get(counter).equals(items.get(0)))
            {
                itemList.add(upToDateItems.get(counter));
                counter++;
            }
        }

        for(int i = 0; i < items.size(); i++)
        {
            System.out.println("MJAU");
            itemList.add(items.get(i));
        }

        setItems(itemList);
    }
}

// Created: 2016-04-17
