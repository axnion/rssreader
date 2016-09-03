package app;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import system.Configuration;

/**
 * Class RSSReader
 *
 * @author Axel Nilsson (axnion)
 */
public class RSSReader extends Application {
    public static Wrapper wrapper;
    public static ContextMenu openContextMenu;
    private static HBox messageBox;
    private static BrowserAccess browserAccess;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        wrapper = new Wrapper();
        messageBox = new HBox();
        browserAccess = new BrowserAccess();
        openContextMenu = null;

        messageBox.setMaxHeight(20);
        messageBox.setMinHeight(20);
        messageBox.setAlignment(Pos.CENTER);
        messageBox.getStyleClass().add("TopBar");

        VBox root = new VBox();
        root.getChildren().add(messageBox);
        root.getChildren().add(wrapper);

        root.setVgrow(wrapper, Priority.ALWAYS);
        root.setVgrow(messageBox, Priority.ALWAYS);

        Scene primaryScene = new Scene(root, 960, 540);
        primaryScene.getStylesheets().add("file:css/style.css");
        primaryScene.getStylesheets().add("file:css/buttons.css");

        createContextMenuEscape();

        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(300);
        primaryStage.setTitle("RSSReader");
        primaryStage.getIcons().add(new Image("file:RSSReader.png"));
        primaryStage.setScene(primaryScene);
        primaryStage.show();

        Timeline guiUpdater = new Timeline(new KeyFrame(Duration.seconds(5),
                event -> wrapper.update()));
        guiUpdater.setCycleCount(Animation.INDEFINITE);
        guiUpdater.play();

        Timeline autoSave = new Timeline(new KeyFrame(Duration.seconds(
                Configuration.getAutoSavePeriod()), event -> {
            try {
                Configuration.save();
            }
            catch(Exception expt) {
                expt.printStackTrace();
            }
        }));
        autoSave.setCycleCount(Animation.INDEFINITE);
        autoSave.play();

        Configuration.startFeedUpdater();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            try {
                Configuration.stopFeedUpdater();
            }
            catch(InterruptedException expt) {
                expt.printStackTrace();

            }
        });
    }

    public static void addFeedList(String listName) {
        Configuration.addFeedList(listName);
        wrapper.addFeedList(listName);
    }

    public static void removeFeedList(String listName) {
        Configuration.removeFeedList(listName);
        wrapper.removeFeedList(listName);
    }

    public static void addFeed(String urlToXml, String listName) {
        Configuration.addFeed(urlToXml, listName);
        wrapper.update();
    }

    public static void removeFeed(String urlToXml, String listName) {
        Configuration.removeFeed(urlToXml, listName);
        wrapper.update();
    }

    public static void newConfiguration() {
        try {
            Configuration.reset();
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        wrapper.reset();
        wrapper.update();
    }

    public static void loadConfiguration(String path) {
        try {
            Configuration.load(path);
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        wrapper.reset();
        wrapper.update();
        showMessage("Loaded " + path);
    }

    public static void saveConfiguration() {
        try {
            Configuration.save();
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        showMessage("Saved");
    }

    public static void saveConfiguration(String path) {
        try {
            Configuration.save(path);
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        showMessage("Saved to " + path);
    }

    public static void openLink(String url) {
        browserAccess.openLink(url);
    }

    private static void showMessage(String message) {
        Text text = new Text(message);
        text.setStyle("-fx-fill: white");
        messageBox.getChildren().add(text);

        Timeline updateTimer = new Timeline(new KeyFrame(Duration.seconds(10),
                event -> messageBox.getChildren().clear()));
        updateTimer.setCycleCount(1);
        updateTimer.play();
    }

    private static void createContextMenuEscape() {
        wrapper.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                if(openContextMenu != null) {
                    openContextMenu.hide();
                }
            }
        });
    }

    /**
     * A class containing a method for opening a link using the getHostService which requires
     * the class to have access to host services.
     */
    private class BrowserAccess {
        /**
         * Opens the default browser with the url given to it.
         * @param url URL to the site we want to open.
         */
        void openLink(String url) {
            getHostServices().showDocument(url);
        }
    }
}