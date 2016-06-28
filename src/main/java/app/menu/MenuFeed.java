package app.menu;

import app.misc.ToggleButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.scene.layout.HBox;
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
        HBox titlePane = new HBox(title);
        title.getStyleClass().add("MenuFeedTitle");

        getChildren().add(title);
    }
}