package com.creditWise.Sagar;

import java.util.List;

public class SpellChecker {
    // Compute the edit distance between two strings
    public static int editDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j; // Insert all characters of word2
                } else if (j == 0) {
                    dp[i][j] = i; // Remove all characters of word1
                } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // No change
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], // Replace
                            Math.min(dp[i - 1][j], // Remove
                                    dp[i][j - 1])); // Insert
                }
            }
        }

        return dp[m][n];
    }

    // Find the closest match from a list of valid terms
    public static String findClosestMatch(String input, List<String> dictionary) {
        String closestMatch = null;
        int minDistance = Integer.MAX_VALUE;

        for (String term : dictionary) {
            int distance = editDistance(input.toLowerCase(), term.toLowerCase());
            if (distance < minDistance) {
                minDistance = distance;
                closestMatch = term;
            }
        }

        return closestMatch;
    }
}

