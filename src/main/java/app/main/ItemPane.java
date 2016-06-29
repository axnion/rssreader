package app.main;

import app.App;
import app.misc.ClickButton;
import app.misc.ToggleButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import rss.Item;

/**
 * Class ItemPane
 *
 * @author Axel Nilsson (axnion)
 */
class ItemPane extends VBox {
    private boolean detailsVisible;
    private Item item;
    private VBox detailsContainer;

    ItemPane(Item item) {
        this.item = item;
        detailsVisible = false;
        detailsContainer = new VBox();

        setMinWidth(470);

        getStyleClass().add("ItemPane");
        getChildren().add(createItemBar());
        getChildren().add(detailsContainer);
    }

    private HBox createItemBar() {
        HBox itemBar = new HBox();

        Text itemTitle = new Text(item.getTitle());
        itemTitle.setFill(Color.WHITE);
        itemTitle.setWrappingWidth(440);

        VBox titleContainer = new VBox(itemTitle);
        titleContainer.setOnMouseClicked(event -> clickItem());

        ClickButton detailsButton = new ClickButton(MaterialIcon.MORE_VERT, "MenuButton", "30px",
                "Show details");
        detailsButton.setOnMouseClicked(event -> showHideDetails());

        Node titleNode = titleContainer;
        itemBar.setHgrow(titleNode, Priority.ALWAYS);
        itemBar.getChildren().addAll(titleNode, detailsButton);

        return itemBar;
    }

    private void showHideDetails() {
        if(detailsVisible) {
            detailsContainer.getChildren().clear();
        }
        else {
            TextFlow linkFlow = new TextFlow();
            Text linkLabel = new Text("Link: ");
            linkLabel.getStyleClass().add("DetailsLable");
            Text linkText = new Text(item.getLink());
            linkText.getStyleClass().add("DetailsText");
            linkFlow.getChildren().addAll(linkLabel, linkText);
            linkFlow.setMinWidth(440);

            TextFlow descriptionFlow = new TextFlow();
            Text descriptionLabel = new Text("Description: ");
            descriptionLabel.getStyleClass().add("DetailsLable");
            Text descriptionText = new Text(item.getDescription());
            descriptionText.getStyleClass().add("DetailsText");
            descriptionFlow.getChildren().addAll(descriptionLabel, descriptionText);
            descriptionFlow.setMinWidth(440);

            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<!DOCTYPE html>");
            htmlBuilder.append("<html>");
            htmlBuilder.append("<head>");
            htmlBuilder.append("<meta charset=utf-8>");
            htmlBuilder.append("</head>");
            htmlBuilder.append("<body>");
            htmlBuilder.append(item.getDescription());
            htmlBuilder.append("</body>");
            htmlBuilder.append("</html>");

            try {
                WebView browser = new WebView();
                WebEngine webEngine = browser.getEngine();
                webEngine.loadContent(htmlBuilder.toString());

                VBox box = new VBox(browser);
                box.setMinHeight(500);
                box.setMaxHeight(1000000000);

//                Scene TestScene = new Scene(box);
//                Stage test = new Stage();
//                test.setScene(TestScene);
//                test.show();

                detailsContainer.getChildren().add(box);
            }
            catch (Exception err) {
                err.printStackTrace();
            }
        }
        detailsVisible = !detailsVisible;
    }

    private void clickItem() {
        App.openLink(item.getLink());
    }
}
