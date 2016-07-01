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
public class Wrapper extends HBox {
    private SideMenu sideMenu;
    FeedListContainer feedListContainer;


    public Wrapper() {
        sideMenu = new SideMenu();
        feedListContainer = new FeedListContainer();

        Node feedListConainerNode = feedListContainer;
        setHgrow(feedListConainerNode, Priority.ALWAYS);

        getChildren().addAll(sideMenu);
        getChildren().addAll(feedListConainerNode);
        getStyleClass().add("Wrapper");
    }

    public void addFeedList(String listName) {
        sideMenu.addFeedList(listName);
        feedListContainer.addFeedList(listName);
    }

    public void removeFeedList(String listName) {
        sideMenu.removeFeedList(listName);
        feedListContainer.removeFeedList(listName);
    }

    public void updateFeedLists() {
        sideMenu.updateFeedLists();
        feedListContainer.updateFeedLists();
    }
}