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
 * FeedList is a "list-like" object that contains Feed objects. It implements the Iterable
 * interface. It has a name that is used as an identifier for each FeedList and an ArrayList of
 * Feed objects.
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedList {
    private String name;
    private String sortingRules;
    private ArrayList<Feed> feeds;
    private RssParser rssParser;

    /**
     * Constructor
     * @param name  The name we want the FeedList to be identified by.
     */
    FeedList(String name) {
        this.name = name;
        this.sortingRules = "DATE_DEC";
        this.feeds = new ArrayList<>();
        this.rssParser = new RssParser();
    }

    /**
     * Used to get the element at a specific index in the FeedList.
     * @param index The index of the element the method show return.
     * @return      A Feed at the specified index in the FeedList.
     */
    Feed get(int index) {
        return feeds.get(index);
    }

    /**
     * Used to add a Feed object to the FeedList.
     * @param url   The url to the feed we want to become a Feed object and be added to the
     *              FeedList.
     */
    void add(String url, boolean newFeedStatus) {
        if(getIndexOf(url) == -1) {
            Feed newFeed = rssParser.getFeed(url);

            try {
                for(Item item : newFeed.getItems()) {
                    Configuration.getDatabaseController().addItem(name, item.getId(),
                            newFeedStatus);
                }
            }
            catch(Exception expt) {
                expt.printStackTrace();
            }

            feeds.add(newFeed);
        }
        else
            throw new FeedAlreadyExists(url, getName());
    }

    /**
     * Used to remove a Feed with the same UrlToXml identifier.
     * @param url   The url to the Feed we want removed from the FeedList.
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
     * Removes all Feed objects in the FeedList.
     */
    void clear() {
        feeds.clear();
    }

    /**
     * Returns the amount of elements in the FeedList.
     * @return  An integer describing the number of elements in the FeedList
     */
    int size() {
        return feeds.size();
    }

    void update() {
        for(int i = 0; i < feeds.size(); i++) {
            Feed updatedFeed = rssParser.getFeed(feeds.get(i).getUrlToXML());

            try {
                for(Item item : updatedFeed.getItems()) {
                    Configuration.getDatabaseController().addItem(name, item.getId(), false);
                }
            }
            catch(Exception expt) {
                expt.printStackTrace();
            }

            feeds.set(i, updatedFeed);
        }
    }

    /**
     * Returns all Item objects in an ArrayList from all of the Feed objects that the FeedList
     * holds. The returned ArrayList is also sorted according to the sorting rules given though the
     * parameter sorting using the sort method.
     * @return          An ArrayList containing all Item objects from all Feed objects in this
     *                  FeedList that is sorted according to the rules in the sorting string.
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
     *
     * @param url
     * @return
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

            titleAsc = getSortingRules().equals("TITLE_ASC") && leftQueue.peek()
                    .compareTitle(rightQueue.peek()) <= 0;
            titleDec = getSortingRules().equals("TITLE_DEC") && leftQueue.peek()
                    .compareTitle(rightQueue.peek()) > 0;
            dateAsc =  getSortingRules().equals("DATE_ASC")  && leftQueue.peek()
                    .compareDate(rightQueue.peek()) <= 0;
            dateDec =  getSortingRules().equals("DATE_DEC")  && leftQueue.peek()
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
     * Accessor method for feeds
     * @return An ArrayList containing Feed objects
     */
    ArrayList<Feed> getFeeds() {
        return feeds;
    }

    /**
     * Accessor method for rssParser
     * @return An RssParser object
     */
    RssParser getRssParser() {
        return rssParser;
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
     * Mutator method for feeds
     * @param feeds An ArrayList of Feed objects to be set as the new feeds
     */
    void setFeeds(ArrayList<Feed> feeds) {
        this.feeds = feeds;
    }

    /**
     * Mutator method for rssParser
     * @param rssParser An RssParser to be set as this FeedLists RssParser
     */
    void setRssParser(RssParser rssParser) {
        this.rssParser = rssParser;
    }
}
