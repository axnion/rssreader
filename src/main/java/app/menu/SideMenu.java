package app.menu;

import app.misc.ClickButton;
import app.misc.ToggleButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    private ToggleButton showHideButton;
    private ClickButton addFeedListButton;
    private TreeView treeView;

    public SideMenu() {
        visible = false;
        setMinWidth(hiddenWidth);
        setMaxWidth(hiddenWidth);
        getStyleClass().add("SideMenu");

        showHideButton = new ToggleButton(MaterialIcon.KEYBOARD_ARROW_RIGHT,
                MaterialIcon.KEYBOARD_ARROW_LEFT, "MenuButton", "30px", "Show/Hide Menu");
        showHideButton.setOnMouseClicked(event -> showHideMenu());
        getChildren().add(showHideButton);

        addFeedListButton = new ClickButton(MaterialIcon.ADD, "MenuButton", "30px",
                "Add feed list");
        addFeedListButton.setOnMouseClicked(event -> showAddFeedListTextField());
        addFeedListButton.setVisible(false);
        getChildren().add(addFeedListButton);

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
        showHideButton.toggle();

        if(visible) {
            visible = false;
            increment = -1;
        }
        else {
            visible = true;
            increment = 1;
        }

        treeView.setVisible(visible);
        addFeedListButton.setVisible(visible);

        Timeline openMenu = new Timeline(new KeyFrame(Duration.millis(1),
                event -> changeWidth(increment * 2)));
        openMenu.setCycleCount(showingWidth - hiddenWidth);
        openMenu.play();
    }

    private void showAddFeedListTextField() {
        treeView.showAddFeedListTextField();
    }

    private void changeWidth(int increment) {
        setMinWidth(getMinWidth() + increment);
        setMaxWidth(getMaxWidth() + increment);
    }
}
