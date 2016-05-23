package client;

import api.Configuration;
import api.Feed;

import api.ItemList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

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

    private ArrayList<ItemListBox> itemListBoxes;
    private HBox itemListContainer;
    private String currentLoadedFile;

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

        //primaryStage.getIcons().add(new Image("file:img/rss_icon.png"));

        root = new VBox();
        //root.setStyle("-fx-background-color: grey");
        itemListContainer = new HBox();
        itemListBoxes = new ArrayList<>();
        currentLoadedFile = null;

        MenuBar menuBar = createMainMenu();

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

    private MenuBar createMainMenu()
    {
        // Menubar
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        Menu add = new Menu("Add");
        Menu settings = new Menu("Settings");

        //File Menu
        MenuItem newConfig = new MenuItem("New");
        newConfig.setOnAction(event -> fileChooserNew());
        MenuItem loadConfig = new MenuItem("Load");
        loadConfig.setOnAction(event -> fileChooserLoad());
        MenuItem saveConfig = new MenuItem("Save");
        saveConfig.setOnAction(event -> fileChooserSave());
        MenuItem saveAsConfig = new MenuItem("Save As");
        saveAsConfig.setOnAction(event -> fileChooserSaveAs());
        file.getItems().addAll(newConfig, loadConfig, saveConfig, saveAsConfig);

        // Add Menu
        MenuItem addFeed = new MenuItem("Add Feed");
        addFeed.setOnAction(event -> openAddFeedWindow());
        MenuItem addList = new MenuItem("Add List");
        addList.setOnAction(event -> openAddListWindow());
        add.getItems().addAll(addFeed, addList);

        // Settings Menu


        menuBar.getMenus().addAll(file, add, settings);
        return menuBar;
    }

    private void openAddFeedWindow()
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
            addFeed(textInput.getText(), true);
            stage.close();
        });

        submit.setOnAction(event ->
        {
            addFeed(textInput.getText(), true);
            stage.close();
        });
    }

    private void openAddListWindow()
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
            addItemList(textInput.getText(), true);
            stage.close();
        });

        submit.setOnAction(event ->
        {
            addItemList(textInput.getText(), true);
            stage.close();
        });
    }

    private void fileChooserNew()
    {
        api = new Configuration();
        itemListContainer = new HBox();
        itemListBoxes = new ArrayList<>();
        currentLoadedFile = null;

        itemListContainer.getChildren().clear();
        itemListBoxes.clear();
        currentLoadedFile = null;
    }

    private void fileChooserLoad()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Configuration");
        File selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile != null)
        {
            loadConfiguration(selectedFile.getPath());
            currentLoadedFile = selectedFile.getPath();
        }
    }

    private void fileChooserSave()
    {
        saveConfiguration(currentLoadedFile);
    }

    private void fileChooserSaveAs()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create new configuration");
        fileChooser.setInitialDirectory(new File(currentLoadedFile));
        File newFile = fileChooser.showSaveDialog(null);

        if(newFile != null)
        {
            saveConfiguration(newFile.getPath());
            loadConfiguration(newFile.getPath());
            currentLoadedFile = newFile.getPath();
        }
    }

    private void startFeedUpdater(int interval)
    {
        Timeline feedUpdater = new Timeline(new KeyFrame(Duration.minutes(interval),
                event -> checkForUpdates()));

        feedUpdater.setCycleCount(Animation.INDEFINITE);
        feedUpdater.play();
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
    }

    private void removeFeed(String url)
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

    private void checkForUpdates()
    {
        api.update();

        for(ItemListBox itemListBox : itemListBoxes)
            itemListBox.updateMenu(api.getFeeds());
    }

    private void loadConfiguration(String path)
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
