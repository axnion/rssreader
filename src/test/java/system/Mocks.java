package system;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

/**
 * Class Mocks
 *
 * @author Axel Nilsson (axnion)
 */
public class Mocks {
    public static FeedList createFeedListMock(String name) {
        FeedList feedLists = mock(FeedList.class);

        doReturn(name).when(feedLists).getName();

        return feedLists;
    }

    public static DatabaseController createDatabaseControllerMock() {
        DatabaseController dbc = mock(DatabaseController.class);

        try {
            doReturn("").when(dbc).getSorting("FeedList1");
            doReturn("DATE_DEC").when(dbc).getSorting("FeedList2");
            doThrow(new Exception()).when(dbc).getSorting("FeedList3");

            doReturn(true).when(dbc).getVisitedStatus("FeedList1", "item_id_1");
            doReturn(false).when(dbc).getVisitedStatus("FeedList1", "item_id_2");
            doThrow(new Exception()).when(dbc).getVisitedStatus("FeedList2", "item_id_3");

            doReturn(true).when(dbc).getStarredStatus("FeedList1", "item_id_1");
            doReturn(false).when(dbc).getStarredStatus("FeedList1", "item_id_2");
            doThrow(new Exception()).when(dbc).getStarredStatus("FeedList2", "item_id_3");

            doNothing().when(dbc).setSorting("FeedList1", "DATE_DEC");
            doThrow(Exception.class).when(dbc).setSorting("FeedList2", "DATE_DEC");

            doNothing().when(dbc).setVisitedStatus("FeedList1", "item_id_1", false);
            doThrow(Exception.class).when(dbc).setVisitedStatus("FeedList2", "item_id_1", false);

            doNothing().when(dbc).setStarredStatus("FeedList1", "item_id_1", false);
            doThrow(Exception.class).when(dbc).setStarredStatus("FeedList2", "item_id_1", false);

            doNothing().when(dbc).addFeedList(anyString());
            doNothing().when(dbc).removeFeedList(anyString());

            doNothing().when(dbc).addFeed(anyString(), anyString());
            doNothing().when(dbc).removeFeed(anyString(), anyString());
        }
        catch(Exception err) {
            err.printStackTrace();
        }

        return dbc;
    }

    public static DatabaseAccessObject createDatabaseAccessObjectMock() {
        DatabaseAccessObject dao = mock(DatabaseAccessObject.class);

        return dao;
    }
}
