package com.creditWise.CardAnalytiX;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.creditWise.Sagar.SpellChecker;
import com.creditWise.Sagar.Validation;

/**
 * Credit Card Suggestion Tool
 */
public class Executer
{

	// Set of valid words from text files in the "text_pages" folder
	private static Set<String> validWords = new HashSet<>();

	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);

		// Load words from files in the "text_pages" folder into validWords set
		loadValidWordsFromFiles("text_pages");

		System.out.println("Welcome to the Credit Card Suggestion Tool!");
		System.out.println("Please answer the following questions to get your best credit card suggestion:");

		// Question 1: Preferred card type (mandatory)
		String cardType = getCardType(scanner);

		// Question 2: Preferred annual fee range (mandatory)
		String annualFeeRange = getMandatoryInput(scanner, "2. What is your preferred annual fee range? (For example: $0-$50, $50-$100, $100+): ");
		while(!Validation.isValidAnnualFeeRange(annualFeeRange))
		{
			System.out.println("Invalid annual fee range format. Please try again.");
			annualFeeRange = getMandatoryInput(scanner, "2. What is your preferred annual fee range? (For example: $0-$50, $50-$100, $100+): ");
		}
		System.out.println("Your preferred annual fee range is: " + annualFeeRange); // Confirmation message for annual fee

		// Question 3: Rewards or additional features (mandatory)
		String rewards = getMandatoryInput(scanner, "3. What kind of rewards or additional features are you looking for? \n(For example: Cashback, Travel Points, Low Fees, Other): ");
		while(!Validation.isValidRewards(rewards, validWords))
		{
			System.out.println("Invalid rewards input. Please try again.");
			rewards = getMandatoryInput(scanner, "3. What kind of rewards or additional features are you looking for? \n(For example: Cashback, Travel Points, Low Fees, Other): ");
		}
		System.out.println("Feature you are looking for is: " + rewards); // Confirmation message for rewards/features

		// Display summary of the user's inputs
		System.out.println("\nThank you for your inputs! Here are your preferences:");
		System.out.println("Preferred Card Type: " + cardType);
		System.out.println("Preferred Annual Fee Range: " + annualFeeRange);
		System.out.println("Desired Rewards/Features: " + rewards);

		// (Here you could add logic to suggest credit cards based on the inputs)

		// Close the scanner
		scanner.close();
	}

	/**
	 * Gathers the user's preferred card type, applying spell check and validation.
	 * Ensures the input is valid and not empty.
	 * @param scanner The Scanner object for user input.
	 * @return The validated card type.
	 */
	private static String getCardType(Scanner scanner)
	{
		String cardType = "";
		while(true)
		{
			if(cardType.isEmpty())
			{
				System.out.print("1. What is your preferred card type? \nFor example: Visa, American Express, MasterCard: ");
			}
			else
			{
				System.out.print("Please enter your card type again: ");
			}
			cardType = scanner.nextLine().trim();

			// Ensure input is not empty
			if(cardType.isEmpty())
			{
				System.out.println("Card type cannot be empty. Please try again.");
				continue;
			}

			// Check if the card type exists in validWords
			if(Validation.isValidCardType(cardType, validWords))
			{
				System.out.println("Your preferred card type is: " + cardType); // Change this line
				break; // Exit loop if the input is valid
			}
			else
			{
				// Suggest a valid card type
				String suggestion = SpellChecker.getSuggestedCardType(cardType, validWords);
				System.out.println("(Spell Check applied: Did you mean \"" + suggestion + "\")?");

				// Ask user if they meant the suggested card type
				System.out.print("Do you mean \"" + suggestion + "\"? (Yes/No): ");
				String response = scanner.nextLine().trim().toLowerCase();
				if(response.equals("yes"))
				{
					cardType = suggestion; // Accept the suggestion
					System.out.println("Your preferred card type is: " + cardType); // Now print the preferred card type
					break; // Exit the loop
				}
				else
				{
					System.out.println("Please enter a valid card type.");
				}
			}
		}
		return cardType;
	}

	/**
	 * Ensures user input is not empty and prompts until a valid response is provided.
	 * @param scanner The Scanner object for user input.
	 * @param prompt The question to display to the user.
	 * @return The validated user input.
	 */
	private static String getMandatoryInput(Scanner scanner, String prompt)
	{
		String input;
		while(true)
		{
			System.out.print(prompt);
			input = scanner.nextLine().trim();
			input = Validation.validateMandatoryField(input, prompt);
			break;
		}
		return input;
	}

	/**
	 * Loads all valid words from files in the specified directory into a Set.
	 * @param directoryPath The path to the folder containing the text files.
	 */
	private static void loadValidWordsFromFiles(String directoryPath)
	{
		// Add valid rewards to the set directly
		validWords.add("cashback");
		validWords.add("travel points");
		validWords.add("low fees");
		validWords.add("other");

		File folder = new File(directoryPath);
		File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt")); // Filter only .txt files

		if(files != null)
		{
			for(File file : files)
			{
				try(Scanner fileScanner = new Scanner(file))
				{
					while(fileScanner.hasNext())
					{
						String word = fileScanner.next().toLowerCase(); // Store words in lowercase for comparison
						validWords.add(word);
					}
				}
				catch(FileNotFoundException e)
				{
					System.err.println("File not found: " + file.getName());
				}
			}
		}
	}
}
