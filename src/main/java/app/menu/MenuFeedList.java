package app.menu;

import app.App;
import app.misc.ClickButton;
import app.misc.ToggleButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import rss.Feed;
import rss.FeedSniffer;
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
    private BorderPane titlePane;
    private VBox feedsContainer;
    private ArrayList<MenuFeed> menuFeeds;
    private ToggleButton showFeedsButton;
    private HBox addFeedMenuContainer;
    private VBox newFeedContainer;

    MenuFeedList(String listName) {
        name = listName;
        menuFeeds = new ArrayList<>();
        feedsContainer = new VBox();
        visible = false;
        addFeedMenuContainer = new HBox();
        newFeedContainer = new VBox();

        getStyleClass().add("MenuFeedList");

        Text title = new Text(listName);
        title.getStyleClass().add("MenuFeedListTitle");

        showFeedsButton = new ToggleButton(MaterialIcon.ARROW_DROP_DOWN, MaterialIcon.ARROW_DROP_UP,
                "MenuButton", "30px", "Show/Hide Feeds");
        showFeedsButton.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                showHideFeeds();
                hideAddFeedMenu();
            }
        });

        titlePane = new BorderPane();
        titlePane.setLeft(title);
        titlePane.setRight(showFeedsButton);
        createContextMenu();

        getChildren().add(titlePane);
        getChildren().add(addFeedMenuContainer);
        getChildren().add(feedsContainer);
        getChildren().add(newFeedContainer);
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

    private void showHideFeeds() {
        if(visible)
            feedsContainer.getChildren().clear();

        else
            feedsContainer.getChildren().addAll(menuFeeds);
        showFeedsButton.toggle();
        visible = !visible;
    }

    void hideFeeds() {
        if(visible) {
            visible = false;
            showFeedsButton.toggle();
        }
        feedsContainer.getChildren().clear();
    }

    private void showAddFeedMenu() {
        if(addFeedMenuContainer.getChildren().size() == 0) {
            if(!visible) {
                showHideFeeds();
            }

            TextField urlInput = new TextField();
            urlInput.setOnAction(event -> {
                displayFeeds(urlInput.getText());
            });

            Node addFeedMenuNode = urlInput;
            addFeedMenuNode.setOnKeyPressed(event -> {
                if(event.getCode().equals(KeyCode.ESCAPE))
                    hideAddFeedMenu();
            });

            addFeedMenuContainer.getChildren().addAll(addFeedMenuNode);
            addFeedMenuContainer.setHgrow(addFeedMenuNode, Priority.ALWAYS);
        }
    }

    void hideAddFeedMenu() {
        if(addFeedMenuContainer.getChildren().size() != 0) {
            addFeedMenuContainer.getChildren().clear();
            newFeedContainer.getChildren().clear();
        }
    }

    private void displayFeeds(String url) {
        newFeedContainer.getChildren().clear();

        FeedSniffer feedSniffer = new FeedSniffer();
        ArrayList<Feed> feeds = feedSniffer.getFeeds(url);

        if(feeds.size() == 0) {
            Text errorMessage = new Text("No feed where found");
            errorMessage.setFill(Color.RED);
            newFeedContainer.getChildren().add(errorMessage);
        }

        for(Feed feed : feeds) {
            newFeedContainer.getChildren().add(new NewFeed(feed));
        }
    }

    private void createContextMenu() {
        ContextMenu rightClickMenu = new ContextMenu();

        MenuItem addFeedButton = new MenuItem("Add Feed");
        MenuItem removeFeedListButton = new MenuItem("Remove this List");
        rightClickMenu.getItems().addAll(addFeedButton, removeFeedListButton);

        titlePane.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.SECONDARY)) {
                if(App.openContextMenu != null)
                    App.openContextMenu.hide();

                rightClickMenu.show(App.root, event.getScreenX(), event.getScreenY());
                App.openContextMenu = rightClickMenu;
            }
        });

        addFeedButton.setOnAction(event -> {
            showAddFeedMenu();
        });

        removeFeedListButton.setOnAction(event -> {
            App.removeFeedList(name);
        });
    }

    String getName() {
        return name;
    }

    private class NewFeed extends HBox {
        NewFeed(Feed feed) {
            Text title = new Text(feed.getTitle());
            title.setFill(Color.WHITE);
            ClickButton button = new ClickButton(MaterialIcon.ADD, "MenuButton", "15px",
                    "Add feed to feedlist");
            button.setOnMouseClicked(event -> {
                if(event.getButton().equals(MouseButton.PRIMARY)) {
                    App.addFeed(feed.getUrlToXML(), name);
                    newFeedContainer.getChildren().remove(this);
                }
            });

            getChildren().addAll(title, button);
        }
    }
}
