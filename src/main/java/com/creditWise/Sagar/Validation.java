package com.creditWise.Sagar;

import java.util.List;
import java.util.Set;

/**
 * Validation class to handle input validation.
 */
public class Validation
{

	
	// Validates that the input for the annual fee range is in a valid format.
	public static boolean isValidAnnualFeeRange(String input)
	{
		// Validate the format of the annual fee range (e.g., "$0-$50", "$50-$100", "$100+")
		return input.matches("\\$\\d+(\\-\\$\\d+|\\+)?");
	}
	
	//Validates that the rewards input is in a valid format.
	public static boolean isValidRewards(String input, Set<String> validWords)
	{
		return validWords.contains(input.toLowerCase());
	}

	
	 //Validates that the card type input is in a valid format.	 
	public static boolean isValidCardType(String input, Set<String> validWords)
	{
		return validWords.contains(input.toLowerCase());
	}

	public static boolean ValidationCardType(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false; // input can't be null or empty
        }

        // Regular expression pattern to match card types with only letters and spaces (no digits or special characters)
        String regex = "^[a-zA-Z]+( [a-zA-Z]+)*$"; 

        // Check if input matches the pattern
        return input.trim().matches(regex);
    }

	public static boolean ValidationAnnualFee(String input) {
		if (input == null || input.trim().isEmpty()) {
			return false; // input can't be null or empty
		}
	
		// Regular expression pattern to match positive annual fees, with optional decimal points
		String regex = "^[0-9]+(\\.[0-9]+)?$"; 
	
		// Check if input matches the pattern
		return input.trim().matches(regex);
	}

	public static boolean documentWordSearchAndFrequency(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false; // input can't be null or empty
        }

        // Regular expression pattern to match card types with only letters and spaces (no digits or special characters)
        String regex = "^[a-zA-Z]+( [a-zA-Z]+)*$"; 

        // Check if input matches the pattern
        return input.trim().matches(regex);
    }

	public static boolean ValidationBankName(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false; // input can't be null or empty
        }

        // Regular expression pattern to match card types with only letters and spaces (no digits or special characters)
        String regex = "^[a-zA-Z]+( [a-zA-Z]+)*$"; 

        // Check if input matches the pattern
        return input.trim().matches(regex);
    }

	public static boolean ValidationInterestRate(String input) {
		if (input == null || input.trim().isEmpty()) {
			return false; // input can't be null or empty
		}
	
		// Regular expression pattern to match positive annual fees, with optional decimal points
		String regex = "^[0-9]+(\\.[0-9]+)?$"; 
	
		// Check if input matches the pattern
		return input.trim().matches(regex);
	}
	
	// Method to validate interest rate input
    public static boolean isValidInterestRate(String input) {
        try {
            // Parse the input to a double
            double interestRate = Double.parseDouble(input);

            // Check if the value is non-negative
            return interestRate >= 0;
        } catch (NumberFormatException e) {
            // Input is not a valid number
            return false;
        }
    }

	// Validation method for bank names
    public static boolean isValidBankName(String userInput, List<String> validBankNames) {
        // Check if the input matches any valid bank name (case-insensitive)
        return validBankNames.stream().anyMatch(bank -> bank.equalsIgnoreCase(userInput));
    }

	//Validates that a mandatory field is not empty.
	public static String validateMandatoryField(String input, String prompt)
	{
		while(input.isEmpty())
		{
			System.out.println("Input cannot be empty. Please try again.");
			System.out.print(prompt);
			input = new java.util.Scanner(System.in).nextLine().trim();
		}
		return input;
	}
}
