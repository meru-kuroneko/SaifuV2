package com.saifu.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saifu.bean.User;
import com.saifu.bean.UserSet;
import com.saifu.model.OAuthModel;

/**
 * Zaimユーザ情報に関するサービス
 * 
 * @author kxn4t
 */
@Service
public class UserService {

	@Autowired
	ApplicationContext context;

	@Autowired
	HttpClientService httpClientService;

	@Value("${zaim.api.user}")
	private String userVerifyURL;

	/** ユーザー情報 */
	private User userInfo;

	/**
	 * ユーザ情報をJSON形式で取得
	 * 
	 * @param oAuthModel
	 * @return ユーザ情報(JSON)
	 * @throws Exception
	 */
	public String getUserVerify(OAuthModel oAuthModel) throws Exception {
		oAuthModel.setURL(userVerifyURL);
		return httpClientService.getRequestToZaim(oAuthModel);
	}

	/**
	 * ユーザー情報を返却
	 * 
	 * @param oAuthModel
	 * @return ユーザー情報(User)
	 * @throws Exception
	 */
	public User getUserData(OAuthModel oAuthModel) throws Exception {

		if (userInfo != null) {
			return userInfo;
		}

		String userJson = getUserVerify(oAuthModel);
		UserSet userset = new UserSet();
		userInfo = new User();
		ObjectMapper mapper = new ObjectMapper();

		// ユーザー情報の設定
		userset = mapper.readValue(userJson, UserSet.class);
		userInfo = userset.getMe();

		return userInfo;
	}

}
