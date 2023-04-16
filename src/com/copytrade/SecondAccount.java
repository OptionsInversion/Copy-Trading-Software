package com.copytrade;

import java.io.IOException;

import com.copytrade.util.TradeParams;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.InputException;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;

public class SecondAccount {

	KiteConnect kiteConnect;

	public void setKiteSdk(KiteConnect kiteSdk) {
		this.kiteConnect = kiteSdk;
	}

	public void performBankNiftyTrade(TradeParams tradeParams, Integer lots) throws KiteException, IOException {
		try {
			String tradeSymbol = "BANKNIFTY23APR"; // This needs to be changed on weekly basis
			String orderType = tradeParams.getOrderType();
			String strikePrice = tradeParams.getStrikePrice();

			if (tradeParams.getIsCallOption()) {
				tradeSymbol = tradeSymbol + strikePrice + "CE";
			} else {
				tradeSymbol = tradeSymbol + strikePrice + "PE";
			}

			handleBankNiftyOptions(tradeSymbol, orderType, lots);
			
		} catch (KiteException e) {
			System.err.println("-------KiteException Error for Second Account-------" +e.getLocalizedMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("-------IOException Error for Second Account-------" +e.getLocalizedMessage());
			e.printStackTrace();
		}
		
	}

	private void handleBankNiftyOptions(String tradeSymbol, String orderType, Integer lots)
			throws KiteException, IOException {

		if (orderType.contains("sell")) { // Perform sell operation
			OrderParams orderParams = new OrderParams();
			orderParams.orderType = Constants.ORDER_TYPE_MARKET;
			orderParams.tradingsymbol = tradeSymbol;
			orderParams.product = Constants.PRODUCT_NRML;
			orderParams.exchange = Constants.EXCHANGE_NFO;
			orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
			orderParams.tag = "myTag";
			Order order = null;
			
			if (orderType.contains("full")) {
				if (lots == null) {
					orderParams.quantity = 500;
				} else {
					orderParams.quantity = lots;
				}
			} else {
				if (lots == null) {
					orderParams.quantity = 250;
				} else {
					orderParams.quantity = lots;
				}
			}
			
			try {
		    	order = kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
		    	System.out.println("Exit success for Old " +tradeSymbol +" order Id: "+order.orderId);
		    } catch (InputException e) {
				System.err.println("-------InputException Error for Second Account -------" +e.message);
				e.printStackTrace();
			} catch (KiteException e) {
				System.err.println("-------KiteException Error for Second Account -------" +e.message);
				e.printStackTrace();
			} catch (Exception e) {
				System.err.println("-------Exception Error for Second Account -------" +e.getMessage());
				e.printStackTrace();
			}
			
		} else { // Perform exit operation
			OrderParams orderParams = new OrderParams();
			orderParams.orderType = Constants.ORDER_TYPE_MARKET;
			orderParams.tradingsymbol = tradeSymbol;
			orderParams.product = Constants.PRODUCT_NRML;
			orderParams.exchange = Constants.EXCHANGE_NFO;
			orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
			orderParams.tag = "myTag";
			Order order = null;

			if (orderType.contains("full")) {
				if (lots == null) {
					orderParams.quantity = 200;
				} else {
					orderParams.quantity = lots;
				}
			} else {
				if (lots == null) {
					orderParams.quantity = 100;
				} else {
					orderParams.quantity = lots;
				}
			}
			
			try {
		    	order = kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
		    	System.out.println("Exit success for Old " +tradeSymbol +" order Id: "+order.orderId);
		    } catch (InputException e) {
				System.err.println("-------InputException Error for Second Account -------" +e.message);
				e.printStackTrace();
			} catch (KiteException e) {
				System.err.println("-------KiteException Error for Second Account -------" +e.message);
				e.printStackTrace();
			} catch (Exception e) {
				System.err.println("-------Exception Error for Second Account -------" +e.getMessage());
				e.printStackTrace();
			}
		}
	}

}

