package app.main;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Class ItemPane
 *
 * @author Axel Nilsson (axnion)
 */
public class ItemPane extends HBox{
    public ItemPane(String title) {
        Text itemTitle = new Text(title);
        itemTitle.setFill(Color.WHITE);
        getChildren().add(itemTitle);
    }
}
