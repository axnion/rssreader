package app.main;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Class FeedListPane
 *
 * @author Axel Nilsson (axnion)
 */
public class FeedListPane extends VBox{
    private String name;
    private ScrollPane scroll;
    private VBox container;

    public FeedListPane(String name) {
        this.name = name;
        container = new VBox();

        setMinWidth(500);
        getStyleClass().add("FeedListPane");

        Text head = new Text(name);
        head.setTextAlignment(TextAlignment.CENTER);
        head.setFill(Color.WHITE);

        container.setStyle("-fx-background-color: #282C2C");

        scroll = new ScrollPane(container);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        getChildren().add(head);
        getChildren().add(scroll);
    }

    public void showAsEmpty() {
        Text emptyText = new Text("List is empty");
        emptyText.setFill(Color.GREY);
        container.getChildren().add(emptyText);
    }

    public void addItemPane(ItemPane itemPane) {
        container.getChildren().add(itemPane);
    }

    public void clear() {
        container.getChildren().clear();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
