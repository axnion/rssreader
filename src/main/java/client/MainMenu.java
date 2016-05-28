package client;

import api.Configuration;
import api.Feed;
import api.ItemList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;

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
        Menu feedsMenu = new Menu("Feeds");
        Menu listsMenu = new Menu("Lists");

        //File Menu
        MenuItem newConfig = new MenuItem("New");
        newConfig.setOnAction(event -> Client.resetApplication());
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
        showFeeds.setOnAction(event -> openShowFeedsWindow());
        feedsMenu.getItems().addAll(addFeed, showFeeds);

        // List Menu
        MenuItem addList = new MenuItem("Add List");
        addList.setOnAction(event -> openAddListWindow());
        MenuItem showList = new MenuItem("Show Lists");
        showList.setOnAction(event -> openShowListsWindow());
        listsMenu.getItems().addAll(addList, showList);

        Client.root.setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.ESCAPE)
            {
                Client.settingsTopBox.getChildren().clear();
                Client.settingsSideBox.getChildren().clear();
            }
        });

        getMenus().addAll(fileMenu, feedsMenu, listsMenu);
    }

    private static void openAddFeedWindow()
    {
        VBox container = new VBox();
        container.setMaxWidth(500);
        container.setAlignment(Pos.CENTER);

        Client.settingsTopBox.getChildren().clear();
        Client.settingsSideBox.getChildren().clear();

        Text text = new Text();
        TextField textInput = new TextField();
        Button submit = new Button();
        text.setText("Enter URL to feed XML file");
        submit.setText("Add Feed");

        container.getChildren().addAll(text, textInput, submit);

        Client.settingsTopBox.getChildren().add(container);

        textInput.setOnAction(event ->
        {
            Client.addFeed(textInput.getText(), true);
            Client.settingsTopBox.getChildren().clear();
        });

        submit.setOnAction(event ->
        {
            Client.addFeed(textInput.getText(), true);
            Client.settingsTopBox.getChildren().clear();
        });
    }

    private static void openAddListWindow()
    {
        VBox container = new VBox();
        container.setMaxWidth(500);
        container.setAlignment(Pos.CENTER);

        Client.settingsTopBox.getChildren().clear();
        Client.settingsSideBox.getChildren().clear();

        Text text = new Text();
        TextField textInput = new TextField();
        Button submit = new Button();
        text.setText("Enter the name of the new list");
        submit.setText("Add List");

        container.getChildren().addAll(text, textInput, submit);

        Client.settingsTopBox.getChildren().add(container);

        textInput.setOnAction(event ->
        {
            Client.addItemList(textInput.getText(), true);
            Client.settingsTopBox.getChildren().clear();
        });

        submit.setOnAction(event ->
        {
            Client.addItemList(textInput.getText(), true);
            Client.settingsTopBox.getChildren().clear();
        });
    }

    static void openShowFeedsWindow()
    {
        VBox container = new VBox();
        container.setMaxWidth(500);
        container.setAlignment(Pos.CENTER);
        VBox itemsContainer = new VBox();

        Client.settingsTopBox.getChildren().clear();
        Client.settingsSideBox.getChildren().clear();

        Feed[] feeds = Client.api.getFeeds();

        if(feeds != null)
        {
            for(Feed feed : feeds)
                itemsContainer.getChildren().add(new ShowMenuItem(feed));

            ScrollPane scrollPane = new ScrollPane(itemsContainer);

            container.getChildren().addAll(scrollPane);
            Client.settingsSideBox.getChildren().add(container);
        }
    }

    static void openShowListsWindow()
    {
        VBox container = new VBox();
        container.setMaxWidth(500);
        container.setAlignment(Pos.CENTER);
        VBox itemsContainer = new VBox();

        Client.settingsTopBox.getChildren().clear();
        Client.settingsSideBox.getChildren().clear();

        ItemList[] itemLists = Client.api.getItemLists();

        if(itemLists != null)
        {
            for(ItemList itemList : itemLists)
                itemsContainer.getChildren().add(new ShowMenuItem(itemList));

            ScrollPane scrollPane = new ScrollPane(itemsContainer);

            container.getChildren().addAll(scrollPane);
            Client.settingsSideBox.getChildren().add(container);
        }
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
        if(Client.currentLoadedFile != null)
            Client.saveConfiguration(Client.currentLoadedFile);
        else
            fileChooserSaveAs();
    }

    private static void fileChooserSaveAs()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create new configuration");
        if(Client.currentLoadedFile != null)
            fileChooser.setInitialDirectory(new File(new File(Client.currentLoadedFile).getParent()));
        File newFile = fileChooser.showSaveDialog(null);

        if(newFile != null)
        {
            Client.saveConfiguration(newFile.getPath());
            Client.currentLoadedFile = newFile.getPath();
        }
    }
}

// Created: 2016-05-27
