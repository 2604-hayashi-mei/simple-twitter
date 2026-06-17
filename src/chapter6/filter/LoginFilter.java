package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// フィルターを適用する対象のページ
@WebFilter(urlPatterns = { "/setting", "/edit" })
public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		// 型変換
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		// セッションを取得する
		HttpSession session = httpRequest.getSession();

		// ログインチェック
		if (session.getAttribute("loginUser") != null) {

			// 【ログイン済み】本来の処理を実行
			chain.doFilter(request, response);
			return;
		} else {
			List<String> errorMessages = new ArrayList<String>();
			errorMessages.add("ログインをしてください。");

			httpRequest.setAttribute("errorMessages", errorMessages);

			httpRequest.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}

	@Override
	public void destroy() {
	}
}