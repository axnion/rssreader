package app.menu;

import app.RSSReader;
import app.misc.ClickButton;
import app.misc.ToggleIconButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
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
import system.rss.Feed;
import system.rss.FeedMinimal;
import system.rss.FeedSniffer;
import system.Configuration;
import system.FeedList;

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

        getChildren().add(titlePane);
        getChildren().add(addFeedMenuContainer);
        getChildren().add(settingsContainer);
        getChildren().add(feedsContainer);
        getChildren().add(newFeedContainer);
    }

    void update() {
        ArrayList<Feed> feeds = Configuration.getAllFeedsFromFeedList(feedList.getName());
        menuFeeds.clear();

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

    void hideAddFeedMenu() {
        if(addFeedMenuContainer.getChildren().size() != 0) {
            addFeedMenuContainer.getChildren().clear();
            newFeedContainer.getChildren().clear();
        }
    }

    private void displayFeeds(String url) {
        newFeedContainer.getChildren().clear();

        FeedSniffer feedSniffer = new FeedSniffer();
        ArrayList<FeedMinimal> feeds = feedSniffer.getFeeds(url);

        if(feeds.size() == 0) {
            Text errorMessage = new Text("No feed where found");
            errorMessage.setFill(Color.RED);
            newFeedContainer.getChildren().add(errorMessage);
        }

        for(FeedMinimal feed : feeds) {
            newFeedContainer.getChildren().add(new NewFeed(feed));
        }
    }

    private void createContextMenu() {
        ContextMenu rightClickMenu = new ContextMenu();

        MenuItem addFeedButton = new MenuItem("Add Feed");
        MenuItem removeFeedListButton = new MenuItem("Remove this List");
        CheckMenuItem showVisitedStatus = new CheckMenuItem("Show items visited status");
        showVisitedStatus.setSelected(Configuration.getFeedListByName(feedList.getName())
                .getShowVisitedStatus());

        Menu sortingMenu = new Menu("Sorting");
        ToggleGroup sortingAlternatives = new ToggleGroup();
        RadioMenuItem titleAsc = new RadioMenuItem("Title Ascending");
        RadioMenuItem titleDec = new RadioMenuItem("Title Descending");
        RadioMenuItem dateAsc = new RadioMenuItem("Date Ascending");
        RadioMenuItem dateDec = new RadioMenuItem("Date Descending");

        if(feedList.getSortingRules().equals("TITLE_ASC"))
            titleAsc.setSelected(true);
        else if(feedList.getSortingRules().equals("TITLE_DEC"))
            titleDec.setSelected(true);
        else if(feedList.getSortingRules().equals("DATE_ASC"))
            dateAsc.setSelected(true);
        else
            dateDec.setSelected(true);

        titleAsc.setOnAction(event -> {
            Configuration.setSortingRules(feedList.getName(), "TITLE_ASC");
            titleAsc.setSelected(true);
        });
        titleDec.setOnAction(event -> {
            Configuration.setSortingRules(feedList.getName(), "TITLE_DEC");
            titleDec.setSelected(true);
        });
        dateAsc.setOnAction(event -> {
            Configuration.setSortingRules(feedList.getName(), "DATE_ASC");
            dateAsc.setSelected(true);
        });
        dateDec.setOnAction(event -> {
            Configuration.setSortingRules(feedList.getName(), "DATE_DEC");
            dateDec.setSelected(true);
        });

        titleAsc.setToggleGroup(sortingAlternatives);
        titleDec.setToggleGroup(sortingAlternatives);
        dateAsc.setToggleGroup(sortingAlternatives);
        dateDec.setToggleGroup(sortingAlternatives);
        sortingMenu.getItems().addAll(titleAsc, titleDec, dateAsc, dateDec);

        rightClickMenu.getItems().addAll(addFeedButton, showVisitedStatus, sortingMenu,
                removeFeedListButton);

        titlePane.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.SECONDARY)) {
                if(RSSReader.openContextMenu != null)
                    RSSReader.openContextMenu.hide();

                rightClickMenu.show(RSSReader.wrapper, event.getScreenX(), event.getScreenY());
                RSSReader.openContextMenu = rightClickMenu;
            }
        });

        addFeedButton.setOnAction(event -> showAddFeedMenu());
        removeFeedListButton.setOnAction(event -> RSSReader.removeFeedList(feedList.getName()));
        showVisitedStatus.setOnAction(event -> Configuration.setShowVisitedStatus(feedList
                        .getName(), showVisitedStatus.isSelected()));
    }

    String getName() {
        return feedList.getName();
    }

    private class NewFeed extends HBox {
        NewFeed(FeedMinimal feed) {
            BorderPane borderPane = new BorderPane();
            Text title = new Text(feed.title);
            title.setWrappingWidth(370);
            title.setFill(Color.WHITE);
            ClickButton button = new ClickButton(MaterialIcon.ADD, "MenuButton", "30px",
                    "Add feed to feedlist");
            button.setOnMouseClicked(event -> {
                if(event.getButton().equals(MouseButton.PRIMARY)) {
                    RSSReader.addFeed(feed.urlToXml, feedList.getName());
                    newFeedContainer.getChildren().remove(this);
                    if(newFeedContainer.getChildren().size() == 0) {
                        hideAddFeedMenu();
                    }
                }
            });

            borderPane.setLeft(title);
            borderPane.setRight(button);

            setHgrow(borderPane, Priority.ALWAYS);
            getChildren().addAll(borderPane);
        }
    }
}
