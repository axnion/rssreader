package app;

import app.main.FeedListPane;
import app.menu.SideMenu;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

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

    void addFeedList(String name) {
        FeedListPane newFeedListPane = new FeedListPane(name);
        feedListPanes.add(newFeedListPane);

        Node node = newFeedListPane;
        setHgrow(node, Priority.ALWAYS);
        getChildren().add(node);
    }

    void removeFeedList(String name) {
        for(int i = 0; i < feedListPanes.size(); i++) {
            if(feedListPanes.get(i).getName().equals(name)) {
                getChildren().remove(feedListPanes.get(i));
                feedListPanes.remove(i);
                break;
            }
        }
    }
}
