package client;

import api.Item;
import javafx.application.Application;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;

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
        link.setText(item.getTitle());

        link.setOnAction(event ->
        {
            bc.openLink(item.getLink());
        });

        this.getChildren().add(link);
    }
}

// Created: 2016-04-22
