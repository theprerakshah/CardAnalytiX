package com.creditWise.CardAnalytiX;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Engine
{
	public static ArrayList<CreditCard> Engine1() throws IOException
	{
		HashMap<String, String> bankToPath = new HashMap<String, String>();

		String SoctiaBankData = "/home/prerakshah/git/CardAnalytiX/text_pages/scotiabank_cards.txt";
		String TDBankData = "/home/prerakshah/git/CardAnalytiX/text_pages/td_cards.txt";
		String RBCBankData = "/home/prerakshah/git/CardAnalytiX/src/main/resources/rbc_cards.txt";
		String CIBCBankData = "/CardAnalytiX/src/main/resources/cibc_cards.txt";
		bankToPath.put("TD Bank", TDBankData);
		bankToPath.put("Soctia Bank", SoctiaBankData);
		//		bankToPath.put("CIBC", CIBCBankData);
		//		bankToPath.put("RBC", RBCBankData);

		ArrayList<CreditCard> cardList = cardObjectFiller(bankToPath);
		return cardList;
	}

	private static ArrayList<CreditCard> cardObjectFiller(HashMap<String, String> BankDataPath) throws IOException
	{
		ArrayList<CreditCard> cardList = new ArrayList<CreditCard>();

		for(Map.Entry<String, String> set : BankDataPath.entrySet())
		{
			//			InputStream inputStream = Engine.class.getClassLoader().getResourceAsStream(set.getValue());
			BufferedReader br = new BufferedReader(new FileReader(set.getValue()));
			String line = "";
			//			String bankName = bankPath.split("/")[6].split("_")[0];
			while((line = br.readLine()) != null)
			{
				String[] tsvElement = line.split("\t");
				cardList.add(new CreditCard(tsvElement[0], tsvElement[1], tsvElement[2], tsvElement[3], tsvElement[4], set.getKey()));
			}
		}

		return cardList;
	}

}
