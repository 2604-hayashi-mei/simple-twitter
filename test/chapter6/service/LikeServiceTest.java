package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;
import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chapter6.beans.Like;
import chapter6.dao.LikeDao;

public class LikeServiceTest {

    private LikeService service;
    private LikeDao dao;

    /**
     * テスト用ユーザーID
     */
    private static final int USER_ID = 1;

    /**
     * テスト用メッセージID
     */
    private static final int MESSAGE_ID = 1;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.out.println("setUpBeforeClass");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        System.out.println("tearDownAfterClass");
    }

    @Before
    public void setUp() throws Exception {

        System.out.println("setUp");

        service = new LikeService();
        dao = new LikeDao();

        Connection connection = getConnection();

        try {

            if (dao.isLiked(connection, USER_ID, MESSAGE_ID)) {
                dao.delete(connection, USER_ID, MESSAGE_ID);
                commit(connection);
            }

        } finally {
            close(connection);
        }
    }

    @After
    public void tearDown() throws Exception {

        System.out.println("tearDown");

        Connection connection = getConnection();

        try {

            if (dao.isLiked(connection, USER_ID, MESSAGE_ID)) {
                dao.delete(connection, USER_ID, MESSAGE_ID);
                commit(connection);
            }

        } finally {
            close(connection);
        }
    }

    /**
     * LikeService#toggle(int, int)
     * のためのテスト・メソッド。
     * （未いいね状態→いいね登録）
     */
    @Test
    public void toggle_01() throws Exception {

        System.out.println("toggle_01");

        service.toggle(USER_ID, MESSAGE_ID);

        Connection connection = getConnection();

        try {

            assertTrue(
                    dao.isLiked(
                            connection,
                            USER_ID,
                            MESSAGE_ID));

        } finally {
            close(connection);
        }
    }

    /**
     * LikeService#toggle(int, int)
     * のためのテスト・メソッド。
     * （いいね済み状態→いいね解除）
     */
    @Test
    public void toggle_02() throws Exception {

        System.out.println("toggle_02");

        Connection connection = getConnection();

        try {

            Like like = new Like();
            like.setUserId(USER_ID);
            like.setMessageId(MESSAGE_ID);

            dao.insert(connection, like);
            commit(connection);

        } finally {
            close(connection);
        }

        service.toggle(USER_ID, MESSAGE_ID);

        connection = getConnection();

        try {

            assertFalse(
                    dao.isLiked(
                            connection,
                            USER_ID,
                            MESSAGE_ID));

        } finally {
            close(connection);
        }
    }

    /**
     * LikeService#toggle(int, int)
     * のためのテスト・メソッド。
     * （複数回実行）
     */
    @Test
    public void toggle_03() throws Exception {

        System.out.println("toggle_03");

        service.toggle(USER_ID, MESSAGE_ID);

        Connection connection = getConnection();

        try {

            assertTrue(
                    dao.isLiked(
                            connection,
                            USER_ID,
                            MESSAGE_ID));

        } finally {
            close(connection);
        }

        service.toggle(USER_ID, MESSAGE_ID);

        connection = getConnection();

        try {

            assertFalse(
                    dao.isLiked(
                            connection,
                            USER_ID,
                            MESSAGE_ID));

        } finally {
            close(connection);
        }
    }
}