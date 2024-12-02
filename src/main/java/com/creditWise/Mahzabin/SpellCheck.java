package com.creditWise.Mahzabin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpellCheck {

    private final Trie root;

    public SpellCheck() {

        root = new Trie();
        // Load vocabulary from vocabulary.txt
        try {
            loadVocabulary("vocabulary.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load words from vocabulary file to the Trie
    public void loadVocabulary(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String word;
        while ((word = reader.readLine()) != null) {
            root.insertKey(root,word);
        }
        reader.close();
    }

    // Check if word exists
    public boolean search(String word) {
        return root.searchKey(root,word);
    }

    // Method for getting alternative spellings
    public List<String> suggestAlternatives(String word) {
        List<String> suggestions = new ArrayList<>();
        dfsWithEditDistance(root, "", word, suggestions, 2); // Edit distance of 2
        return suggestions;
    }

    // Helper method for doing DFS with edit distance
    private void dfsWithEditDistance(Trie node, String currentWord, String target, List<String> suggestions, int maxDist) {
        if (node.wordEnd && EditDistance(currentWord, target) <= maxDist) {
            suggestions.add(currentWord);
        }
        for (int i = 0; i < 26; i++) {
            if (node.child[i] != null) {
                char nextChar = (char) (i + 'a');
                dfsWithEditDistance(node.child[i], currentWord + nextChar, target, suggestions, maxDist);
            }
        }
    }

    // Compute Edit Distance (Levenshtein Distance)
    private int EditDistance(String word1, String word2) {
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];
        for (int i = 0; i <= word1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= word2.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= word1.length(); i++) {
            for (int j = 1; j <= word2.length(); j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i][j - 1], dp[i - 1][j]));
            }
        }
        return dp[word1.length()][word2.length()];
    }


}
