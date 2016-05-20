package client;

import api.Configuration;
import api.Item;
import javafx.application.Application;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Class ItemBox
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
class ItemBox extends HBox
{
    private Configuration api;
    private Item item;
    private String itemListName;

    ItemBox(Item item, String itemListName, Configuration api, Client.BrowserControl bc)
    {
        this.api = api;
        this.item = item;
        this.itemListName = itemListName;
        Hyperlink link = new Hyperlink();

        link.setOnAction(event ->
        {
            bc.openLink(item.getLink());
        });

        VBox starContainer = new VBox();
        ImageView imageView;

        if(item.isStarred())
        {
            imageView = new ImageView(new Image("file:img/star_border.png"));
            imageView.setUserData("border");
        }
        else
        {
            imageView = new ImageView(new Image("file:img/star_solid.png"));
            imageView.setUserData("solid");
        }

        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        starContainer.setOnMouseClicked((event -> changeStar(imageView)));
        starContainer.getChildren().add(imageView);

        link.setText(item.getTitle());
        this.getChildren().add(link);
        this.getChildren().add(starContainer);
    }

    private void changeStar(ImageView imageView)
    {
        if(imageView.getUserData().toString().equals("solid"))
            imageView.setUserData("border");
        else if(imageView.getUserData().toString().equals("border"))
            imageView.setUserData("solid");

        imageView.setImage(new Image("file:img/star_" + imageView.getUserData().toString() + ".png"));

        api.setStarred(imageView.getUserData().toString().equals("border"), itemListName,
                item.getId());
    }
}

// Created: 2016-04-22
