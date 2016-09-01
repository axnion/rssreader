package system;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import system.rss.Feed;
import system.rss.Item;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class DatabaseAccessObjectJSON
 *
 * @author Axel Nilsson (axnion)
 */
class DatabaseAccessObjectJSON implements DatabaseAccessObject {
    private String path;
    private Date lastSaved;

    DatabaseAccessObjectJSON() {
        path = "temp.json";
        lastSaved = new Date(0);
    }

    public ArrayList<FeedList> load() throws IOException {
        ArrayList<FeedList> feedLists = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode node = objectMapper.readTree(new File(path));

        for(int i = 0; i < node.size(); i++) {
            String name = node.get(i).get("name").textValue();
            String sortingRules = node.get(i).get("sortingRules").textValue();
            boolean showVisitedStatus = node.get(i).get("showVisitedStatus").booleanValue();

            FeedList feedList = new FeedList(name, sortingRules, showVisitedStatus);
            loadFeeds(node.get(i).get("feeds"), feedList);
            feedLists.add(feedList);
        }

        return feedLists;
    }

    private void loadFeeds(JsonNode node, FeedList feedList) {
        for(int i = 0; i < node.size(); i++) {
            feedList.add(node.get(i).get("urlToXML").textValue());
            loadItems(node.get(i).get("items"), feedList.get(feedList.size() - 1));
        }
    }

    private void loadItems(JsonNode node, Feed feed) {
        ArrayList<Item> items = feed.getItems();
        boolean found;

        for(Item item : items) {
            found = false;
            for(int i = 0; i < node.size(); i++) {
                if(item.getId().equals(node.get(i).get("id").textValue())) {
                    item.setVisited(node.get(i).get("visited").booleanValue());
                    item.setStarred(node.get(i).get("starred").booleanValue());
                    found = true;
                    break;
                }
            }

            if(!found) {
                item.setVisited(false);
                item.setStarred(false);
            }
        }
    }

    public void save(ArrayList<FeedList> feedLists, Date configurationLastUpdated)
            throws IOException {
        Map[] feedListMaps = new Map[feedLists.size()];

        for(int i = 0; i < feedLists.size(); i++) {
            feedListMaps[i] = createFeedListMap(feedLists.get(i));
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File(path), feedListMaps);
    }

    private Map<String, Object> createFeedListMap(FeedList feedList) {
        Map[] feedMaps = new Map[feedList.getFeeds().size()];

        for(int i = 0; i < feedList.getFeeds().size(); i++) {
            feedMaps[i] = createFeedMap(feedList.getFeeds().get(i));
        }

        Map<String, Object> feedListMap = new HashMap<>();
        feedListMap.put("name", feedList.getName());
        feedListMap.put("sortingRules", feedList.getSortingRules());
        feedListMap.put("showVisitedStatus", feedList.getShowVisitedStatus());
        feedListMap.put("feeds", feedMaps);

        return feedListMap;
    }

    private Map<String, Object> createFeedMap(Feed feed) {
        Map[] itemMaps = new Map[feed.getItems().size()];

        for(int i = 0; i < feed.getItems().size(); i++) {
            itemMaps[i] = createItemMap(feed.getItems().get(i));
        }

        Map<String, Object> feedMap = new HashMap<>();
        feedMap.put("urlToXML", feed.getUrlToXML());
        feedMap.put("items", itemMaps);

        return feedMap;
    }

    private Map<String, Object> createItemMap(Item item) {
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("id", item.getId());
        itemMap.put("visited", item.isVisited());
        itemMap.put("starred", item.isStarred());

        return itemMap;
    }

    /*
    ----------------------------------- ACCESSORS AND MUTATORS -------------------------------------
    */

    /**
     * Accessor method for path.
     *
     * @return  A String containing the path currently used in this object.
     */
    public String getPath() {
        return path;
    }

    /**
     * Accessor method for lastSaved.
     *
     * @return  A Date object containing the last time this object saved to the current path.
     */
    Date getLastSaved() {
        return lastSaved;
    }

    /**
     * Mutator method for path.
     *
     * @param path  A String containing the new path to be set as to path.
     */
    public void setPath(String path) {
        this.path = path;
        setLastSaved(new Date(0));
    }

    /**
     * Mutator method for lastsaved.
     *
     * @param lastSavedParam    A Date object to be set as the last time this object last saved to
     *                          current path.
     */
    void setLastSaved(Date lastSavedParam) {
        lastSaved = lastSavedParam;
    }
}
