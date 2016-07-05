package app.menu;

import app.App;
import app.misc.ClickButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import rss.Feed;
import rss.FeedSniffer;

import java.util.ArrayList;

/**
 * Class AddFeedMenu
 *
 * @author Axel Nilsson (axnion)
 */
class AddFeedMenu extends VBox {
    private String feedListName;
    private VBox feedContainer;

    AddFeedMenu(String name) {
        feedListName = name;
        HBox searchBar = new HBox();
        feedContainer = new VBox();

        TextField urlInput = new TextField();
        urlInput.setOnAction(event -> {
            displayFeeds(urlInput.getText());
        });

        ClickButton reloadButton = new ClickButton(MaterialIcon.REFRESH, "MenuButton", "30px",
                "Reload");
        reloadButton.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                displayFeeds(urlInput.getText());
            }
        });

        Node urlInputNode = urlInput;
        Node reloadButtonNode = reloadButton;
        searchBar.setHgrow(urlInputNode, Priority.ALWAYS);
        searchBar.setHgrow(reloadButtonNode, Priority.SOMETIMES);

        searchBar.getChildren().addAll(urlInputNode, reloadButtonNode);

        getChildren().addAll(searchBar, feedContainer);
    }

    private void displayFeeds(String url) {
        feedContainer.getChildren().clear();

        FeedSniffer feedSniffer = new FeedSniffer();
        ArrayList<Feed> feeds = feedSniffer.getFeeds(url);

        if(feeds.size() == 0) {
            Text errorMessage = new Text("No feed where found");
            errorMessage.setFill(Color.RED);
            feedContainer.getChildren().add(errorMessage);
        }

        for(Feed feed : feeds) {
            feedContainer.getChildren().add(new NewFeed(feed));
        }
    }

    private class NewFeed extends HBox {
        NewFeed(Feed feed) {
            Text title = new Text(feed.getTitle());
            title.setFill(Color.WHITE);
            ClickButton button = new ClickButton(MaterialIcon.ADD, "MenuButton", "15px",
                    "Add feed to feedlist");
            button.setOnMouseClicked(event -> {
                if(event.getButton().equals(MouseButton.PRIMARY)) {
                    App.addFeed(feed.getUrlToXML(), feedListName);
                }
            });

            getChildren().addAll(title, button);
        }
    }
}
