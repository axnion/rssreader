package app.main;

import app.App;
import app.misc.ClickButton;
import app.misc.ToggleColorButton;
import app.misc.ToggleIconButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import htmlParser.HtmlParser;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import rss.Item;
import system.Configuration;

/**
 * Class ItemPane
 *
 * @author Axel Nilsson (axnion)
 */
class ItemPane extends VBox {
    private boolean detailsVisible;
    private Item item;
    private VBox detailsContainer;
    private String feedListName;

    ItemPane(Item item, String feedListName) {
        this.item = item;
        this.feedListName = feedListName;
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
        titleContainer.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY))
                App.openLink(item.getLink());
        });

        boolean startStarredStatus = false;
        try {
            startStarredStatus = Configuration.isStarred(feedListName, item.getId());
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        ToggleColorButton starredButton = new ToggleColorButton(MaterialIcon.STAR,
                "ToggleColorButtonOn", "ToggleColorButtonOff", startStarredStatus,
                "20px", "Star this item");

        starredButton.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                try {
                    starredButton.toggle();
                    Configuration.setStarred(feedListName, item.getId(),
                            !Configuration.isStarred(feedListName, item.getId()));
                }
                catch(Exception expt) {
                    starredButton.toggle();
                    expt.printStackTrace();
                }
            }
        });

        ClickButton detailsButton = new ClickButton(MaterialIcon.MORE_VERT, "MenuButton", "30px",
                "Show details");
        detailsButton.setOnMouseClicked(event -> showHideDetails());

        Node titleNode = titleContainer;
        itemBar.setHgrow(titleNode, Priority.ALWAYS);
        itemBar.getChildren().addAll(titleNode, starredButton, detailsButton);

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

            //detailsContainer.getChildren().addAll(linkFlow, descriptionFlow);

            HtmlParser htmlParser = new HtmlParser();
            TextFlow textFlow = htmlParser.getAsTextFlow(item.getDescription());
            detailsContainer.getChildren().add(textFlow);
            detailsContainer.minHeight(100);
        }
        detailsVisible = !detailsVisible;
    }
}
