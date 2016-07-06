package app.menu;

import app.App;
import app.misc.ToggleButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;

/**
 * Class SideMenu
 *
 * @author Axel Nilsson (axnion)
 */
public class SideMenu extends VBox {
    private boolean visible;
    private int hiddenWidth = 40;
    private int showingWidth = 230;
    private BorderPane topBar;
    private ToggleButton showHideButton;
    private TreeView treeView;

    public SideMenu() {
        visible = false;
        setMinWidth(hiddenWidth);
        setMaxWidth(hiddenWidth);
        getStyleClass().add("SideMenu");

        topBar = new BorderPane();

        showHideButton = new ToggleButton(MaterialIcon.KEYBOARD_ARROW_RIGHT,
                MaterialIcon.KEYBOARD_ARROW_LEFT, "MenuButton", "30px", "Show/Hide Menu");
        showHideButton.setOnMouseClicked(event -> showHideMenu());
        topBar.setLeft(showHideButton);

        createMenuButtons();

        treeView = new TreeView();
        treeView.managedProperty().bind(treeView.visibleProperty());
        treeView.setVisible(false);

        getChildren().add(topBar);
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

    private void createMenuButtons() {
        HBox buttons = new HBox();
        buttons.setSpacing(10);

        Button newBtn = new Button("New");
        newBtn.getStyleClass().add("CustomButton");
        newBtn.setOnAction(event -> App.newConfiguration());

        Button saveBtn = new Button("Save");
        saveBtn.getStyleClass().add("CustomButton");
        saveBtn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save configuration");
            fileChooser.setInitialFileName("newConfiguration.db");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite",
                    "*.db"));
            File savedFile = fileChooser.showSaveDialog(null);

            if(savedFile != null) {
                App.saveConfiguration(savedFile.getAbsolutePath());
            }
        });

        Button loadBtn = new Button("Load");
        loadBtn.getStyleClass().add("CustomButton");
        loadBtn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load configuration");
            fileChooser.setInitialFileName("~");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite",
                    "*.db"));
            File loadedFile = fileChooser.showOpenDialog(null);

            if(loadedFile != null) {
                App.loadConfiguration(loadedFile.getAbsolutePath());
            }
        });

        buttons.getChildren().addAll(newBtn, saveBtn, loadBtn);
        buttons.setVisible(false);

        topBar.setRight(buttons);
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
            treeView.closeAll();
        }

        treeView.setVisible(visible);
        topBar.getRight().setVisible(visible);

        Timeline openMenu = new Timeline(new KeyFrame(Duration.millis(0.5),
                event -> changeWidth(increment * 2)));
        openMenu.setCycleCount(showingWidth - hiddenWidth);
        openMenu.play();
    }

    private void changeWidth(int increment) {
        setMinWidth(getMinWidth() + increment);
        setMaxWidth(getMaxWidth() + increment);
    }
}
