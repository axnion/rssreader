package client;

import javafx.scene.control.CheckBox;

/**
 * Class MenuFeed
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
class MenuFeed extends CheckBox
{
    private String value;
    private boolean lastState;

    MenuFeed()
    {
        value = "";
        lastState = false;
    }

    String getValue()
    {
        return value;
    }

    void setValue(String value)
    {
        this.value = value;
    }

    boolean wasSelected()
    {
        return lastState;
    }

    void setLastState(boolean newState)
    {
        lastState = newState;
    }
}

// Created: 2016-05-06
