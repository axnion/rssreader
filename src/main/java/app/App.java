package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import system.Configuration;

/**
 * Class App
 *
 * @author Axel Nilsson (axnion)
 */
public class App extends Application {
    static Wrapper root;
    static BrowserAccess browserAccess;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        browserAccess = new BrowserAccess();
        root = new Wrapper();

        Configuration.loadDatabase();

        Scene primaryScene = new Scene(root, 960, 540);
        primaryScene.getStylesheets().add("file:css/style.css");

        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(300);
        primaryStage.setTitle("RSSReader");
        primaryStage.getIcons().add(new Image("file:img/rss_icon.png"));
        primaryStage.setScene(primaryScene);
        primaryStage.show();

        addFeedList("MyFeedList");
        addFeedList("MyFeedList2");
        addFeed("http://feeds.feedburner.com/sakerhetspodcasten", "MyFeedList");
        addFeed("http://feedpress.me/kodsnack", "MyFeedList2");

        //root.updateFeedLists();
    }

    static void addFeedList(String listName) {
        Configuration.addFeedList(listName);
        root.addFeedList(listName);
    }

    static void removeFeedList(String listName) {
        Configuration.removeFeedList(listName);
        root.removeFeedList(listName);
    }

    static void addFeed(String urlToXml, String listName) {
        Configuration.addFeed(urlToXml, listName);
        root.updateFeedLists();
    }

    static void removeFeed(String urlToXml, String listName) {
        Configuration.removeFeed(urlToXml, listName);
        root.updateFeedLists();
    }

    /**
     * A class containing a method for opening a link using the getHostService which requires
     * the class to have access to host services.
     */
    class BrowserAccess {
        /**
         * Opens the default browser with the url given to it.
         * @param url URL to the site we want to open.
         */
        void openLink(String url) {
            getHostServices().showDocument(url);
        }
    }
}