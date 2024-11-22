package com.creditWise.CardAnalytiX;

import java.util.Set;

/**
 * Validation class to handle input validation.
 */
public class Validation {

    /**
     * Validates that the input for the annual fee range is in a valid format.
     * @param input The annual fee range input.
     * @return true if the format is valid, false otherwise.
     */
    public static boolean isValidAnnualFeeRange(String input) {
        // Validate the format of the annual fee range (e.g., "$0-$50", "$50-$100", "$100+")
        return input.matches("\\$\\d+(\\-\\$\\d+|\\+)?");
    }

    /**
     * Validates that the rewards input is in a valid format.
     * @param input The rewards input.
     * @param validWords The set of valid words to check against.
     * @return true if the input is valid, false otherwise.
     */
    public static boolean isValidRewards(String input, Set<String> validWords) {
        return validWords.contains(input.toLowerCase());
    }

    /**
     * Validates that the card type input is in a valid format.
     * @param input The card type input.
     * @param validWords The set of valid words to check against.
     * @return true if the card type is valid, false otherwise.
     */
    public static boolean isValidCardType(String input, Set<String> validWords) {
        return validWords.contains(input.toLowerCase());
    }

    /**
     * Validates that a mandatory field is not empty.
     * @param input The user input.
     * @param prompt The prompt message to display.
     * @return The validated input.
     */
    public static String validateMandatoryField(String input, String prompt) {
        while (input.isEmpty()) {
            System.out.println("Input cannot be empty. Please try again.");
            System.out.print(prompt);
            input = new java.util.Scanner(System.in).nextLine().trim();
        }
        return input;
    }
}
