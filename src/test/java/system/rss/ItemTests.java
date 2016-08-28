package system.rss;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Class ItemTests
 *
 * This is the test suite with tests for the Item class.
 *
 * @author Axel Nilsson (axnion)
 */
public class ItemTests {
    /**
     * Name: Create without param
     * Unit: Item()
     *
     * Testing the constructor without parameters and compare the values of the fields to the
     * expected results using the accessors.
     */
    @Test
    public void createWithoutParam() {
        Item testItem = new Item();

        assertEquals(testItem.getTitle(), "");
        assertEquals(testItem.getLink(), "");
        assertEquals(testItem.getDescription(), "");
        assertEquals(testItem.getId(), "");
        assertTrue(testItem.getDate().before(new Date()));
    }

    /**
     * Name: Accessors and mutators
     * Unit: getTitle(), getLink(), getDescription(), getId(), getDate(), getFeedIdentifier(),
     *       isVisited(), isStarred(), setTitle(String), setLink(String), setDescription(String),
     *       setId(String), setDate(String), setFeedIdentifier(String), setVisited(boolean),
     *       setStarred(boolean)
     *
     * Testing the accessors and mutators so we can determine if they are setting the correct values
     * and returning the same values.
     */
    @Test
    public void accessorsAndMutators() {
        Item testItem = new Item();

        // Using mutators to set a value to every fields
        testItem.setTitle("A Title");
        testItem.setLink("https://www.google.se");
        testItem.setDescription("This is a test");
        testItem.setId("a_title");
        testItem.setFeedIdentifier("my_feed");
        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        testItem.setVisited(true);
        testItem.setStarred(true);

        // Using accessors to get the values of each field.
        assertEquals(testItem.getTitle(), "A Title");
        assertEquals(testItem.getLink(), "https://www.google.se");
        assertEquals(testItem.getDescription(), "This is a test");
        assertEquals(testItem.getId(), "a_title");
        assertEquals(testItem.getFeedIdentifier(), "my_feed");
        assertEquals(testItem.getDate().getTime() / 1000, 1451649600);
        assertEquals(testItem.isVisited(), true);
        assertEquals(testItem.isStarred(), true);
    }

    /**
     * Name: Compare title equal
     * Unit: compareTitle(Item)
     *
     * Checks the result of comparing by title two Items with the same title. It should return a 0.
     */
    @Test
    public void compareTitleEqual() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setTitle("A title");
        other.setTitle("A title");

        assertEquals(0, testItem.compareTitle(other));
    }

    /**
     * Name: Compare title other higher
     * Unit: compareTitle(Item)
     *
     * Checks the result of comparing by title two Items where the other Item is later
     * alphabetically and has a higher value. The return should be smaller than 0.
     */
    @Test
    public void compareTitleOtherHigher() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setTitle("A title");
        other.setTitle("B title");

        assertTrue(testItem.compareTitle(other) < 0);
    }

    /**
     * Name: Compare title other lower
     * Unit: compareTitle(Item)
     *
     * Checks the result of comparing by title two Items where the other Item is earlier
     * alphabetically and has a smaller value. The return should be larger than 0.
     */
    @Test
    public void compareTitleOtherLower() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setTitle("B title");
        other.setTitle("A title");

        assertTrue(testItem.compareTitle(other) > 0);
    }

    /**
     * Name: Compare date equal
     * Unit: compareDate(Item)
     *
     * Checks the result of comparing two Items by date. Both dates are the same so the returned
     * value should be 0.
     */
    @Test
    public void compareDateEqual() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Jan 2016 12:00:00 +0000");

        assertEquals(0, testItem.compareDate(other));
    }

    /**
     * Name: Compare date other year higher
     * Unit: compareDate(Item)
     *
     * Checks the result of comparing two Items by date. The other Items years is later than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherYearHigher() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2015 12:00:00 +0000");
        other.setDate("Mon, 01 Jan 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) < 0);
    }

    /**
     * Name: Compare date other month higher
     * Unit: compareDate(Item)
     *
     * Checks the result of comparing two Items by date. The other Items months is later than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherMonthHigher() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Feb 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) < 0);
    }

    /**
     * Name: Compare date other day higher
     * Unit: compareDate(Item)
     *
     * Checks the result of comparing two Items by date. The other Items days is later than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherDayHigher() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 05 Jan 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) < 0);
    }

    /**
     * Name: Compare date other hour higher
     * Unit: compareDate(Item)
     *
     * Checks the result of comparing two Items by date. The other Items hours is later than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherHourHigher() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Jan 2016 13:00:00 +0000");

        assertTrue(testItem.compareDate(other) < 0);
    }

    /**
     * Name: Compare date other minute higher
     * Unit: compareDate(Item)
     *
     * Checks the result of comparing two Items by date. The other Items minutes is later than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherMinuteHigher() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Jan 2016 12:15:00 +0000");

        assertTrue(testItem.compareDate(other) < 0);
    }

    /**
     * Name: Compare date other second higher
     * Unit: compareDate(Item)
     *
     * Checks the result of comparing two Items by date. The other Items seconds is later than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherSecondHigher() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Jan 2016 12:00:20 +0000");

        assertTrue(testItem.compareDate(other) < 0);
    }

    /**
     * Name: Compare date other year lower
     * Unit: compareDate(Item)
     *
     * Checks the result of comparing two Items by date. The other Items years is earlier than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherYearLower() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 02 Jan 2015 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) > 0);
    }

    /**
     * Name: Compare date other month lower
     * Unit: compareDate(Item)
     *
     * Checks the result of comparing two Items by date. The other Items months is earlier than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherMonthLower() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Aug 2016 12:00:00 +0000");
        other.setDate("Mon, 02 Jan 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) > 0);
    }

    /**
     * Name: Compare date other day lower
     * Unit: compareDate(Item)
     *
     * Checks the result of comparing two Items by date. The other Items days is earlier than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherDayLower() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 10 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 05 Jan 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) > 0);
    }

    /**
     * Name: Compare date other hour lower
     * Unit: compareDate(Item)
     *
     * Checks the result of comparing two Items by date. The other Items hours is earlier than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherHourLower() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Jan 2016 10:00:00 +0000");

        assertTrue(testItem.compareDate(other) > 0);
    }

    /**
     * Name: Compare date other minute lower
     * Unit: compareDate(Item)
     *
     * Checks the result of comparing two Items by date. The other Items minutes is earlier than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherMinuteLower() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:15:00 +0000");
        other.setDate("Mon, 01 Jan 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) > 0);
    }

    /**
     * Name: Compare date other second lower
     * Unit: compareDate(Item)
     *
     * Checks the result of comparing two Items by date. The other Items seconds is earlier than this
     * objects so the result should be smaller than 0.
     */
    @Test
    public void compareDateOtherSecondLower() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Jan 2016 12:00:15 +0000");
        other.setDate("Mon, 01 Jan 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) > 0);
    }

    /**
     * Name: Compare months
     * Unit: compareDate(Item)
     *
     * Tests all the months so they have the correct values. It goes though all months and compare
     * them to the next month.
     */
    @Test
    public void compareMonths() {
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
     * Name: Compare nonexistent months
     * Unit: compareDate(Item)
     *
     * Tries to set the date with a non existing month.
     */
    @Test
    public void compareNonexistentMonths() {
        Item testItem = new Item();
        Item other = new Item();

        testItem.setDate("Mon, 01 Bla 2016 12:00:00 +0000");
        other.setDate("Mon, 01 Jan 2016 12:00:00 +0000");

        assertTrue(testItem.compareDate(other) < 0);
    }


}