package chapter6.dao;

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

public class LikeDaoTest {

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
     * LikeDao#insert(Connection, Like)
     * のためのテスト・メソッド。
     * （いいね登録）
     */
    @Test
    public void insert_01() throws Exception {

        System.out.println("insert_01");

        Connection connection = getConnection();

        try {

            Like like = new Like();
            like.setUserId(USER_ID);
            like.setMessageId(MESSAGE_ID);

            dao.insert(connection, like);

            commit(connection);

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
     * LikeDao#delete(Connection, int, int)
     * のためのテスト・メソッド。
     * （いいね削除）
     */
    @Test
    public void delete_01() throws Exception {

        System.out.println("delete_01");

        Connection connection = getConnection();

        try {

            Like like = new Like();
            like.setUserId(USER_ID);
            like.setMessageId(MESSAGE_ID);

            dao.insert(connection, like);
            commit(connection);

            dao.delete(connection, USER_ID, MESSAGE_ID);
            commit(connection);

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
     * LikeDao#isLiked(Connection, int, int)
     * のためのテスト・メソッド。
     * （登録済み）
     */
    @Test
    public void isLiked_01() throws Exception {

        System.out.println("isLiked_01");

        Connection connection = getConnection();

        try {

            Like like = new Like();
            like.setUserId(USER_ID);
            like.setMessageId(MESSAGE_ID);

            dao.insert(connection, like);
            commit(connection);

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
     * LikeDao#isLiked(Connection, int, int)
     * のためのテスト・メソッド。
     * （未登録）
     */
    @Test
    public void isLiked_02() throws Exception {

        System.out.println("isLiked_02");

        Connection connection = getConnection();

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
