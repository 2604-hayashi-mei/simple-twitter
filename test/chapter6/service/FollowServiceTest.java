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

import chapter6.beans.Follow;
import chapter6.dao.FollowDao;

public class FollowServiceTest {

    private FollowService service;
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

        service = new FollowService();
        dao = new FollowDao();

        Connection connection = getConnection();

        try {

            if (dao.isFollowing(
                    connection,
                    FOLLOWER_ID,
                    FOLLOWING_ID)) {

                dao.delete(
                        connection,
                        FOLLOWER_ID,
                        FOLLOWING_ID);

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

            if (dao.isFollowing(
                    connection,
                    FOLLOWER_ID,
                    FOLLOWING_ID)) {

                dao.delete(
                        connection,
                        FOLLOWER_ID,
                        FOLLOWING_ID);

                commit(connection);
            }

        } finally {
            close(connection);
        }
    }

    /**
     * FollowService#toggle(int, int)
     * のためのテスト・メソッド。
     * （未フォロー状態→フォロー登録）
     */
    @Test
    public void toggle_01() throws Exception {

        System.out.println("toggle_01");

        service.toggle(
                FOLLOWER_ID,
                FOLLOWING_ID);

        Connection connection = getConnection();

        try {

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
     * FollowService#toggle(int, int)
     * のためのテスト・メソッド。
     * （フォロー済み状態→フォロー解除）
     */
    @Test
    public void toggle_02() throws Exception {

        System.out.println("toggle_02");

        Connection connection = getConnection();

        try {

            Follow follow = new Follow();

            follow.setFollowerId(FOLLOWER_ID);
            follow.setFollowingId(FOLLOWING_ID);

            dao.insert(connection, follow);

            commit(connection);

        } finally {
            close(connection);
        }

        service.toggle(
                FOLLOWER_ID,
                FOLLOWING_ID);

        connection = getConnection();

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

    /**
     * FollowService#toggle(int, int)
     * のためのテスト・メソッド。
     * （連続実行で登録→解除）
     */
    @Test
    public void toggle_03() throws Exception {

        System.out.println("toggle_03");

        service.toggle(
                FOLLOWER_ID,
                FOLLOWING_ID);

        Connection connection = getConnection();

        try {

            assertTrue(
                    dao.isFollowing(
                            connection,
                            FOLLOWER_ID,
                            FOLLOWING_ID));

        } finally {
            close(connection);
        }

        service.toggle(
                FOLLOWER_ID,
                FOLLOWING_ID);

        connection = getConnection();

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