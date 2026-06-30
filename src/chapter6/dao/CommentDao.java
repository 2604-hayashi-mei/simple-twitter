package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.Comment;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class CommentDao {
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public CommentDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	public void insert(Connection connection, Comment comment) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO comments ( ");
			sql.append("    user_id, ");
			sql.append("    message_id, "); // どのつぶやきに対する返信か
			sql.append("    text, ");
			sql.append("    created_date, ");
			sql.append("    updated_date ");
			sql.append(") VALUES ( ");
			sql.append("    ?, "); // user_id
			sql.append("    ?, "); // message_id
			sql.append("    ?, "); // text
			sql.append("    CURRENT_TIMESTAMP, "); // created_date
			sql.append("    CURRENT_TIMESTAMP "); // updated_date
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, comment.getUserId());
			ps.setInt(2, comment.getMessageId());
			ps.setString(3, comment.getText());

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
	
	public void delete(Connection connection, int id) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			//SQL文作成
			String sql = "DELETE FROM comments WHERE id = ?";

			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
	
	public Comment select(Connection connection, int id) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			//SQL文作成
			String sql = "SELECT * FROM comments WHERE id = ?";
			ps = connection.prepareStatement(sql);

			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();

			List<Comment> comments = toComments(rs);
			if (comments.isEmpty()) {
				return null;
			} else if (2 <= comments.size()) {
				log.log(Level.SEVERE, "メッセージが重複しています", new IllegalStateException());
				throw new IllegalStateException("メッセージが重複しています");
			} else {
				return comments.get(0);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
	
	private List<Comment> toComments(ResultSet rs) throws SQLException {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());
		List<Comment> comments = new ArrayList<Comment>();
		try {
			while (rs.next()) {
				Comment comment = new Comment();
				comment.setId(rs.getInt("id"));
				comment.setUserId(rs.getInt("user_id"));
				comment.setText(rs.getString("text"));
				comment.setCreatedDate(rs.getTimestamp("created_date"));
				comment.setUpdatedDate(rs.getTimestamp("updated_date"));

				comments.add(comment);
			}
			return comments;
		} finally {
			close(rs);
		}
	}
	
	public void update(Connection connection, Comment comment) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			//SQL文作成
			String sql = "UPDATE comments SET text = ?, updated_date = CURRENT_TIMESTAMP WHERE id = ?";
			ps = connection.prepareStatement(sql);

			ps.setString(1, comment.getText());
			ps.setInt(2, comment.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
}
