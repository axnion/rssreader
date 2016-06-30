//package client;
//
//import api.Feed;
//import api.ItemList;
//
//import javafx.geometry.Insets;
//import javafx.scene.control.*;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//
///**
// * Class ListBox
// *
// * This is the class used in the graphical client to represent the Lists.
// *
// * @author Axel Nilsson (axnion)
// * @version 1.0
// */
//class ListBox extends ScrollPane
//{
//    private VBox container;
//    private String name;
//    private BorderPane topBar;
//    private VBox itemList;
//    private VBox settingsButton;
//    private MenuFeed[] checkboxes;
//    private VBox checkBoxContainer;
//    private boolean isSettingsOpen;
//
//    /**
//     * Constructor
//     * @param thisName The name of the ItemList this ListBox is associated with.
//     */
//    ListBox(String thisName)
//    {
//        name = thisName;
//        itemList = new VBox();
//        container = new VBox();
//        topBar = new BorderPane();
//        settingsButton = new VBox();
//        isSettingsOpen = false;
//        checkBoxContainer = new VBox();
//        checkboxes = null;
//
//        ImageView settings = new ImageView(new Image("file:img/settings.png"));
//        settings.setFitHeight(30);
//        settings.setFitWidth(30);
//
//        settingsButton.getChildren().add(settings);
//        settingsButton.setOnMouseClicked(event -> switchMode());
//        settingsButton.setPadding(new Insets(0, 10, 0, 0));
//        settingsButton.setOpacity(1);
//        settingsButton.setOnMouseEntered(event -> settingsButton.setOpacity(0.6));
//        settingsButton.setOnMouseExited(event -> settingsButton.setOpacity(1));
//
//        topBar.setLeft(new Label(name));
//        topBar.setRight(settingsButton);
//        topBar.setPadding(new Insets(10));
//
//        container.getChildren().addAll(topBar, itemList);
//        setContent(container);
//
//        setHbarPolicy(ScrollBarPolicy.NEVER);
//        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
//
//        setMinWidth(400);
//        topBar.setMinWidth(400);
//
//        if(Client.listBoxes.size() == 0)
//            topBar.setPrefWidth(Client.primaryScene.getWidth() - 20);
//        else
//            topBar.setPrefWidth((Client.primaryScene.getWidth() - 20) / Client.listBoxes.size());
//
//        Client.primaryScene.widthProperty().addListener(event ->
//        {
//            topBar.setPrefWidth((Client.primaryScene.getWidth() - 20) / Client.listBoxes.size());
//        });
//
//        updateMenu(Client.api.getFeeds());
//    }
//
//    /**
//     * Updates the settings menu for the ListBox. Adds Feeds if there have been more added.
//     * @param feeds An array with all Feeds in the Configuration.
//     */
//    void updateMenu(Feed[] feeds)
//    {
//        if(checkboxes != null)
//            checkBoxContainer.getChildren().removeAll(checkboxes);
//
//        if(feeds == null)
//            return;
//
//        checkboxes = new MenuFeed[feeds.length];
//
//        for(int i = 0; i < feeds.length; i++)
//        {
//            checkboxes[i] = new MenuFeed();
//            checkboxes[i].setText(feeds[i].getTitle());
//            checkboxes[i].setValue(feeds[i].getUrlToXML());
//
//            String[] urls = Client.api.getItemList(name).getFeedUrls();
//
//            if(urls != null)
//            {
//                for(int j = 0; j < urls.length; j++)
//                {
//                    if(feeds[i].getUrlToXML().equals(urls[j]))
//                    {
//                        checkboxes[i].setSelected(true);
//                        checkboxes[i].setLastState(true);
//                        updateItems(Client.api.getItemList(name));
//                        break;
//                    }
//                }
//            }
//        }
//
//        for(MenuFeed checkbox : checkboxes)
//        {
//            checkbox.setOnAction((event) ->
//            {
//                for(int i = 0; i < checkboxes.length; i++)
//                {
//                    if(checkboxes[i].isSelected() && !checkboxes[i].wasSelected())
//                    {
//                        Client.api.addFeedToItemList(name, checkbox.getValue());
//                        Client.api.update();
//                        checkboxes[i].setLastState(true);
//                    }
//                    else if(!checkboxes[i].isSelected() && checkboxes[i].wasSelected())
//                    {
//                        Client.api.removeFeedFromItemList(name, checkbox.getValue());
//                        Client.api.update();
//                        checkboxes[i].setLastState(false);
//                    }
//                }
//
//                updateItems(Client.api.getItemList(name));
//            });
//        }
//
//        checkBoxContainer.getChildren().addAll(checkboxes);
//    }
//
//    /**
//     * Updates all the Items in the ListBox.
//     * @param items The ItemList associated with this ListBox.
//     */
//    void updateItems(ItemList items)
//    {
//        itemList.getChildren().clear();
//
//        if(items.getItems() != null)
//        {
//            for(int i = 0; i < items.getItems().length; i++)
//                itemList.getChildren().add(new ItemBox(items.getItems()[i]));
//        }
//    }
//
//    /**
//     * Sets the width of the ListBox.
//     * @param width The width we want to set to the ListBox in pixels.
//     */
//    void setListWidth(double width)
//    {
//        setMaxWidth(width);
//        topBar.setMaxWidth(width);
//    }
//
//    /**
//     * Creates the sort settings for the settings menu.
//     * @return An HBox containing the sort settings.
//     */
//    private HBox createSortSettings()
//    {
//        HBox sortContainer = new HBox();
//        VBox sortTargetContainer = new VBox();
//        VBox sortDirectionContainer = new VBox();
//
//        ToggleGroup target = new ToggleGroup();
//        ToggleGroup direction = new ToggleGroup();
//
//        RadioButton radioTitle = new RadioButton("Title");
//        RadioButton radioDate = new RadioButton("Date");
//        radioTitle.setUserData("TITLE");
//        radioDate.setUserData("DATE");
//        radioTitle.setToggleGroup(target);
//        radioDate.setToggleGroup(target);
//        sortTargetContainer.getChildren().addAll(radioTitle, radioDate);
//
//        RadioButton radioAsc = new RadioButton("Ascending");
//        RadioButton radioDec = new RadioButton("Descending");
//        radioAsc.setUserData("ASC");
//        radioDec.setUserData("DEC");
//        radioAsc.setToggleGroup(direction);
//        radioDec.setToggleGroup(direction);
//
//
//        String sorting = Client.api.getItemList(name).getSorting();
//
//        if(sorting.equals("TITLE_ASC"))
//        {
//            radioTitle.setSelected(true);
//            radioAsc.setSelected(true);
//        }
//        else if(sorting.equals("TITLE_DEC"))
//        {
//            radioTitle.setSelected(true);
//            radioDec.setSelected(true);
//        }
//        else if(sorting.equals("DATE_ASC"))
//        {
//            radioDate.setSelected(true);
//            radioAsc.setSelected(true);
//        }
//        else
//        {
//            radioDate.setSelected(true);
//            radioDec.setSelected(true);
//        }
//
//        sortDirectionContainer.getChildren().addAll(radioAsc, radioDec);
//        sortContainer.getChildren().addAll(sortTargetContainer, sortDirectionContainer);
//        target.selectedToggleProperty().addListener((event) -> updateSorting(target, direction));
//        direction.selectedToggleProperty().addListener((event) -> updateSorting(target, direction));
//
//        return sortContainer;
//    }
//
//    /**
//     * Updates sorting in the api if these has been any changes in the graphical interface.
//     * @param target    The ToggleGroup that decides the target, title or date.
//     * @param direction The ToggleGroup that decides direction, ascending or descending.
//     */
//    private void updateSorting(ToggleGroup target, ToggleGroup direction)
//    {
//        String str = target.getSelectedToggle().getUserData().toString();
//        str += "_";
//        str += direction.getSelectedToggle().getUserData().toString();
//
//        Client.api.setSorting(str, getName());
//        updateItems(Client.api.getItemList(getName()));
//    }
//
//    /**
//     * Switches between settings and list view.
//     */
//    private void switchMode()
//    {
//        if(isSettingsOpen)
//            showList();
//        else
//            showSettings();
//    }
//
//    /**
//     * Puts the settings menu in the container.
//     */
//    private void showSettings()
//    {
//        updateMenu(Client.api.getFeeds());
//        container.getChildren().clear();
//        container.getChildren().addAll(topBar, createSortSettings(), checkBoxContainer);
//        isSettingsOpen = true;
//    }
//
//    /**
//     * Puts the List in the container.
//     */
//    private void showList()
//    {
//        container.getChildren().clear();
//        container.getChildren().addAll(topBar, itemList);
//        isSettingsOpen = false;
//    }
//
//    /**
//     * Returns the name set to this ListBox.
//     * @return Name associated with the ListBox.
//     */
//    String getName()
//    {
//        return name;
//    }
//}
//
//// Created: 2016-05-06
