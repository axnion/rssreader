package app.menu;

import app.RSSReader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import system.rss.Feed;

/**
 * Class MenuFeed
 *
 * @author Axel Nilsson (axnion)
 */
class MenuFeed extends VBox{
    private Feed feed;
    private String feedListName;

    MenuFeed(Feed feed, String feedListName) {
        this.feed = feed;
        this.feedListName = feedListName;

        createContextMenu();

        getStyleClass().add("MenuFeed");
        Text title = new Text(feed.getTitle());
        title.setWrappingWidth(400);
        title.getStyleClass().add("MenuFeedTitle");

        getChildren().add(title);
    }

    private void createContextMenu() {
        ContextMenu rightClickMenu = new ContextMenu();

        MenuItem showDetails = new MenuItem("Show details");
        MenuItem deleteFeed = new MenuItem("Delete feed");

        deleteFeed.setOnAction(event -> {
            RSSReader.removeFeed(feed.getUrlToXML(), feedListName);
        });

        rightClickMenu.getItems().add(showDetails);
        rightClickMenu.getItems().add(deleteFeed);

        setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.SECONDARY)) {
                if(RSSReader.openContextMenu != null)
                    RSSReader.openContextMenu.hide();

                rightClickMenu.show(RSSReader.wrapper, event.getScreenX(), event.getScreenY());
                RSSReader.openContextMenu = rightClickMenu;
            }
        });
    }

    public String getFeedUrlToXML() {
        return feed.getUrlToXML();
    }
}