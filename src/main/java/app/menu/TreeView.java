package app.menu;

import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Class TreeView
 *
 * @author Axel Nilsson (axnion)
 */
class TreeView extends VBox {
    private ArrayList<MenuFeedList> menuFeedLists;

    TreeView() {
        menuFeedLists = new ArrayList<>();
    }

    void addFeedList(String listName) {
        menuFeedLists.add(new MenuFeedList(listName));

        getChildren().clear();
        getChildren().addAll(menuFeedLists);
    }

    void removeFeedList(String listName) {
        for(MenuFeedList menuFeedList : menuFeedLists) {
            if(menuFeedList.getName().equals(listName)) {
                menuFeedLists.remove(menuFeedList);
                break;
            }
        }

        getChildren().clear();
        getChildren().addAll(menuFeedLists);
    }

    void updateFeedList() {
        for(MenuFeedList menuFeedList : menuFeedLists)
            menuFeedList.update();
    }
}
