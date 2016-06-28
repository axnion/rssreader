package app.menu;

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

        Text title = new Text(feed.getTitle());

        getChildren().add(title);
    }
}