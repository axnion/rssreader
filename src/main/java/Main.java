
import client.Client;

/**
 * Class Main
 *
 * This is the main class. This is where the client is launched.
 *
 * @author Axel Nilsson (axnion)
 * @version 1.0
 */
public class Main
{
    /**
     * This is the main method. This is the method that is called from the jar when launching the
     * application.
     * @param args Arguments passed the the application.
     */
    public static void main(String[] args)
    {
        Client client = new Client();
        client.launchJavaFX();
    }
}

// Created: 2016-04-11
