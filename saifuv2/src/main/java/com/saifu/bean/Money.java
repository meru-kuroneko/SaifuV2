package com.saifu.bean;

import java.math.BigDecimal;

import lombok.Data;

/**
 * レシート入力データ
 * 
 * @author meru_kuroneko
 *
 */
@Data
public class Money {

	private String id;
	private String mode;
	private String user_id;
	private String date;
	private String category_id;
	private String genre_id;
	private String to_account_id;
	private String from_account_id;
	private BigDecimal amount;
	private String comment;
	private String active;
	private String name;
	private String receipt_id;
	private String place;
	private String place_uid;
	private String created;
	private String currency_code;

}
