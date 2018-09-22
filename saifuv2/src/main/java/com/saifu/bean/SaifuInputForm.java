package com.saifu.bean;

import java.math.BigDecimal;
import java.util.List;

import com.saifu.bean.CalculateData;
import lombok.Data;

/**
 * Saifu.html Form情報
 * 
 * @author meru_kuroneko
 *
 */
@Data
public class SaifuInputForm {

	/** 最終精算日 */
	private String lastCalcDate;

	/** メモ欄 */
	private String[] key;

	/** 合計 */
	private BigDecimal sum;

	/** 条件ごとの計算結果 */
	private List<CalculateData> calculateDataList;

	/** メモ欄空白レコード */
	private List<Money> emptyList;
}
