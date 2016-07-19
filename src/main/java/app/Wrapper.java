package app;

import app.main.FeedListContainer;
import app.menu.SideMenu;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import system.Configuration;

import java.util.Date;

/**
 * Class Wrapper
 *
 * @author Axel Nilsson (axnion)
 */
class Wrapper extends HBox {
    private SideMenu sideMenu;
    private FeedListContainer feedListContainer;
    private Date lastUpdated;

    Wrapper() {
        reset();
        getStyleClass().add("Wrapper");
        lastUpdated = new Date();
    }

    void reset() {
        getChildren().clear();
        sideMenu = new SideMenu();
        feedListContainer = new FeedListContainer();

        Node feedListConainerNode = feedListContainer;
        setHgrow(feedListConainerNode, Priority.ALWAYS);

        getChildren().add(sideMenu);
        getChildren().add(feedListConainerNode);
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
        if(lastUpdated.before(Configuration.getLastUpdated())) {
            sideMenu.updateFeedLists();
            feedListContainer.updateFeedLists();
            lastUpdated = new Date();
            System.out.println("Wrapper - Wrapper updated");
        }
    }
}