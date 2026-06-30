package chapter6.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.User;
import chapter6.beans.UserMessage;
import chapter6.service.TimelineService;

@WebServlet("/timeline")
public class TimelineServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		User loginUser =
				(User) request.getSession()
				.getAttribute("loginUser");

		if(loginUser == null) {
			response.sendRedirect("login");
			return;
		}

		List<UserMessage> messages =
				new TimelineService().select(loginUser.getId());

		request.setAttribute("messages", messages);

		request.getRequestDispatcher("/timeline.jsp")
				.forward(request, response);
	}
}