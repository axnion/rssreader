package app.main;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import system.rss.Item;
import system.Configuration;
import system.FeedList;

import java.util.ArrayList;

/**
 * Class FeedListContainer
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedListContainer extends HBox {
    private HBox container;
    private ArrayList<FeedListPane> feedListPanes;

    public FeedListContainer() {
        feedListPanes = new ArrayList<>();
        container = new HBox();

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Node scrollPaneNode = scrollPane;
        setHgrow(scrollPane, Priority.ALWAYS);
        getChildren().add(scrollPaneNode);
    }

    public void addFeedList(String listName) {
        FeedListPane newFeedListPane = new FeedListPane(listName);
        feedListPanes.add(newFeedListPane);
        updateFeedLists();
    }

    public void removeFeedList(String listName) {
        for(int i = 0; i < feedListPanes.size(); i++) {
            if(feedListPanes.get(i).getName().equals(listName)) {
                feedListPanes.remove(i);
                updateFeedLists();
                break;
            }
        }
    }

    public void updateFeedLists() {
        container.getChildren().clear();

        Node node;

        ArrayList<FeedList> feedLists = Configuration.getFeedLists();

        for(FeedList feedList : feedLists) {
            ArrayList<Item> items = Configuration.getAllItemsFromFeedList(feedList.getName());

            FeedListPane feedListPane = new FeedListPane(feedList.getName());
            if(items.size() != 0) {
                for(Item item : items) {
                    feedListPane.addItemPane(new ItemPane(item, feedList.getName(),
                            feedList.getShowVisitedStatus()));
                }

                node = feedListPane;
                setHgrow(node, Priority.ALWAYS);
                container.getChildren().add(node);
            }
        }
    }
}
