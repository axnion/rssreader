package app;

import app.main.FeedListContainer;
import app.menu.SideMenu;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Class Wrapper
 *
 * @author Axel Nilsson (axnion)
 */
class Wrapper extends HBox {
    private SideMenu sideMenu;
    private FeedListContainer feedListContainer;


    Wrapper() {
        reset();
        getStyleClass().add("Wrapper");
    }

    void reset() {
        getChildren().clear();
        sideMenu = new SideMenu();
        feedListContainer = new FeedListContainer();

        Node feedListConainerNode = feedListContainer;
        setHgrow(feedListConainerNode, Priority.ALWAYS);

        getChildren().addAll(sideMenu);
        getChildren().addAll(feedListConainerNode);
    }

    void addFeedList(String listName) {
        sideMenu.addFeedList(listName);
        feedListContainer.addFeedList(listName);
    }

    void removeFeedList(String listName) {
        sideMenu.removeFeedList(listName);
        feedListContainer.removeFeedList(listName);
    }

    void update() {
        sideMenu.updateFeedLists();
        feedListContainer.updateFeedLists();
    }
}