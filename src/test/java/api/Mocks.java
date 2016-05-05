package api;

import static org.mockito.Mockito.*;

/**
 * Class Mocks
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
public class Mocks
{
    static Feed createMockFeed(String title, String link, String desc, String url)
    {
        Feed feed = mock(Feed.class);

        when(feed.getTitle()).thenReturn(title);
        when(feed.getLink()).thenReturn(link);
        when(feed.getDescription()).thenReturn(desc);
        when(feed.getUrlToXML()).thenReturn(url);

        return feed;
    }

    static Item createMockItem(String title, String link, String desc, String id,
                                boolean isVisited, boolean isStarred)
    {
        Item item = mock(Item.class);

        when(item.getTitle()).thenReturn(title);
        when(item.getLink()).thenReturn(link);
        when(item.getDescription()).thenReturn(desc);
        when(item.getId()).thenReturn(id);
        when(item.isVisited()).thenReturn(isVisited);
        when(item.isStarred()).thenReturn(isStarred);

        return item;
    }

    static ItemList createMockItemList(String name, String sorting)
    {
        ItemList itemList = mock(ItemList.class);

        when(itemList.getName()).thenReturn(name);
        when(itemList.getSorting()).thenReturn(sorting);

        return itemList;
    }
}

// Created: 2016-05-04
