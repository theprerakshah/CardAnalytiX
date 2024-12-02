package com.creditWise.Prerak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.creditWise.CardAnalytiX.CreditCard;

public class PageRanking
{
	public static Map<String, Integer> rankBanksByKeywordFrequency(List<CreditCard> creditCards, String keywords)
	{
		Map<String, Integer> bankScores = new HashMap<>();

		// Calculate scores for each bank
		for(CreditCard card : creditCards)
		{
			int score = calculateKeywordScore(card, keywords);

			// Add score to the bank's total
			bankScores.put(card.getBank(), bankScores.getOrDefault(card.getBank(), 0) + score);
		}

		// Sort banks by scores in descending order
		return sortByValue(bankScores);
	}

	private static int calculateKeywordScore(CreditCard card, String keyword)
	{
		int score = 0;

		// Combine relevant text fields for keyword matching
		String content = card.getCardName() + " " + card.getCardType() + " " + card.getAdditionalFeatures();

		// Count keyword occurrences
		score += countOccurrences(content.toLowerCase(), keyword.toLowerCase());

		return score;
	}

	private static int countOccurrences(String text, String keyword)
	{
		int count = 0;
		int index = text.indexOf(keyword);

		while(index != -1)
		{
			count++;
			index = text.indexOf(keyword, index + keyword.length());
		}

		return count;
	}

	private static Map<String, Integer> sortByValue(Map<String, Integer> map)
	{
		List<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
		entries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

		Map<String, Integer> sortedMap = new LinkedHashMap<>();
		for(Map.Entry<String, Integer> entry : entries)
		{
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public static Map<String, Integer> RankBankBasedOnWordFrequency(String wordSearch, ArrayList<CreditCard> cardList) throws IOException
	{
		String userInput = "master card";
		// Rank banks based on keyword frequency
		Map<String, Integer> rankedBanks = rankBanksByKeywordFrequency(cardList, userInput);
		return rankedBanks;
	}
	//	public static void main(String[] args) throws IOException
	//	{
	//		ArrayList<CreditCard> cardList = Engine.Engine1();
	//		String userInput = "master card";
	//		// Rank banks based on keyword frequency
	//		Map<String, Integer> rankedBanks = rankBanksByKeywordFrequency(cardList, userInput);
	//		
	//		// Display ranked banks
	//		System.out.println("Ranked the bank webpage according to");
	//		for(Map.Entry<String, Integer> entry : rankedBanks.entrySet())
	//		{
	//			System.out.println(entry.getKey() + " - Score: " + entry.getValue());
	//		}
	//	}
}
