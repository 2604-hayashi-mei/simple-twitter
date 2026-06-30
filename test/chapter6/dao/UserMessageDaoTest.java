package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chapter6.beans.UserMessage;

public class UserMessageDaoTest {

    private Connection connection;
    private UserMessageDao dao;

    private static final int USER_ID = 1;

    @Before
    public void setUp() throws Exception {

        System.out.println("setUp");

        connection = getConnection();
        dao = new UserMessageDao();
    }

    @After
    public void tearDown() throws Exception {

        System.out.println("tearDown");

        close(connection);
    }

    /**
     * UserMessageDao#selectTimeline(Connection, int)
     * タイムライン取得
     */
    @Test
    public void selectTimeline_01() {

        List<UserMessage> messages =
                dao.selectTimeline(connection, USER_ID);

        assertNotNull(messages);
    }

    /**
     * UserMessageDao#selectTimeline(Connection, int)
     * タイムラインにデータが存在する
     */
    @Test
    public void selectTimeline_02() {

        List<UserMessage> messages =
                dao.selectTimeline(connection, USER_ID);

        assertFalse(messages.isEmpty());
    }

    /**
     * UserMessageDao#selectTimeline(Connection, int)
     * 存在しないユーザー
     */
    @Test
    public void selectTimeline_03() {

        List<UserMessage> messages =
                dao.selectTimeline(connection, 999999);

        assertNotNull(messages);
        assertTrue(messages.isEmpty());
    }
}
