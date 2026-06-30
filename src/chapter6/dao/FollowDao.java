package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import chapter6.beans.Follow;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class FollowDao {

	Logger log = Logger.getLogger("twitter");

	public FollowDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	public void insert(Connection connection, Follow follow) {

		PreparedStatement ps = null;
		try {

			String sql =
					"INSERT INTO follows ("
							+ " follower_id,"
							+ " following_id,"
							+ " created_date"
							+ ") VALUES ("
							+ " ?, ?, CURRENT_TIMESTAMP)";

			ps = connection.prepareStatement(sql);

			ps.setInt(1, follow.getFollowerId());
			ps.setInt(2, follow.getFollowingId());

			ps.executeUpdate();

		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public void delete(Connection connection, int followerId, int followingId) {

		PreparedStatement ps = null;
		try {

			String sql =
					"DELETE FROM follows "
							+ "WHERE follower_id = ? "
							+ "AND following_id = ?";

			ps = connection.prepareStatement(sql);

			ps.setInt(1, followerId);
			ps.setInt(2, followingId);

			ps.executeUpdate();

		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public boolean isFollowing(Connection connection,
			int followerId,
			int followingId) {

		PreparedStatement ps = null;
		try {

			String sql =
					"SELECT id "
							+ "FROM follows "
							+ "WHERE follower_id = ? "
							+ "AND following_id = ?";

			ps = connection.prepareStatement(sql);

			ps.setInt(1, followerId);
			ps.setInt(2, followingId);

			ResultSet rs = ps.executeQuery();

			try {
				return rs.next();
			} finally {
				close(rs);
			}

		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
}
