package com.creditWise.Prerak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import com.creditWise.CardAnalytiX.CreditCard;

public class WordSearcher
{
	//		public static String	numberRegex				= "(^\\n|[\\+]|^([\\$\\%]{0,1}[0-9.\\\\]{0,9}[\\%]{0,1})$|^[sbm]{1}$)";
	public static String	numberRegex				= "(^\\d+(\\.\\d+)?[%\\$]?|[%\\$]?\\d+(\\.\\d+)?[%\\$]?$)";
	public static String[]	stopWordArray			= {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself",
			"she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is",
			"are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
			"of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off",
			"over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no",
			"nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

	public static String	specialCharacterRegex	= "([\\®\\\"\\*\\|\\+\\‡\\◊\\™\\(\\)])";

	public static HashMap<String, HashMap<String, ArrayList<String>>> invertedIndexing(ArrayList<CreditCard> cardList, String word) throws IOException
	{
		HashMap<String, HashMap<String, ArrayList<String>>> dataForFrequency = new HashMap<String, HashMap<String, ArrayList<String>>>();
		HashMap<String, ArrayList<String>> tempmap = new HashMap<String, ArrayList<String>>();
		//		ArrayList<CreditCard> cardList = Engine.Engine1();
		Set<Integer> resultSet = new HashSet<>();
		//		ArrayList<ArrayList<String>> tempList = new ArrayList<ArrayList<String>>();

		ArrayList<String> RBCBank = dataScrapping(cardList, "RBC");
		ArrayList<String> CIBCBank = dataScrapping(cardList, "CIBC");
		ArrayList<String> TDBank = dataScrapping(cardList, "TD Bank");
		ArrayList<String> SOCTIABank = dataScrapping(cardList, "Scotia Bank");
		HashMap<Integer, String> bankIdMap = bankToIdMapping();
		HashMap<String, ArrayList<String>> bankNameToListMap = bankNameToListMapping(RBCBank, CIBCBank, TDBank, SOCTIABank);

		TrieNode root = new TrieNode();
		IndexFillerAndReverse index = new IndexFillerAndReverse();
		index.assignWordToDocument(CIBCBank, 1, root);
		index.assignWordToDocument(SOCTIABank, 2, root);
		index.assignWordToDocument(RBCBank, 3, root);
		index.assignWordToDocument(TDBank, 4, root);

		resultSet = wordSearcher(word, index, root, bankIdMap);
		if(resultSet != null)
		{
			for(Integer id : resultSet)
			{
				tempmap.put(bankIdMap.get(id), bankNameToListMap.get(bankIdMap.get(id)));
			}
			dataForFrequency.put(word, tempmap);
			return dataForFrequency;
		}
		else
		{
			//			System.out.println("Teh Word \" " + word + " \"  not found in any of the webpages!");
			return null;
		}

	}

	private static ArrayList<String> wordSeparator(ArrayList<String> wordList, String separator)
	{
		ArrayList<String> temp = new ArrayList<String>();
		if(wordList != null)
		{
			for(String str : wordList)
			{
				if(str.toLowerCase().contains("state") && separator.equals("-"))
				{
					str = str.split("/?")[0];
				}
				temp.addAll(new ArrayList<String>(Arrays.asList(str.split(separator))));
			}
		}
		return temp;
	}

	private static HashMap<Integer, String> bankToIdMapping()
	{
		HashMap<Integer, String> bankIdMap = new HashMap<Integer, String>();
		HashMap<String, ArrayList<String>> bankNameToListMap = new HashMap<String, ArrayList<String>>();

		bankIdMap.put(1, "CIBC_BANK_DATA");
		bankIdMap.put(2, "SOCTIA_BANK_DATA");
		bankIdMap.put(3, "RBC_BANK_DATA");
		bankIdMap.put(4, "TD_BANK_DATA");
		return bankIdMap;

	}

	private static HashMap<String, ArrayList<String>> bankNameToListMapping(ArrayList<String> RBCBank, ArrayList<String> CIBCBank, ArrayList<String> TDBank, ArrayList<String> SOCTIABank)
	{
		HashMap<String, ArrayList<String>> bankNameToListMap = new HashMap<String, ArrayList<String>>();
		bankNameToListMap.put("CIBC_BANK_DATA", CIBCBank);
		bankNameToListMap.put("SOCTIA_BANK_DATA", SOCTIABank);
		bankNameToListMap.put("RBC_BANK_DATA", RBCBank);
		bankNameToListMap.put("TD_BANK_DATA", TDBank);
		return bankNameToListMap;

	}

	private static ArrayList<String> dataScrapping(ArrayList<CreditCard> creditCardList, String bankName) throws IOException
	{

		ArrayList<String> wordList = new ArrayList<String>();
		ArrayList<String> lineList = new ArrayList<String>();

		for(CreditCard creditCard : creditCardList)
		{
			if(creditCard.getBank().equalsIgnoreCase(bankName))
			{
				lineList.add(creditCard.getCardName());
				lineList.add(creditCard.getCardType());
				lineList.add(creditCard.getAdditionalFeatures());

			}
		}
		ArrayList<String> commaSeprated = wordSeparator(lineList, ",");
		ArrayList<String> slashSeprated = wordSeparator(commaSeprated, "/");
		ArrayList<String> hypenSeprated = wordSeparator(slashSeprated, "-");
		ArrayList<String> spaceSeprated = wordSeparator(hypenSeprated, " ");

		wordList = preProcess(spaceSeprated);
		//		wordList.removeAll(Arrays.asList(stopWordArray));
		Set<String> stopWords1 = new HashSet<>(Arrays.asList(stopWordArray));
		wordList.removeIf(stopWords1::contains);

		wordList.removeIf(item -> item == null || item.trim().isEmpty());
		//		for(int i = 0; i < wordList.size(); i++)
		//		{
		//			System.out.println("Index\t" + i + "\tword\t" + wordList.get(i));
		//		}
		return wordList;
	}

	private static ArrayList<String> preProcess(ArrayList<String> wordList)
	{
		Pattern pattern = Pattern.compile(numberRegex);
		ArrayList<String> processedWordsList = new ArrayList<>();

		for(int i = 0; i < wordList.size(); i++)
		{
			String word = wordList.get(i).trim();
			if(pattern.matcher(word).matches())
			{
				//				System.out.println("----------------" + word + "-----------------");
				continue;
			}

			String processedWord = word.replaceAll(specialCharacterRegex, "").trim();
			if(!processedWord.trim().isEmpty())
			{
				processedWordsList.add(processedWord);
			}

		}

		return processedWordsList;
	}

	private static Set<Integer> wordSearcher(String wordToBeSearched, IndexFillerAndReverse index, TrieNode root, HashMap<Integer, String> bankIdMap)
	{
		Set<Integer> results = index.wordDocumnetFinder(wordToBeSearched, root);
		if(results.isEmpty())
		{
			System.out.println("The word \"" + wordToBeSearched + "\" is not found in any of Bank Website");
			return null;
		}
		else
		{
			for(Integer id : results)
				System.out.println("The word \"" + wordToBeSearched + "\"is found in " + bankIdMap.get(id) + " Bank Website");
		}
		return results;
	}

}
