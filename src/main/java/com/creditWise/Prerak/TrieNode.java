package com.creditWise.Prerak;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TrieNode
{
	HashMap<Character, TrieNode>	children;
	Set<Integer>					documentsId;
	boolean							isEnd;

	public TrieNode()
	{
		children = new HashMap<Character, TrieNode>();
		documentsId = new HashSet<>();

	}

	public HashMap<Character, TrieNode> getChildren()
	{
		return children;
	}

	public void setChildren(HashMap<Character, TrieNode> children)
	{
		this.children = children;
	}

	public Set<Integer> getDocumnetsId()
	{
		return documentsId;
	}

	public void setDocumnetsId(Set<Integer> documnetsId)
	{
		this.documentsId = documnetsId;
	}

	public boolean isIsend()
	{
		return isEnd;
	}

	public void setIsend(boolean isend)
	{
		this.isEnd = isend;
	}

	public void wordInsert(String word, int documentId)
	{
		TrieNode tempNode = this;
		for(char ch : word.toCharArray())
		{
			tempNode.getChildren().putIfAbsent(ch, new TrieNode());
			tempNode = tempNode.getChildren().get(ch);
		}
		tempNode.isEnd = true;
		tempNode.documentsId.add(documentId);
	}

	public TrieNode wordNodeSearcher(String word)
	{
		TrieNode tempNode = this;
		for(Character ch : word.toCharArray())
		{
			if(!tempNode.getChildren().containsKey(ch))
			{
				return null;
			}
			tempNode = tempNode.getChildren().get(ch);
		}

		return tempNode;
	}

}
