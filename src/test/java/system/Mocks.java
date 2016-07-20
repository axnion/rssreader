package system;

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
        FeedList feedLists = mock(FeedList.class);

        doReturn(name).when(feedLists).getName();

        return feedLists;
    }

    static DatabaseAccessObject createDatabaseAccessObjectMock() {
        DatabaseAccessObject dao = mock(DatabaseAccessObject.class);

        return dao;
    }
}
