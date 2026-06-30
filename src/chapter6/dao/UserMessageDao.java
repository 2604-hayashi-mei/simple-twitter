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

import chapter6.beans.UserMessage;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class UserMessageDao {
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public UserMessageDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	public List<UserMessage> select(Connection connection, Integer userId, int loginUserId, int num, String start, String end) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ");
			sql.append("    messages.id as id, ");
			sql.append("    messages.text as text, ");
			sql.append("    messages.user_id as user_id, ");
			sql.append("    users.account as account, ");
			sql.append("    users.name as name, ");
			sql.append("    messages.created_date as created_date, ");
			sql.append("    COUNT(DISTINCT likes.id) as like_count, ");
			sql.append("    CASE ");
			sql.append("        WHEN follows.id IS NULL THEN 0 ");
			sql.append("        ELSE 1 ");
			sql.append("    END as following ");
			sql.append("FROM messages ");
			sql.append("INNER JOIN users ");
			sql.append("ON messages.user_id = users.id ");
			sql.append("LEFT JOIN likes ");
			sql.append("ON messages.id = likes.message_id ");
			sql.append("LEFT JOIN follows ");
			sql.append("ON follows.following_id = messages.user_id ");
			sql.append("AND follows.follower_id = ? ");

			sql.append(" WHERE messages.created_date BETWEEN ? AND ? ");

			if (userId != null) {
				sql.append(" AND messages.user_id = ? ");
			}
			
			sql.append("GROUP BY ");
			sql.append("    messages.id, ");
			sql.append("    messages.text, ");
			sql.append("    messages.user_id, ");
			sql.append("    users.account, ");
			sql.append("    users.name, ");
			sql.append("    messages.created_date, ");
			sql.append("    follows.id ");
			
			sql.append("ORDER BY messages.created_date DESC limit " + num);

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, loginUserId); // follows.follower_id
			ps.setString(2, start);
			ps.setString(3, end);

			if (userId != null) {
			    ps.setInt(4, userId);
			}

			ResultSet rs = ps.executeQuery();

			List<UserMessage> messages = toUserMessages(rs);
			return messages;
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
	
	public List<UserMessage> selectTimeline(Connection connection, int userId) {

		PreparedStatement ps = null;

		try {

			StringBuilder sql = new StringBuilder();

			sql.append("SELECT ");
			sql.append("    messages.id as id, ");
			sql.append("    messages.text as text, ");
			sql.append("    messages.user_id as user_id, ");
			sql.append("    users.account as account, ");
			sql.append("    users.name as name, ");
			sql.append("    messages.created_date as created_date, ");
			sql.append("    COUNT(DISTINCT likes.id) as like_count, ");
			sql.append("    CASE ");
			sql.append("        WHEN follows.id IS NULL THEN 0 ");
			sql.append("        ELSE 1 ");
			sql.append("    END as following ");
			sql.append("FROM messages ");

			sql.append("INNER JOIN users ");
			sql.append("ON messages.user_id = users.id ");

			sql.append("LEFT JOIN likes ");
			sql.append("ON messages.id = likes.message_id ");
			sql.append("LEFT JOIN follows ");
			sql.append("ON follows.following_id = messages.user_id ");
			sql.append("AND follows.follower_id = ? ");

			sql.append("WHERE ");
			sql.append("messages.user_id = ? ");
			sql.append("OR ");
			sql.append("messages.user_id IN ( ");
			sql.append("    SELECT following_id ");
			sql.append("    FROM follows ");
			sql.append("    WHERE follower_id = ? ");
			sql.append(") ");

			sql.append("GROUP BY ");
			sql.append("messages.id, ");
			sql.append("messages.text, ");
			sql.append("messages.user_id, ");
			sql.append("users.account, ");
			sql.append("users.name, ");
			sql.append("messages.created_date ");

			sql.append("ORDER BY messages.created_date DESC");

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, userId); // follows JOIN
			ps.setInt(2, userId); // 自分の投稿
			ps.setInt(3, userId); // IN句

			ResultSet rs = ps.executeQuery();

			return toTimelineMessages(rs);

		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<UserMessage> toUserMessages(ResultSet rs) throws SQLException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<UserMessage> messages = new ArrayList<UserMessage>();
		try {
			while (rs.next()) {
				UserMessage message = new UserMessage();
				message.setId(rs.getInt("id"));
				message.setText(rs.getString("text"));
				message.setUserId(rs.getInt("user_id"));
				message.setAccount(rs.getString("account"));
				message.setName(rs.getString("name"));
				message.setCreatedDate(rs.getTimestamp("created_date"));
				message.setLikeCount(rs.getInt("like_count"));
				
				message.setFollowing(
		                rs.getInt("following") == 1
		            );

				messages.add(message);
			}
			return messages;
		} finally {
			close(rs);
		}
	}
	
	private List<UserMessage> toTimelineMessages(ResultSet rs)
			throws SQLException {

		List<UserMessage> messages = new ArrayList<UserMessage>();

		try {

			while (rs.next()) {

				UserMessage message = new UserMessage();

				message.setId(rs.getInt("id"));
				message.setText(rs.getString("text"));
				message.setUserId(rs.getInt("user_id"));
				message.setAccount(rs.getString("account"));
				message.setName(rs.getString("name"));
				message.setCreatedDate(rs.getTimestamp("created_date"));
				message.setLikeCount(rs.getInt("like_count"));

				// フォロー状態
				message.setFollowing(
						rs.getInt("following") == 1);

				messages.add(message);
			}

			return messages;

		} finally {
			close(rs);
		}
	}
}
