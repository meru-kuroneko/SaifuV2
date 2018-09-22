package com.saifu.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saifu.bean.CalculateData;
import com.saifu.bean.Money;
import com.saifu.bean.MoneySet;
import com.saifu.bean.SaifuInputForm;
import com.saifu.model.OAuthModel;

/**
 * Zaimレシート入力情報に関するサービス
 * 
 * @author meru_kuroneko
 */
@Service
public class MoneyService {

	@Autowired
	HttpClientService httpClientService;

	@Value("${zaim.api.money}")
	private String moneyGetURL;

	/** 最終時刻（頻度が高くないので、とりあえず固定値） */
	private final String HHMMSS = " 23:59:59";

	/**
	 * Moneyレコードの取得
	 * 
	 * @param oAuthModel
	 * @return レシート入力情報(JSON)
	 * @throws Exception
	 */
	public List<Money> getMoney(OAuthModel oAuthModel) throws Exception {

		oAuthModel.setURL(moneyGetURL);
		String moneyJson = httpClientService.getRequestToZaim(oAuthModel);

		MoneySet moneyset = new MoneySet();
		List<Money> moneyRec = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();

		moneyset = mapper.readValue(moneyJson, MoneySet.class);
		moneyRec = moneyset.getMoney();

		return moneyRec;
	}

	/**
	 * 分割精算処理
	 * 
	 * @param oAuthModel
	 * @param saifuInputForm
	 * @return レシート入力情報(JSON)
	 * @throws Exception
	 */
	public SaifuInputForm doCalculate(OAuthModel oAuthModel, SaifuInputForm saifuInputForm) throws Exception {
		oAuthModel.setURL(moneyGetURL);

		// Moneyレコードの取得
		List<Money> moneyRec = getMoney(oAuthModel);

		// メモ欄入力数に応じて計算結果オブジェクトを生成
		List<CalculateData> calcDataList = new ArrayList<>();
		for (String memoItem : saifuInputForm.getKey()) {
			if (!StringUtils.isEmpty(memoItem)) {
				CalculateData calc = new CalculateData();
				calc.setCondition(memoItem);
				calc.setDifference(new BigDecimal(0));
				calc.setSum(new BigDecimal(0));
				calcDataList.add(calc);
			}
		}

		// 最終精算日のデフォルト値設定対象外
		String lastDate = "2017-07-16 23:59:59";
		if (judgeDateFormat(saifuInputForm.getLastCalcDate() + HHMMSS)) {
			lastDate = saifuInputForm.getLastCalcDate() + HHMMSS;
		}

		// メモ欄空白レコード
		List<Money> memoEmpty = new ArrayList<Money>();

		BigDecimal sum = BigDecimal.valueOf(0);
		// 計算対象とするMoneyレコードの絞り込み
		for (Money money : moneyRec) {

			// 古いデータは計算対象外
			if (isOldDate(lastDate, money.getCreated())) {
				continue;
			}

			boolean addFlag = false;
			for (CalculateData calRec : calcDataList) {
				if (calRec.getCondition().equals(money.getComment()) && !addFlag) {
					// 条件に合致したら小計に加算する
					calRec.setSum(calRec.getSum().add(money.getAmount()));
					addFlag = true;
				}
			}

			// メモ欄がいずれにも合致しなかった場合
			if (!addFlag) {
				memoEmpty.add(money);
			}
			// 合計を加算
			sum = sum.add(money.getAmount());
		}

		// 差分計算
		BigDecimal avr = pullAverage(calcDataList);
		for (CalculateData calcRec : calcDataList) {
			calcRec.setDifference(avr.subtract(calcRec.getSum()));
		}

		saifuInputForm.setSum(sum);
		saifuInputForm.setEmptyList(memoEmpty);
		saifuInputForm.setCalculateDataList(calcDataList);

		return saifuInputForm;
	}

	/**
	 * 日付フォーマットの整合性チェック 比較対象 ： yyyy-MM-dd HH:mm:ss
	 * 
	 * @param createDate
	 *            データの登録日
	 * @return true = 正しいフォーマット
	 */
	public boolean judgeDateFormat(String date) {
		SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			d1.parse(date);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * 前回の登録日と比較し、古いかどうかをチェックする
	 * 
	 * @param createDate
	 *            データの登録日
	 * @return true = 新規データ・計算する,false = 登録日以前・計算しない
	 */
	private boolean isOldDate(String lastDateString, String createDate) {

		boolean result = true;
		SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date lastDate = null;
		try {
			lastDate = d1.parse(lastDateString);
			Date editCreateDate = d1.parse(createDate);

			if (lastDate.before(editCreateDate)) {
				return false;
			}

		} catch (ParseException e1) {
			e1.printStackTrace();
			return result;
		}

		return result;
	}

	/**
	 * 平均を算出する（条件に合わないデータは平均値から除外される）
	 * 
	 * @param calculateDataList
	 * @return 平均値
	 */
	private BigDecimal pullAverage(List<CalculateData> calculateDataList) {

		BigDecimal sum = BigDecimal.valueOf(0);

		for (CalculateData condition : calculateDataList) {
			sum = sum.add(condition.getSum());
		}

		return sum.divide(BigDecimal.valueOf(calculateDataList.size()));
	}
}
