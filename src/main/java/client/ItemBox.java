package client;

import api.Item;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * Class ItemBox
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
public class ItemBox extends HBox
{
    public ItemBox(Item item)
    {
        Text text = new Text();
        text.setText(item.getTitle());
        this.getChildren().add(text);
    }
}

// Created: 2016-04-22
