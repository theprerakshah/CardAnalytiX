package com.creditWise.Prerak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class IndexFillerAndReverse
{

	TrieNode							root;
	HashMap<Integer, ArrayList<String>>	documentStringMap;

	public IndexFillerAndReverse()
	{
		root = new TrieNode();
		documentStringMap = new HashMap<Integer, ArrayList<String>>();
	}

	public void assignWordToDocument(ArrayList<String> worldList, int DocumentId, TrieNode root)
	{
		documentStringMap.put(DocumentId, worldList);

		for(String word : worldList)
		{
			root.wordInsert(word.toLowerCase(), DocumentId);

		}

	}

	public Set<Integer> wordDocumnetFinder(String word, TrieNode root)
	{

		TrieNode searchedNode = root.wordNodeSearcher(word);

		if(searchedNode == null || !searchedNode.isIsend())
		{
			return new HashSet<Integer>();
		}

		return searchedNode.getDocumnetsId();

	}
}
