package com.creditWise.Sagar;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Validation class to handle input validation.
 */
public class Validation
{
	
	//Validates that the rewards input is in a valid format.
	public static boolean isValidRewards(String input, Set<String> validWords)
	{
		return validWords.contains(input.toLowerCase());
	}

    
    public static boolean isWordOnly(String input) {
        return input.matches("[a-zA-Z\\s]+"); // Matches only letters and spaces
    }

     public static String getWordOnlyInput(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            if (isWordOnly(input)) {
                return input; // Return valid input
            } else {
                System.out.println("Invalid input. Please enter valid words.");
            }
        }
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
	//Validates that the Annual Fee input is in a valid format.	
	public static boolean ValidationAnnualFee(String input) {
		if (input == null || input.trim().isEmpty()) {
			return false; // input can't be null or empty
		}
	
		// Regular expression pattern to match positive annual fees, with optional decimal points
		String regex = "^[0-9]+(\\.[0-9]+)?$"; 
	
		// Check if input matches the pattern
		return input.trim().matches(regex);
	}
	//Validates that the documentwordsearchandfrequency input is in a valid format.
	public static boolean documentWordSearchAndFrequency(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false; // input can't be null or empty
        }

        // Regular expression pattern to match card types with only letters and spaces (no digits or special characters)
        String regex = "^[a-zA-Z]+( [a-zA-Z]+)*$"; 

        // Check if input matches the pattern
        return input.trim().matches(regex);
    }

	//Validates that the Bank Name input is in a valid format.
	public static boolean ValidationBankName(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false; // input can't be null or empty
        }

        // Regular expression pattern to match card types with only letters and spaces (no digits or special characters)
        String regex = "^[a-zA-Z]+( [a-zA-Z]+)*$"; 

        // Check if input matches the pattern
        return input.trim().matches(regex);
    }

	//Validates that the Interest Rate input is in a valid format.
	public static boolean ValidationInterestRate(String input) {
		if (input == null || input.trim().isEmpty()) {
			return false; // input can't be null or empty
		}
	
		// Regular expression pattern to match positive annual fees, with optional decimal points
		String regex = "^[0-9]+(\\.[0-9]+)?%?$"; 
	
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

    public static String validateYesNoInput(BufferedReader reader) throws IOException {
        String input;

        while (true) {
            System.out.println("Do you want word count from this bankWebsite? (Y | N):");
            input = reader.readLine();
            input = input.trim();

            if (input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N")) {
                return input; // Return valid input
            } else {
                System.out.println("Invalid input. Please enter 'Y' or 'N'.");
            }
        }
    }

	// Validation method for bank names
    public static boolean isValidBankName(String userInput, List<String> validBankNames) {
        // Check if the input matches any valid bank name (case-insensitive)
        return validBankNames.stream().anyMatch(bank -> bank.equalsIgnoreCase(userInput));
    }

}
