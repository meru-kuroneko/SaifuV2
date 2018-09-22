package com.saifu.service;

import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saifu.bean.OAuthProperty;
import com.saifu.model.OAuthModel;

import lombok.Getter;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;

/**
 * OAuth1.0a に関するサービス
 * Bean定義してあるconsumerとproviderを使用する
 *
 * 処理の流れ：
 * 1. 認証ページのURLを生成して遷移
 * 2. zaimよりoauth_verifierが返ってくるのでそれを用いてaccess_tokenを生成
 * 3. 生成した後、access_tokenとaccess_token_secretを保存
 * 4. 保存したaccess_tokenとaccess_token_secretを用いてリクエストに署名
 * 5. 署名したリクエストをZaimに投げる
 *
 * @author kxn4t
 */
@Service
public class OAuthService {

	@Autowired
	private OAuthProperty oAuthProperty;

	@Getter
	@Autowired
	private OAuthConsumer consumer;

	@Getter
	@Autowired
	private OAuthProvider provider;

	/**
	 * Zaimの認証ページのURLを生成する
	 * @return 認証ページのURL
	 */
	public String createURLtoRetrieveToken() throws Exception {

		return provider.retrieveRequestToken(consumer, oAuthProperty.getCallback());
	}

	/**
	 * Zaimより返却されたoauthVerifierを用いてアクセストークンを生成する
	 * @param oauthVerifier
	 * @return boolean
	 */
	public boolean initAccessToken(String oauthVerifier) {
		try {
			// accessTokenとaccessTokenSecretを生成する
			provider.retrieveAccessToken(consumer, oauthVerifier);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * リクエストに署名する
	 * @param httpRequest
	 * @return
	 * @throws Exception 署名できない場合
	 */
	public HttpRequest signRequest(HttpRequest httpRequest, OAuthModel oAuthModel) throws Exception {

		// トークンをセット
		consumer.setTokenWithSecret(oAuthModel.getAccessToken(), oAuthModel.getAccessTokenSecret());
		// 署名
		consumer.sign(httpRequest);

		return httpRequest;
	}

}
