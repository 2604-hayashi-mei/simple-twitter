package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chapter6.beans.Comment;
import chapter6.beans.UserComment;

public class CommentDaoTest {

    private Connection connection;
    private CommentDao dao;

    private int testCommentId = -1;

    @Before
    public void setUp() throws Exception {

        System.out.println("setUp");

        connection = getConnection();
        dao = new CommentDao();
    }

    @After
    public void tearDown() throws Exception {

        System.out.println("tearDown");

        if (testCommentId != -1) {

            Comment comment = dao.select(connection, testCommentId);

            if (comment != null) {
                dao.delete(connection, testCommentId);
                commit(connection);
            }

            testCommentId = -1;
        }

        close(connection);
    }

    /**
     * テスト用コメント作成
     */
    private int createComment(String text) {

        String uniqueText = text + "_" + System.currentTimeMillis();

        Comment comment = new Comment();

        comment.setUserId(1);
        comment.setMessageId(1);
        comment.setText(uniqueText);

        dao.insert(connection, comment);
        commit(connection);

        List<UserComment> comments = new UserCommentDao().select(connection);

        for (UserComment c : comments) {

            if (uniqueText.equals(c.getText())) {

                testCommentId = c.getId();

                return c.getId();
            }
        }

        throw new RuntimeException("テストデータ作成失敗");
    }

    /**
     * CommentDao#select(Connection, int)
     * 存在するコメント取得
     */
    @Test
    public void select_01() {

        int id = createComment("JUnit_Select_01");

        Comment comment = dao.select(connection, id);

        assertNotNull(comment);
        assertEquals(id, comment.getId());
    }

    /**
     * CommentDao#select(Connection, int)
     * 存在しないコメント取得
     */
    @Test
    public void select_02() {

        Comment comment = dao.select(connection, 999999);

        assertNull(comment);
    }

    /**
     * CommentDao#update(Connection, Comment)
     * コメント編集（正常系）
     */
    @Test
    public void update_01() {

        int id = createComment("Before");

        Comment comment = dao.select(connection, id);

        String updatedText = "After_" + System.currentTimeMillis();

        comment.setText(updatedText);

        dao.update(connection, comment);
        commit(connection);

        Comment updated = dao.select(connection, id);

        assertNotNull(updated);
        assertEquals(updatedText, updated.getText());
    }

    /**
     * CommentDao#update(Connection, Comment)
     * コメント編集（空文字）
     */
    @Test
    public void update_02() {

        int id = createComment("Before");

        Comment comment = dao.select(connection, id);

        comment.setText("");

        dao.update(connection, comment);
        commit(connection);

        Comment updated = dao.select(connection, id);

        assertNotNull(updated);
        assertEquals("", updated.getText());
    }

    /**
     * CommentDao#update(Connection, Comment)
     * 存在しないコメント編集
     */
    @Test
    public void update_03() {

        Comment comment = new Comment();

        comment.setId(999999);
        comment.setUserId(1);
        comment.setMessageId(1);
        comment.setText("NoData");

        dao.update(connection, comment);
        commit(connection);

        Comment updated = dao.select(connection, 999999);

        assertNull(updated);
    }

    /**
     * CommentDao#delete(Connection, int)
     * コメント削除
     */
    @Test
    public void delete_01() {

        int id = createComment("DeleteTest");

        dao.delete(connection, id);
        commit(connection);

        Comment comment = dao.select(connection, id);

        assertNull(comment);

        // tearDownで二重削除しないようにする
        testCommentId = -1;
    }

    /**
     * CommentDao#delete(Connection, int)
     * 存在しないコメント削除
     */
    @Test
    public void delete_02() {

        dao.delete(connection, 999999);
        commit(connection);

        Comment comment = dao.select(connection, 999999);

        assertNull(comment);
    }
}