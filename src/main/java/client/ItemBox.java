package client;

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
    ItemBox(Item item, Client.BrowserControl bc)
    {
        Hyperlink link = new Hyperlink();


        link.setOnAction(event ->
        {
            bc.openLink(item.getLink());
        });

        VBox starContainer = new VBox();
        ImageView imageView;

        if(item.isStarred())
        {
            imageView = new ImageView(new Image("img/star_border.png"));
            imageView.setUserData("border");
        }
        else
        {
            imageView = new ImageView(new Image("img/star_solid.png"));
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

        imageView.setImage(new Image("img/star_" + imageView.getUserData().toString() + ".png"));
    }
}

// Created: 2016-04-22
