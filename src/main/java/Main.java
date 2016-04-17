import api.Feed;
import api.Item;

import java.util.Scanner;

/**
 * Class Main
 *
 * @author Axel Nilsson (axnion)
 * @version 0.1
 */
public class Main
{
    public static void main(String[] args)
    {
        System.out.print("URL TO RSS FEED: ");
        Scanner scan = new Scanner(System.in);
        String url = scan.nextLine();

        Feed feed = new Feed(url);
        feed.update();

        System.out.println("TITLE: " + feed.getTitle());
        System.out.println("LINK: " + feed.getLink());
        System.out.println("DESCRIPTION: " + feed.getDescription());
        System.out.println("URLTOXML: " + feed.getUrlToXML());
        System.out.println("");

        Item[] items = feed.getItems();

        for(Item item : items)
        {
            if(item == null)
                break;

            System.out.println("TITLE: " + item.getTitle());
            System.out.println("LINK: " + item.getLink());
            System.out.println("DESCRIPTION: " + item.getDescription());
            System.out.println("ID: " + item.getId());
        }
    }
}

// Created: 2016-04-11
