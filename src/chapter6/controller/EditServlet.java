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
		//IDキャッチ
		String messageIdParam = request.getParameter("id");
		if (messageIdParam == null || messageIdParam.isEmpty() || !messageIdParam.matches("^[0-9]+$")) {
			List<String> errorMessages = new ArrayList<String>();
			errorMessages.add("不正なパラメータが入力されました");

			HttpSession session = request.getSession();
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}
		//ID変換
		int tweetId = Integer.parseInt(messageIdParam);
		//サービス呼び出し
		Message message = new MessageService().select(tweetId);

		if (message == null) {
			List<String> errorMessages = new ArrayList<String>();
			errorMessages.add("不正なパラメータが入力されました");
			request.getSession().setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		request.setAttribute("message", message);
		request.getRequestDispatcher("/edit.jsp").forward(request, response);

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

		String messageIdParam = request.getParameter("id");
		String text = request.getParameter("text");

		if (messageIdParam != null && text != null) {

			//文字列をint型に変換
			int tweetId = Integer.parseInt(messageIdParam);

			List<String> errorMessages = new ArrayList<String>();

			if (!isValid(text, errorMessages)) {
				//テキストとID詰めなおし
				Message message = new Message();
				message.setId(tweetId);
				message.setText(text);

				request.setAttribute("message", message);
				request.setAttribute("errorMessages", errorMessages);

				request.getRequestDispatcher("/edit.jsp").forward(request, response);
				return;
			}
			Message message = new Message();
			message.setId(tweetId);
			message.setText(text);
			new MessageService().update(message);

			response.sendRedirect("./");
		}
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