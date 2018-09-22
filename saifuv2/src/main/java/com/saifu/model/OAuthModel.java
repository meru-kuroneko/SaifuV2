package com.saifu.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 認証に必要なトークン類を保持
 * @author kxn4t
 */
public class OAuthModel {
	@Getter
	private String accessToken;
	@Getter
	private String accessTokenSecret;
	@Setter
	private String URL;

	public OAuthModel(String accessToken, String accessTokenSecret){
		this.accessToken = accessToken;
		this.accessTokenSecret = accessTokenSecret;
	}

	/**
	 * セットするの忘れそうなので
	 * 空のまま呼び出そうとするとExceptionとしとく
	 * @return
	 */
	public String getURL() {
		if (this.URL.isEmpty()) {
			new IllegalAccessException("URL is Empty. Please Set URL.");
		}
		return this.URL;
	};
}
