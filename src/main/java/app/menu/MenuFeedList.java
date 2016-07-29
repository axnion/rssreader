package app.menu;

import app.App;
import app.misc.ClickButton;
import app.misc.ToggleIconButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import rss.Feed;
import rss.FeedSniffer;
import system.Configuration;
import system.FeedList;

import java.io.File;
import java.util.ArrayList;

/**
 * Class MenuFeedList
 *
 * @author Axel Nilsson (axnion)
 */
class MenuFeedList extends VBox{
    private boolean visible;
    private FeedList feedList;
    private BorderPane titlePane;
    private HBox settings;
    private VBox settingsContainer;
    private VBox feedsContainer;
    private ArrayList<MenuFeed> menuFeeds;
    private ToggleIconButton showFeedsButton;
    private HBox addFeedMenuContainer;
    private VBox newFeedContainer;

    MenuFeedList(FeedList feedList) {
        this.feedList = feedList;
        menuFeeds = new ArrayList<>();
        settings = new HBox();
        settingsContainer = new VBox();
        feedsContainer = new VBox();
        visible = false;
        addFeedMenuContainer = new HBox();
        newFeedContainer = new VBox();

        getStyleClass().add("MenuFeedList");

        Text title = new Text(feedList.getName());
        title.getStyleClass().add("MenuFeedListTitle");

        showFeedsButton = new ToggleIconButton(MaterialIcon.ARROW_DROP_DOWN, MaterialIcon.ARROW_DROP_UP,
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
        createSettings();

        getChildren().add(titlePane);
        getChildren().add(addFeedMenuContainer);
        getChildren().add(settingsContainer);
        getChildren().add(feedsContainer);
        getChildren().add(newFeedContainer);
    }

    void update() {
        menuFeeds.clear();
        ArrayList<Feed> feeds = Configuration.getAllFeedsFromFeedList(feedList.getName());

        for(int i = 0; i < feeds.size(); i++) {
            MenuFeed menuFeed = new MenuFeed(feeds.get(i), feedList.getName());

            if(i % 2 == 0) {
                menuFeed.setStyle("-fx-background-color: #575757");
            }

            menuFeeds.add(menuFeed);
        }

        if(visible) {
            feedsContainer.getChildren().clear();
            feedsContainer.getChildren().addAll(menuFeeds);
        }
    }

    private void showHideFeeds() {
        if(visible) {
            feedsContainer.getChildren().clear();
            settingsContainer.getChildren().clear();
        }
        else {
            feedsContainer.getChildren().addAll(menuFeeds);
            settingsContainer.getChildren().add(settings);
        }

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

    private void createSettings() {
        RadioButton showVisitedButton = new RadioButton("Show not visited");
        showVisitedButton.setStyle("-fx-text-fill: white");
        showVisitedButton.setSelected(Configuration.getFeedListByName(feedList.getName()).
                getShowVisitedStatus());
        showVisitedButton.setOnAction(event -> {
            Configuration.setShowVisitedStatus(feedList.getName(), showVisitedButton.isSelected());
        });

        ComboBox<String> sortingSelection = new ComboBox<>();
        ObservableList<String> sortingOptions = FXCollections.observableArrayList(
                "Title Ascending",
                "Title Descending",
                "Date Ascending",
                "Date Descending"
        );
        sortingSelection.setItems(sortingOptions);

        if(feedList.getSortingRules().equals("TITLE_ASC"))
            sortingSelection.setValue("Title Ascending");
        else if(feedList.getSortingRules().equals("TITLE_DEC"))
            sortingSelection.setValue("Title Descending");
        else if(feedList.getSortingRules().equals("DATE_ASC"))
            sortingSelection.setValue("Date Ascending");
        else
            sortingSelection.setValue("Date Descending");

        sortingSelection.setOnAction(event -> {
            String newSortingRule = sortingSelection.getSelectionModel().getSelectedItem();
            if(newSortingRule.equals("Title Ascending"))
                Configuration.setSortingRules(feedList.getName(), "TITLE_ASC");
            else if(newSortingRule.equals("Title Descending"))
                Configuration.setSortingRules(feedList.getName(), "TITLE_DEC");
            else if(newSortingRule.equals("Date Ascending"))
                Configuration.setSortingRules(feedList.getName(), "DATE_ASC");
            else
                Configuration.setSortingRules(feedList.getName(), "DATE_DEC");
        });

        settings.getChildren().add(showVisitedButton);
        settings.getChildren().add(sortingSelection);
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

                rightClickMenu.show(App.wrapper, event.getScreenX(), event.getScreenY());
                App.openContextMenu = rightClickMenu;
            }
        });

        addFeedButton.setOnAction(event -> showAddFeedMenu());
        removeFeedListButton.setOnAction(event -> App.removeFeedList(feedList.getName()));
    }

    String getName() {
        return feedList.getName();
    }

    private class NewFeed extends HBox {
        NewFeed(Feed feed) {
            BorderPane borderPane = new BorderPane();
            Text title = new Text(feed.getTitle());
            title.setWrappingWidth(370);
            title.setFill(Color.WHITE);
            ClickButton button = new ClickButton(MaterialIcon.ADD, "MenuButton", "30px",
                    "Add feed to feedlist");
            button.setOnMouseClicked(event -> {
                if(event.getButton().equals(MouseButton.PRIMARY)) {
                    App.addFeed(feed.getUrlToXML(), feedList.getName());
                    newFeedContainer.getChildren().remove(this);
                }
            });

            borderPane.setLeft(title);
            borderPane.setRight(button);

            setHgrow(borderPane, Priority.ALWAYS);
            getChildren().addAll(borderPane);
        }
    }
}
