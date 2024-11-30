package com.creditWise.CardAnalytiX;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Engine
{
	public static void main(String[] args) throws IOException
	{

		String SoctiaBankData = "/home/prerakshah/git/CardAnalytiX/text_pages/scotiabank_cards.txt";
		String TDBankData = "/home/prerakshah/git/CardAnalytiX/text_pages/scotiabank_cards.txt";
		String RBCBankData = "/home/prerakshah/git/CardAnalytiX/text_pages/scotiabank_cards.txt";
		String CIBCBankData = "/home/prerakshah/git/CardAnalytiX/text_pages/scotiabank_cards.txt";
		String BankData[] = {SoctiaBankData, TDBankData};

		//		CardList has all the creditcard data. Use this list for data.
		ArrayList<CreditCard> cardList = cardObjectFiller(BankData);
		for(CreditCard creditCard : cardList)
		{
			System.out.println(creditCard.getCardType());
		}

	}

	private static ArrayList<CreditCard> cardObjectFiller(String[] BankDataPath) throws IOException
	{
		ArrayList<CreditCard> cardList = new ArrayList<CreditCard>();
		for(String bankPath : BankDataPath)
		{
			BufferedReader br = new BufferedReader(new FileReader(bankPath));
			String line = "";
			while((line = br.readLine()) != null)
			{
				String[] tsvElement = line.split("\t");
				cardList.add(new CreditCard(tsvElement[0], tsvElement[1], tsvElement[2], tsvElement[3], tsvElement[4]));
			}

		}

		return cardList;
	}

}
