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
	
	public List<Comment> select(Connection connection) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());
		
		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM comments ORDER BY created_date ASC";
			ps = connection.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			List<Comment> comments = toComments(rs);
			return comments;
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
				comment.setMessageId(rs.getInt("message_id"));
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
}
