package com.copytrade.util;

/**
 * 
 * Defines the type of trade we take.
 * 
 * @param instrumentType: Bnf Or Fnf.
 * @param isCallOption:   Is it Call Or Put option type.
 * @param orderType:      Type of order: sell_full, sell_half, exit_full, exit_half.
 * @param strikePrice:    Strike price.
 * 
 * @author Options Inversion
 * 
 */
public class TradeParams {

	String instrumentType;
	boolean isCallOption;
	String orderType;
	String strikePrice;

	public TradeParams(String instrumentType, boolean isCallOption, String orderType, String strikePrice) {
		this.instrumentType = instrumentType;
		this.isCallOption = isCallOption;
		this.orderType = orderType;
		this.strikePrice = strikePrice;
	}

	public String getSinstrumentType() {
		return this.instrumentType;
	}

	public boolean getIsCallOption() {
		return this.isCallOption;
	}

	public String getOrderType() {
		return this.orderType;
	}

	public String getStrikePrice() {
		return this.strikePrice;
	}
	
	public String getTradeInfo() {
		return this.instrumentType +" "+this.isCallOption +" "+this.orderType +" "+this.strikePrice;
	}

}
