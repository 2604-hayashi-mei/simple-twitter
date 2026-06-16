package chapter6.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// フィルターを適用する対象のページ
@WebFilter({ "/setting", "/edit" })
public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		// 型変換
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// セッションを取得する
		HttpSession session = httpRequest.getSession(false);
		try {
		// ログインチェック
		if (session.getAttribute("loginUser").toString().length() > 0) {
			
			// 【ログイン済み】本来の処理を実行
			chain.doFilter(request, response);
			return; 
		}
		} catch(NullPointerException e) {
			
		}

		// 【未ログイン】
		HttpSession messageSession = httpRequest.getSession(true);
		messageSession.setAttribute("filterError", "ログインをしてください。");

		// ログインページへリダイレクト
		httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
	}

	@Override
	public void destroy() {}
}