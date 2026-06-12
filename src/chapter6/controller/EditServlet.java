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

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	private static final long serialVersionUID = 1L;

	/**
	 * つぶやき編集画面の表示（GET）
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		String idParam = request.getParameter("id");
		if (idParam == null || idParam.isEmpty()) {
			redirectWithErrorMessage(request, response);
			return;
		}
		try {
			//ID変換
			int tweetId = Integer.parseInt(idParam);
			//サービス呼び出し
			Message message = new MessageService().selectTweet(tweetId);

			if (message == null) {
				redirectWithErrorMessage(request, response);
				return;
			}

			request.setAttribute("message", message);
			request.getRequestDispatcher("/edit.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			redirectWithErrorMessage(request, response);
		}
	}

	/**
	 * つぶやき編集実行・保存（POST）
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		//文字化け防止
		request.setCharacterEncoding("UTF-8");

		String idParam = request.getParameter("id");
		String text = request.getParameter("text");

		if (idParam != null && text != null) {
			try {
				//文字列をint型に変換
				int tweetId = Integer.parseInt(idParam);

				List<String> errorMessages = new ArrayList<String>();

				if (isValid(text, errorMessages) == true) {
					Message message = new Message();
					message.setId(tweetId);
					message.setText(text);
					new MessageService().update(message);

					response.sendRedirect("./");
					return;
				} else {
					//テキストとID詰めなおし
					Message message = new Message();
					message.setId(tweetId);
					message.setText(text);

					request.setAttribute("message", message);
					request.setAttribute("errorMessages", errorMessages);

					request.getRequestDispatcher("/edit.jsp").forward(request, response);
					return;
				}
			} catch (NumberFormatException e) {
				redirectWithErrorMessage(request, response);
				return;
			}
		}
		response.sendRedirect("./");
	}

	private void redirectWithErrorMessage(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		java.util.List<String> errorMessages = new java.util.ArrayList<String>();
		errorMessages.add("不正なパラメータが入力されました");

		request.getSession().setAttribute("errorMessages", errorMessages);

		response.sendRedirect("./");
	}

	private boolean isValid(String text, List<String> errorMessages) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		if (StringUtils.isBlank(text)) {
			errorMessages.add("メッセージを入力してください");
		} else if (140 < text.length()) { // 140文字超えチェック
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