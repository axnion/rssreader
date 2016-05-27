package client;

import api.Configuration;
import api.Feed;

import api.ItemList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Class Client
 *
 * @author Axel Nilsson (axnion)
 * @version 0.2
 */
public class Client extends Application
{
    private static VBox root;

    static HBox itemListContainer;
    static ArrayList<ItemListBox> itemListBoxes;
    static String currentLoadedFile;
    static BrowserControl bc;
    static Configuration api;

    public void launchJavaFX()
    {
        launch();
    }

    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("RSSReader");
        api = new Configuration();
        bc = new BrowserControl();
        //primaryStage.getIcons().add(new Image("file:img/rss_icon.png"));

        root = new VBox();
        itemListContainer = new HBox();
        itemListBoxes = new ArrayList<>();
        currentLoadedFile = null;

        MenuBar menuBar = new MainMenu();

        // addMenu
        VBox addMenu = new VBox();
        HBox saveAndLoadArea = new HBox();
        HBox feedAddArea = new HBox();
        HBox itemListAddArea = new HBox();
        addMenu.getChildren().addAll(menuBar, saveAndLoadArea, feedAddArea, itemListAddArea);

        startFeedUpdater(1);

        root.getChildren().add(addMenu);
        root.getChildren().add(itemListContainer);

        primaryStage.setScene(new Scene(root, 500, 1000));
        primaryStage.show();
    }

    private static void startFeedUpdater(int interval)
    {
        Timeline feedUpdater = new Timeline(new KeyFrame(Duration.minutes(interval),
                event -> checkForUpdates()));

        feedUpdater.setCycleCount(Animation.INDEFINITE);
        feedUpdater.play();
    }

    static void addFeed(String url, boolean addToConfig)
    {
        if(url.equals(""))
            return;

        if(addToConfig)
            api.addFeedToConfiguration(url);

        try
        {
            api.update();
        }
        catch(RuntimeException err)
        {
            displayErrorMessage(err.getMessage());
            err.printStackTrace();
        }
    }

    static void removeFeed(String url)
    {
        if(url.equals(""))
            return;

        if(api.getItemLists() != null)
        {
            for(ItemList itemList : api.getItemLists())
            {
                for(String feedUrl : itemList.getFeedUrls())
                {
                    if(feedUrl.equals(url))
                        api.removeFeedFromItemList(itemList.getName(), url);
                }
            }
        }

        api.removeFeedFromConfiguration(url);

        try
        {
            api.update();
        }
        catch(RuntimeException err)
        {
            displayErrorMessage(err.getMessage());
            err.printStackTrace();
        }

        for(ItemListBox itemListBox : itemListBoxes)
        {
            itemListBox.updateMenu(api.getFeeds());
            itemListBox.updateItems(api.getItemList(itemListBox.getName()));
        }
    }

    static void addItemList(String name, boolean addToConfig)
    {
        if(name.equals(""))
            return;

        try
        {
            Button removeBtn = new Button();
            removeBtn.setText("Remove");
            removeBtn.setOnAction((event) -> removeItemList(name));

            if(addToConfig)
                api.addItemListToConfiguration(name);

            itemListBoxes.add(new ItemListBox(name, removeBtn));

            itemListContainer.getChildren().removeAll(itemListBoxes);

            for(ItemListBox itemListBox : itemListBoxes)
                itemListContainer.getChildren().add(itemListBox);
        }
        catch(RuntimeException err)
        {
            displayErrorMessage(err.getMessage());
            err.printStackTrace();
        }
    }

    static void removeItemList(String name)
    {
        if(name.equals(""))
            return;

        try
        {
            api.removeItemListFromConfiguration(name);

            int i;
            for(i = 0; i < itemListBoxes.size(); i++)
            {
                if(itemListBoxes.get(i).getName().equals(name))
                    break;
            }

            itemListContainer.getChildren().removeAll(itemListBoxes);

            itemListBoxes.remove(i);
            for(ItemListBox itemListBox : itemListBoxes)
                itemListContainer.getChildren().add(itemListBox);
        }
        catch(RuntimeException err)
        {
            displayErrorMessage(err.getMessage());
            err.printStackTrace();
        }
    }

    static void checkForUpdates()
    {
        api.update();

        for(ItemListBox itemListBox : itemListBoxes)
            itemListBox.updateMenu(api.getFeeds());
    }

    static void loadConfiguration(String path)
    {
        api.loadConfig(path);

        root.getChildren().removeAll(itemListContainer);
        itemListContainer = new HBox();
        root.getChildren().add(itemListContainer);
        itemListBoxes.clear();

        Feed[] feeds = api.getFeeds();
        ItemList[] itemLists = api.getItemLists();

        for(Feed feed : feeds)
            addFeed(feed.getUrlToXML(), false);

        for(ItemList itemList : itemLists)
            addItemList(itemList.getName(), false);
    }

    static void saveConfiguration(String path)
    {
        api.saveConfig(path);
    }

    static void displayErrorMessage(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong!");
        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText(message);
        alert.showAndWait();
    }

    class BrowserControl
    {
        void openLink(String url)
        {
            getHostServices().showDocument(url);
        }
    }
}

// Created: 2016-04-18
