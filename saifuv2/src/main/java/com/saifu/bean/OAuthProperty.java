package com.saifu.bean;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;

/**
 * OAuth認証に必要な情報をプロパティファイルより取得し保持
 * @author kxn4t
 */
public class OAuthProperty {

	@Getter
	@Value("${saifu.consumerKey}")
	private String consumerKey;

	@Getter
	@Value("${saifu.consumerSecret}")
	private String consumerSecret;

	@Getter
	@Value("${saifu.callback}")
	private String callback;

	@Getter
	@Value("${zaim.requestTokenUrl}")
	private String requestTokenUrl;

	@Getter
	@Value("${zaim.authorizeUrl}")
	private String authorizeUrl;

	@Getter
	@Value("${zaim.accessTokenUrl}")
	private String accessTokenUrl;

}
