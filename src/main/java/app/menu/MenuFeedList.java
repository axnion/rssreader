package app.menu;

import app.misc.ToggleButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    private boolean visible;
    private String name;
    private VBox feedsContainer;
    private ArrayList<MenuFeed> menuFeeds;
    private ToggleButton showFeedsButton;

    MenuFeedList(String listName) {
        name = listName;
        menuFeeds = new ArrayList<>();
        feedsContainer = new VBox();
        visible = false;

        getStyleClass().add("MenuFeedList");

        Text title = new Text(listName);
        title.getStyleClass().add("MenuFeedListTitle");

        showFeedsButton = new ToggleButton(MaterialIcon.ARROW_DROP_DOWN, MaterialIcon.ARROW_DROP_UP,
                "MenuButton", "30px", "Show/Hide Feeds");
        showFeedsButton.setOnMouseClicked(event -> showAndHideFeeds());

        BorderPane titlePane = new BorderPane();
        titlePane.setLeft(title);
        titlePane.setRight(showFeedsButton);

        getChildren().add(titlePane);
        getChildren().add(feedsContainer);
    }

    void showAndHideFeeds() {
        if(visible)
            feedsContainer.getChildren().clear();

        else
            feedsContainer.getChildren().addAll(menuFeeds);
        showFeedsButton.toggle();
        visible = !visible;
    }

    void update() {
        menuFeeds.clear();
        ArrayList<Feed> feeds = Configuration.getAllFeedsFromFeedList(name);

        for(Feed feed : feeds) {
            menuFeeds.add(new MenuFeed(feed));
        }

        if(visible) {
            feedsContainer.getChildren().clear();
            feedsContainer.getChildren().addAll(menuFeeds);
        }
    }

    String getName() {
        return name;
    }
}
