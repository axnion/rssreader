package app.menu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Class CustomButton
 *
 * @author Axel Nilsson (axnion)
 */
class CustomButton extends VBox {
    private ImageView imageView;

    CustomButton(String image, String styleClass) {
        imageView = new ImageView(new Image(image));
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        setPrefSize(30, 30);

        getChildren().add(imageView);
    }

    ImageView getImage() {
        return imageView;
    }

    void setImage(String path) {
        imageView.setImage(new Image(path));
    }
}
