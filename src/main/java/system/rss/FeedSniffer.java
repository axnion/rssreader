package system.rss;

import java.util.ArrayList;

/**
 * Class FeedSniffer
 *
 * This method is (to be) used as a sniffer to search for RSS feeds on a website.
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedSniffer {
    //private ArrayList<FeedMinimal> feeds;
    private RssParser rssParser;

    /**
     * Constructor
     *
     * Initializes the RssParser
     */
    public FeedSniffer() {
        //feeds = new ArrayList<>();
        rssParser = new RssParser();
    }

    /**
     * Takes a String containing a URL to an RSS feed. In the future a URL to a website should
     * search the html for links to an RSS feed.
     *
     * @param url   A String containing a link to an RSS feed.
     * @return      An ArrayList of MinimalFeed objects with the Feeds found at url.
     */
    public ArrayList<FeedMinimal> getFeeds(String url) {
        ArrayList<FeedMinimal> feeds = new ArrayList<>();
        feeds.add(rssParser.getMinimalFeed(url));
        return feeds;
    }

//    public ArrayList<FeedMinimal> getFeeds(String url) {
//        feeds.clear();
//
//        if(getFileType(url).equals("rss")) {
//            feeds.add(rssParser.getMinimalFeed(url));
//        }
//        else if(getFileType(url).equals("html")) {
//            feeds.addAll(searchHtml(url));
//        }
//
//        return feeds;
//    }
//
//    private ArrayList<FeedMinimal> searchHtml(String url) {
//        ArrayList<FeedMinimal> foundFeeds = new ArrayList<>();
//
//        try {
//            org.jsoup.nodes.Document htmlDoc = Jsoup.connect(url).get();
//            Elements elements = htmlDoc.body().getAllElements();
//
//            for(Element el : elements) {
//                if(el.nodeName().equals("a") && getFileType(el.attr("href")).equals("rss")) {
//                    foundFeeds.add(rssParser.getMinimalFeed(el.attr("href")));
//                }
//            }
//        }
//        catch(Exception err) {
//            err.printStackTrace();
//        }
//
//        return foundFeeds;
//    }
//
//    private String getFileType(String url) {
//        if(!url.substring(0, 4).equals("http")) {
//            url = "http://" + url;
//        }
//
//        try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document xmlDoc = builder.parse(url);
//
//            if(xmlDoc.getDocumentElement().getTagName().equals("system/rss")) {
//                return "system/rss";
//            }
//            else {
//                throw new Exception();
//            }
//        }
//        catch(Exception exc1) {
//            try {
//                org.jsoup.nodes.Document htmlDoc = Jsoup.connect(url).get();
//                return "html";
//            }
//            catch(Exception exc2) {
//                exc2.printStackTrace();
//                return "";
//            }
//        }
//    }
//
//    private void downloadFromLink(String url) {
//
//    }
}
