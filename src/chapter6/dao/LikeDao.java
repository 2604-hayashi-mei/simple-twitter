package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.Like;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class LikeDao {
	Logger log = Logger.getLogger("twitter");

	public LikeDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	public void insert(Connection connection, Like like) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO likes ( ");
			sql.append("    user_id, ");
			sql.append("    message_id, ");
			sql.append("    created_date ");
			sql.append(") VALUES ( ");
			sql.append("    ?, "); // user_id
			sql.append("    ?, "); // message_id
			sql.append("    CURRENT_TIMESTAMP "); // created_date
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, like.getUserId());
			ps.setInt(2, like.getMessageId());

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public void delete(Connection connection, int userId, int messageId) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			String sql = "DELETE FROM likes WHERE user_id = ? AND message_id = ?";

			ps = connection.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, messageId);

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public boolean isLiked(Connection connection, int userId, int messageId) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			String sql = "SELECT id FROM likes WHERE user_id = ? AND message_id = ?";

			ps = connection.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, messageId);

			ResultSet rs = ps.executeQuery();
			try {
				return rs.next();
			} finally {
				close(rs);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
}
