package api;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class ItemTest
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1.1
 */
public class ItemTest
{
    /**
     * Unit test case: 1
     * Testing the constructor without parameters and compare the values of the fields to the
     * expected results using the accessors.
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
     * Unit test case: 2
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

    /**
     * Unit test case: 3
     * Testing the accessors and mutators so we can determine if they are setting the correct values
     * and returning the same values.
     */
    @Test
    public void accessorsAndMutators()
    {
        Item testItem = new Item();

        // Using mutators to set a value to every fields
        testItem.setTitle("A Title");
        testItem.setLink("https://www.google.se");
        testItem.setDescription("This is a test");
        testItem.setId("a_title");
        testItem.setVisited(true);
        testItem.setStarred(true);

        // Using accessors to get the values of each field.
        assertEquals(testItem.getTitle(), "A Title");
        assertEquals(testItem.getLink(), "https://www.google.se");
        assertEquals(testItem.getDescription(), "This is a test");
        assertEquals(testItem.getId(), "a_title");
        assertTrue(testItem.isVisited());
        assertTrue(testItem.isStarred());

        // Using mutators to edit each fields
        testItem.setTitle("B Title");
        testItem.setLink("https://www.bing.se");
        testItem.setDescription("This is another test");
        testItem.setId("b_title");
        testItem.setVisited(false);
        testItem.setStarred(false);

        // Using accessors to get the values of each field.
        assertEquals(testItem.getTitle(), "B Title");
        assertEquals(testItem.getLink(), "https://www.bing.se");
        assertEquals(testItem.getDescription(), "This is another test");
        assertEquals(testItem.getId(), "b_title");
        assertFalse(testItem.isVisited());
        assertFalse(testItem.isStarred());
    }
}

// Created: 2016-04-17
