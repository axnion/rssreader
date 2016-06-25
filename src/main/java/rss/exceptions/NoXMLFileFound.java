package rss.exceptions;

/**
 * Class NoXMLFileFound
 *
 * @author Axel Nilsson (axnion)
 */
public class NoXMLFileFound extends RuntimeException {
    public NoXMLFileFound() {
        super();
    }

    public NoXMLFileFound(String url) {
        super("No XML file found at \"" + url + "\"");
    }
}