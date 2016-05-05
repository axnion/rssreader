package client;

import api.Configuration;
import api.Feed;
import api.ItemList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Class Client
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
public class Client extends Application
{
    private Configuration api;
    private VBox root;
    private HBox urlInputArea;
    private VBox feedList;
    private VBox itemList;
    private ScrollPane itemContainer;

    public Client()
    {
        api = new Configuration();
        api.addItemList("List");
    }

    public void launchJavaFX()
    {
        launch();
    }

    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("RSSReader");

        root = new VBox();
        urlInputArea = new HBox();
        feedList = new VBox();
        itemList = new VBox();
        itemContainer = new ScrollPane(itemList);


        // urlInputArea
        TextField urlInput = new TextField();
        urlInput.setMinWidth(300);
        Button btn = new Button();
        btn.setText("Add");
        urlInputArea.getChildren().addAll(urlInput, btn);
        btn.setOnAction((event) ->
        {
            api.addFeed(urlInput.getText());
            api.addFeedToItemList(0, urlInput.getText());
            urlInput.setText("");
            api.update();
            updateFeeds(api.getFeeds());
            updateItems(api.getItemLists()[0]);
        });

        feedList.getChildren().add(new Label("Feeds"));
        itemList.getChildren().add(new Label("ItemList"));

        root.getChildren().addAll(urlInputArea, feedList, itemContainer);

        primaryStage.setScene(new Scene(root, 500, 1000));
        primaryStage.show();
    }

    private void updateFeeds(Feed[] feeds)
    {
        feedList.getChildren().clear();
        feedList.getChildren().add(new Label("Feeds"));

        for(int i = 0; i < feeds.length; i++)
        {
            feedList.getChildren().add(new FeedBox(feeds[i]));
        }
    }

    private void updateItems(ItemList items)
    {
        itemList.getChildren().clear();
        itemList.getChildren().add(new Label("ItemList"));

        for(int i = 0; i < items.getItems().length; i++)
        {
            itemList.getChildren().add(new ItemBox(items.getItems()[i], new BrowserControl()));
        }
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
