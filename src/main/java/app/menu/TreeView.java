package app.menu;

import app.App;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import system.Configuration;
import system.exceptions.FeedListAlreadyExists;

import java.util.ArrayList;

/**
 * Class TreeView
 *
 * @author Axel Nilsson (axnion)
 */
class TreeView extends VBox {
    private TextField addFeedListInput;
    private ArrayList<MenuFeedList> menuFeedLists;

    TreeView() {
        addFeedListInput = null;
        menuFeedLists = new ArrayList<>();
    }

    void addFeedList(String listName) {
        menuFeedLists.add(new MenuFeedList(listName));

        getChildren().clear();
        getChildren().addAll(menuFeedLists);
    }

    void removeFeedList(String listName) {
        for(MenuFeedList menuFeedList : menuFeedLists) {
            if(menuFeedList.getName().equals(listName)) {
                menuFeedLists.remove(menuFeedList);
                break;
            }
        }

        getChildren().clear();
        getChildren().addAll(menuFeedLists);
    }

    void updateFeedList() {
        for(MenuFeedList menuFeedList : menuFeedLists)
            menuFeedList.update();
    }

    void showAddFeedListTextField() {
        if(addFeedListInput != null) {
            return;
        }

        Text errMessage = new Text();
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
