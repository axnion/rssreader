package rss;

import rss.exceptions.NoXMLFileFound;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.util.ArrayList;

/**
 * Class RssParser
 *
 * RssParser class is used to create Feed objects from a URL to an XML file containing an RSS feed.
 *
 * It has two public methods. getFeed takes a URL to an XML document with an RSS feed as an argument
 * and creates a new Feed object with the information from the rss feed.
 * updateFeed takes an already existing Feed and updates all information and Items in the Feed.
 *
 * @author Axel Nilsson (axnion)
 */
public class RssParser {

    /**
     * Constructor
     */
    public RssParser() {

    }

    /**
     * Uses the other methods in the class to read the XML file and creates and returns a new Feed
     * object with the results from the methods.
     *
     * @param url   A String containing the URL to the XML file.
     * @return      A Feed representing the content of the feed from the XML file at url.
     */
    public Feed getFeed(String url) {
        Element channel = getChannelElement(url);

        String title = getTitle(channel);
        String link = getLink(channel);
        String description = getDescription(channel);
        ArrayList<Item> items = getItems(channel, url);

        return new Feed(title, link, description,url, items);
    }

    /**
     * Updates an already created Feed object. It takes one Feed as an argument and updates the
     * Feeds title, link and description. It also gets all items from the rss feed and compares the
     * updated ArrayList with the old and copies visited and starred status to the new Items and
     * determines which Items are new. Returns the update status.
     *
     * @param feed  The Feed object to be updated.
     * @return      True if
     */
    public boolean updateFeed(Feed feed) {
        Element channel = getChannelElement(feed.getUrlToXML());
        boolean updated = false;

        feed.setTitle(getTitle(channel));
        feed.setLink(getLink(channel));
        feed.setDescription(getDescription(channel));

        boolean newItemStatus;
        ArrayList<Item> oldItems = feed.getItems();
        ArrayList<Item> newItems = getItems(channel, feed.getUrlToXML());

        for(Item newItem : newItems) {
            newItemStatus = true;
            newItem.setVisited(false);
            newItem.setStarred(false);
            for(Item oldItem : oldItems) {
                if(newItem.getId().equals(oldItem.getId())) {
                    newItem.setVisited(oldItem.isVisited());
                    newItem.setStarred(oldItem.isStarred());
                    newItemStatus = false;
                }
            }
            if(newItemStatus)
                updated = true;
        }

        feed.setItems(newItems);
        return updated;
    }

    /**
     * Takes a URL to an XML file with an rss feed and creates a Document object. It then gets the
     * channel element and returns it. If something goes wrong while reading the XML file a
     * NoXMLFileFound exception is thrown.
     *
     * @param url   A String containing the URL to the XML file.
     * @return      The channel element from the XML file.
     */
    private Element getChannelElement(String url) {
        Element channel;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(url);
            document.getDocumentElement().normalize();
            channel = (Element) document.getElementsByTagName("channel").item(0);
        }
        catch(Exception err) {
            throw new NoXMLFileFound(url);
        }

        return channel;
    }

    /**
     * Takes the channel Element and gets the title node and returns the value. If no title node is
     * found then a String with 'Untitled' is returned.
     *
     * @param channel   The channel Element of the Document.
     * @return          A String containing the title of the feed.
     */
    private String getTitle(Element channel) {
        Node node = channel.getElementsByTagName("title").item(0);

        if(node.getParentNode().getNodeName().equals("channel")) {
            return node.getFirstChild().getNodeValue();
        }

        return "Untitled";
    }

    /**
     * Takes the channel Element and gets the link node and returns the value. If no link node is
     * found then an empty String is returned.
     *
     * @param channel   The channel Element of the Document.
     * @return          A String containing the link of the feed.
     */
    private String getLink(Element channel) {
        Node node = channel.getElementsByTagName("link").item(0);

        if(node.getParentNode().getNodeName().equals("channel")) {
            return node.getFirstChild().getNodeValue();
        }

        return "";
    }

    /**
     * Takes the channel Element and gets the description node and returns the value. If no
     * description is found then an empty String is returned.
     *
     * @param channel   The channel Element of the Document.
     * @return          A String containing the description of the feed.
     */
    private String getDescription(Element channel) {
        Node node = channel.getElementsByTagName("description").item(0);

        if(node.getParentNode().getNodeName().equals("channel")) {
            return node.getFirstChild().getNodeValue();
        }

        return "";
    }

    /**
     * Iterates through every node in the channel Element and looks for item nodes. If an item node
     * is found then the method will get the id, title, link, pubDate, and description from the item
     * node and produce a new Item object and put it in the ArrayList. The ArrayList is then
     * returned.
     *
     * @param channel   The channel Element of the Document.
     * @param url       The URL to the XML file, is added to every Item as Feed identifier.
     * @return          An ArrayList of Item objects from the RSS feed.
     */
    private ArrayList<Item> getItems(Element channel, String url) {
        NodeList nodes = channel.getChildNodes();
        ArrayList<Item> items = new ArrayList<>();

        for(int i = 0; i < nodes.getLength(); i++) {
            if(nodes.item(i).getNodeName().equals("item")) {
                Element itemElement = (Element) nodes.item(i);
                Item item = new Item();

                try {
                    item.setId(itemElement.getElementsByTagName("guid").item(0).getFirstChild()
                            .getNodeValue());
                }
                catch(RuntimeException err) {
                    continue;
                }

                try {
                    item.setTitle(itemElement.getElementsByTagName("title").item(0).getFirstChild()
                            .getNodeValue());
                }
                catch(RuntimeException err) {
                    item.setTitle("Untitled");
                }

                try {
                    item.setLink(itemElement.getElementsByTagName("link").item(0).getFirstChild()
                            .getNodeValue());
                }
                catch(RuntimeException err) {
                    continue;
                }

                try {
                    item.setDate(itemElement.getElementsByTagName("pubDate").item(0).getFirstChild()
                            .getNodeValue());
                }
                catch(RuntimeException err) {
                    item.setDate("Mon, 01 Jan 0001 00:00:00 +0000");
                }

                try {
                    item.setDescription(itemElement.getElementsByTagName("description").item(0)
                            .getFirstChild().getNodeValue());
                }
                catch(RuntimeException err) {
                    item.setDescription("");
                }

                item.setFeedIdentifier(url);
                items.add(item);
            }
        }
        return items;
    }
}
