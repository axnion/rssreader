package app.main;

import app.App;
import app.misc.ClickButton;
import app.misc.ToggleColorButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.scene.Group;
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
    private Group detailsGroup;
    private VBox detailsContainer;

    ItemPane(Item item, String feedListName) {
        detailsVisible = false;
        detailsGroup = new Group();
        detailsContainer = new VBox();

        setMinWidth(470);

        getStyleClass().add("ItemPane");

        if(!item.isVisited())
            getStyleClass().add("NotVisited");

        createDetailsGroup(item);
        getChildren().add(createItemBar(item, feedListName));
        getChildren().add(detailsContainer);
    }

    private HBox createItemBar(Item item, String feedListName) {
        HBox itemBar = new HBox();

        Text itemTitle = new Text(item.getTitle());
        itemTitle.setFill(Color.WHITE);
        itemTitle.setWrappingWidth(440);

        VBox titleContainer = new VBox(itemTitle);
        titleContainer.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                getStyleClass().clear();
                getStyleClass().add("ItemPane");
                App.openLink(item.getLink());
                Configuration.setVisited(item.getId(), item.getFeedIdentifier(), feedListName,
                        true);
            }
        });

        ToggleColorButton starredButton = new ToggleColorButton(MaterialIcon.STAR,
                "ToggleColorButtonOn", "ToggleColorButtonOff", item.isStarred(),
                "20px", "Star this item");

        starredButton.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                starredButton.toggle();
                Configuration.setStarred(item.getId(), item.getFeedIdentifier(), feedListName,
                        starredButton.getCurrentStatus());
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

    private void createDetailsGroup(Item item) {
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

        detailsGroup.getChildren().addAll(linkFlow, descriptionFlow);
    }

    private void showHideDetails() {
        if(detailsVisible) {
            detailsContainer.getChildren().clear();
        }
        else {
            detailsContainer.getChildren().add(detailsGroup);
            detailsContainer.minHeight(100);
        }
        detailsVisible = !detailsVisible;
    }
}
