package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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
        Scene primaryScene = new Scene(root, 960, 540);

        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(300);
        primaryStage.setTitle("RSSReader");
        primaryStage.getIcons().add(new Image("file:img/rss_icon.png"));
        primaryStage.setScene(primaryScene);
        primaryStage.show();

        addFeedList("Test1");
        addFeedList("Test2");
        addFeedList("Test3");
        addFeedList("Test4");

        removeFeedList("Test3");
    }

    static void addFeedList(String name) {
        root.addFeedList(name);
    }

    static void removeFeedList(String name) {
        root.removeFeedList(name);
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