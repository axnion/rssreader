package client;

import api.Configuration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

/**
 * Class MainMenu
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
class MainMenu extends MenuBar
{
    MainMenu()
    {
        Menu fileMenu = new Menu("File");
        Menu feedMenu = new Menu("Feed");
        Menu listMenu = new Menu("List");
        Menu settingsMenu = new Menu("Settings");

        //File Menu
        MenuItem newConfig = new MenuItem("New");
        newConfig.setOnAction(event -> fileChooserNew());
        MenuItem loadConfig = new MenuItem("Load");
        loadConfig.setOnAction(event -> fileChooserLoad());
        MenuItem saveConfig = new MenuItem("Save");
        saveConfig.setOnAction(event -> fileChooserSave());
        MenuItem saveAsConfig = new MenuItem("Save As");
        saveAsConfig.setOnAction(event -> fileChooserSaveAs());
        fileMenu.getItems().addAll(newConfig, loadConfig, saveConfig, saveAsConfig);

        // Feed Menu
        MenuItem addFeed = new MenuItem("Add Feed");
        addFeed.setOnAction(event -> openAddFeedWindow());
        MenuItem showFeeds = new MenuItem("Show Feeds");
        //showFeeds.setOnAction(event -> openShowFeedsWindow());
        feedMenu.getItems().addAll(addFeed, showFeeds);

        // List Menu
        MenuItem addList = new MenuItem("Add List");
        addList.setOnAction(event -> openAddListWindow());
        MenuItem showList = new MenuItem("Show List");
        listMenu.getItems().addAll(addList, showList);

        // Settings Menu

        getMenus().addAll(fileMenu, feedMenu, listMenu, settingsMenu);
    }

    private static void openAddFeedWindow()
    {
        VBox sceneRoot = new VBox();
        sceneRoot.setPadding(new Insets(10));

        Text text = new Text();
        text.setText("Enter URL to feed XML file");

        TextField textInput = new TextField();

        Button submit = new Button();
        submit.setText("Add");

        sceneRoot.getChildren().addAll(text, textInput, submit);
        sceneRoot.setAlignment(Pos.CENTER);

        Stage stage = new Stage();
        stage.setTitle("Add Feed");
        stage.setResizable(false);
        stage.setScene(new Scene(sceneRoot,400 , 100));
        stage.show();

        textInput.setOnAction(event ->
        {
            Client.addFeed(textInput.getText(), true);
            stage.close();
        });

        submit.setOnAction(event ->
        {
            Client.addFeed(textInput.getText(), true);
            stage.close();
        });
    }

    private static void openAddListWindow()
    {
        VBox sceneRoot = new VBox();
        sceneRoot.setPadding(new Insets(10));

        Text text = new Text();
        text.setText("Enter the name of the new list");

        TextField textInput = new TextField();

        Button submit = new Button();
        submit.setText("Add");

        sceneRoot.getChildren().addAll(text, textInput, submit);
        sceneRoot.setAlignment(Pos.CENTER);

        Stage stage = new Stage();
        stage.setTitle("Add List");
        stage.setResizable(false);
        stage.setScene(new Scene(sceneRoot,400 , 100));
        stage.show();

        textInput.setOnAction(event ->
        {
            Client.addItemList(textInput.getText(), true);
            stage.close();
        });

        submit.setOnAction(event ->
        {
            Client.addItemList(textInput.getText(), true);
            stage.close();
        });
    }

//    private static void openShowFeedsWindow()
//    {
//        VBox sceneRoot = new VBox();
//        sceneRoot.setPadding(new Insets(10));
//
//        TextField textInput = new TextField();
//
//        Button submit = new Button();
//        submit.setText("Add");
//
//        sceneRoot.getChildren().addAll(text, textInput, submit);
//        sceneRoot.setAlignment(Pos.CENTER);
//
//        Stage stage = new Stage();
//        stage.setTitle("Add List");
//        stage.setResizable(false);
//        stage.setScene(new Scene(sceneRoot,400 , 100));
//        stage.show();
//    }

    private static void fileChooserNew()
    {
        Client.api = new Configuration();
        Client.itemListContainer = new HBox();
        Client.itemListBoxes = new ArrayList<>();
        Client.currentLoadedFile = null;

        Client.itemListContainer.getChildren().clear();
        Client.itemListBoxes.clear();
        Client.currentLoadedFile = null;
    }

    private static void fileChooserLoad()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Configuration");
        File selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile != null)
        {
            Client.loadConfiguration(selectedFile.getPath());
            Client.currentLoadedFile = selectedFile.getPath();
        }
    }

    private static void fileChooserSave()
    {
        Client.saveConfiguration(Client.currentLoadedFile);
    }

    private static void fileChooserSaveAs()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create new configuration");
        fileChooser.setInitialDirectory(new File(Client.currentLoadedFile));
        File newFile = fileChooser.showSaveDialog(null);

        if(newFile != null)
        {
            Client.saveConfiguration(newFile.getPath());
            Client.loadConfiguration(newFile.getPath());
            Client.currentLoadedFile = newFile.getPath();
        }
    }
}

// Created: 2016-05-27
