package com.saifu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.saifu.bean.OAuthProperty;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

@Configuration
public class BeanConfig {

	@Bean
	OAuthProperty oAuthProperty() {
		return new OAuthProperty();
	}

	// accessTokenがユーザ毎に違うためsession毎に持つ必要あり
	@Bean
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
	OAuthConsumer oAuthConsumer(OAuthProperty oAuthProperty) {
		return new CommonsHttpOAuthConsumer(oAuthProperty.getConsumerKey(),
				oAuthProperty.getConsumerSecret());
	}

	// accessTokenがユーザ毎に違うためsession毎に持つ必要あり
	@Bean
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
	OAuthProvider oAuthProvider(OAuthProperty oAuthProperty) {
		return new CommonsHttpOAuthProvider(oAuthProperty.getRequestTokenUrl(),
				oAuthProperty.getAccessTokenUrl(), oAuthProperty.getAuthorizeUrl());
	}

}
