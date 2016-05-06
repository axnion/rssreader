package client;

import javafx.scene.control.CheckBox;

/**
 * Class MenuFeed
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
public class MenuFeed extends CheckBox
{
    private String value;
    private boolean lastState;

    public MenuFeed()
    {
        value = "";
        lastState = false;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public boolean wasSelected()
    {
        return lastState;
    }

    public void setLastState(boolean newState)
    {
        lastState = newState;
    }
}

// Created: 2016-05-06
