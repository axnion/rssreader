package client;

import javafx.scene.control.CheckBox;

/**
 * Class MenuFeed
 *
 * This is a custom CheckBox that has a value set to it.
 *
 * @author Axel Nilsson (axnion)
 * @version 1.0
 */
class MenuFeed extends CheckBox
{
    private String value;
    private boolean lastState;

    /**
     * Constructor.
     */
    MenuFeed()
    {
        value = "";
        lastState = false;
    }


    /*
    ------------------------------- ACCESSORS AND MUTATORS -----------------------------------------
    */

    /**
     * Returns the value.
     * @return  The value set to this MenuFeed.
     */
    String getValue()
    {
        return value;
    }

    /**
     * Used to set the value of value.
     * @param value The value we want value to have.
     */
    void setValue(String value)
    {
        this.value = value;
    }

    /**
     * Returns the last state select was in.
     * @return True if it was selected before, false if not.
     */
    boolean wasSelected()
    {
        return lastState;
    }

    /**
     * Sets the previous state of the select.
     * @param newState True if it was selected before, false if not.
     */
    void setLastState(boolean newState)
    {
        lastState = newState;
    }
}

// Created: 2016-05-06
