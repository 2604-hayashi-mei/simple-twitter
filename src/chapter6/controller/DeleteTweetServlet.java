package chapter6.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/deleteMessage" })
public class DeleteTweetServlet extends HttpServlet {
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	public DeleteTweetServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		String idParam = request.getParameter("id");

		if (idParam != null) {
			try {
				//int型に変換
				int tweetId = Integer.parseInt(idParam);
				//サービス呼び出して命令
				new MessageService().delete(tweetId);
			} catch (NumberFormatException e) {
				log.warning("不正なつぶやきIDが送信されました" + idParam);
			}

		}
		//処理が終わったらトップへ
		response.sendRedirect("./");
	}
}
