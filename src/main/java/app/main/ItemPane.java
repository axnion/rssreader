package app.main;

import app.App;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import rss.Item;

/**
 * Class ItemPane
 *
 * @author Axel Nilsson (axnion)
 */
public class ItemPane extends HBox{
    private Item item;

    public ItemPane(Item item) {
        this.item = item;

        Text itemTitle = new Text(item.getTitle());
        itemTitle.setFill(Color.WHITE);

        setOnMouseClicked(event -> clickItem());

        getChildren().add(itemTitle);
        getStyleClass().add("ItemPane");
    }

    private void clickItem() {
        App.openLink(item.getLink());
    }
}
