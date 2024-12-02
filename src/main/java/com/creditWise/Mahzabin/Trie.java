package com.creditWise.Mahzabin;

public class Trie {

    Trie[] child;                                    // Array for child nodes of each node
    boolean wordEnd;                                 // Used for indicating the end of a string

    // Constructor
    public Trie() {
        wordEnd = false;                             // Initialize the wordEnd variable with false
        child = new Trie[26];
    }

    // Method to insert a key into the Trie
    public void insertKey(Trie root, String key)
    {
        key=key.toLowerCase();
        Trie current = root;                         // Initialize the current pointer with the root node
        for (char c : key.toCharArray())             // Iterating across the length of the string
        {
            if(c-'a'>=0 && c-'a'<26){
                if (current.child[c - 'a'] == null)      // Checking if the node exists for the current character in the Trie
                {
                    Trie newNode = new Trie();           // Making a new node If node for current character does not exist.
                    current.child[c - 'a'] = newNode;    // Keeping the reference for the newly created node
                }
                current = current.child[c - 'a'];        // Moving the current pointer to the newly created node
            }
        }
        current.wordEnd = true;                      // Marking the end of the word
    }


    // Method to search a key in the Trie
    public boolean searchKey(Trie root, String key)
    {
        key=key.toLowerCase();
        Trie current = root;                     // Initializing the current pointer with the root node
        for (char c : key.toCharArray())         // Iterating across the length of the string
        {
            if(c-'a'>=0 && c-'a'<26){                 // a condition to make sure we only insert alphabets also for not getting any error
                if (current.child[c - 'a'] == null)  // Checking if the node exists for the current character in the Trie
                    return false;
                current = current.child[c - 'a'];    // Moving the current pointer to its child
            }
        }
        return current.wordEnd;                  // Return true if the word exists
    }

}
