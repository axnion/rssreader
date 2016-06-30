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
 * @author Axel Nilsson (axnion)
 */
public class RssParser {
    public RssParser() {

    }

    public Feed getFeed(String url) {
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

        String title = getTitle(channel);
        String link = getLink(channel);
        String description = getDescription(channel);
        ArrayList<Item> items = getItems(channel);

        return new Feed(title, link, description,url, items);
    }

    private String getTitle(Element channel) {
        Node node = channel.getElementsByTagName("title").item(0);

        if(node.getParentNode().getNodeName().equals("channel")) {
            return node.getFirstChild().getNodeValue();
        }

        return "Untitled";
    }

    private String getLink(Element channel) {
        Node node = channel.getElementsByTagName("link").item(0);

        if(node.getParentNode().getNodeName().equals("channel")) {
            return node.getFirstChild().getNodeValue();
        }

        return "";
    }

    private String getDescription(Element channel) {
        Node node = channel.getElementsByTagName("description").item(0);

        if(node.getParentNode().getNodeName().equals("channel")) {
            return node.getFirstChild().getNodeValue();
        }

        return "";
    }

    private ArrayList<Item> getItems(Element channel) {
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

                items.add(item);
            }
        }

        return items;
    }
}
