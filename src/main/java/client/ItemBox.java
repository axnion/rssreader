package client;

import api.Item;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Class ItemBox
 *
 * This is the class used in the graphical client to represent the Items in the Lists.
 *
 * @author Axel Nilsson (axnion)
 * @version 1.0
 */
class ItemBox extends BorderPane
{
    private Item item;

    /**
     * Constructor
     * @param item The Item we want this ItemBox to be associated with.
     */
    ItemBox(Item item)
    {
        this.item = item;
        Hyperlink link = new Hyperlink();

        if(!item.isVisited())
            this.setStyle("-fx-background-color: #ff9a10");
        else
            this.setStyle("-fx-background-color: #fff");

        setPadding(new Insets(5));

        link.setOnAction(event ->
        {
            changeVisited();
            Client.bc.openLink(item.getLink());
        });

        VBox starContainer = new VBox();
        ImageView imageView;

        if(item.isStarred())
        {
            imageView = new ImageView(new Image("file:img/star_border.png"));
            imageView.setUserData("border");
        }
        else
        {
            imageView = new ImageView(new Image("file:img/star_solid.png"));
            imageView.setUserData("solid");
        }

        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        starContainer.getChildren().add(imageView);
        starContainer.setOnMouseClicked((event -> changeStar(imageView)));
        starContainer.setPadding(new Insets(0, 20, 0, 0));
        starContainer.setOpacity(1);
        starContainer.setOnMouseEntered(event -> starContainer.setOpacity(0.6));
        starContainer.setOnMouseExited(event -> starContainer.setOpacity(1));

        setMinWidth(400);
        link.setText(item.getTitle());

        Client.primaryScene.widthProperty().addListener(event ->
        {
            setPrefWidth((Client.mainStage.getWidth() - 20) / Client.listBoxes.size() - 40);
        });

        setLeft(link);
        setRight(starContainer);
    }

    /**
     * Changes the visited status of this ItemBox. It will set the item to visited.
     */
    private void changeVisited()
    {
        this.setStyle("-fx-background-color: #fff");
        Client.api.setVisited(true, item.getId());
    }

    /**
     * Changes the starred status of this ItemBox. If it was starred it will unstar and the other
     * way around.
     * @param imageView The ImageView which hold the star image.
     */
    private void changeStar(ImageView imageView)
    {
        if(imageView.getUserData().toString().equals("solid"))
            imageView.setUserData("border");
        else if(imageView.getUserData().toString().equals("border"))
            imageView.setUserData("solid");

        imageView.setImage(new Image("file:img/star_" + imageView.getUserData().toString() + ".png"));

        Client.api.setStarred(imageView.getUserData().toString().equals("border"), item.getId());
    }
}

// Created: 2016-04-22
