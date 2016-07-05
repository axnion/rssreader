package app.menu;

import app.App;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import rss.Feed;

/**
 * Class MenuFeed
 *
 * @author Axel Nilsson (axnion)
 */
public class MenuFeed extends VBox{
    private Feed feed;
    private String feedListName;

    MenuFeed(Feed feed, String feedListName) {
        this.feed = feed;
        this.feedListName = feedListName;

        createContextMenu();

        getStyleClass().add("MenuFeed");
        Text title = new Text(feed.getTitle());
        title.getStyleClass().add("MenuFeedTitle");

        getChildren().add(title);
    }

    private void createContextMenu() {
        ContextMenu rightClickMenu = new ContextMenu();

        MenuItem showDetails = new MenuItem("Show details");
        MenuItem deleteFeed = new MenuItem("Delete feed");

        deleteFeed.setOnAction(event -> {
            App.removeFeed(feed.getUrlToXML(), feedListName);
        });

        rightClickMenu.getItems().add(showDetails);
        rightClickMenu.getItems().add(deleteFeed);

        setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.SECONDARY)) {
                if(App.openContextMenu != null)
                    App.openContextMenu.hide();

                rightClickMenu.show(App.root, event.getScreenX(), event.getScreenY());
                App.openContextMenu = rightClickMenu;
            }
        });
    }
}