package chapter6.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chapter6.beans.Comment;
import chapter6.beans.UserComment;

public class CommentServiceTest {

    private CommentService service;

    private int testCommentId = -1;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("setUpBeforeClass");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("tearDownAfterClass");
    }

    @Before
    public void setUp() {
        System.out.println("setUp");
        service = new CommentService();
    }

    @After
    public void tearDown() {

        System.out.println("tearDown");

        if (testCommentId != -1) {

            Comment comment = service.select(testCommentId);

            if (comment != null) {
                service.delete(testCommentId);
            }

            testCommentId = -1;
        }
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

        service.insert(comment);

        List<UserComment> comments = service.select();

        for (UserComment c : comments) {

            if (uniqueText.equals(c.getText())) {

                testCommentId = c.getId();

                return c.getId();
            }
        }

        throw new RuntimeException("テストデータ作成失敗");
    }

    /**
     * 存在するコメント取得
     */
    @Test
    public void select_01() {

        int id = createComment("Select");

        Comment comment = service.select(id);

        assertNotNull(comment);
        assertEquals(id, comment.getId());
    }

    /**
     * 存在しないコメント取得
     */
    @Test
    public void select_02() {

        Comment comment = service.select(999999);

        assertNull(comment);
    }

    /**
     * コメント編集（正常系）
     */
    @Test
    public void update_01() {

        int id = createComment("Before");

        Comment comment = service.select(id);

        String updatedText = "After_" + System.currentTimeMillis();

        comment.setText(updatedText);

        service.update(comment);

        Comment updated = service.select(id);

        assertNotNull(updated);
        assertEquals(updatedText, updated.getText());
    }

    /**
     * コメント編集（空文字）
     */
    @Test
    public void update_02() {

        int id = createComment("Before");

        Comment comment = service.select(id);

        comment.setText("");

        service.update(comment);

        Comment updated = service.select(id);

        assertNotNull(updated);
        assertEquals("", updated.getText());
    }

    /**
     * 存在しないコメント更新
     */
    @Test
    public void update_03() {

        Comment comment = new Comment();

        comment.setId(999999);
        comment.setUserId(1);
        comment.setMessageId(1);
        comment.setText("NoData");

        service.update(comment);

        assertNull(service.select(999999));
    }

    /**
     * コメント削除（正常系）
     */
    @Test
    public void delete_01() {

        int id = createComment("Delete");

        service.delete(id);

        Comment comment = service.select(id);

        assertNull(comment);

        testCommentId = -1;
    }

    /**
     * 存在しないコメント削除
     */
    @Test
    public void delete_02() {

        service.delete(999999);

        assertNull(service.select(999999));
    }

}