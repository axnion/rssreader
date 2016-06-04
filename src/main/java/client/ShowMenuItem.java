package client;

import api.Feed;
import api.ItemList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * Class ShowMenuItem
 *
 * @author Axel Nilsson (axnion)
 * @version 1.0
 */
class ShowMenuItem extends BorderPane
{
    private String type;
    private String identifier;
    private Text text;
    private HBox buttonContainer;

    /**
     * Constructor
     * @param itemList The ItemList associated with this menu item.
     */
    ShowMenuItem(ItemList itemList)
    {
        type = "ItemList";
        identifier = itemList.getName();
        text = new Text();
        buttonContainer = new HBox();

        addText(itemList.getName());

        ImageView remove = new ImageView(new Image("file:img/remove.png"));
        remove.setFitHeight(20);
        remove.setFitWidth(20);
        buttonContainer.getChildren().add(remove);
        buttonContainer.setOnMouseClicked(event -> deleteItem());
        buttonContainer.setOnMouseEntered(event -> buttonContainer.setStyle("-fx-background-color: grey;"));
        buttonContainer.setOnMouseExited(event -> buttonContainer.setStyle(""));

        setMinWidth(250);

        setLeft(text);
        setRight(buttonContainer);
    }

    /**
     * Constructor
     * @param feed The Feed associated with this menu item.
     */
    ShowMenuItem(Feed feed)
    {
        type = "Feed";
        identifier = feed.getUrlToXML();
        text = new Text();
        buttonContainer = new HBox();

        addText(feed.getTitle());

        ImageView remove = new ImageView(new Image("file:img/remove.png"));
        remove.setFitHeight(20);
        remove.setFitWidth(20);
        buttonContainer.getChildren().add(remove);
        buttonContainer.setOnMouseClicked(event -> deleteItem());
        buttonContainer.setOnMouseEntered(event -> buttonContainer.setStyle("-fx-background-color: grey;"));
        buttonContainer.setOnMouseExited(event -> buttonContainer.setStyle(""));

        setMinWidth(250);

        setLeft(text);
        setRight(buttonContainer);
    }

    /**
     * Adds text to the menu item.
     * @param title The title that will be shown in the menu.
     */
    private void addText(String title)
    {
        if(title.length() > 25)
            title = title.substring(0, 25) + "...";

        text.setText(title);
    }

    /**
     * Deletes the Feed or ItemList associated with this menu item.
     */
    private void deleteItem()
    {
        if(type.equals("Feed"))
            Client.removeFeed(identifier);
        else
            Client.removeItemList(identifier);
    }
}

// Created: 2016-05-28
