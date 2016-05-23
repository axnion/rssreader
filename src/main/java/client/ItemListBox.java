package client;

import api.Configuration;
import api.Feed;
import api.ItemList;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
    private MenuFeed[] checkboxes;
    private VBox checkBoxContainer;
    private Client.BrowserControl browserControl;
    private Configuration api;

    ItemListBox(String name, Configuration api, Client.BrowserControl bc, Button removeButton)
    {
        this.name = name;
        this.api = api;
        itemList = new VBox();
        VBox container = new VBox();
        browserControl = bc;
        checkboxes = null;
        checkBoxContainer = new VBox();

        container.getChildren().addAll(removeButton, new Label(name), createSortSettings(), checkBoxContainer, itemList);
        setContent(container);

        updateMenu(api.getFeeds());
    }

    private HBox createSortSettings()
    {
        HBox sortContainer = new HBox();
        VBox sortTargetContainer = new VBox();
        VBox sortDirectionContainer = new VBox();

        ToggleGroup target = new ToggleGroup();
        ToggleGroup direction = new ToggleGroup();

        RadioButton radioTitle = new RadioButton("Title");
        RadioButton radioDate = new RadioButton("Date");
        radioTitle.setUserData("TITLE");
        radioDate.setUserData("DATE");
        radioTitle.setToggleGroup(target);
        radioDate.setToggleGroup(target);
        sortTargetContainer.getChildren().addAll(radioTitle, radioDate);

        RadioButton radioAsc = new RadioButton("Ascending");
        RadioButton radioDec = new RadioButton("Descending");
        radioAsc.setUserData("ASC");
        radioDec.setUserData("DEC");
        radioAsc.setToggleGroup(direction);
        radioDec.setToggleGroup(direction);


        String sorting = api.getItemList(name).getSorting();

        if(sorting.equals("TITLE_ASC"))
        {
            radioTitle.setSelected(true);
            radioAsc.setSelected(true);
        }
        else if(sorting.equals("TITLE_DEC"))
        {
            radioTitle.setSelected(true);
            radioDec.setSelected(true);
        }
        else if(sorting.equals("DATE_ASC"))
        {
            radioDate.setSelected(true);
            radioAsc.setSelected(true);
        }
        else
        {
            radioDate.setSelected(true);
            radioDec.setSelected(true);
        }

        sortDirectionContainer.getChildren().addAll(radioAsc, radioDec);
        sortContainer.getChildren().addAll(sortTargetContainer, sortDirectionContainer);
        target.selectedToggleProperty().addListener((event) -> updateSorting(target, direction));
        direction.selectedToggleProperty().addListener((event) -> updateSorting(target, direction));

        return sortContainer;
    }

    private void updateSorting(ToggleGroup target, ToggleGroup direction)
    {
        String str = target.getSelectedToggle().getUserData().toString();
        str += "_";
        str += direction.getSelectedToggle().getUserData().toString();

        api.setSorting(str, getName());
        updateItems(api.getItemList(getName()));
    }

    void updateMenu(Feed[] feeds)
    {
        if(checkboxes != null)
            checkBoxContainer.getChildren().removeAll(checkboxes);

        if(feeds == null)
            return;

        checkboxes = new MenuFeed[feeds.length];

        for(int i = 0; i < feeds.length; i++)
        {
            checkboxes[i] = new MenuFeed();
            checkboxes[i].setText(feeds[i].getTitle());
            checkboxes[i].setValue(feeds[i].getUrlToXML());

            String[] urls = api.getItemList(name).getFeedUrls();

            if(urls != null)
            {
                for(int j = 0; j < urls.length; j++)
                {
                    if(feeds[i].getUrlToXML().equals(urls[j]))
                    {
                        checkboxes[i].setSelected(true);
                        checkboxes[i].setLastState(true);
                        updateItems(api.getItemList(name));
                        break;
                    }
                }
            }
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

        checkBoxContainer.getChildren().addAll(checkboxes);
    }

    void updateItems(ItemList items)
    {
        itemList.getChildren().clear();

        if(items.getItems() != null)
        {
            for(int i = 0; i < items.getItems().length; i++)
                itemList.getChildren().add(new ItemBox(items.getItems()[i], name, api, browserControl));
        }
    }

    String getName()
    {
        return name;
    }
}

// Created: 2016-05-06
