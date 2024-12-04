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

		String ScotiaBankData = "/scotiabank_cards.txt";
		String TDBankData = "/td_cards.txt";
		String CIBCBankData = "/cibc_cards.txt";
		String RBCBankData = "/rbc_cards.txt";
		bankToPath.put("TD Bank", TDBankData);
		bankToPath.put("Scotia Bank", ScotiaBankData);
		bankToPath.put("CIBC", CIBCBankData);
		bankToPath.put("RBC", RBCBankData);

		ArrayList<CreditCard> cardList = cardObjectFiller(bankToPath);
		return cardList;
	}

	private static ArrayList<CreditCard> cardObjectFiller(HashMap<String, String> BankDataPath) throws IOException
	{
		ArrayList<CreditCard> cardList = new ArrayList<CreditCard>();

		for(Map.Entry<String, String> mapi : BankDataPath.entrySet())
		{
			boolean firstTimer = false;
			InputStream inputStream = Engine.class.getResourceAsStream(mapi.getValue());
			if(inputStream == null)
			{
				System.out.println("there is null in inputstream");
				break;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			//			BufferedReader br = new BufferedReader(new FileReader(set.getValue()));
			String line = "";
			//			String bankName = bankPath.split("/")[6].split("_")[0];
			while((line = br.readLine()) != null)
			{
				if(firstTimer)
				{
					String[] tsvElement = line.split("\t");
					cardList.add(new CreditCard(tsvElement[0], tsvElement[1], tsvElement[2], tsvElement[3], tsvElement[4], mapi.getKey()));

				}
				firstTimer = true;
			}
		}

		return cardList;
	}

}
