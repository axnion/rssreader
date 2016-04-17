package api;

import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mockito.*;

/**
 * Class ItemTest
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
public class ItemTest
{
    /**
     * Testing the constructor without parameters and compare the values of the fields to the
     * expected results using the accessors.
     *
     */
    @Test
    public void createWithoutParam()
    {
        Item testItem = new Item();

        assertEquals(testItem.getTitle(), "");
        assertEquals(testItem.getLink(), "");
        assertEquals(testItem.getDescription(), "");
        assertEquals(testItem.getId(), "");
        assertFalse(testItem.isVisited());
        assertFalse(testItem.isStarred());
    }

    /**
     * Testing the constructor without parameters and compare the values of the fields to the
     * expected results using the accessors.
     */
    @Test
    public void createWithParam()
    {
        Item testItem = new Item("A Title", "https://www.google.se", "This is a test", "a_title", true, true);

        assertEquals(testItem.getTitle(), "A Title");
        assertEquals(testItem.getLink(), "https://www.google.se");
        assertEquals(testItem.getDescription(), "This is a test");
        assertEquals(testItem.getId(), "a_title");
        assertTrue(testItem.isVisited());
        assertTrue(testItem.isStarred());
    }
}

// Created: 2016-04-17
