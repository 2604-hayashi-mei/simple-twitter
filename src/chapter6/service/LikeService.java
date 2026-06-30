package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.Like;
import chapter6.dao.LikeDao;
import chapter6.logging.InitApplication;

public class LikeService {
	Logger log = Logger.getLogger("twitter");

	public LikeService() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	public void toggle(int userId, int messageId) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			connection = getConnection();
			LikeDao likeDao = new LikeDao();

			if (likeDao.isLiked(connection, userId, messageId)) {
				likeDao.delete(connection, userId, messageId);
			} else {
				Like like = new Like();
				like.setUserId(userId);
				like.setMessageId(messageId);
				likeDao.insert(connection, like);
			}

			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} finally {
			close(connection);
		}
	}
}