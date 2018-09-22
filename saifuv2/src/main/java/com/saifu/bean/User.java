package com.saifu.bean;

import lombok.Data;

/**
 * APIから返却されるUser情報
 * 
 * @author meru_kuroneko
 *
 */
@Data
public class User {

	/** アクティブ */
	private int active;

	/** カバーURL */
	private String cover_image_url;

	/** 通貨 */
	private String currency_code;

	/** 入力日 */
	private int day;

	/** 入力日数 */
	private int day_count;

	/** ユーザーID */
	private String id;

	/** 入力レコード数 */
	private int input_count;

	/** ログイン */
	private String login;

	/** 入力月 */
	private int month;

	/** ユーザー名 */
	private String name;

	/** プロフィールアイコンURL */
	private String profile_image_url;

	/** プロフィール更新日 */
	private String profile_modified;

	/** 繰り返し回数 */
	private int repeat_count;

	/** 入力週 */
	private int week;

}
