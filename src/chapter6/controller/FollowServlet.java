package chapter6.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chapter6.beans.User;
import chapter6.service.FollowService;

@WebServlet("/follow")
public class FollowServlet extends HttpServlet {

	@Override
	protected void doPost(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		User loginUser =
				(User) session.getAttribute("loginUser");

		int followingId =
				Integer.parseInt(
						request.getParameter("followingId"));

		new FollowService().toggle(
				loginUser.getId(),
				followingId);

		response.sendRedirect("./");
	}
}