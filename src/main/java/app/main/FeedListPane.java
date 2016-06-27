package app.main;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Class FeedListPane
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedListPane extends VBox{
    private String name;

    public FeedListPane(String name) {
        this.name = name;

        setMinWidth(300);
        getStyleClass().add("FeedListPane");

        Text head = new Text(name);
        head.setFill(Color.WHITE);

        getChildren().add(head);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
