package client;

import api.Feed;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * Class FeedBox
 *
 * @author Axel Nilsson (axnion)
 * @version 1.0
 */
class FeedBox extends HBox
{
    FeedBox(Feed feed)
    {
        Text text = new Text();
        text.setText(feed.getTitle());
        this.getChildren().add(text);
    }
}

// Created: 2016-04-22
