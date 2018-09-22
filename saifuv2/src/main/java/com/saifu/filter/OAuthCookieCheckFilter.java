package com.saifu.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * OAuth認証に必要なCookieがあるかチェックする
 * 認証もどきのFilter
 * なければ認証画面にリダイレクト
 * もし別filterを追加する場合は@Componentを外してBean定義する必要あり
 * @author kxn4t
 */
@Component
public class OAuthCookieCheckFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		//-------------------------------------------------------
		// 認証途中かどうかチェック
		//-------------------------------------------------------
		// アクセス先がloginであれば認証途中なので弾く
		if(req.getRequestURI().equals("/login")) {
			chain.doFilter(request, response);
			return;
		};

		// パラメータにtokenがあれば認証途中なので弾く
		if (!StringUtils.isEmpty(request.getParameter("oauth_token"))) {
			chain.doFilter(request, response);
			return;
		}

		//-------------------------------------------------------
		// Cookieチェック
		//-------------------------------------------------------
		// もしCookieがnullだったら認証ないのでリダイレクト
		Cookie[] cookies = req.getCookies();
		if (cookies == null) {
			((HttpServletResponse) response).sendRedirect("/login");
			return;
		}

		// Cookieにtokenがあるかチェック、あれば弾く
		for (Cookie cookie : cookies) {
			if ("accessToken".equals(cookie.getName())) {
				chain.doFilter(request, response);
				return;
			}
		}

		// 全部スルーしたら認証されていないのでリダイレクトして認証させる
		((HttpServletResponse) response).sendRedirect("/login");
		return;
	}

	@Override
	public void destroy() {
	}

}
