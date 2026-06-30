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

import chapter6.beans.Follow;

public class FollowDaoTest {

    private FollowDao dao;

    /**
     * テスト用フォロワーID
     */
    private static final int FOLLOWER_ID = 1;

    /**
     * テスト用フォロー対象ID
     */
    private static final int FOLLOWING_ID = 2;

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

        dao = new FollowDao();

        Connection connection = getConnection();

        try {

            if (dao.isFollowing(connection, FOLLOWER_ID, FOLLOWING_ID)) {
                dao.delete(connection, FOLLOWER_ID, FOLLOWING_ID);
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

            if (dao.isFollowing(connection, FOLLOWER_ID, FOLLOWING_ID)) {
                dao.delete(connection, FOLLOWER_ID, FOLLOWING_ID);
                commit(connection);
            }

        } finally {
            close(connection);
        }
    }

    /**
     * FollowDao#insert(Connection, Follow)
     * のためのテスト・メソッド。
     * （フォロー登録）
     */
    @Test
    public void insert_01() throws Exception {

        System.out.println("insert_01");

        Connection connection = getConnection();

        try {

            Follow follow = new Follow();
            follow.setFollowerId(FOLLOWER_ID);
            follow.setFollowingId(FOLLOWING_ID);

            dao.insert(connection, follow);

            commit(connection);

            assertTrue(
                    dao.isFollowing(
                            connection,
                            FOLLOWER_ID,
                            FOLLOWING_ID));

        } finally {
            close(connection);
        }
    }

    /**
     * FollowDao#delete(Connection, int, int)
     * のためのテスト・メソッド。
     * （フォロー解除）
     */
    @Test
    public void delete_01() throws Exception {

        System.out.println("delete_01");

        Connection connection = getConnection();

        try {

            Follow follow = new Follow();
            follow.setFollowerId(FOLLOWER_ID);
            follow.setFollowingId(FOLLOWING_ID);

            dao.insert(connection, follow);
            commit(connection);

            dao.delete(connection, FOLLOWER_ID, FOLLOWING_ID);
            commit(connection);

            assertFalse(
                    dao.isFollowing(
                            connection,
                            FOLLOWER_ID,
                            FOLLOWING_ID));

        } finally {
            close(connection);
        }
    }

    /**
     * FollowDao#isFollowing(Connection, int, int)
     * のためのテスト・メソッド。
     * （登録済み）
     */
    @Test
    public void isFollowing_01() throws Exception {

        System.out.println("isFollowing_01");

        Connection connection = getConnection();

        try {

        	Follow follow = new Follow();
            follow.setFollowerId(FOLLOWER_ID);
            follow.setFollowingId(FOLLOWING_ID);

            dao.insert(connection, follow);
            commit(connection);

            assertTrue(
                    dao.isFollowing(
                            connection,
                            FOLLOWER_ID,
                            FOLLOWING_ID));

        } finally {
            close(connection);
        }
    }

    /**
     * FollowDao#isFollowing(Connection, int, int)
     * のためのテスト・メソッド。
     * （未登録）
     */
    @Test
    public void isFollowing_02() throws Exception {

        System.out.println("isFollowing_02");

        Connection connection = getConnection();

        try {

            assertFalse(
                    dao.isFollowing(
                            connection,
                            FOLLOWER_ID,
                            FOLLOWING_ID));

        } finally {
            close(connection);
        }
    }
}

