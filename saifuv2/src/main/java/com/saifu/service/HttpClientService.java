package com.saifu.service;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saifu.handler.APIResponseHandler;
import com.saifu.model.OAuthModel;

/**
 * Zaimにリクエストを投げるサービス
 * https://hc.apache.org
 * https://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html#d5e43
 * @author kxn4t
 */
@Service
public class HttpClientService {

	@Autowired
	OAuthService oAuthService;
	// ステータスコードを見て、エラーとするHandler
	ResponseHandler<String> responseHandler = new APIResponseHandler();

	/**
	 * ZaimにGETリクエストを投げる
	 * @param oAuthModel
	 * @return JSON
	 * @throws Exception
	 */
	public String getRequestToZaim(OAuthModel oAuthModel) throws Exception {

		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {
			// リクエストに署名する
			HttpGet httpget = new HttpGet(oAuthModel.getURL());
			oAuthService.signRequest(httpget, oAuthModel);

			// 取得
			String responseBody = httpclient.execute(httpget, responseHandler);
			return responseBody;

		} finally {
			httpclient.close();
		}
	}

}
