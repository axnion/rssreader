package app.menu;

import app.RSSReader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import system.rss.Feed;

/**
 * Class MenuFeed
 *
 * @author Axel Nilsson (axnion)
 */
class MenuFeed extends VBox{
    private Feed feed;
    private String feedListName;

    MenuFeed(Feed feed, String feedListName) {
        this.feed = feed;
        this.feedListName = feedListName;

        createContextMenu();

        Image image;

        try {
            image = new Image(feed.getImage());
        }
        catch (IllegalArgumentException expt) {
            image = new Image("file:img/default_feed.png");
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);

        getStyleClass().add("MenuFeed");

        String titleStr = feed.getTitle();
        if(titleStr.length() >= 50) {
            titleStr = titleStr.substring(0, 50) + "...";
        }
        Text title = new Text(titleStr);
        title.getStyleClass().add("MenuFeedTitle");

        HBox container = new HBox();
        container.getChildren().add(imageView);
        container.getChildren().add(title);

        getChildren().add(container);
    }

    private void createContextMenu() {
        ContextMenu rightClickMenu = new ContextMenu();

        MenuItem showDetails = new MenuItem("Show details");
        MenuItem deleteFeed = new MenuItem("Delete feed");

//        Menu move = new Menu("Move");
//        MenuItem moveDown = new MenuItem("Move Down");
//        MenuItem moveUp = new MenuItem("Move Up");
//
//        moveDown.setOnAction(event -> {
//
//        });

        deleteFeed.setOnAction(event -> {
            RSSReader.removeFeed(feed.getUrlToXML(), feedListName);
        });

        rightClickMenu.getItems().add(showDetails);
        rightClickMenu.getItems().add(deleteFeed);

        setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.SECONDARY)) {
                if(RSSReader.openContextMenu != null)
                    RSSReader.openContextMenu.hide();

                rightClickMenu.show(RSSReader.wrapper, event.getScreenX(), event.getScreenY());
                RSSReader.openContextMenu = rightClickMenu;
            }
        });
    }

    public String getFeedUrlToXML() {
        return feed.getUrlToXML();
    }
}