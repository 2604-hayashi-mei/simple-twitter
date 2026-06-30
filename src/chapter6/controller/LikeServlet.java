package chapter6.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.LikeService;

@WebServlet(urlPatterns = { "/like" })
public class LikeServlet extends HttpServlet {
	Logger log = Logger.getLogger("twitter");

	public LikeServlet() {
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

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("loginUser");

		String messageId = request.getParameter("messageId");
		int tweetId = Integer.parseInt(messageId);

		new LikeService().toggle(user.getId(), tweetId);

		response.sendRedirect("./");
	}
}