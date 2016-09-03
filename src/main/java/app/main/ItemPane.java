package app.main;

import app.RSSReader;
import app.misc.ClickButton;
import app.misc.ToggleColorButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import system.rss.Item;
import system.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Class ItemPane
 *
 * @author Axel Nilsson (axnion)
 */
class ItemPane extends VBox {
    private Item item;
    private String feedListName;
    private boolean detailsVisible;
    private ArrayList<TextFlow> details;
    private VBox detailsContainer;
    private ContextMenu contextMenu;
    private boolean showVisitedStatus;

    ItemPane(Item item, String feedListName, boolean showVisitedStatus) {
        this.item = item;
        this.feedListName = feedListName;
        this.showVisitedStatus = showVisitedStatus;
        detailsVisible = false;
        details = new ArrayList<>();
        detailsContainer = new VBox();

        setMinWidth(470);

        setVisited(item.isVisited());

        //createDetailsGroup(item);
        getChildren().add(createItemBar(feedListName));
        getChildren().add(detailsContainer);
    }

    private HBox createItemBar(String feedListName) {
        HBox itemBar = new HBox();

        Text itemTitle = new Text(item.getTitle());
        itemTitle.setFill(Color.WHITE);
        itemTitle.setWrappingWidth(440);

        VBox titleContainer = new VBox(itemTitle);
        titleContainer.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                setVisited(true);
                RSSReader.openLink(item.getLink());
                Configuration.setVisited(feedListName, item.getFeedIdentifier(), item.getId(),
                        true);
            }
        });

        ToggleColorButton starredButton = new ToggleColorButton(MaterialIcon.STAR,
                "ToggleColorButtonOn", "ToggleColorButtonOff", item.isStarred(),
                "20px", "Star this item");

        starredButton.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                starredButton.toggle();
                Configuration.setStarred(feedListName, item.getFeedIdentifier(), item.getId(),
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

    private void createContextMenu(boolean visited) {
        contextMenu = new ContextMenu();

        if(showVisitedStatus) {
            MenuItem changeVisited;
            if(visited)
                changeVisited = new MenuItem("Set to not visited");
            else
                changeVisited = new MenuItem("Set to visited");

            changeVisited.setOnAction(event -> {
                setVisited(!visited);
                Configuration.setVisited(feedListName, item.getFeedIdentifier(), item.getId(),
                        !visited);
            });

            contextMenu.getItems().clear();
            contextMenu.getItems().add(changeVisited);
        }

        setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.SECONDARY)) {
                if(RSSReader.openContextMenu != null)
                    RSSReader.openContextMenu.hide();

                contextMenu.show(RSSReader.wrapper, event.getScreenX(), event.getScreenY());
                RSSReader.openContextMenu = contextMenu;
            }
        });
    }

    private void createDetailsGroup() {
        TextFlow linkFlow = new TextFlow();
        Text linkLabel = new Text("Link: ");
        linkLabel.getStyleClass().add("DetailsLable");
        Hyperlink linkText = new Hyperlink(item.getLink());
        linkFlow.getChildren().addAll(linkLabel, linkText);
        linkFlow.setMinWidth(440);
        details.add(linkFlow);

        TextFlow descriptionFlow = new TextFlow();
        Text descriptionLabel = new Text("Description: ");
        descriptionLabel.getStyleClass().add("DetailsLable");
        descriptionFlow.getChildren().add(descriptionLabel);

        try {
            Document document = Jsoup.parse("<html>" + item.getDescription() + "</html>");
            Element element = document.body();
            List<org.jsoup.nodes.Node> nodeList = element.childNodes();

            for(org.jsoup.nodes.Node node : nodeList) {
                if(node.nodeName().equals("p")) {
                    Text descriptionText = new Text(node.childNode(0).toString());
                    descriptionText.getStyleClass().add("DetailsText");
                    descriptionFlow.getChildren().add(descriptionText);
                }
            }
        }
        catch(RuntimeException expt) {
            Text descriptionText = new Text(item.getDescription());
            descriptionFlow.getChildren().add(descriptionText);
            expt.printStackTrace();
        }

        descriptionFlow.setMinWidth(440);
        details.add(descriptionFlow);

        TextFlow idFlow = new TextFlow();
        Text idLabel = new Text("ID: ");
        idLabel.getStyleClass().add("DetailsLable");
        Text idText = new Text(item.getId());
        idText.getStyleClass().add("DetailsText");
        idFlow.getChildren().addAll(idLabel, idText);
        idFlow.setMinWidth(440);
        details.add(idFlow);

        TextFlow dateFlow = new TextFlow();
        Text dateLabel = new Text("Date: ");
        dateLabel.getStyleClass().add("DetailsLable");
        Text dateText = new Text(item.getDate().toString());
        dateText.getStyleClass().add("DetailsText");
        dateFlow.getChildren().addAll(dateLabel, dateText);
        dateFlow.setMinWidth(440);
        details.add(dateFlow);
    }

    private void showHideDetails() {
        if(detailsVisible) {
            detailsContainer.getChildren().clear();
        }
        else {
            detailsContainer.getChildren().addAll(details);
            detailsContainer.minHeight(100);
        }
        detailsVisible = !detailsVisible;
    }

    private void setVisited(boolean status) {
        getStyleClass().clear();
        getStyleClass().add("ItemPane");

        if(showVisitedStatus) {
            createContextMenu(status);

            if(!status)
                getStyleClass().add("NotVisited");
        }
    }
}
