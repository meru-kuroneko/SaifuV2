package com.saifu.bean;

import java.util.List;

import lombok.Data;

@Data
public class MoneySet {

	private List<Money> money;

	private String requested;
}
