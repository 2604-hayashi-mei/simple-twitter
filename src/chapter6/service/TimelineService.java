package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.util.List;

import chapter6.beans.UserMessage;
import chapter6.dao.UserMessageDao;

public class TimelineService {

	public List<UserMessage> select(int userId) {

		Connection connection = null;

		try {

			connection = getConnection();

			List<UserMessage> messages =
					new UserMessageDao().selectTimeline(connection, userId);

			commit(connection);

			return messages;

		} finally {
			close(connection);
		}
	}
}
