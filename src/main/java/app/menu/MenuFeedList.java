package app.menu;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import rss.Feed;
import system.Configuration;

import java.util.ArrayList;

/**
 * Class MenuFeedList
 *
 * @author Axel Nilsson (axnion)
 */
class MenuFeedList extends VBox{
    private String name;
    private VBox feedsContainer;
    private ArrayList<MenuFeed> menuFeeds;

    MenuFeedList(String listName) {
        name = listName;
        menuFeeds = new ArrayList<>();
        feedsContainer = new VBox();

        Text text = new Text(listName);

        getChildren().add(text);
        getChildren().add(feedsContainer);
    }

    void update() {
        menuFeeds.clear();
        ArrayList<Feed> feeds = Configuration.getAllFeedsFromFeedList(name);

        for(Feed feed : feeds) {
            menuFeeds.add(new MenuFeed(feed));
        }

        feedsContainer.getChildren().clear();
        feedsContainer.getChildren().addAll(menuFeeds);
    }

    String getName() {
        return name;
    }
}
