package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;

import chapter6.beans.Follow;
import chapter6.dao.FollowDao;

public class FollowService {

	public void toggle(int followerId, int followingId) {

		Connection connection = null;

		try {

			connection = getConnection();

			FollowDao followDao = new FollowDao();

			if (followDao.isFollowing(
					connection,
					followerId,
					followingId)) {

				followDao.delete(
						connection,
						followerId,
						followingId);

			} else {

				Follow follow = new Follow();

				follow.setFollowerId(followerId);
				follow.setFollowingId(followingId);

				followDao.insert(connection, follow);
			}

			commit(connection);

		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}
}