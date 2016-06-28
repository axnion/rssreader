//package app.menu;
//
//import de.jensd.fx.glyphs.materialicons.MaterialIcon;
//import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
//import javafx.scene.Node;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Text;
//
///**
// * Class CustomButton
// *
// * @author Axel Nilsson (axnion)
// */
//class CustomButton extends VBox {
//    CustomButton(MaterialIcon materialIcon, String styleClass, String fontSize) {
//        Text icon = MaterialIconFactory.get().createIcon(materialIcon, fontSize);
//        icon.getStyleClass().add(styleClass);
//        getChildren().add(icon);
//    }
//
//    ImageView getImage() {
//        return imageView;
//    }
//
//    void setIcon(String path) {
//        imageView.setImage(new Image(path));
//    }
//}
