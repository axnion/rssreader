package system;

import java.util.Date;

/**
 * Class UpdaterThread
 *
 * @author Axel Nilsson (axnion)
 */
class RunnableUpdater implements Runnable {
    private volatile boolean running = true;

    void terminate() {
        running = false;
    }

    public void run() {
        if(running) {
            Thread.currentThread().setName("UpdaterThread");
            boolean updated = false;

            System.out.println("UpdaterThread running");

            for(FeedList feedList : Configuration.getFeedLists()) {
                if(feedList.update())
                    updated = true;
            }

            if(updated) {
                Configuration.setLastUpdated(new Date());
                System.out.print("Update: Configuration");
            }
        }
    }
}
