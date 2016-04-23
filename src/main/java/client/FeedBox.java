package client;

import api.Feed;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * Class FeedBox
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
public class FeedBox extends HBox
{
    public FeedBox(Feed feed)
    {
        Text text = new Text();
        text.setText(feed.getTitle());
        this.getChildren().add(text);
    }

    public FeedBox()
    {
        Text text = new Text();
        text.setText("FEEDBOX");
        this.getChildren().add(text);
    }
}

// Created: 2016-04-22
