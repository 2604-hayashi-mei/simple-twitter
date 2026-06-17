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

import chapter6.beans.UserComment;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class UserCommentDao {
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public UserCommentDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	public List<UserComment> select(Connection connection) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ");
			sql.append("    c.id AS id, ");
			sql.append("    c.user_id AS user_id, ");
			sql.append("    c.message_id AS message_id, ");
			sql.append("    c.text AS text, ");
			sql.append("    c.created_date AS created_date, ");
			sql.append("    c.updated_date AS updated_date, ");
			sql.append("    u.name AS name, ");
			sql.append("    u.account AS account ");
			sql.append("FROM comments c ");
			sql.append("INNER JOIN users u ON c.user_id = u.id ");
			sql.append("ORDER BY c.created_date ASC");

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();

			List<UserComment> comments = toComments(rs);
			return comments;
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<UserComment> toComments(ResultSet rs) throws SQLException {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<UserComment> comments = new ArrayList<UserComment>();
		try {
			while (rs.next()) {
				UserComment comment = new UserComment();
				comment.setId(rs.getInt("id"));
				comment.setUserId(rs.getInt("user_id"));
				comment.setMessageId(rs.getInt("message_id"));
				comment.setText(rs.getString("text"));
				comment.setCreatedDate(rs.getTimestamp("created_date"));
				comment.setUpdatedDate(rs.getTimestamp("updated_date"));
				comment.setName(rs.getString("name"));
				comment.setAccount(rs.getString("account"));

				comments.add(comment);
			}
			return comments;
		} finally {
			close(rs);
		}
	}
}
