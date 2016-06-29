package app.misc;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Class ClickButton
 *
 * @author Axel Nilsson (axnion)
 */
public class ClickButton extends VBox {
    private Text icon;

    public ClickButton(MaterialIcon materialIcon, String styleClass, String size,
                        String tooltipText) {
        icon = MaterialIconFactory.get().createIcon(materialIcon, size);
        icon.getStyleClass().add(styleClass);

        Tooltip.install(this, new Tooltip(tooltipText));

        getChildren().add(icon);
    }
}
