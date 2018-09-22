package com.saifu.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.saifu.service.OAuthService;

/**
 * zaimの認証など
 * /index→zaimの認証ページ→/authenticated→/saifu
 * @author kxn4t
 */
@Controller
public class IndexController {

	@Autowired
	OAuthService oauthService;

	/**
	 * 認証済みかどうかはfilterでチェックする
	 * 問題なければ認証済みページへリダイレクト
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/")
	public String index() {
		return "redirect:/saifu";
	}

	/**
	 * zaimで認証後のコールバックでのアクセス
	 * cookieにトークンを詰めてから認証済みページへリダイレクト
	 * @param oauthToken zaimより返却
	 * @param oauthVerifier zaimより返却
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/authenticated")
	public String authenticated(@RequestParam(value = "oauth_token") String oauthToken,
			@RequestParam(value = "oauth_verifier") String oauthVerifier, HttpServletResponse response)
			throws Exception {

		// アクセストークンを生成
		oauthService.initAccessToken(oauthVerifier);

		// cookieに生成したアクセストークンを詰める
		Cookie accessTokenCookie = new Cookie("accessToken", oauthService.getConsumer().getToken());
		Cookie accessTokenSecretCookie = new Cookie("accessTokenSecret", oauthService.getConsumer().getTokenSecret());
		response.addCookie(accessTokenCookie);
		response.addCookie(accessTokenSecretCookie);

		return "redirect:/saifu";
	}

	/**
	 * zaimで認証する
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/login")
	public String login() throws Exception {

		// 認証用のURLを生成して遷移
		return "redirect:" + oauthService.createURLtoRetrieveToken();
	}

}
