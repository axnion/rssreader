package system;

import rss.Feed;
import rss.Item;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

/**
 * Class Mocks
 *
 * @author Axel Nilsson (axnion)
 */
class Mocks {
    static FeedList createFeedListMock(String name) {
        FeedList feedList = mock(FeedList.class);
        doReturn(name).when(feedList).getName();
        return feedList;
    }

    static DatabaseAccessObject createDatabaseAccessObjectMock() {
        DatabaseAccessObject dao = mock(DatabaseAccessObject.class);

        try {
            doNothing().when(dao).setPath(anyString());
            doNothing().when(dao).save();
            doReturn(new ArrayList<FeedList>()).when(dao).load();
        }
        catch(Exception expt) {
            expt.printStackTrace();
        }

        return dao;
    }
}
