package app.menu;

import app.App;
import app.misc.ClickButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
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
    private TextField addFeedListInput;
    private Text errMessage;
    private ArrayList<MenuFeedList> menuFeedLists;

    TreeView() {
        BorderPane topBar = new BorderPane();

        Label header = new Label("Feedlists");
        header.setFont(Font.font(20));
        header.setTextFill(Color.WHITE);

        ClickButton addFeedListButton = new ClickButton(MaterialIcon.ADD, "MenuButton", "30px",
                "Add feed list");
        addFeedListButton.setOnMouseClicked(event -> showAddFeedListTextField());

        treeViewContainer = new VBox();
        addFeedListInput = null;
        menuFeedLists = new ArrayList<>();

        getStyleClass().add("SideMenuItem");

        topBar.setLeft(header);
        topBar.setRight(addFeedListButton);

        getChildren().add(topBar);
        getChildren().add(treeViewContainer);
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
        menuFeedLists.clear();
        treeViewContainer.getChildren().clear();

        for(FeedList feedList : feedLists) {
            MenuFeedList newMenuFeedList = new MenuFeedList(feedList);
            newMenuFeedList.update();
            menuFeedLists.add(newMenuFeedList);
        }

        treeViewContainer.getChildren().addAll(menuFeedLists);
    }

    void closeAll() {
        getChildren().remove(addFeedListInput);
        addFeedListInput = null;
        getChildren().remove(errMessage);

        for(MenuFeedList list : menuFeedLists) {
            list.hideAddFeedMenu();
            list.hideFeeds();
        }
    }

    private void showAddFeedListTextField() {
        if(addFeedListInput != null) {
            return;
        }

        errMessage = new Text();
        errMessage.setFill(Color.RED);
        addFeedListInput = new TextField();
        addFeedListInput.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                try {
                    App.addFeedList(addFeedListInput.getText());
                    getChildren().remove(errMessage);
                    getChildren().remove(addFeedListInput);
                    addFeedListInput = null;
                    updateFeedList();
                }
                catch(FeedListAlreadyExists err) {
                    errMessage.setText("A list with that name already exists");
                }
            }
            else if(event.getCode().equals(KeyCode.ESCAPE)) {
                getChildren().remove(errMessage);
                getChildren().remove(addFeedListInput);
                addFeedListInput = null;
            }
        });

        getChildren().add(addFeedListInput);
        getChildren().add(errMessage);
    }
}
