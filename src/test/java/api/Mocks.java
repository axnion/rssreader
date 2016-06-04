package api;

import static org.mockito.Mockito.*;

/**
 * Class Mocks
 *
 * This is a class with static methods to create the mock objects used in testing classes in the
 * api package.
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
public class Mocks
{
    /**
     * Creates a mock object of the Feed class. It sets what each accessor method should return
     * and returns the mocked object.
     *
     * @param title The title getTitle() should return.
     * @param link  The link getLink() should return.
     * @param desc  The text getDescription() should return.
     * @param url   The url getUrlToXML() should return.
     * @return      A mock object of the Feed class.
     */
    public static Feed createMockFeed(String title, String link, String desc, String url)
    {
        Feed feed = mock(Feed.class);

        when(feed.getTitle()).thenReturn(title);
        when(feed.getLink()).thenReturn(link);
        when(feed.getDescription()).thenReturn(desc);
        when(feed.getUrlToXML()).thenReturn(url);

        return feed;
    }

    /**
     * Creates a mock object of the Item class. It sets what each accessor method should return and
     * returns the mocked object.
     *
     * @param title     The title getTitle() should return.
     * @param link      The link getLink() should return.
     * @param desc      The text getDescription() should return.
     * @param id        The id the getId() should return.
     * @param isVisited The boolean value isVisited should return.
     * @param isStarred The boolean value isStarred should return.
     * @return          A mock object of the Item class.
     */
    public static Item createMockItem(String title, String link, String desc, String id,
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

    /**
     * Creates a mock object of the ItemList class. It sets what each accessor method should return
     * and returns the mocked object.
     *
     * @param name      The name getName() should return.
     * @param sorting   The String getSorting() should return.
     * @return          A mock object of the ItemList class.
     */
    public static ItemList createMockItemList(String name, String sorting)
    {
        ItemList itemList = mock(ItemList.class);

        when(itemList.getName()).thenReturn(name);
        when(itemList.getSorting()).thenReturn(sorting);

        return itemList;
    }
}

// Created: 2016-05-04
