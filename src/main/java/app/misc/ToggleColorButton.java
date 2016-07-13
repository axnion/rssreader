package app.misc;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Class ToggleColorButton
 *
 * @author Axel Nilsson (axnion)
 */
public class ToggleColorButton extends VBox {
    private boolean currentStatus;
    private Text icon;
    private String onStyle;
    private String offStyle;

    public ToggleColorButton(MaterialIcon icon, String on, String off, boolean startStatus,
                             String size, String tooltipText) {
        this.icon = MaterialIconFactory.get().createIcon(icon, size);
        currentStatus = startStatus;
        onStyle = on;
        offStyle = off;

        this.icon.getStyleClass().clear();
        if(currentStatus) {
            this.icon.getStyleClass().add(onStyle);
        }
        else {
            this.icon.getStyleClass().add(offStyle);
        }

        Tooltip.install(this, new Tooltip(tooltipText));
        getChildren().add(this.icon);
    }

    public void toggle() {
        this.icon.getStyleClass().clear();
        if(currentStatus) {
            this.icon.getStyleClass().add(offStyle);
        }
        else {
            this.icon.getStyleClass().add(onStyle);
        }

        currentStatus = !currentStatus;
    }
}
