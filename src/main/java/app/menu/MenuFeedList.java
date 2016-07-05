package app.menu;

import app.App;
import app.misc.ToggleButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
    private BorderPane titlePane;
    private VBox feedsContainer;
    private ArrayList<MenuFeed> menuFeeds;
    private ToggleButton showFeedsButton;
    private HBox addFeedMenuContainer;

    MenuFeedList(String listName) {
        name = listName;
        menuFeeds = new ArrayList<>();
        feedsContainer = new VBox();
        visible = false;
        addFeedMenuContainer = null;

        getStyleClass().add("MenuFeedList");

        Text title = new Text(listName);
        title.getStyleClass().add("MenuFeedListTitle");

        showFeedsButton = new ToggleButton(MaterialIcon.ARROW_DROP_DOWN, MaterialIcon.ARROW_DROP_UP,
                "MenuButton", "30px", "Show/Hide Feeds");
        showFeedsButton.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY))
                showHideFeeds();
        });

        titlePane = new BorderPane();
        titlePane.setLeft(title);
        titlePane.setRight(showFeedsButton);
        createContextMenu();

        getChildren().add(titlePane);
        getChildren().add(feedsContainer);
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

    private void showAddFeedMenu() {
        if(addFeedMenuContainer == null) {
            if(!visible) {
                showHideFeeds();
            }

            addFeedMenuContainer = new HBox();
            Node addFeedMenuNode = new AddFeedMenu(name);

            addFeedMenuNode.setOnKeyPressed(event -> {
                if(event.getCode().equals(KeyCode.ESCAPE))
                    hideAddFeedMenu();
            });

            addFeedMenuContainer.getChildren().addAll(addFeedMenuNode);
            addFeedMenuContainer.setHgrow(addFeedMenuNode, Priority.ALWAYS);

            getChildren().add(addFeedMenuContainer);
        }
    }

    private void hideAddFeedMenu() {
        if(addFeedMenuContainer != null) {
            getChildren().remove(addFeedMenuContainer);
            addFeedMenuContainer = null;
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
}
