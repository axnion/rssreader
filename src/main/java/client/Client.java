package client;

import api.Configuration;
import api.Feed;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    Configuration api;
    private VBox root;
    //private HBox saveAndLoadArea;
    private HBox urlInputArea;
    private VBox feedList;
    private ItemListBox[] itemListBoxes;

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

        root = new VBox();
        urlInputArea = new HBox();
        feedList = new VBox();
        api.addItemListToConfiguration("List");
        itemListBoxes = new ItemListBox[1];
        itemListBoxes[0] = new ItemListBox("List", api, new BrowserControl());

        // urlInputArea
        TextField urlInput = new TextField();
        urlInput.setMinWidth(300);
        Button btn = new Button();
        btn.setText("Add");
        urlInputArea.getChildren().addAll(urlInput, btn);
        btn.setOnAction((event) ->
        {
            api.addFeedToConfiguration(urlInput.getText());
            urlInput.setText("");

            try
            {
                api.update();
            }
            catch(RuntimeException err)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(err.getLocalizedMessage());
                alert.setHeaderText("Look, an Information Dialog");
                alert.setContentText(err.getMessage());
                alert.showAndWait();
            }

            updateFeeds(api.getFeeds());
        });

        feedList.getChildren().add(new Label("Feeds"));

        root.getChildren().add(urlInputArea);
        root.getChildren().add(feedList);
        for(ItemListBox itemListBox : itemListBoxes)
            root.getChildren().add(itemListBox);

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

    class BrowserControl
    {
        void openLink(String url)
        {
            getHostServices().showDocument(url);
        }
    }
}

// Created: 2016-04-18
