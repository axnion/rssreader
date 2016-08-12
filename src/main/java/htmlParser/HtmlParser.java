package htmlParser;

import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import system.rss.exceptions.NoXMLFileFound;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 * Class HtmlParser
 *
 * @author Axel Nilsson (axnion)
 */
public class HtmlParser {
    private TextFlow parsedText;

    public HtmlParser() {
        parsedText = new TextFlow();
    }

    public TextFlow getAsTextFlow(String html) {
        parsedText = new TextFlow();
        Document document;

        String str = "<html>";
        html = str + html;
        html += "</html>";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(html)));
            document.getDocumentElement().normalize();
        }
        catch(Exception err) {
            throw new NoXMLFileFound(html);     // CREATE A NEW EXCEPTION FOR THIS!
        }

        parse(document.getDocumentElement(), new StringBuilder());

        return parsedText;
    }

    private void parse(Node node, StringBuilder addon) {
        if(node.getNodeName().equals("p")) {
            Text text = new Text(node.getFirstChild().getNodeValue());
            parsedText.getChildren().add(text);
        }
        else if(node.getNodeName().equals("a")) {
            // LINKS ARE NOT WORKING
            NamedNodeMap attributes = node.getAttributes();
            Hyperlink link = new Hyperlink(attributes.getNamedItem("href").getNodeValue());

            link.setText(node.getFirstChild().getNodeValue());

            parsedText.getChildren().add(link);
        }
        else if(node.getNodeName().equals("ul")) {
            if(addon.length() == 0) {
                addon.append("* ");
            }
            else if(addon.charAt(0) == '*') {
                addon.append("\t");
            }
        }
        else if(node.getNodeName().equals("li")) {
            Text text = new Text(addon.toString());
            parsedText.getChildren().add(text);
        }

        NodeList nodeList = node.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if(currentNode.getNodeType() == Node.ELEMENT_NODE) {
                parse(currentNode, addon);
            }
        }

        if(node.getNodeName().equals("p")) {
            Text text = new Text("\n");
            parsedText.getChildren().add(text);
        }
        else if(node.getNodeName().equals("ul")) {
            if(addon.length() >= 3 && addon.charAt(2) == '\t') {
                addon.deleteCharAt(2);
            }
            else {
                addon.deleteCharAt(1);
                addon.deleteCharAt(0);
            }
            String str = addon.toString();
            System.out.println(str);
        }
        else if(node.getNodeName().equals("li")) {
            Text text = new Text("\n");
            parsedText.getChildren().add(text);
        }
    }
}
