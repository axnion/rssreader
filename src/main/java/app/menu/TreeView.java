package app.menu;

import app.RSSReader;
import app.misc.ClickButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import system.Configuration;
import system.FeedList;
import system.exceptions.FeedListAlreadyExists;

import java.util.ArrayList;

/**
 * Class TreeView
 *
 * @author Axel Nilsson (axnion)
 */
class TreeView extends VBox {
    private VBox treeViewContainer;
    private HBox topBar;
    private TextField inputBar;
    private Text errorMessage;
    private ArrayList<MenuFeedList> menuFeedLists;
    private ClickButton addFeedListButton;

    TreeView() {
        topBar = new HBox();
        inputBar = null;

        errorMessage = new Text();
        errorMessage.setFill(Color.RED);

        addFeedListButton = new ClickButton(MaterialIcon.ADD,
                "MenuButton", "30px", "Add feed list");
        addFeedListButton.setOnMouseClicked(event -> {
            if(inputBar == null) {
                showAddFeedListInput();
            }
            else {
                hideAddFeedListInput();
            }
        });

        treeViewContainer = new VBox();
        menuFeedLists = new ArrayList<>();
        topBar.getChildren().add(addFeedListButton);

        getChildren().add(topBar);
        getChildren().add(errorMessage);
        getChildren().add(treeViewContainer);
        getStyleClass().add("SideMenuItem");
    }

    void addFeedList(String listName) {
        menuFeedLists.add(new MenuFeedList(Configuration.getFeedListByName(listName)));

        treeViewContainer.getChildren().clear();
        treeViewContainer.getChildren().addAll(menuFeedLists);
    }

    void removeFeedList(String listName) {
        for(MenuFeedList menuFeedList : menuFeedLists) {
            if(menuFeedList.getName().equals(listName)) {
                menuFeedLists.remove(menuFeedList);
                break;
            }
        }

        treeViewContainer.getChildren().clear();
        treeViewContainer.getChildren().addAll(menuFeedLists);
    }

    void updateFeedList() {
        ArrayList<FeedList> feedLists = Configuration.getFeedLists();
        boolean feedListExists;

        for(FeedList feedList : feedLists) {
            feedListExists = false;
            for(MenuFeedList menuFeedList : menuFeedLists) {
                if(feedList.getName().equals(menuFeedList.getName())) {
                    feedListExists = true;
                    menuFeedList.update();
                }
            }

            if(!feedListExists) {
                MenuFeedList newMenuFeedList = new MenuFeedList(feedList);
                newMenuFeedList.update();
                menuFeedLists.add(newMenuFeedList);
            }

        }

        treeViewContainer.getChildren().clear();
        treeViewContainer.getChildren().addAll(menuFeedLists);
    }

    private void showAddFeedListInput() {
        inputBar = new TextField();
        inputBar.setMinWidth(350);
        inputBar.setMaxWidth(350);
        inputBar.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                try {
                    RSSReader.addFeedList(inputBar.getText());
                    hideAddFeedListInput();
                    updateFeedList();
                }
                catch(FeedListAlreadyExists err) {
                    errorMessage.setText("A list with that name already exists");
                }
            }
            else if(event.getCode().equals(KeyCode.ESCAPE)) {
                hideAddFeedListInput();
            }
        });

        topBar.getChildren().add(inputBar);
        addFeedListButton.setRotate(45);
    }

    private void hideAddFeedListInput() {
        topBar.getChildren().remove(inputBar);
        inputBar = null;
        addFeedListButton.setRotate(0);
        errorMessage.setText("");
    }
}
