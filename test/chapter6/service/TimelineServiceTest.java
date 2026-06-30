package chapter6.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chapter6.beans.UserMessage;

public class TimelineServiceTest {

    private TimelineService service;

    private static final int USER_ID = 1;

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

        service = new TimelineService();
    }

    @After
    public void tearDown() throws Exception {

        System.out.println("tearDown");
    }

    /**
     * TimelineService#select(int)
     * タイムライン取得
     */
    @Test
    public void select_01() {

        System.out.println("select_01");

        List<UserMessage> messages = service.select(USER_ID);

        assertNotNull(messages);
    }

    /**
     * TimelineService#select(int)
     * タイムラインにデータが存在する
     */
    @Test
    public void select_02() {

        System.out.println("select_02");

        List<UserMessage> messages = service.select(USER_ID);

        assertFalse(messages.isEmpty());
    }

    /**
     * TimelineService#select(int)
     * 存在しないユーザー
     */
    @Test
    public void select_03() {

        System.out.println("select_03");

        List<UserMessage> messages = service.select(999999);

        assertNotNull(messages);
        assertTrue(messages.isEmpty());
    }
}