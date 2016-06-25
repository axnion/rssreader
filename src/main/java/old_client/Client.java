//package client;
//
//import api.Configuration;
//import api.Feed;
//
//import api.ItemList;
//import javafx.animation.Animation;
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.application.Application;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.image.Image;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//
//import java.util.ArrayList;
//
///**
// * Class Client
// *
// * This is the main class the the Client package. It extends Application so JavaFX is launched from
// * this class. Most of the class is static and all fields can be accessed by the other classes
// * in the package.
// *
// * @author Axel Nilsson (axnion)
// * @version 1.0
// */
//public class Client extends Application
//{
//    static VBox root;
//    static Stage mainStage;
//    static Scene primaryScene;
//    static VBox settingsTopBox;
//    static VBox settingsSideBox;
//    static HBox itemListContainer;
//    static ArrayList<ListBox> listBoxes;
//    static String currentLoadedFile;
//    static MainMenu menuBar;
//    static BrowserControl bc;
//    static Configuration api;
//
//    /**
//     * Launches JavaFX
//     */
//    public void launchJavaFX()
//    {
//        launch();
//    }
//
//    /**
//     * The start method for JavaFX. This is called when JavaFX is launched.
//     * @param primaryStage The main stage for the application.
//     */
//    public void start(Stage primaryStage)
//    {
//        api = new Configuration();
//        mainStage = primaryStage;
//        root = new VBox();
//        itemListContainer = new HBox();
//        listBoxes = new ArrayList<>();
//        settingsTopBox = new VBox();
//        settingsSideBox = new VBox();
//        currentLoadedFile = null;
//
//        bc = new BrowserControl();
//        primaryScene = new Scene(root, 960, 540);
//
//        settingsTopBox.setStyle("-fx-background-color: #fff;");
//        settingsTopBox.setAlignment(Pos.CENTER);
//        settingsSideBox.setStyle("-fx-background-color: #fff;");
//        settingsSideBox.setAlignment(Pos.TOP_CENTER);
//        settingsSideBox.setMaxWidth(400);
//
//        itemListContainer.setPrefHeight(primaryScene.getHeight());
//        itemListContainer.setStyle("-fx-background-color: lightgrey;");
//        itemListContainer.getChildren().add(settingsSideBox);
//
//        primaryScene.heightProperty().addListener(e ->
//        {
//            settingsSideBox.setPrefHeight(primaryScene.getHeight());
//            itemListContainer.setPrefHeight(primaryScene.getHeight());
//        });
//
//        primaryScene.widthProperty().addListener(e ->
//        {
//            for(ListBox listBox : listBoxes)
//                listBox.setListWidth((mainStage.getWidth() - 20) / listBoxes.size());
//        });
//
//        menuBar = new MainMenu();
//        startFeedUpdater(1);
//
//        ScrollPane listScroll = new ScrollPane(itemListContainer);
//        listScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
//        listScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//
//        root.getChildren().addAll(menuBar, settingsTopBox, listScroll);
//
//        mainStage.setMinWidth(300);
//        mainStage.setMinHeight(300);
//        mainStage.setTitle("RSSReader");
//        mainStage.getIcons().add(new Image("file:img/rss_icon.png"));
//        mainStage.setScene(primaryScene);
//        mainStage.show();
//    }
//
//    /**
//     * Called when we want to add a feed to the current Configuration.
//     * @param url           The URL of the feed we want added.
//     * @param addToConfig   True if the feed should be added to the Configuration.
//     */
//    static void addFeed(String url, boolean addToConfig)
//    {
//        if(url.equals(""))
//            return;
//
//        if(addToConfig)
//            api.addFeedToConfiguration(url);
//
//        try
//        {
//            api.update();
//        }
//        catch(RuntimeException err)
//        {
//            displayErrorMessage(err.getMessage());
//            err.printStackTrace();
//        }
//    }
//
//    /**
//     * Called when we want to remove a feed from the current Configuration.
//     * @param url The URL to the XML file of the Feed we want to remove.
//     */
//    static void removeFeed(String url)
//    {
//        if(url.equals(""))
//            return;
//
//        if(api.getItemLists() != null)
//        {
//            for(ItemList itemList : api.getItemLists())
//            {
//                if(itemList.getFeedUrls() == null)
//                    continue;
//
//                for(String feedUrl : itemList.getFeedUrls())
//                {
//                    if(feedUrl.equals(url))
//                        api.removeFeedFromItemList(itemList.getName(), url);
//                }
//            }
//        }
//
//        api.removeFeedFromConfiguration(url);
//
//        try
//        {
//            api.update();
//        }
//        catch(RuntimeException err)
//        {
//            displayErrorMessage(err.getMessage());
//            err.printStackTrace();
//        }
//
//        for(ListBox itemListBox : listBoxes)
//        {
//            itemListBox.updateMenu(api.getFeeds());
//            itemListBox.updateItems(api.getItemList(itemListBox.getName()));
//        }
//
//        menuBar.openShowFeedsWindow();
//    }
//
//    /**
//     * Called when we want add an ItemList to the Configuration.
//     * @param name          The name we want to give the ItemList
//     * @param addToConfig   True if the feed should be added to the Configuration.
//     */
//    static void addItemList(String name, boolean addToConfig)
//    {
//        if(name.equals(""))
//            return;
//
//        try
//        {
//            if(addToConfig)
//                api.addItemListToConfiguration(name);
//
//            listBoxes.add(new ListBox(name));
//
//            itemListContainer.getChildren().removeAll(listBoxes);
//
//            for(ListBox listBox : listBoxes)
//            {
//                listBox.setListWidth((mainStage.getWidth() - 20) / listBoxes.size());
//                itemListContainer.getChildren().add(listBox);
//            }
//        }
//        catch(RuntimeException err)
//        {
//            displayErrorMessage(err.getMessage());
//            err.printStackTrace();
//        }
//    }
//
//    /**
//     * Called when we want to remove an ItemList from the Configuration.
//     * @param name  The name of the ItemList we want to remove.
//     */
//    static void removeItemList(String name)
//    {
//        if(name.equals(""))
//            return;
//
//        try
//        {
//            api.removeItemListFromConfiguration(name);
//
//            int i;
//            for(i = 0; i < listBoxes.size(); i++)
//            {
//                if(listBoxes.get(i).getName().equals(name))
//                    break;
//            }
//
//            itemListContainer.getChildren().removeAll(listBoxes);
//
//            listBoxes.remove(i);
//
//            for(ListBox listBox : listBoxes)
//            {
//                listBox.setListWidth((mainStage.getWidth() - 20) / listBoxes.size());
//                itemListContainer.getChildren().add(listBox);
//            }
//
//        }
//        catch(RuntimeException err)
//        {
//            displayErrorMessage(err.getMessage());
//            err.printStackTrace();
//        }
//
//        menuBar.openShowListsWindow();
//    }
//
//    /**
//     * Updates the Configuration and all the ListBoxes.
//     */
//    static void checkForUpdates()
//    {
//        api.update();
//
//        for(ListBox itemListBox : listBoxes)
//            itemListBox.updateMenu(api.getFeeds());
//    }
//
//    /**
//     * Called when we want to load a Configuration from the file system.
//     * @param path  The path to the file we want to load.
//     */
//    static void loadConfiguration(String path)
//    {
//        Client.resetApplication();
//        api.loadConfig(path);
//
//        Feed[] feeds = api.getFeeds();
//        ItemList[] itemLists = api.getItemLists();
//
//        if(feeds != null)
//        {
//            for(Feed feed : feeds)
//                addFeed(feed.getUrlToXML(), false);
//        }
//
//        if(itemLists != null)
//        {
//            for(ItemList itemList : itemLists)
//                addItemList(itemList.getName(), false);
//        }
//    }
//
//    /**
//     * Called when we want to save a Configuration to the file system.
//     * @param path The path to the file we want to write.
//     */
//    static void saveConfiguration(String path)
//    {
//        api.saveConfig(path);
//    }
//
//    /**
//     * Resets the whole application and clears the interface.
//     */
//    static void resetApplication()
//    {
//        api = new Configuration();
//        itemListContainer.getChildren().removeAll(listBoxes);
//        listBoxes.clear();
//        settingsTopBox.getChildren().clear();
//        settingsSideBox.getChildren().clear();
//        currentLoadedFile = null;
//    }
//
//    /**
//     * Displays an alert window and shows a message to the user.
//     * @param message   The message we want to display to the user.
//     */
//    static void displayErrorMessage(String message)
//    {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Something went wrong!");
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    /**
//     * Starts the updater that updates the Configuration every minute.
//     * @param interval The interval we want the Configuration to update. In minutes.
//     */
//    private static void startFeedUpdater(int interval)
//    {
//        Timeline feedUpdater = new Timeline(new KeyFrame(Duration.minutes(interval),
//                event -> checkForUpdates()));
//
//        feedUpdater.setCycleCount(Animation.INDEFINITE);
//        feedUpdater.play();
//    }
//
//    /**
//     * A class containing a method for opening a link using the getHostService which requires
//     * the class to have access to host services.
//     */
//    class BrowserControl
//    {
//        /**
//         * Opens the default browser with the url given to it.
//         * @param url URL to the site we want to open.
//         */
//        void openLink(String url)
//        {
//            getHostServices().showDocument(url);
//        }
//    }
//}
//
//// Created: 2016-04-18
