package client;

import api.Configuration;
import api.Feed;

import api.ItemList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
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
    static VBox root;
    static Stage mainStage;
    static Scene primaryScene;
    static VBox settingsTopBox;
    static VBox settingsSideBox;
    static HBox itemListContainer;
    static ArrayList<ListBox> listBoxes;
    static String currentLoadedFile;
    static MainMenu menuBar;
    static BrowserControl bc;
    static Configuration api;

    public void launchJavaFX()
    {
        launch();
    }

    public void start(Stage primaryStage)
    {
        api = new Configuration();
        mainStage = primaryStage;
        root = new VBox();
        itemListContainer = new HBox();
        listBoxes = new ArrayList<>();
        settingsTopBox = new VBox();
        settingsSideBox = new VBox();
        currentLoadedFile = null;

        bc = new BrowserControl();
        primaryScene = new Scene(root, 960, 540);

        settingsTopBox.setStyle("-fx-background-color: #fff;");
        settingsTopBox.setAlignment(Pos.CENTER);
        settingsSideBox.setStyle("-fx-background-color: #fff;");
        settingsSideBox.setAlignment(Pos.TOP_CENTER);
        settingsSideBox.setMaxWidth(400);

        itemListContainer.setPrefHeight(primaryScene.getHeight());
        itemListContainer.setStyle("-fx-background-color: lightgrey;");
        itemListContainer.getChildren().add(settingsSideBox);

        primaryScene.heightProperty().addListener(e ->
        {
            settingsSideBox.setPrefHeight(primaryScene.getHeight());
            itemListContainer.setPrefHeight(primaryScene.getHeight());
        });

        primaryScene.widthProperty().addListener(e ->
        {
            for(ListBox listBox : listBoxes)
                listBox.setPrefWidth((mainStage.getWidth() - 20) / listBoxes.size());
        });

        menuBar = new MainMenu();
        startFeedUpdater(1);

        ScrollPane listScroll = new ScrollPane(itemListContainer);
        listScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        listScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        root.getChildren().addAll(menuBar, settingsTopBox, listScroll);

        mainStage.setTitle("RSSReader");
        //mainStage.getIcons().add(new Image("file:img/rss_icon.png"));
        mainStage.setScene(primaryScene);
        mainStage.show();
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

        for(ListBox itemListBox : listBoxes)
        {
            itemListBox.updateMenu(api.getFeeds());
            itemListBox.updateItems(api.getItemList(itemListBox.getName()));
        }

        menuBar.openShowFeedsWindow();
    }

    static void addItemList(String name, boolean addToConfig)
    {
        if(name.equals(""))
            return;

        try
        {
            if(addToConfig)
                api.addItemListToConfiguration(name);

            listBoxes.add(new ListBox(name));

            itemListContainer.getChildren().removeAll(listBoxes);

            for(ListBox listBox : listBoxes)
            {
                listBox.setPrefWidth((mainStage.getWidth() - 20) / listBoxes.size());
                itemListContainer.getChildren().add(listBox);
            }
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
            for(i = 0; i < listBoxes.size(); i++)
            {
                if(listBoxes.get(i).getName().equals(name))
                    break;
            }

            itemListContainer.getChildren().removeAll(listBoxes);

            listBoxes.remove(i);

            for(ListBox listBox : listBoxes)
            {
                listBox.setPrefWidth((mainStage.getWidth() - 20) / listBoxes.size());
                itemListContainer.getChildren().add(listBox);
            }

        }
        catch(RuntimeException err)
        {
            displayErrorMessage(err.getMessage());
            err.printStackTrace();
        }

        menuBar.openShowListsWindow();
    }

    static void checkForUpdates()
    {
        api.update();

        for(ListBox itemListBox : listBoxes)
            itemListBox.updateMenu(api.getFeeds());
    }

    static void loadConfiguration(String path)
    {
        api.loadConfig(path);

        root.getChildren().removeAll(itemListContainer);
        itemListContainer = new HBox();
        root.getChildren().add(itemListContainer);
        listBoxes.clear();

        Feed[] feeds = api.getFeeds();
        ItemList[] itemLists = api.getItemLists();

        if(feeds != null)
        {
            for(Feed feed : feeds)
                addFeed(feed.getUrlToXML(), false);
        }

        if(itemLists != null)
        {
            for(ItemList itemList : itemLists)
                addItemList(itemList.getName(), false);
        }
    }

    static void saveConfiguration(String path)
    {
        api.saveConfig(path);
    }

    static void resetApplication()
    {
        api = new Configuration();
        itemListContainer.getChildren().removeAll(listBoxes);
        listBoxes.clear();
        settingsTopBox.getChildren().clear();
        settingsSideBox.getChildren().clear();
        currentLoadedFile = null;
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
