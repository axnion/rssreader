package system;

import system.exceptions.FeedAlreadyExists;
import system.exceptions.FeedDoesNotExist;
import rss.Feed;
import rss.Item;
import rss.RssParser;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class FeedList
 *
 * FeedList is a "list-like" object that contains Feed objects. It has a name that is used as an
 * identifier for each FeedList and an ArrayList of Feed objects.
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedList {
    private String name;
    private String sortingRules;
    private RssParser rssParser;
    private boolean showVisitedStatus;
    private ArrayList<Feed> feeds;

    /**
     * Constructor
     *
     * @param name  The name the FeedList is to be identified by.
     */
    FeedList(String name, String sortingRules, boolean showVisitedStatus) {
        this.name = name;
        this.sortingRules = sortingRules;
        this.rssParser = new RssParser();
        this.showVisitedStatus = showVisitedStatus;
        this.feeds = new ArrayList<>();
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
     * @return  An integer describing the number of elements in the FeedList
     */
    int size() {
        return feeds.size();
    }

    /**
     * Calls the update method on all Feeds.
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
    Feed getFeedByUrl(String url) {
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

    void setVisited(String feedIdentifier, String itemId, boolean status) {
        getFeedByUrl(feedIdentifier).setVisited(itemId, status);
    }

    void setStarred(String feedIdentifier, String itemId, boolean status) {
        getFeedByUrl(feedIdentifier).setStarred(itemId, status);
    }

    /*
    ------------------------------- ACCESSORS AND MUTATORS -----------------------------------------
    */

    /**
     * Accessor method for name
     * @return  A String containing the value of the name field
     */
    public String getName() {
        return name;
    }

    /**
     * Accessor method for sortingRules
     * @return A String containing the value of the sortingRules field
     */
    public String getSortingRules() {
        return sortingRules;
    }

    /**
     * Accessor method for rssParser
     * @return An RssParser object
     */
    RssParser getRssParser() {
        return rssParser;
    }

    public boolean getShowVisitedStatus() {
        return showVisitedStatus;
    }

    /**
     * Accessor method for feeds
     * @return An ArrayList containing Feed objects
     */
    ArrayList<Feed> getFeeds() {
        return feeds;
    }

    /**
     * Mutator method for name
     * @param name A String containing the new value of name
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Mutator method for sortingRules
     * @param sortingRules A String containing the new value of sortingRules
     */
    void setSortingRules(String sortingRules) {
        this.sortingRules = sortingRules;
    }

    /**
     * Mutator method for rssParser
     * @param rssParser An RssParser to be set as this FeedLists RssParser
     */
    void setRssParser(RssParser rssParser) {
        this.rssParser = rssParser;
    }

    /**
     * Mutator method for showVisitedStatus
     *
     * @param showVisitedStatus True if
     */
    void setShowVisitedStatus(boolean showVisitedStatus) {
            this.showVisitedStatus = showVisitedStatus;
    }

    /**
     * Mutator method for feeds
     * @param feeds An ArrayList of Feed objects to be set as the new feeds
     */
    void setFeeds(ArrayList<Feed> feeds) {
        this.feeds = feeds;
    }
}
