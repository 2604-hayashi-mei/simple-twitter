package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Comment;
import chapter6.service.CommentService;

@WebServlet("/editComment")
public class EditCommentServlet extends HttpServlet {
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	private static final long serialVersionUID = 1L;

	/**
	 * 返信編集画面の表示（GET）
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<String> errorMessages = new ArrayList<String>();

		//IDキャッチ
		String commentId = request.getParameter("id");
		if (StringUtils.isBlank(commentId) || !commentId.matches("^[0-9]+$")) {
			errorMessages.add("不正なパラメータが入力されました");

			HttpSession session = request.getSession();
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}
		//ID変換
		int commentIdInt = Integer.parseInt(commentId);
		//サービス呼び出し
		Comment comment = new CommentService().select(commentIdInt);

		if (comment == null) {
			errorMessages.add("不正なパラメータが入力されました");
			request.getSession().setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		request.setAttribute("comment", comment);
		request.getRequestDispatcher("/editComment.jsp").forward(request, response);

	}

	/**
	 * 返信編集実行・保存（POST）
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		String commentId = request.getParameter("id");
		String text = request.getParameter("text");

		//文字列をint型に変換
		int commentIdInt = Integer.parseInt(commentId);
		List<String> errorMessages = new ArrayList<String>();

		if (!isValid(text, errorMessages)) {
			
			Comment comment = new Comment();
			comment.setId(Integer.parseInt(commentId));
			comment.setText(text);

			request.setAttribute("comment", comment);
			request.setAttribute("errorMessages", errorMessages);

			request.getRequestDispatcher("/editComment.jsp").forward(request, response);
			return;
		}
		Comment comment = new Comment();
		comment.setId(commentIdInt);
		comment.setText(text);
		new CommentService().update(comment);

		response.sendRedirect("./");
	}

	private boolean isValid(String text, List<String> errorMessages) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		if (StringUtils.isBlank(text)) {
			errorMessages.add("メッセージを入力してください");
			// 140文字超えチェック
		} else if (140 < text.length()) {
			errorMessages.add("140文字以下で入力してください");
		}

		// エラーが1つでも入っていたら不合格(false)を返す
		if (errorMessages.size() != 0) {
			return false;
		}
		// エラーがなければ合格(true)を返す
		return true;
	}
}