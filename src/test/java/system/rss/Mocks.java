package system.rss;

import system.rss.exceptions.NoXMLFileFound;

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.*;

/**
 * Class Mocks
 *
 * This class contains static methods for creating Mocks of different kinds to imitate objects in
 * the rss package.
 *
 * @author Axel Nilsson (axnion)
 */
public class Mocks {
    /**
     * Creates a mock of a Feed object which can return the title, link, description, url, and
     * items.
     *
     * @param title The title of the Feed
     * @param link  The link to the Feed
     * @param desc  The description of the Feed
     * @param url   The URL to the XML file
     * @param items An ArrayList of Items.
     * @return      A Feed mock which can return its title, link, description, url, and items.
     */
    public static Feed createFeedMock(String title, String link, String desc, String url,
                                      ArrayList<Item> items) {
        Feed feed = mock(Feed.class);

        doReturn(title).when(feed).getTitle();
        doReturn(link).when(feed).getLink();
        doReturn(desc).when(feed).getDescription();
        doReturn(url).when(feed).getUrlToXML();
        doReturn(items).when(feed).getItems();

        for(Item item : items)
            when(feed.getItemById(item.getId())).thenReturn(item);

        return feed;
    }

    /**
     * Creates a mock of an Item object which can return the title, link, description, date, id,
     * visited status and starred status.
     *
     * @param title         The title of the Item
     * @param link          The link to the Item
     * @param desc          The description of the Item
     * @param date          A Date object of the time stamp from the rs feed.
     * @param id            The identifier of the Item
     * @param visitedStatus The visited status of the Item
     * @param starredStatus The starred status of the item
     * @return              An Item mock which can return it's title, link, description, date, id,
     *                      visited status and starred status.
     */
    public static Item createItemMock(String title, String link, String desc,String date,
                                      String id, boolean visitedStatus, boolean starredStatus) {
        Item item = mock(Item.class);

        doReturn(title).when(item).getTitle();
        doReturn(link).when(item).getLink();
        doReturn(desc).when(item).getDescription();
        doReturn(new Date(Long.parseUnsignedLong(date))).when(item).getDate();
        doReturn(id).when(item).getId();
        doReturn(visitedStatus).when(item).isVisited();
        doReturn(starredStatus).when(item).isStarred();

        doCallRealMethod().when(item).compareDate(any(Item.class));
        doCallRealMethod().when(item).compareTitle(any(Item.class));

        doNothing().when(item).setVisited(anyBoolean());
        doNothing().when(item).setStarred(anyBoolean());

        return item;
    }

    /**
     * Creates an RssParser mock which returns diffrent results depending on what is passed as an
     * argument on getFeed method.
     *
     * @return An RssParser mock which returns diffrent results depending on the argument passed
     * to it.
     */
    public static RssParser createRssParser() {
        RssParser rssParser = mock(RssParser.class);

        ArrayList<Item> items = new ArrayList<>();

        doReturn(Mocks.createFeedMock("FeedTitle1", "http://feed-website-1.com",
                "Description1", "http://feed-website-1.com/feed.xml", items)).when(rssParser)
                .getFeed("http://feed-website-1.com/feed.xml");

        doReturn(Mocks.createFeedMock("FeedTitle3", "http://feed-website-3.com",
                "Description3", "http://feed-website-3.com/feed.xml", items)).when(rssParser)
                .getFeed("http://feed-website-3.com/feed.xml");

        doThrow(new NoXMLFileFound("http://feed-website-4.com/feed.xml")).when(rssParser)
                .getFeed("http://feed-website-4.com/feed.xml");

        return rssParser;
    }
}
