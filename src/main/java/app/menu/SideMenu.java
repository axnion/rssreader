package app.menu;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Class SideMenu
 *
 * @author Axel Nilsson (axnion)
 */
public class SideMenu extends VBox {
    private boolean visible;
    private int hiddenWidth = 30;
    private int showingWidth = 230;
    private CustomButton showHideButton;
    private TreeView treeView;

    public SideMenu() {
        visible = false;
        setMinWidth(hiddenWidth);
        setMaxWidth(hiddenWidth);
        getStyleClass().add("SideMenu");

        showHideButton = new CustomButton("file:img/arrowRight.png", "SideMenuArrow");
        showHideButton.setOnMouseClicked(event -> showHideMenu());
        getChildren().add(showHideButton);

        treeView = new TreeView();
        treeView.managedProperty().bind(treeView.visibleProperty());
        treeView.setVisible(false);
        getChildren().add(treeView);
    }

    public void addFeedList(String listName) {
        treeView.addFeedList(listName);
    }

    public void removeFeedList(String listName) {
        treeView.removeFeedList(listName);
    }

    public void updateFeedLists() {
        treeView.updateFeedList();
    }

    private void showHideMenu() {
        int increment;

        if(visible) {
            visible = false;
            increment = -1;
            showHideButton.setImage("file:img/arrowRight.png");
        }
        else {
            visible = true;
            increment = 1;
            showHideButton.setImage("file:img/arrowLeft.png");
        }

        treeView.setVisible(visible);

        Timeline openMenu = new Timeline(new KeyFrame(Duration.millis(1),
                event -> changeWidth(increment * 2)));
        openMenu.setCycleCount(showingWidth - hiddenWidth);
        openMenu.play();
    }

    private void changeWidth(int increment) {
        setMinWidth(getMinWidth() + increment);
        setMaxWidth(getMaxWidth() + increment);
    }
}
