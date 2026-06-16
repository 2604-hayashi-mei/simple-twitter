package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.Comment;
import chapter6.beans.UserMessage;
import chapter6.dao.CommentDao;
import chapter6.dao.UserCommentDao;
import chapter6.logging.InitApplication;

public class CommentService {
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");
	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public CommentService() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}
	
	/**
	 * 返信（コメント）をDBに登録する
	 * @param comment 登録するコメントデータが入った箱
	 */
	public void insert(Comment comment) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());
		
		Connection connection = null;
		try {
			connection = getConnection();
			new CommentDao().insert(connection, comment);
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
	
	/**
	 * 全ての返信（コメント）を条件なしで全件取得し、つぶやきデータからユーザー情報を紐付ける
	 * @param messages つぶやきの一覧（ユーザー情報入り）
	 * @return ユーザー名とアカウント名がセットされたコメントリスト
	 */
	public List<Comment> select(List<UserMessage> messages) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());
		
		Connection connection = null;
		try {
			connection = getConnection();
			
			List<Comment> comments = new UserCommentDao().select(connection);
			commit(connection);
			
			for (Comment comment : comments) {
				for (UserMessage message : messages) {
					if (comment.getUserId() == message.getUserId()) {
						comment.setName(message.getName());
						comment.setAccount(message.getAccount());
						break;
					}
				}
			}
			
			return comments;
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
