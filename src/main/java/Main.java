
import api.Configuration;
import api.Feed;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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
        Feed[] feeds = new Feed[3];
        feeds[0] = new Feed("http://www.axelnilsson.tech/feed.xml");
        feeds[1] = new Feed("http://feeds.feedburner.com/sakerhetspodcasten");
        feeds[2] = new Feed("http://feeds2.feedburner.com/AllJupiterVideos");

        for(Feed feed : feeds)
            feed.update();

        Configuration config = new Configuration("C:\\Development\\1DV430\\test.json");

        config.setFeeds(feeds);
        config.saveConfigurationFile();
    }





//    public static void main(String[] args)
//    {
//        launch(args);
//    }
//
//    public void start(Stage primaryStage)
//    {
//        primaryStage.setTitle("Hello World!");
//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//
//        StackPane root = new StackPane();
//        root.getChildren().add(btn);
//        primaryStage.setScene(new Scene(root, 300, 250));
//        primaryStage.show();
//    }




//    public static void main(String[] args)
//    {
//        Scanner in = new Scanner(System.in);
//
//        String str = in.nextLine();
//
//        Feed feed = new Feed(str);
//        feed.update();
//
//        System.out.println("TITLE: " + feed.getTitle());
//        System.out.println("LINK: " + feed.getLink());
//        System.out.println("DESCRIPTION: " + feed.getDescription());
//        System.out.println("URLTOXML: " + feed.getUrlToXML());
//        System.out.println("");
//
//        ArrayList<Item> items = feed.getItems();
//
//        for(Item item : items)
//        {
//            if(item == null)
//                break;
//
//            System.out.println("TITLE: " + item.getTitle());
//            System.out.println("LINK: " + item.getLink());
//            System.out.println("DESCRIPTION: " + item.getDescription());
//            System.out.println("ID: " + item.getId());
//        }
//
//        in.nextLine();
//    }
}

// Created: 2016-04-11
