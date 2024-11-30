package com.creditWise.Sagar;

import java.util.Set;

/**
 * SpellChecker for suggesting the closest valid card type.
 */
public class SpellChecker
{

	/**
	 * Suggests the closest valid card type based on the user's input.
	 * @param input The user's input string.
	 * @param validCardTypes The set of valid card types.
	 * @return The closest matching card type or "Unknown" if no match is found.
	 */
	public static String getSuggestedCardType(String input, Set<String> validCardTypes)
	{
		String bestMatch = "Unknown";
		int smallestDistance = Integer.MAX_VALUE;

		// Iterate through all valid card types
		for(String validCardType : validCardTypes)
		{
			int distance = calculateEditDistance(input.toLowerCase(), validCardType.toLowerCase());

			// Update best match if a closer match is found
			if(distance < smallestDistance)
			{
				smallestDistance = distance;
				bestMatch = validCardType;
			}
		}

		// Return "Unknown" if no match is within a reasonable threshold
		return smallestDistance <= 3 ? bestMatch : "Unknown";
	}

	/**
	 * Calculates the Edit Distance (Levenshtein Distance) between two strings.
	 * @param str1 The first string.
	 * @param str2 The second string.
	 * @return The edit distance between the two strings.
	 */
	private static int calculateEditDistance(String str1, String str2)
	{
		int m = str1.length();
		int n = str2.length();

		// Initialize a 2D array for dynamic programming
		int[][] dp = new int[m + 1][n + 1];

		// Base cases: Transforming an empty string
		for(int i = 0; i <= m; i++)
		{
			dp[i][0] = i; // Deletion cost
		}
		for(int j = 0; j <= n; j++)
		{
			dp[0][j] = j; // Insertion cost
		}

		// Compute the edit distance
		for(int i = 1; i <= m; i++)
		{
			for(int j = 1; j <= n; j++)
			{
				if(str1.charAt(i - 1) == str2.charAt(j - 1))
				{
					dp[i][j] = dp[i - 1][j - 1]; // No cost if characters are the same
				}
				else
				{
					dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], // Substitution cost
							Math.min(dp[i - 1][j], // Deletion cost
									dp[i][j - 1] // Insertion cost
							));
				}
			}
		}

		return dp[m][n];
	}
}
