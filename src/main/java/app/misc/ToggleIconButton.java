package app.misc;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Class ToggleIconButton
 *
 * @author Axel Nilsson (axnion)
 */
public class ToggleIconButton extends VBox {
    private boolean currentStatus;
    private Text icon1;
    private Text icon2;

    public ToggleIconButton(MaterialIcon off, MaterialIcon on, String styleClass, String size,
                            String tooltipText) {
        currentStatus = false;

        icon1 = MaterialIconFactory.get().createIcon(off, size);
        icon2 = MaterialIconFactory.get().createIcon(on, size);
        icon1.getStyleClass().add(styleClass);
        icon2.getStyleClass().add(styleClass);

        Tooltip.install(this, new Tooltip(tooltipText));

        getChildren().add(icon1);
    }

    public void toggle() {
        getChildren().clear();

        if(currentStatus)
            getChildren().add(icon1);
        else
            getChildren().add(icon2);

        currentStatus = !currentStatus;
    }
}
