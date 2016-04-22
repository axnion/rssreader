package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    public void launchJavaFX()
    {
        launch();
    }

    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("RSSReader");

        VBox root = new VBox();
        HBox urlInputArea = new HBox();
        VBox feedList = new VBox();
        VBox itemList = new VBox();

        // urlInputArea
        TextField urlInput = new TextField();
        urlInput.setMinWidth(300);
        Button btn = new Button();
        btn.setText("Add");
        urlInputArea.getChildren().addAll(urlInput, btn);
        btn.setOnAction((event) ->
        {

        });

        root.getChildren().addAll(urlInputArea, feedList, itemList);

        primaryStage.setScene(new Scene(root, 500, 1000));
        primaryStage.show();
    }
}

// Created: 2016-04-18
