package client;

import api.Configuration;
import api.Feed;
import api.ItemList;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * Class ItemListBox
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
class ItemListBox extends ScrollPane
{
    private String name;
    private VBox itemList;
    private VBox container;
    private MenuFeed[] checkboxes;
    private Client.BrowserControl browserControl;
    private Configuration api;

    ItemListBox(String name, Configuration api, Client.BrowserControl bc)
    {
        this.name = name;
        this.api = api;
        itemList = new VBox();
        container = new VBox();
        browserControl = bc;
        checkboxes = null;

        container.getChildren().add(itemList);
        setContent(container);
    }
    void updateMenu(Feed[] feeds)
    {
        if(feeds == null)
            return;

        if(checkboxes != null)
            container.getChildren().removeAll(checkboxes);

        checkboxes = new MenuFeed[feeds.length];

        for(int i = 0; i < feeds.length; i++)
        {
            checkboxes[i] = new MenuFeed();
            checkboxes[i].setText(feeds[i].getTitle());
            checkboxes[i].setValue(feeds[i].getUrlToXML());
            //checkboxes[i].setAllowIndeterminate(false);
        }

        for(MenuFeed checkbox : checkboxes)
        {
            checkbox.setOnAction((event) ->
            {
                for(int i = 0; i < checkboxes.length; i++)
                {
                    if(checkboxes[i].isSelected() && !checkboxes[i].wasSelected())
                    {
                        api.addFeedToItemList(name, checkbox.getValue());
                        api.update();
                        checkboxes[i].setLastState(true);
                    }
                    else if(!checkboxes[i].isSelected() && checkboxes[i].wasSelected())
                    {
                        api.removeFeedFromItemList(name, checkbox.getValue());
                        api.update();
                        checkboxes[i].setLastState(false);
                    }
                }

                updateItems(api.getItemList(name));
            });
        }

        container.getChildren().addAll(checkboxes);
    }

    void updateItems(ItemList items)
    {
        itemList.getChildren().clear();
        itemList.getChildren().add(new Label("ItemList"));

        if(items.getItems() != null)
        {
            for(int i = 0; i < items.getItems().length; i++)
                itemList.getChildren().add(new ItemBox(items.getItems()[i], browserControl));
        }
    }
}

// Created: 2016-05-06
