package api;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

/**
 * Test Class ConfigurationTest
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
public class ConfigurationTest
{
    private Configuration config;

    @Before
    public void createObject()
    {
        config = new Configuration();
    }

    @Test
    public void accessorsAndMutators()
    {
        assertNull(config.getFeeds());
        assertNull(config.getItemLists());

        config.setFeeds(createMockFeedArray());
        config.setItemLists(createMockItemListArray());

        assertEquals("Feed 1", config.getFeeds()[0].getTitle());
        assertEquals("Feed 2", config.getFeeds()[1].getTitle());
        assertEquals("ItemList 1", config.getItemLists()[0].getName());
        assertEquals("ItemList 2", config.getItemLists()[1].getName());
    }

    private Feed[] createMockFeedArray()
    {
        Feed[] feeds = new Feed[2];

        feeds[0] = mock(Feed.class);
        feeds[1] = mock(Feed.class);
        when(feeds[0].getTitle()).thenReturn("Feed 1");
        when(feeds[1].getTitle()).thenReturn("Feed 2");

        return feeds;
    }

    private ItemList[] createMockItemListArray()
    {
        ItemList[] itemList = new ItemList[2];

        itemList[0] = mock(ItemList.class);
        itemList[1] = mock(ItemList.class);
        when(itemList[0].getName()).thenReturn("ItemList 1");
        when(itemList[1].getName()).thenReturn("ItemList 2");

        return itemList;
    }
}

// Created: 2016-05-01