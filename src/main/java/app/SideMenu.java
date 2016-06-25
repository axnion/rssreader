package app;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Class SideMenu
 *
 * @author Axel Nilsson (axnion)
 */
class SideMenu extends VBox{
    private boolean visible;

    SideMenu() {
        visible = false;
        setMinWidth(50);
        setMaxWidth(50);
        setStyle("-fx-background-color: greenyellow");

        Button btn = new Button();
        btn.setOnAction(event -> menuVisibility());

        getChildren().add(btn);
    }

    void menuVisibility() {
        if(visible)
            hideMenu();
        else
            showMenu();
    }

    void showMenu() {
        visible = true;
        Timeline feedUpdater = new Timeline(new KeyFrame(Duration.millis(1), event -> inceaseWidth()));

        feedUpdater.setCycleCount(100);
        feedUpdater.play();
    }

    void hideMenu() {
        visible = false;
        Timeline feedUpdater = new Timeline(new KeyFrame(Duration.millis(1), event -> decreaseWidth()));

        feedUpdater.setCycleCount(100);
        feedUpdater.play();
    }

    void inceaseWidth() {
        setMinWidth(getMinWidth() + 2);
        setMaxWidth(getMaxWidth() + 2);
    }

    void decreaseWidth() {
        setMinWidth(getMinWidth() - 2);
        setMaxWidth(getMaxWidth() - 2);
    }
}
