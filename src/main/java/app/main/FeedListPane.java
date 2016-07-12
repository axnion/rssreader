package app.main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * Class FeedListPane
 *
 * @author Axel Nilsson (axnion)
 */
class FeedListPane extends VBox {
    private String name;
    private VBox container;

    FeedListPane(String name) {
        this.name = name;
        container = new VBox();

        setMinWidth(500);
        getStyleClass().add("FeedListPane");

        Text head = new Text(name);
        head.getStyleClass().add("FeedListPaneTitle");
        HBox titlePane = new HBox(head);
        titlePane.setPadding(new Insets(5));
        titlePane.setAlignment(Pos.CENTER);

        ScrollPane scroll = new ScrollPane(container);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        getChildren().add(titlePane);
        getChildren().add(scroll);
    }

    void addItemPane(ItemPane itemPane) {
        container.getChildren().add(itemPane);
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }
}
