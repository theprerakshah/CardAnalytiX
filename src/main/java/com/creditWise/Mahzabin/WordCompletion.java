package com.creditWise.Mahzabin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * WordCompletion class to handle word completion and suggestion.
 */
public class WordCompletion
{

	/**
	 * Suggests a valid word from the provided set of valid words.
	 * @param userInput The user input that may be incomplete.
	 * @param validWords The set of valid words to match against.
	 * @return The suggested word based on user input.
	 */
	private AVLTree avlTree;

	// Creating a Constructor of WordCompletion and creating an instance of AVL Tree and loads the vocabulary
	public WordCompletion()
	{
		//making an object of AVL tree
		avlTree = new AVLTree();
		// Load vocabulary from vocabulary.txt
		try
		{
			loadVocabulary("vocabulary.txt");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	// Load words from vocabulary file to the AVL tree
	private void loadVocabulary(String filePath) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line;
		while((line = reader.readLine()) != null)
		{
			avlTree.insert(line.trim());
		}
		reader.close();
	}

	// Collect words based on prefix using DFS traversal
	private void collectWords(AVLNode node, String prefix, MinHeap heap)
	{
		if(node == null)
			return;

		if(node.word.toLowerCase().startsWith(prefix.toLowerCase()))
		{
			// Insert node frequency into MinHeap and track corresponding nodes elsewhere
			heap.insert(node.frequency);
		}
		collectWords(node.left, prefix, heap);
		collectWords(node.right, prefix, heap);
	}

	// Autocomplete function that returns top suggestions
	public List<String> autocomplete(String prefix, int suggestionNumber)
	{
		// Creating a MinHeap with maximum size equal to suggestionNumber
		MinHeap heap = new MinHeap(suggestionNumber);
		// Collect possible matches for the prefix
		collectWords(avlTree.root, prefix, heap);

		// Prepare suggestions by extracting nodes from the heap
		List<String> suggestions = new ArrayList<>();

		while(heap.size() > 0)
		{
			int frequency = heap.remove();
			AVLNode matchingNode = findNodeByFrequency(avlTree.root, frequency, prefix);

			if(matchingNode != null)
			{
				suggestions.add(matchingNode.word);
			}
		}

		return suggestions;
	}

	// Helper method to find an AVL node by its frequency and prefix
	private AVLNode findNodeByFrequency(AVLNode node, int frequency, String prefix)
	{
		if(node == null)
			return null;

		if(node.frequency == frequency && node.word.toLowerCase().startsWith(prefix.toLowerCase()))
		{
			return node;
		}

		AVLNode leftSearch = findNodeByFrequency(node.left, frequency, prefix);
		if(leftSearch != null)
			return leftSearch;

		return findNodeByFrequency(node.right, frequency, prefix);
	}

}
