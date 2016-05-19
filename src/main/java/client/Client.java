package client;

import api.Configuration;
import api.Feed;


import api.ItemList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

/**
 * Class Client
 *
 * @author Axel Nilsson (axnion)
 * @version 0.2
 */
public class Client extends Application
{
    private VBox root;
    private Configuration api;
    private VBox feedList;
    private ArrayList<ItemListBox> itemListBoxes;
    HBox itemListContainer;

    public Client()
    {
        api = new Configuration();
    }

    public void launchJavaFX()
    {
        launch();
    }

    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("RSSReader");

        primaryStage.getIcons().add(new Image("file:RSSReader.ico"));

        root = new VBox();
        feedList = new VBox();
        itemListContainer = new HBox();
        itemListBoxes = new ArrayList<>();

        // addMenu
        VBox addMenu = new VBox();
        HBox saveAndLoadArea = new HBox();
        HBox feedAddArea = new HBox();
        HBox itemListAddArea = new HBox();
        addMenu.getChildren().addAll(saveAndLoadArea, feedAddArea, itemListAddArea);

        // saveAndLoadArea
        TextField saveFileInput = new TextField();
        saveFileInput.setMinWidth(300);
        Button saveConfigBtn = new Button();
        saveConfigBtn.setText("Save");
        Button loadConfigBtn = new Button();
        loadConfigBtn.setText("Load");
        saveAndLoadArea.getChildren().addAll(saveFileInput, saveConfigBtn, loadConfigBtn);
        saveConfigBtn.setOnAction((event) -> saveConfiguration(saveFileInput.getText()));
        loadConfigBtn.setOnAction((event) -> loadConfiguration(saveFileInput.getText()));

        // feedAddArea
        TextField feedInput = new TextField();
        feedInput.setMinWidth(300);
        Button addFeedBtn = new Button();
        addFeedBtn.setText("Add Feed");
        feedAddArea.getChildren().addAll(feedInput, addFeedBtn);
        addFeedBtn.setOnAction((event) ->
        {
            addFeed(feedInput.getText(), true);
            feedInput.setText("");
        });

        // itemListAddArea
        TextField itemListNameInput = new TextField();
        itemListNameInput.setMinWidth(300);
        Button addItemListBtn = new Button();
        addItemListBtn.setText("Add Item List");
        itemListAddArea.getChildren().addAll(itemListNameInput, addItemListBtn);
        addItemListBtn.setOnAction((event) ->
        {
            addItemList(itemListNameInput.getText(), true);
            itemListNameInput.setText("");
        });

        feedList.getChildren().add(new Label("Feeds"));

        root.getChildren().add(addMenu);
        root.getChildren().add(feedList);
        root.getChildren().add(itemListContainer);

        primaryStage.setScene(new Scene(root, 500, 1000));
        primaryStage.show();
    }

    private void updateFeeds(Feed[] feeds)
    {
        feedList.getChildren().clear();
        feedList.getChildren().add(new Label("Feeds"));

        if(feeds == null)
            return;

        for(int i = 0; i < feeds.length; i++)
            feedList.getChildren().add(new FeedBox(feeds[i]));

        for(ItemListBox itemListBox : itemListBoxes)
            itemListBox.updateMenu(feeds);
    }

    private void addFeed(String url, boolean addToConfig)
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

        updateFeeds(api.getFeeds());
    }

    private void addItemList(String name, boolean addToConfig)
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

            itemListBoxes.add(new ItemListBox(name, api, new BrowserControl(), removeBtn));

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

    private void removeItemList(String name)
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

    private void loadConfiguration(String path)
    {
        api.loadConfig(path);

        root.getChildren().removeAll(feedList);
        root.getChildren().removeAll(itemListContainer);
        feedList = new VBox();
        itemListContainer = new HBox();
        root.getChildren().add(feedList);
        root.getChildren().add(itemListContainer);
        itemListBoxes.clear();

        Feed[] feeds = api.getFeeds();
        ItemList[] itemLists = api.getItemLists();

        for(Feed feed : feeds)
            addFeed(feed.getUrlToXML(), false);

        for(ItemList itemList : itemLists)
            addItemList(itemList.getName(), false);
    }

    private void saveConfiguration(String path)
    {
        api.saveConfig(path);
    }

    private void displayErrorMessage(String message)
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
