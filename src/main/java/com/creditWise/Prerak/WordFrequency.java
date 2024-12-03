
package com.creditWise.Prerak;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class WordFrequency
{
	public static String	numberRegex				= "(^\\n|[\\+]|^([\\$\\%]{0,1}[0-9.\\\\]{0,9}[\\%]{0,1})$|^[sbm]{1}$)";

	public static String	specialCharacterRegex	= "([\\®\\\"\\*\\|\\+\\‡\\◊\\™\\(\\)])";

	public static void countFrequency(HashMap<String, HashMap<String, ArrayList<String>>> dataForFrequecy) throws IOException
	{

		for(Entry<String, HashMap<String, ArrayList<String>>> data : dataForFrequecy.entrySet())
		{
			String searchWord = data.getKey();
			HashMap<String, ArrayList<String>> bankAndList = data.getValue();
			for(Entry<String, ArrayList<String>> sheetyBhai : bankAndList.entrySet())
			{
				String bankName = sheetyBhai.getKey();

				topNFequentWord(sortingAccordingFrequency(frequencyCounter(sheetyBhai.getValue())), 10, bankName);

			}

		}

		//		HashMap<String, Integer> tdWordFrequency = frequencyCounter(TDBank);
		//		HashMap<String, Integer> soctiaWordFrequency = frequencyCounter(soctiaBank);
		//		HashMap<String, Integer> rbcWordFrequency = frequencyCounter(RBCBank);
		//		HashMap<String, Integer> cibcWordFrequency = frequencyCounter(CIBCBank);
		//
		//		tdWordFrequency = sortingAccordingFrequency(tdWordFrequency);
		//		soctiaWordFrequency = sortingAccordingFrequency(soctiaWordFrequency);
		//		rbcWordFrequency = sortingAccordingFrequency(rbcWordFrequency);
		//		cibcWordFrequency = sortingAccordingFrequency(cibcWordFrequency);
		//
		//		int topN = 10;
		//
		//		topNFequentWord(soctiaWordFrequency, topN, "Soctia_Bank_Page");
		//		topNFequentWord(rbcWordFrequency, topN, "RBC_Bank_Page");
		//		topNFequentWord(tdWordFrequency, topN, "TD_Bank_Page");
		//		topNFequentWord(cibcWordFrequency, topN, "CIBC_Bank_Page");
	}

	private static void topNFequentWord(HashMap<String, Integer> sortedMap, int topN, String page)
	{
		List<Map.Entry<String, Integer>> entryList = new ArrayList<>(sortedMap.entrySet());

		ListIterator<Map.Entry<String, Integer>> iterator = entryList.listIterator(entryList.size());

		System.out.println("Top " + topN + " Elements in " + page + "\n");
		System.out.printf("%-20s%-16s\n", "word", "Frequency");

		int i = 50;
		while(iterator.hasPrevious() && i-- > 0)
		{
			Map.Entry<String, Integer> entry = iterator.previous();
			System.out.printf("%-20s%-16s\n", entry.getKey(), entry.getValue());
		}
		System.out.println("\n\n");
	}

	private static HashMap<String, Integer> sortingAccordingFrequency(HashMap<String, Integer> wordFrequency)
	{
		ArrayList<Map.Entry<String, Integer>> pairList = new ArrayList<Map.Entry<String, Integer>>(wordFrequency.entrySet());

		QuickSort.quickSort(pairList, 0, pairList.size() - 1);

		LinkedHashMap<String, Integer> sortedHashMap = new LinkedHashMap<String, Integer>();
		for(Map.Entry<String, Integer> entry : pairList)
		{
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		return sortedHashMap;
	}

	private static HashMap<String, Integer> frequencyCounter(ArrayList<String> wordList)
	{
		HashMap<String, Integer> tempMap = new HashMap<String, Integer>();

		for(String word : wordList)
		{
			word = word.toLowerCase();

			if(tempMap.containsKey(word))
			{
				tempMap.replace(word, tempMap.get(word) + 1);
			}
			else
			{
				tempMap.put(word, 1);
			}
		}

		return tempMap;
	}

	private static ArrayList<String> dataScrapping(String filePath) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		ArrayList<String> wordList = new ArrayList<String>();
		String line = "";
		ArrayList<String> lineList = new ArrayList<String>();

		while((line = br.readLine()) != null)
		{
			lineList.addAll(new ArrayList<String>(Arrays.asList(line.split(","))));
		}

		ArrayList<String> slashSeprated = wordSeparator(lineList, "/");
		ArrayList<String> hypenSeprated = wordSeparator(slashSeprated, "-");
		ArrayList<String> spaceSeprated = wordSeparator(hypenSeprated, " ");

		wordList = preProcess(spaceSeprated);
		return wordList;
	}

	private static ArrayList<String> preProcess(ArrayList<String> wordList)
	{
		Pattern pattern = Pattern.compile(numberRegex);
		ArrayList<String> processedWordsList = new ArrayList<>();

		for(int i = 0; i < wordList.size(); i++)
		{
			if(pattern.matcher(wordList.get(i)).matches())
			{
				wordList.remove(i);
			}
			else
			{
				// Remove special characters
				String processedWord = wordList.get(i).replaceAll(specialCharacterRegex, "");
				processedWordsList.add(processedWord);
			}
		}

		return processedWordsList;
	}

	private static ArrayList<String> wordSeparator(ArrayList<String> wordList, String separator)
	{
		ArrayList<String> temp = new ArrayList<String>();
		if(wordList != null)
		{
			for(String str : wordList)
			{
				// Special handling for "state" when using hyphen separator
				if(str.toLowerCase().contains("state") && separator.equals("-"))
				{
					str = str.split("/?")[0];
				}
				temp.addAll(new ArrayList<String>(Arrays.asList(str.split(separator))));
			}
		}
		return temp;
	}
}
