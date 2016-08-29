package system;

import system.exceptions.FeedAlreadyExists;
import system.exceptions.FeedDoesNotExist;
import system.rss.Feed;
import system.rss.Item;
import system.rss.RssParser;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class FeedList
 *
 * FeedList is a "list-like" object that contains Feed objects. It has a name field which is used as
 * a unique identifier for each FeedList object, a String of sorting rules which tells the sort
 * method which rules to follow when sorting the Item objects, and a showVisitedStatus which is true
 * if the visited status of Items should be shown in this FeedList. There is also a RssParser object
 * and an ArrayList of Feed objects.
 *
 * The class has some methods taken from List like get, add, remove, and size.
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedList {
    private String name;
    private String sortingRules;
    private boolean showVisitedStatus;
    private RssParser rssParser;
    private ArrayList<Feed> feeds;

    /**
     * Constructor
     * Takes the arguments passed though the parameters and assigns the values to the corresponding
     * fields. Also creates new RssParser and ArrayList.
     *
     * @param nameParam                 The unique name of the FeedList. Is used as identifier.
     * @param sortingRulesParam         The rules the sorting will follow when sorting all Item
     *                                  objects from the Feeds.
     * @param showVisitedStatusParam    True if the visited status of all Items in this FeedList
     *                                  should be displayed.
     */
    FeedList(String nameParam, String sortingRulesParam, boolean showVisitedStatusParam) {
        name = nameParam;
        sortingRules = sortingRulesParam;
        showVisitedStatus = showVisitedStatusParam;
        rssParser = new RssParser();
        feeds = new ArrayList<>();
    }

    /**
     * Returns the Feed object at the specified index passed though the index parameter.
     *
     * @param index The index of the element to be returned.
     * @return      A Feed at the specified index in the FeedList.
     */
    Feed get(int index) {
        return feeds.get(index);
    }

    /**
     * Used to add a new Feed to feeds. The url to the XML file is passed as an argument though the
     * url parameter. The url is then passed to the getFeed method in the RssParser object which
     * returns a Feed object which is added to feeds.
     *
     * @param url   The url to the XML file of the Feed to be added.
     */
    void add(String url) {
        if(getIndexOf(url) == -1)
            feeds.add(rssParser.getFeed(url));
        else
            throw new FeedAlreadyExists(url, getName());
    }

    /**
     * Used to remove the Feed object with the same url as the url passed as an argument though the
     * url parameter. If no Feed with that url is found then FeedDoesNotExist exception is thrown.
     *
     * @param url   The url to the XML file of the Feed to be removed.
     */
    void remove(String url) {
        int index = getIndexOf(url);

        if(index > -1) {
            feeds.remove(index);
        }
        else
            throw new FeedDoesNotExist(url, getName());
    }

    /**
     * Returns the amount of elements in the FeedList.
     *
     * @return  An integer describing the number of elements in the FeedList
     */
    int size() {
        return feeds.size();
    }

    /**
     * Calls the update method on all Feeds.
     *
     * @return True if a Feed got updated.
     */
    boolean update() {
        boolean updated = false;
        for(Feed feed : feeds) {
            if(rssParser.updateFeed(feed))
                updated = true;
        }
        return updated;
    }

    /**
     * Returns all Item objects in an ArrayList from all of the Feed objects the FeedList holds. The
     * returned ArrayList is also sorted according to the rules in sortingRules.
     *
     * @return  An ArrayList containing all Item objects from all Feed objects in this
     *          FeedList that is sorted according to sortingRules.
     */
    ArrayList<Item> getAllItems() {
        ArrayList<Item> items = new ArrayList<>();

        for(Feed feed : feeds) {
            items.addAll(feed.getItems());
        }

        items = sort(items);

        return items;
    }

    /**
     * Checks all Feed objects in feeds for a Feed object with a url that is the same as the
     * argument passed though the url parameter. If one is found it's returned, but if none is found
     * FeedDoesNotExist exception is thrown.
     *
     * @param url   The url of the Feed the method is searching for.
     * @return      A Feed object from feeds with a urlToXml which is the same as url.
     */
    private Feed getFeedByUrl(String url) {
        int index = getIndexOf(url);
        if(index != -1)
            return feeds.get(index);
        else
            throw new FeedDoesNotExist(url, name);
    }

    /**
     * Returns an integer containing the index of a Feed object in the FeedList with the same
     * identifier as the argument Feed.
     *
     * @param url   A String containing the url to the feed we want to get the index of.
     * @return      An integer with the index of the Feed object in the FeedList. If it does not
     *              exist then -1 is returned.
     */
    private int getIndexOf(String url) {
        for(int i = 0; i < feeds.size(); i++) {
            if(feeds.get(i).getUrlToXML().equals(url))
                return i;
        }
        return -1;
    }

    /**
     * Sorts an ArrayList of Item objects using the merge sort algorithm.
     *
     * @param items An ArrayList of Item objects to be sorted
     * @return      An ArrayList containing all Item objects from the argument ArrayList but sorted
     *              according to the rules specified in the rules argument.
     */
    private ArrayList<Item> sort(ArrayList<Item> items) {
        if(items.size() <= 1)
            return items;

        ArrayList<Item> out = new ArrayList<>();
        ArrayList<Item> left = new ArrayList<>();
        ArrayList<Item> right = new ArrayList<>();
        LinkedList<Item> leftQueue = new LinkedList<>();
        LinkedList<Item> rightQueue = new LinkedList<>();

        boolean titleAsc;
        boolean titleDec;
        boolean dateAsc;
        boolean dateDec;

        for(Item item : items) {
            if(left.size() < items.size() / 2)
                left.add(item);
            else
                right.add(item);
        }

        left = sort(left);
        right = sort(right);

        leftQueue.addAll(left);
        rightQueue.addAll(right);

        for(int i = 0; i < items.size(); i++) {
            if(leftQueue.isEmpty()) {
                out.add(rightQueue.pop());
                continue;
            }
            else if(rightQueue.isEmpty()) {
                out.add(leftQueue.pop());
                continue;
            }

            titleAsc = sortingRules.equals("TITLE_ASC") && leftQueue.peek()
                    .compareTitle(rightQueue.peek()) <= 0;
            titleDec = sortingRules.equals("TITLE_DEC") && leftQueue.peek()
                    .compareTitle(rightQueue.peek()) > 0;
            dateAsc =  sortingRules.equals("DATE_ASC")  && leftQueue.peek()
                    .compareDate(rightQueue.peek()) <= 0;
            dateDec =  sortingRules.equals("DATE_DEC")  && leftQueue.peek()
                    .compareDate(rightQueue.peek()) > 0;

            if(titleAsc || titleDec || dateAsc || dateDec) {
                out.add(leftQueue.pop());
            }
            else {
                out.add(rightQueue.pop());
            }
        }

        return out;
    }

    /**
     * Calls the setVisited method of a Feed object which identifier is equal to the argument passed
     * though feedIdentifier parameter, and passes itemId and status as arguments.
     *
     * @param feedIdentifier    The unique identifier of the Feed containing the Item.
     * @param itemId            The unique identifier of the Item to be updated.
     * @param status            The new visited status of the Item. True if the user has visited the
     *                          item, false if not.
     */
    void setVisited(String feedIdentifier, String itemId, boolean status) {
        getFeedByUrl(feedIdentifier).setVisited(itemId, status);
    }

    /**
     * Calls the setStarred method of a Feed object which identifier is equal to the argument passed
     * though feedIdentifier parameter, and passes itemId and status as arguments.
     *
     * @param feedIdentifier    The unique identifier of the Feed containing the Item.
     * @param itemId            The unique identifier of the Item to be updated.
     * @param status            The new starred status of the Item. True if the user has starred the
     *                          item, false if not.
     */
    void setStarred(String feedIdentifier, String itemId, boolean status) {
        getFeedByUrl(feedIdentifier).setStarred(itemId, status);
    }

    /*
    ------------------------------- ACCESSORS AND MUTATORS -----------------------------------------
    */

    /**
     * Accessor method for name
     *
     * @return  A String containing the value of the name field
     */
    public String getName() {
        return name;
    }

    /**
     * Accessor method for sortingRules
     *
     * @return A String containing the value of the sortingRules field
     */
    public String getSortingRules() {
        return sortingRules;
    }

    /**
     * Accessor method for showVisitedStaus
     *
     * @return True if the visited status of Items in this FeedList should be shown, false if not.
     */
    public boolean getShowVisitedStatus() {
        return showVisitedStatus;
    }

    /**
     * Accessor method for rssParser
     *
     * @return The RssParser currently in use by this FeedList
     */
    RssParser getRssParser() {
        return rssParser;
    }

    /**
     * Accessor method for feeds
     *
     * @return An ArrayList containing Feed objects
     */
    ArrayList<Feed> getFeeds() {
        return feeds;
    }

    /**
     * Mutator method for name
     *
     * @param nameParam A String containing the new value of name
     */
    void setName(String nameParam) {
        name = nameParam;
    }

    /**
     * Mutator method for sortingRules
     *
     * @param sortingRulesParam A String containing the new value of sortingRules
     */
    void setSortingRules(String sortingRulesParam) {
        sortingRules = sortingRulesParam;
    }

    /**
     * Mutator method for showVisitedStatus
     *
     * @param showVisitedStatusParam True if
     */
    void setShowVisitedStatus(boolean showVisitedStatusParam) {
        showVisitedStatus = showVisitedStatusParam;
        ArrayList<Item> items = getAllItems();

        for(Item item : items) {
            item.setVisited(true);
        }
    }

    /**
     * Mutator method for rssParser
     *
     * @param rssParserParam An RssParser to be set as this FeedLists RssParser
     */
    void setRssParser(RssParser rssParserParam) {
        rssParser = rssParserParam;
    }

    /**
     * Mutator method for feeds
     *
     * @param feedsParam An ArrayList of Feed objects to be set as the new feeds
     */
    void setFeeds(ArrayList<Feed> feedsParam) {
        feeds = feedsParam;
    }
}
