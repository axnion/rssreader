package app;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Class FeedListPane
 *
 * @author Axel Nilsson (axnion)
 */
class FeedListPane extends VBox{
    private String name;

    FeedListPane(String name) {
        this.name = name;

        setPrefWidth(200);
        getStyleClass().add("FeedListPane");

        Text head = new Text(name);
        head.setFill(Color.WHITE);

        getChildren().add(head);
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }
}
