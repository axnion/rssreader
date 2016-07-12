package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import system.Configuration;

/**
 * Class App
 *
 * @author Axel Nilsson (axnion)
 */
public class App extends Application {
    public static Wrapper root;
    public static ContextMenu openContextMenu;
    private static BrowserAccess browserAccess;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        browserAccess = new BrowserAccess();
        root = new Wrapper();
        openContextMenu = null;

        Scene primaryScene = new Scene(root, 960, 540);
        primaryScene.getStylesheets().add("file:css/style.css");
        primaryScene.getStylesheets().add("file:css/buttons.css");

        createContextMenuEscape();

        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(300);
        primaryStage.setTitle("RSSReader");
        primaryStage.getIcons().add(new Image("file:img/rss_icon.png"));
        primaryStage.setScene(primaryScene);
        primaryStage.show();

//        addFeedList("MyFeedList");
//        addFeedList("MyFeedList2");
//        addFeed("http://feeds.feedburner.com/sakerhetspodcasten", "MyFeedList");
//        addFeed("http://feedpress.me/kodsnack", "MyFeedList2");

        root.updateFeedLists();
    }

    public static void addFeedList(String listName) {
        Configuration.addFeedList(listName);
        root.addFeedList(listName);
    }

    public static void removeFeedList(String listName) {
        Configuration.removeFeedList(listName);
        root.removeFeedList(listName);
    }

    public static void addFeed(String urlToXml, String listName) {
        Configuration.addFeed(urlToXml, listName);
        root.updateFeedLists();
    }

    public static void removeFeed(String urlToXml, String listName) {
        Configuration.removeFeed(urlToXml, listName);
        root.updateFeedLists();
    }

    public static void newConfiguration() {
        try {
            Configuration.newDatabase();
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        root.reset();
        root.updateFeedLists();
    }

    public static void saveConfiguration(String path) {
        try {
            Configuration.saveDatabase(path);
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        root.updateFeedLists();
    }

    public static void loadConfiguration(String path) {
        try {
            Configuration.loadDatabase(path);
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        root.reset();
        root.updateFeedLists();
    }

    public static void openLink(String url) {
        browserAccess.openLink(url);
    }

    private static void createContextMenuEscape() {
        root.setOnMouseClicked(event -> {
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