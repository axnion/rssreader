package system;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class UpdaterThread
 *
 * @author Axel Nilsson (axnion)
 */
class UpdaterThread implements Runnable {
    public void run() {
        Thread.currentThread().setName("UpdaterThread");

        ArrayList<FeedList> feedLists = Configuration.getFeedLists();
        boolean updated = false;
        for(FeedList feedList : feedLists) {
            if(feedList.update())
                updated = true;
        }

        if(updated) {
            Configuration.setLastUpdated(new Date());
            System.out.println("UpdaterThread - Configuration Updated");
        }

    }
}
