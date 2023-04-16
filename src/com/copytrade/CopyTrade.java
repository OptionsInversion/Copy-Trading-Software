package com.copytrade;

import java.io.IOException;
import java.util.Scanner;
import org.json.JSONException;
import com.copytrade.util.TradeParams;
import com.copytrade.util.TradeConstants;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.*;

public class CopyTrade {

	public static void main(String[] args) throws JSONException, IOException, KiteException {

		KiteConnect kiteSdkFirstAccount = new KiteConnect(TradeConstants.apiKeyFirstAccount);
		kiteSdkFirstAccount.setUserId(TradeConstants.userIdFirstAccount);
		
		KiteConnect kiteSdkSecondAccount = new KiteConnect(TradeConstants.apiKeySecondAccount);
		kiteSdkSecondAccount.setUserId(TradeConstants.userIdSecondAccount);


		User userFirst = null;
		User userSecond = null;

		try {
			userFirst = kiteSdkFirstAccount.generateSession(TradeConstants.reqTokenFirstAccount, TradeConstants.secKeyFirstAccount);
			userSecond = kiteSdkSecondAccount.generateSession(TradeConstants.reqTokenSecondAccount, TradeConstants.secKeySecondAccount);
		} catch (IOException ex) {
			System.out.println("IO exception " + ex.getLocalizedMessage());
		} catch (KiteException ex) {
			System.out.println("Kite exception " + ex.getLocalizedMessage());
		} catch (JSONException ex) {
			System.out.println("JSONException " + ex.getLocalizedMessage());
		}

		kiteSdkFirstAccount.setAccessToken(userFirst.accessToken);
		kiteSdkFirstAccount.setPublicToken(userFirst.publicToken);
		
		kiteSdkSecondAccount.setAccessToken(userSecond.accessToken);
		kiteSdkSecondAccount.setPublicToken(userSecond.publicToken);

		Profile profileFirstUser = null;
		Profile profileSecondUser = null;
		
		try {
			profileFirstUser = kiteSdkFirstAccount.getProfile();
			profileSecondUser = kiteSdkSecondAccount.getProfile();
			
		} catch (KiteException e) {
			System.out.println("KiteException while profile " + e.getLocalizedMessage());
		} catch (IOException e) {
			System.out.println("IOException while profile " + e.getLocalizedMessage());
		}

		System.out.println("User info fetched successfully for all users! I'm ready to Take your Trades now :)");
		System.out.println("First account name = " + profileFirstUser.userName);
		System.out.println("Second account name = " + profileSecondUser.userName);

		FirstAccount firstAccount = new FirstAccount();
		firstAccount.setKiteSdk(kiteSdkFirstAccount);
		
		SecondAccount secondAccount = new SecondAccount();
		secondAccount.setKiteSdk(kiteSdkSecondAccount);

		System.out.println("Start Trade: ");
		System.out.println("---------------------------------------------------");
		
		Scanner scanner = new Scanner(System.in);
		String tradeType = "";

		while (true) {
			tradeType = scanner.nextLine();

			if (tradeType.equalsIgnoreCase("")) {
				continue;
			}

			String[] inputArray = tradeType.split(" ");
			
			boolean isBasketOrder = inputArray[2].contains("basket");
			if (isBasketOrder) {
				boolean isFullBasket = inputArray[2].contains("basket_full");
				handleBasketOrders(isFullBasket, inputArray, firstAccount, secondAccount);

			} else { // Other normal orders
				String instrumentType = inputArray[0];
				boolean isCallOption = inputArray[1].equals("call");
				String orderType = inputArray[2];
				String strikePrice = inputArray[3];
				
				Integer firstAccountLots = null;
				Integer secondAccountLots = null;
				
				if (inputArray.length > 3) {
					try {
						firstAccountLots = Integer.valueOf(inputArray[4]);
						secondAccountLots = Integer.valueOf(inputArray[5]);
					} catch (Exception e) {
						System.err.println("-------You must provide last 2 inputs in numbers or you entered wrong input" +e.getLocalizedMessage());
					}
				}

				TradeParams tradeParams = new TradeParams(instrumentType, isCallOption, orderType, strikePrice);
				System.out.println("Operation =  "+tradeParams.getTradeInfo());
				firstAccount.performBankNiftyTrade(tradeParams, firstAccountLots);
				secondAccount.performBankNiftyTrade(tradeParams, secondAccountLots);
			}

			System.out.println("-------------------- COMPLETED ---------------------");
			System.out.println("Next Adjustment: ");
		}

	}

	
	private static void handleBasketOrders (Boolean isFullBasket, String[] inputArray, FirstAccount firstAccount, 
			SecondAccount secondAccount) throws JSONException, IOException, KiteException {
		// Full basket order
		if (isFullBasket) {
			String instrumentType = inputArray[0];
			boolean isCallOption = inputArray[1].equals("call");
			String orderType = "buy_full";
			String strikePrice = inputArray[3];

			TradeParams tradeParams = new TradeParams(instrumentType, isCallOption, orderType, strikePrice);
			System.out.println("Operation =  "+tradeParams.getTradeInfo());
			firstAccount.performBankNiftyTrade(tradeParams, null);
			secondAccount.performBankNiftyTrade(tradeParams, null);
			
			System.out.println("-------Basket Mid Complete-------------");

			orderType = "sell_full";
			strikePrice = inputArray[4];

			tradeParams = new TradeParams(instrumentType, isCallOption, orderType, strikePrice);
			System.out.println("Operation =  "+tradeParams.getTradeInfo());
			firstAccount.performBankNiftyTrade(tradeParams, null);
			secondAccount.performBankNiftyTrade(tradeParams, null);

			// half basket order
		} else {
			String instrumentType = inputArray[0];
			boolean isCallOption = inputArray[1].equals("call");
			String orderType = "buy_half";
			String strikePrice = inputArray[3];

			TradeParams tradeParams = new TradeParams(instrumentType, isCallOption, orderType, strikePrice);
			System.out.println("Operation =  "+tradeParams.getTradeInfo());
			firstAccount.performBankNiftyTrade(tradeParams, null);
			secondAccount.performBankNiftyTrade(tradeParams, null);
			
			System.out.println("-------Basket Mid Complete-------------");

			orderType = "sell_half";
			strikePrice = inputArray[4];

			tradeParams = new TradeParams(instrumentType, isCallOption, orderType, strikePrice);
			System.out.println("Operation =  "+tradeParams.getTradeInfo());
			firstAccount.performBankNiftyTrade(tradeParams, null);
			secondAccount.performBankNiftyTrade(tradeParams, null);
		}
	}
}
