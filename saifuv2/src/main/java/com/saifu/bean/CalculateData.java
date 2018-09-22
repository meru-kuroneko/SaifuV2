package com.saifu.bean;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 金額計算結果
 * 
 * @author meru_kuroneko
 *
 */
@Data
public class CalculateData {

	/** 小計 */
	private BigDecimal sum;

	/** 条件とするメモ欄文字列 */
	private String condition;

	/** 金額差分 */
	private BigDecimal difference;

}
