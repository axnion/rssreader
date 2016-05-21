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
        assertEquals(testItem.getDate(), "");
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
        Item testItem = new Item("A Title", "https://www.google.se", "This is a test", "a_title",
                "Mon, 01 Jan 2016 12:00:00 +0000", true, true);

        assertEquals(testItem.getTitle(), "A Title");
        assertEquals(testItem.getLink(), "https://www.google.se");
        assertEquals(testItem.getDescription(), "This is a test");
        assertEquals(testItem.getId(), "a_title");
        assertEquals(testItem.getDate(), "Mon, 01 Jan 2016 12:00:00 +0000");
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
        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        testItem.setVisited(true);
        testItem.setStarred(true);

        // Using accessors to get the values of each field.
        assertEquals(testItem.getTitle(), "A Title");
        assertEquals(testItem.getLink(), "https://www.google.se");
        assertEquals(testItem.getDescription(), "This is a test");
        assertEquals(testItem.getId(), "a_title");
        assertEquals(testItem.getDate(), "Mon, 01 Jan 2016 12:00:00 +0000");
        assertTrue(testItem.isVisited());
        assertTrue(testItem.isStarred());

        // Using mutators to edit each fields
        testItem.setTitle("B Title");
        testItem.setLink("https://www.bing.se");
        testItem.setDescription("This is another test");
        testItem.setId("b_title");
        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        testItem.setVisited(false);
        testItem.setStarred(false);

        // Using accessors to get the values of each field.
        assertEquals(testItem.getTitle(), "B Title");
        assertEquals(testItem.getLink(), "https://www.bing.se");
        assertEquals(testItem.getDescription(), "This is another test");
        assertEquals(testItem.getId(), "b_title");
        assertEquals(testItem.getDate(), "Mon, 01 Jan 2016 12:00:00 +0000");
        assertFalse(testItem.isVisited());
        assertFalse(testItem.isStarred());
    }

    /**
     * Test Case: 72
     * Checks the result of comparing by title two Items with the same title. It should return a 0.
     */
    @Test
    public void compareTitleEqual()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setTitle("A title");
        other.setTitle("A title");

        assertEquals(0, testItem.compareTitle(other));
    }

    /**
     * Test Case: 73
     * Checks the result of comparing by title two Items where the other Item is later
     * alphabetically and has a higher value. The return should be smaller than 0.
     */
    @Test
    public void compareTitleOtherHigher()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setTitle("A title");
        other.setTitle("B title");

        assertTrue(testItem.compareTitle(other) < 0);
    }

    /**
     * Test Case: 74
     * Checks the result of comparing by title two Items where the other Item is earlier
     * alphabetically and has a smaller value. The return should be larger than 0.
     */
    @Test
    public void compareTitleOtherLower()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setTitle("B title");
        other.setTitle("A title");

        assertTrue(testItem.compareTitle(other) > 0);
    }

    /**
     * Test Case: 75
     * Checks the result of comparing two Items by date. Both dates are the same so the returned
     * value should be 0.
     */
    @Test
    public void compareDateEqual()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Jan 2016 12:00:00 +0000");

        assertEquals(0, testItem.compareDate(other));
    }

    /**
     * Test Case: 76
     * Checks the result of comparing two Items by date. The other Items years is later than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherYearHigher()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2015 12:00:00 +0000");
        other.setDate("Mon, 01 Jan 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) < 0);
    }

    /**
     * Test Case: 77
     * Checks the result of comparing two Items by date. The other Items months is later than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherMonthHigher()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Feb 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) < 0);
    }

    /**
     * Test Case: 78
     * Checks the result of comparing two Items by date. The other Items days is later than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherDayHigher()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 05 Jan 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) < 0);
    }

    /**
     * Test Case: 79
     * Checks the result of comparing two Items by date. The other Items hours is later than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherHourHigher()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Jan 2016 13:00:00 +0000");

        assertTrue(testItem.compareDate(other) < 0);
    }

    /**
     * Test Case: 80
     * Checks the result of comparing two Items by date. The other Items minutes is later than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherMinuteHigher()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Jan 2016 12:15:00 +0000");

        assertTrue(testItem.compareDate(other) < 0);
    }

    /**
     * Test Case: 81
     * Checks the result of comparing two Items by date. The other Items seconds is later than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherSecondHigher()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Jan 2016 12:00:20 +0000");

        assertTrue(testItem.compareDate(other) < 0);
    }

    /**
     * Test Case: 82
     * Checks the result of comparing two Items by date. The other Items years is earlier than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherYearLower()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 02 Jan 2015 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) > 0);
    }

    /**
     * Test Case: 83
     * Checks the result of comparing two Items by date. The other Items months is earlier than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherMonthLower()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Aug 2016 12:00:00 +0000");
        other.setDate("Mon, 02 Jan 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) > 0);
    }

    /**
     * Test Case: 84
     * Checks the result of comparing two Items by date. The other Items days is earlier than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherDayLower()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 10 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 05 Jan 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) > 0);
    }

    /**
     * Test Case: 85
     * Checks the result of comparing two Items by date. The other Items hours is earlier than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherHourLower()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Jan 2016 10:00:00 +0000");

        assertTrue(testItem.compareDate(other) > 0);
    }

    /**
     * Test Case: 86
     * Checks the result of comparing two Items by date. The other Items minutes is earlier than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherMinuteLower()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:15:00 +0000");
        other.setDate("Mon, 01 Jan 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) > 0);
    }

    /**
     * Test Case: 87
     * Checks the result of comparing two Items by date. The other Items seconds is earlier than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherSecondLower()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:15 +0000");
        other.setDate("Mon, 01 Jan 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) > 0);
    }

    /**
     * Test Case: 93
     * Tests all the months so they have the correct values. It goes though all months and compare
     * them to the next month.
     */
    @Test
    public void compareMonths()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Feb 2016 12:00:00 +0000");
        assertTrue(testItem.compareDate(other) < 0);

        testItem.setDate("Mon, 01 Feb 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Mar 2016 12:00:00 +0000");
        assertTrue(testItem.compareDate(other) < 0);

        testItem.setDate("Mon, 01 Mar 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Apr 2016 12:00:00 +0000");
        assertTrue(testItem.compareDate(other) < 0);

        testItem.setDate("Mon, 01 Apr 2016 12:00:00 +0000");
        other.setDate("Mon, 01 May 2016 12:00:00 +0000");
        assertTrue(testItem.compareDate(other) < 0);

        testItem.setDate("Mon, 01 May 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Jun 2016 12:00:00 +0000");
        assertTrue(testItem.compareDate(other) < 0);

        testItem.setDate("Mon, 01 Jun 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Jul 2016 12:00:00 +0000");
        assertTrue(testItem.compareDate(other) < 0);

        testItem.setDate("Mon, 01 Jul 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Aug 2016 12:00:00 +0000");
        assertTrue(testItem.compareDate(other) < 0);

        testItem.setDate("Mon, 01 Aug 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Sep 2016 12:00:00 +0000");
        assertTrue(testItem.compareDate(other) < 0);

        testItem.setDate("Mon, 01 Sep 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Oct 2016 12:00:00 +0000");
        assertTrue(testItem.compareDate(other) < 0);

        testItem.setDate("Mon, 01 Oct 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Nov 2016 12:00:00 +0000");
        assertTrue(testItem.compareDate(other) < 0);

        testItem.setDate("Mon, 01 Nov 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Dec 2016 12:00:00 +0000");
        assertTrue(testItem.compareDate(other) < 0);
    }

    /**
     * Test case: 104
     */
    @Test
    public void compareNonexistentMonth()
    {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Bla 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Jan 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) < 0);
    }
}
// Created: 2016-04-17
