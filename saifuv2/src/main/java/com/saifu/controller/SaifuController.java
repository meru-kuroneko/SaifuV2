package com.saifu.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.saifu.bean.SaifuInputForm;
import com.saifu.model.OAuthModel;
import com.saifu.service.MoneyService;
import com.saifu.service.UserService;

/**
 * 認証済みページのつもり
 * 
 * @author kxn4t
 */
@Controller
public class SaifuController {

	@Autowired
	UserService userService;
	@Autowired
	MoneyService moneyService;

	@RequestMapping("/saifu")
	public String saifuTop(@CookieValue(value = "accessToken") String accessToken,
			@CookieValue(value = "accessTokenSecret") String accessTokenSecret, Model model) throws Exception {

		OAuthModel oAuthModel = new OAuthModel(accessToken, accessTokenSecret);

		// ユーザー情報
		model.addAttribute("zaimUser", userService.getUserData(oAuthModel));
		// Form情報（レシート入力情報なし）
		SaifuInputForm saifuInputForm = new SaifuInputForm();
		model.addAttribute("saifuInputForm", saifuInputForm);

		return "saifu";
	}

	/**
	 * 分割精算呼び出し
	 * 
	 * @param accessToken
	 * @param accessTokenSecret
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saifu/callCalculate")
	public String callCalculate(@CookieValue(value = "accessToken") String accessToken,
			@CookieValue(value = "accessTokenSecret") String accessTokenSecret, Model model,
			@ModelAttribute SaifuInputForm saifuInputForm) throws Exception {

		OAuthModel oAuthModel = new OAuthModel(accessToken, accessTokenSecret);

		// ユーザー情報
		model.addAttribute("zaimUser", userService.getUserData(oAuthModel));
		// Form情報（レシート入力情報あり）
		model.addAttribute("saifuInputForm", moneyService.doCalculate(oAuthModel, saifuInputForm));

		return "saifu";
	}
}
