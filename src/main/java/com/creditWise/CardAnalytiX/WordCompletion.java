package com.creditWise.CardAnalytiX;

import java.util.Set;
import java.util.Scanner;

/**
 * WordCompletion class to handle word completion and suggestion.
 */
public class WordCompletion {

    /**
     * Suggests a valid word from the provided set of valid words.
     * @param userInput The user input that may be incomplete.
     * @param validWords The set of valid words to match against.
     * @return The suggested word based on user input.
     */
    public static String getSuggestedWord(String userInput, Set<String> validWords) {
        for (String validWord : validWords) {
            if (validWord.startsWith(userInput.toLowerCase())) {
                return validWord;
            }
        }
        return null; // No match found
    }

    /**
     * Prompts the user with the suggestion and asks for confirmation.
     * @param suggestedWord The suggested word.
     * @param userInput The original user input.
     * @param scanner The Scanner object for user input.
     * @return The accepted or original user input.
     */
    public static String getConfirmedWord(String suggestedWord, String userInput, Scanner scanner) {
        if (suggestedWord != null) {
            System.out.println("(Word Completion Check applied: Did you mean \"" + suggestedWord + "\")?");
            System.out.print("Do you mean \"" + suggestedWord + "\"? (Yes/No): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes")) {
                return suggestedWord; // Accept the suggestion
            } else {
                return userInput; // Reject the suggestion, return original input
            }
        } else {
            return userInput; // No suggestion, return original input
        }
    }
}
