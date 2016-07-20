package rss;

import rss.exceptions.NoXMLFileFound;

import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

public class Mocks {
    public static Feed createFeedMock(String title, String link, String desc, String url,
                                      ArrayList<Item> items) {
        Feed feed = mock(Feed.class);

        doReturn(title).when(feed).getTitle();
        doReturn(link).when(feed).getLink();
        doReturn(desc).when(feed).getDescription();
        doReturn(url).when(feed).getUrlToXML();
        doReturn(items).when(feed).getItems();

        return feed;
    }

    public static Item createItemMock(String title, String link, String desc,String date,
                                      String id) {
        Item item = mock(Item.class);

        doReturn(title).when(item).getTitle();
        doReturn(link).when(item).getLink();
        doReturn(desc).when(item).getDescription();
        doReturn(new Date(Long.parseUnsignedLong(date))).when(item).getDate();
        doReturn(id).when(item).getId();

        doCallRealMethod().when(item).compareDate(any(Item.class));
        doCallRealMethod().when(item).compareTitle(any(Item.class));

        return item;
    }

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
