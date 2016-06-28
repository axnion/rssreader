package app.menu;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
    private VBox showHideButtonContainer;
    private Text showHideButton;
    private TreeView treeView;

    public SideMenu() {
        visible = false;
        setMinWidth(hiddenWidth);
        setMaxWidth(hiddenWidth);
        getStyleClass().add("SideMenu");

        showHideButton = MaterialIconFactory.get().createIcon(MaterialIcon.KEYBOARD_ARROW_RIGHT,
                "30px");
        showHideButton.getStyleClass().add("SideMenuArrow");
        showHideButtonContainer = new VBox(showHideButton);
        showHideButtonContainer.setOnMouseClicked(event -> showHideMenu());
        getChildren().add(showHideButtonContainer);

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
            showHideButtonContainer.getChildren().clear();
            showHideButton = MaterialIconFactory.get().createIcon(MaterialIcon.KEYBOARD_ARROW_RIGHT,
                    "30px");
            showHideButton.getStyleClass().add("SideMenuArrow");
            showHideButtonContainer.getChildren().add(showHideButton);
        }
        else {
            visible = true;
            increment = 1;
            showHideButtonContainer.getChildren().clear();
            showHideButton = MaterialIconFactory.get().createIcon(MaterialIcon.KEYBOARD_ARROW_LEFT,
                    "30px");
            showHideButton.getStyleClass().add("SideMenuArrow");
            showHideButtonContainer.getChildren().add(showHideButton);
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
