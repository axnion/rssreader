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

    MenuFeed(Feed feed) {
        this.feed = feed;

        getStyleClass().add("MenuFeed");

        Text title = new Text(feed.getTitle());
        title.getStyleClass().add("MenuFeedTitle");

        ContextMenu rightClickMenu = new ContextMenu();
        rightClickMenu.getItems().add(new MenuItem("Test1"));
        rightClickMenu.getItems().add(new MenuItem("Test2"));

        setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.SECONDARY)) {
                if(App.openContextMenu != null)
                    App.openContextMenu.hide();

                rightClickMenu.show(App.root, event.getScreenX(), event.getScreenY());
                App.openContextMenu = rightClickMenu;
            }


        });

        getChildren().add(title);
    }
}