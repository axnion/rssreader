package app;

import app.main.FeedListPane;
import app.main.ItemPane;
import app.menu.SideMenu;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import rss.Item;
import system.Configuration;

import java.util.ArrayList;

/**
 * Class Wrapper
 *
 * @author Axel Nilsson (axnion)
 */
class Wrapper extends HBox {
    private SideMenu sideMenu;
    private ArrayList<FeedListPane> feedListPanes;

    Wrapper() {
        sideMenu = new SideMenu();
        feedListPanes = new ArrayList<>();

        getChildren().addAll(sideMenu);
        getChildren().addAll(feedListPanes);
        getStyleClass().add("Wrapper");
    }

    void addFeedList(String listName) {
        FeedListPane newFeedListPane = new FeedListPane(listName);
        feedListPanes.add(newFeedListPane);
        updateFeedLists();
    }

    void removeFeedList(String listName) {
        for(int i = 0; i < feedListPanes.size(); i++) {
            if(feedListPanes.get(i).getName().equals(listName)) {
                feedListPanes.remove(i);
                updateFeedLists();
                break;
            }
        }
    }

    void updateFeedLists() {
        getChildren().clear();
        getChildren().add(sideMenu);

        Node node;
        for(FeedListPane feedListPane : feedListPanes) {
            ArrayList<Item> items = Configuration.getAllItemsFromFeedList(feedListPane.getName());
            for(Item item : items) {
                feedListPane.getChildren().add(new ItemPane(item.getTitle()));
            }

            node = feedListPane;
            setHgrow(node, Priority.ALWAYS);
            getChildren().add(node);
        }
    }
}