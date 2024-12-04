package com.creditWise.CardAnalytiX;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.creditWise.DataHandler.BankNameMap;
import com.creditWise.DataHandler.CardTypeMap;
import com.creditWise.DataHandler.HtmlToText;
import com.creditWise.DataHandler.Webcrawler;
import com.creditWise.Mahzabin.SpellCheck;
import com.creditWise.Mahzabin.WordCompletion;
import com.creditWise.Prerak.WordFrequency;
import com.creditWise.Prerak.WordSearcher;
import com.creditWise.Sagar.PageRanking;
import com.creditWise.Sagar.Validation;
import com.creditWise.Sagar.mergeSort;
import com.creditWise.Sakshi.SearchFrequencyRBTree;

/**
 * Credit Card Suggestion Tool
 */
public class Executer
{
	private static WordCompletion	wordCompletion	= new WordCompletion();
	private static SpellCheck		spellCheck		= new SpellCheck();
	private static BankNameMap		bankNameMap		= new BankNameMap();
	private static Webcrawler		Webcrawler		= new Webcrawler();

	public static void main(String[] args) throws IOException
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to the Credit Card Suggestion Tool!");
		System.out.println("Please answer the following question to get your best credit card suggestion:");
		while(true)
		{

			System.out.println("\n1. Do you want to crawl the website?");
			System.out.println("2. Or do you want to use existing data?");
			System.out.println("3. Do you want to Exit the Tool");
			System.out.print("Enter your choice (1, 2, or 3): ");
			try
			{
				int choice = scanner.nextInt();
				scanner.nextLine();
				switch(choice)
				{
					case 1:
						System.out.println("\nYou chose to crawl the website.");
						System.out.println("Initializing web crawler...");
						Webcrawler.main(args);
						HtmlToText.main(args);

						case2Handler();
						break;

					case 2:
						case2Handler();

						break;
					case 3:
						System.out.println("Exiting the tool...");
						scanner.close();
						return; // Exit the program

					default:
						System.out.println("Invalid choice. Please restart the tool and enter 1, 2 or 3.");
						break;
				}
			}
			catch(InputMismatchException e)
			{
				// Handle non-integer input
				System.out.println("Invalid input! Please enter a number between 1 and 7.");

				// Clear the invalid input
				scanner.nextLine();
			}
			catch(Exception e)
			{
				// Catch any other unexpected exceptions
				System.out.println("An unexpected error occurred: " + e.getMessage());
				scanner.nextLine(); // Clear the input buffer
			}
		}
	}

	public static void case2Handler() throws IOException
	{
		Scanner scanner = new Scanner(System.in);
		while(true)
		{

			System.out.println("\n1. Show all Credit card Data");
			System.out.println("2. Fetch credit card according to this preferences: Card Type, Annual Fee, Bank Name, Interest Rate");
			System.out.println("3. Fetch data according to word Frequency");
			System.out.println("4. Search");
			System.out.println("5. Most Popular Suggetions");
			System.out.println("6. Ranking website according to your specific requirement");
			System.out.println("7. Go Back to Main Menu");

			System.out.print("Enter your choice (1, 2, 3, 4, 5 or 6): ");
			try
			{
				ArrayList<CreditCard> cardList = Engine.Engine1();
				int choice = scanner.nextInt();
				//				scanner.nextLine();
				switch(choice)
				{
					case 1:
						System.out.println("Fetching all Credit card Data");
						System.out.println("Showing all Credit card Data");
						printCreditCardData(cardList);

						break;

					case 2:
						prefernceBaseCaseHandler(cardList);

						break;
					case 3:
						documentWordSearchAndFrequency(cardList);

						break;
					case 4:
						SearchFrequencyRBTree.SearchInputs();
						break;
					case 5:
						viewPopularSearchTerms();
						break;

					case 6:
						wordFrequencyRankBased(cardList);
						break;

					case 7:
						System.out.println("Going back to the main menu...");
						
						return;

					default:
						System.out.println("Invalid choice. Please restart the tool and enter 1, 2, 3, or 4:");
						break;
				}

			}
			catch(InputMismatchException e)
			{
				// Handle non-integer input
				System.out.println("Invalid input! Please enter a number between 1 and 7.");

				// Clear the invalid input
				scanner.nextLine();
			}
			catch(Exception e)
			{
				// Catch any other unexpected exceptions
				System.out.println("An unexpected error occurred: " + e.getMessage());
				scanner.nextLine(); // Clear the input buffer
			}
		}
	}

	private static void prefernceBaseCaseHandler(ArrayList<CreditCard> cardList)
	{
		Scanner scanner = new Scanner(System.in);
		while(true)
		{
			System.out.println("\n1. for Searching card according to Card type ");
			System.out.println("2. for Searching card according to Annual Fee");
			System.out.println("3. for Searching card according to Bank Name");
			System.out.println("4. for Searching card according to Interest Rate");
			System.out.println("5. Go Back to Previous Menu");
			System.out.print("Enter your choice (1, 2, 3, 4, or 5): ");
			try
			{
				int choice = scanner.nextInt();
				//				scanner.nextLine();
				String userInput = "";
				ArrayList<CreditCard> resultCardList = new ArrayList<CreditCard>();
				switch(choice)
				{
					case 1:

						if(scanner.hasNextLine())
							scanner.nextLine(); // Clears leftover newline
						while(true)
						{
							System.out.println("Select Card Type From These Options. \n" + "    Visa Card\n" + "    American Express\n" + "    Costco Cards\n" + "    Student Cards\n"
									+ "    Cash Back Cards\n" + "    Travel Rewards Cards\n" + "    Business Credit Cards\n" + "    Low Interest Cards\n" + "    Mastercard");
							System.out.println("Input:");

							// Take user input
							userInput = scanner.nextLine();

							// Step 1: Validate Bank Name
							if(!Validation.ValidationCardType(userInput))
							{
								System.out.println("Please Enter a Valid Credit Card Type.");
								continue; // Restart the loop for a valid input
							}
							if(!CardTypeMap.getCardType(userInput).equalsIgnoreCase("Null"))
							{
								resultCardList = basedOnCardType(userInput, cardList);
								break;
							}

							// Step 2: Spell Check and Word Completion
							userInput = spellCheckAndWordComplete(userInput);

							// If "Try Again" is returned, it means neither spelling nor word completion succeeded
							if(userInput.equalsIgnoreCase("Try Again"))
							{
								System.out.println("Invalid input after suggestions. Please try again.");
								continue; // Restart the loop for new input
							}

							// Step 3: Map Lookup
							String cardType = CardTypeMap.getCardType(userInput);

							if(cardType.equalsIgnoreCase("Null"))
							{
								System.out.println("Please Enter a Valid Card Type.");
								continue; // Restart the loop if no valid bank name is found in the map
							}

							// If all checks pass, break out of the loop
							resultCardList = basedOnCardType(userInput, cardList);
							break;
						}
						break;

					case 2:
						System.out.println("Select card based on Annual Fee, Enter Your preferd annual fee:");
						userInput = scanner.next();

						while(!Validation.ValidationAnnualFee(userInput))
						{
							System.out.println("Enter Annual Fee in Correct Format ");
							userInput = scanner.next();
						}
						resultCardList = basedOnAnnualFee(userInput, cardList);

						break;
					case 3:
						if(scanner.hasNextLine())
							scanner.nextLine(); // Clears leftover newline
						while(true)
						{
							System.out.println("Select card based on Bank Name, Enter Your preferred Bank: [RBC, Scotia Bank, CIBC, TD Bank]");

							// Take user input
							userInput = scanner.nextLine();

							// Step 1: Validate Bank Name
							if(!Validation.ValidationBankName(userInput))
							{
								System.out.println("Invalid Bank Name. Please enter a valid bank name.");
								continue; // Restart the loop for a valid input
							}
							if(!bankNameMap.getBankName(userInput).equalsIgnoreCase("Null"))
							{
								resultCardList = basedOnBankName(userInput, cardList);
								break;
							}

							// Step 2: Spell Check and Word Completion
							userInput = spellCheckAndWordComplete(userInput);

							// If "Try Again" is returned, it means neither spelling nor word completion succeeded
							if(userInput.equalsIgnoreCase("Try Again"))
							{
								System.out.println("Invalid input after suggestions. Please try again.");
								continue; // Restart the loop for new input
							}

							// Step 3: Map Lookup
							String bankName = bankNameMap.getBankName(userInput);

							if(bankName.equalsIgnoreCase("Null"))
							{
								System.out.println("Bank Name not found. Please enter a valid bank name from the options.");
								continue; // Restart the loop if no valid bank name is found in the map
							}

							// If all checks pass, break out of the loop
							resultCardList = basedOnBankName(userInput, cardList);
							break;
						}
						break;
					case 4:
						System.out.println("Select card based on Interest Rate, Enter Your preferd Interest Rate:");
						userInput = scanner.next();

						while(!Validation.ValidationInterestRate(userInput))
						{
							System.out.println("Enter Interest Rate in Correct Format");
							userInput = scanner.next();
						}
						resultCardList = basedOnInterestRate(userInput, cardList);

						break;
					case 5:
						System.out.println("Going back to the previous menu...");
						return;

					default:
						System.out.println("Invalid choice. Please restart the tool and enter 1 or 2.");
						break;
				}
			}
			catch(InputMismatchException e)
			{
				// Handle non-integer input
				System.out.println("Invalid input! Please enter a number between 1 and.");

				// Clear the invalid input
				scanner.nextLine();
			}
			catch(Exception e)
			{
				// Catch any other unexpected exceptions
				System.out.println("An unexpected error occurred: " + e.getMessage());
				scanner.nextLine(); // Clear the input buffer
			}
		}
	}

	//Spell checking and word completion implementation.
	public static String spellCheckAndWordComplete(String userInput)
	{
		Scanner scanner = new Scanner(System.in);
		String correctSpelledWord;

		try
		{
			// Case 1: The word is spelled correctly
			if(spellCheck.search(userInput))
			{
				correctSpelledWord = userInput;

				// Move to autocomplete
				System.out.println("Suggested Autocomplete words: ");
				List<String> suggestions = wordCompletion.autocomplete(correctSpelledWord, 5);

				for(int i = 0; i < suggestions.size(); i++)
				{
					System.out.println((i + 1) + " " + suggestions.get(i));
				}

				int input = getValidInput(scanner, 0, suggestions.size(), "To choose a suggested word type the number associated with it, or type 0 to not choose any of the suggestions.");

				if(input == 0)
				{
					return correctSpelledWord;
				}
				else
				{
					return suggestions.get(input - 1);
				}
			}
			else
			{
				// Case 2: The word is not spelled correctly, but has possible prefix matches
				if(wordCompletion.doesPrefixExist(userInput))
				{
					System.out.println("Suggested Autocomplete words: ");
					List<String> suggestions = wordCompletion.autocomplete(userInput, 5);

					for(int i = 0; i < suggestions.size(); i++)
					{
						System.out.println((i + 1) + " " + suggestions.get(i));
					}

					int input = getValidInput(scanner, 0, suggestions.size(), "To choose a suggested word type the number associated with it, or type 0 to not choose any and type again.");

					if(input == 0)
					{
						return "Try Again";
					}
					else
					{
						return suggestions.get(input - 1);
					}
				}
				else
				{
					// Case 3: The word does not have valid suggestions or matches
					System.out.println(userInput + " might be spelled incorrectly.");
					List<String> correctSpell = spellCheck.suggestAlternatives(userInput);

					if(!correctSpell.isEmpty())
					{
						System.out.println("Did you mean these?");
						for(int i = 0; i < correctSpell.size(); i++)
						{
							System.out.println((i + 1) + " " + correctSpell.get(i));
						}

						int input = getValidInput(scanner, 0, correctSpell.size(),
								"To choose a suggested spelling type the number associated with it, or type 0 if none matches and you want to type again.");

						if(input == 0)
						{
							return "Try Again";
						}
						else
						{
							return correctSpell.get(input - 1);
						}
					}
					else
					{
						return "Try Again";
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("An unexpected error occurred: " + e.getMessage());
			return "Try Again";
		}
	}

	private static int getValidInput(Scanner scanner, int min, int max, String message)
	{
		int input = -1;

		while(true)
		{
			try
			{
				System.out.println(message);
				input = Integer.parseInt(scanner.nextLine());

				// Check if the input is within the valid range
				if(input >= min && input <= max)
				{
					break;
				}
				else
				{
					System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
				}
			}
			catch(NumberFormatException e)
			{
				System.out.println("Invalid input. Please enter a valid integer.");
			}
		}

		return input;
	}

	// Method to print all credit card data
	public static void printCreditCardData(ArrayList<CreditCard> cardList)
	{
		if(cardList.isEmpty())
		{
			System.out.println("No credit card data available.");
			return;
		}
		// Print table header
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("| %-20s | %-50s | %-20s | %-12s | %-22s | %-150s |\n", "Bank Name", "Card Name", "Card Type", "Annual Fee", "Interest Rate", "Additional Features");
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------");

		// Print each card's details in rows
		for(CreditCard card : cardList)
		{
			System.out.printf("| %-20s | %-50s | %-20s | %-12.2s | %-22s | %-150s |\n", card.getBank(), card.getCardName(), card.getCardType(), card.getAnnualFee(), card.getPurchaseInterestRate(),
					card.getAdditionalFeatures());
		}

		// Footer line for the table
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

	}

	private static void documentWordSearchAndFrequency(ArrayList<CreditCard> cardList) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String word;

		// Loop until valid input is provided
		while(true)
		{
			System.out.println("Enter the word you want to look for in the bank website:");
			word = reader.readLine();

			// Validate input using the regex
			if(Validation.documentWordSearchAndFrequency(word))
			{
				break; // Exit loop if input is valid
			}
			else
			{
				System.out.println("Invalid input. Please enter a valid word.");
			}
		}

		HashMap<String, HashMap<String, ArrayList<String>>> dataForFrequency = WordSearcher.invertedIndexing(cardList, word.trim().toLowerCase());
		if(dataForFrequency != null)
		{
			while (true) {
				System.out.println("Do you want word count from this bankWebsite? (Y | N):");
				String freqInput = reader.readLine();
				freqInput = freqInput.trim();
			
				if (freqInput.equalsIgnoreCase("Y")) {
					WordFrequency.countFrequency(dataForFrequency);
					break; // Exit the loop after valid input
				} else if (freqInput.equalsIgnoreCase("N")) {
					System.out.println("Skipping word count...");
					break; // Exit the loop after valid input
				} else {
					System.out.println("Invalid input. Please enter 'Y' or 'N'.");
				}
			}
		}
	}

	private static ArrayList<CreditCard> basedOnInterestRate(String userInput, ArrayList<CreditCard> cardList)
	{
		ArrayList<CreditCard> resultCardList = new ArrayList<>();
		Scanner scanner = new Scanner(System.in);

		// Validate input
		while(!Validation.isValidInterestRate(userInput))
		{
			System.out.println("Invalid Interest Rate. Please enter a valid non-negative number:");
			userInput = scanner.nextLine();
		}

		// Convert the validated input to a double
		double userInterestRate = -1;
		try
		{
			userInterestRate = Double.parseDouble(userInput); // Convert String to double
		}
		catch(NumberFormatException e)
		{
			System.out.println("Error: Invalid number format.");
			return resultCardList; // Return empty list if input is invalid
		}

		// Filter the cards based on interest rate
		for(CreditCard card : cardList)
		{
			try
			{
				// Extract numeric part from the interest rate string (remove non-numeric characters)
				String interestRateString = card.getPurchaseInterestRate().replaceAll("[^0-9.]", "");

				// If the string is not empty after removing non-numeric characters
				if(!interestRateString.isEmpty())
				{
					double cardInterestRate = Double.parseDouble(interestRateString);

					// Now compare the interest rate
					if(cardInterestRate <= userInterestRate)
					{
						resultCardList.add(card);
					}
				}
				else
				{
					System.out.println("Invalid interest rate for card: " + card.getCardName());
				}
			}
			catch(NumberFormatException e)
			{
				System.out.println("Error parsing the interest rate for card: " + card.getCardName());
			}
		}

		// Ask the user for sorting preference: 1 for Ascending, 2 for Descending
		String orderChoice = "";
		while(!orderChoice.equals("1") && !orderChoice.equals("2"))
		{
			System.out.println("Do you want to sort the results by Interest Rate in : \n 1. Ascending order\n 2. Descending order");
			orderChoice = scanner.nextLine();

			if(!orderChoice.equals("1") && !orderChoice.equals("2"))
			{
				System.out.println("Invalid input! Please enter 1 for ascending or 2 for descending.");
			}
		}

		// Check if user chose ascending or descending
		boolean isAscending = true; // Default is ascending
		if(orderChoice.equals("2"))
		{
			isAscending = false;
		}

		// Sort the result list using MergeSort
		resultCardList = mergeSort.sort(resultCardList, isAscending);

		// Handle the result and display
		if(resultCardList.isEmpty())
		{
			System.out.println("No credit cards found with an interest rate of " + userInterestRate + " or less.");
		}
		else
		{
			System.out.println("Credit cards found with an interest rate of " + userInterestRate + " or less:");
			printCreditCardData(resultCardList);
		}

		return resultCardList;
	}

	// Implement word Completion, spell checking , validation
	private static ArrayList<CreditCard> basedOnBankName(String userInput, ArrayList<CreditCard> cardList)
	{
		ArrayList<CreditCard> resultCardList = new ArrayList<>();
		Scanner scanner = new Scanner(System.in);

		// Validate the user input (you may add any specific validation for the input)
		while(userInput == null || userInput.isEmpty())
		{
			System.out.println("Invalid input. Please enter a valid bank name:");
			userInput = scanner.nextLine();
		}

		// Filter the cards based on the bank name (case insensitive matching)
		for(CreditCard card : cardList)
		{
			if(card.getBank() != null && card.getBank().toLowerCase().contains(userInput.toLowerCase()))
			{
				resultCardList.add(card);
			}
		}

		// Ask the user for sorting preference: 1 for Ascending, 2 for Descending
		String orderChoice = "";
		while(!orderChoice.equals("1") && !orderChoice.equals("2"))
		{
			System.out.println("Do you want to sort the results in : \n 1. Ascending order\n 2. Descending order");
			orderChoice = scanner.nextLine();

			if(!orderChoice.equals("1") && !orderChoice.equals("2"))
			{
				System.out.println("Invalid input! Please enter 1 for ascending or 2 for descending.");
			}
		}

		// Check if user chose ascending or descending
		boolean isAscending = true; // Default is ascending
		if(orderChoice.equals("2"))
		{
			isAscending = false;
		}

		// Sort the result list by Annual Fee
		resultCardList = mergeSort.sortByAnnualFee(resultCardList, isAscending);

		// Display the result
		if(resultCardList.isEmpty())
		{
			System.out.println("No credit cards found for the specified bank name.");
		}
		else
		{
			System.out.println("Credit cards found for the bank: " + userInput);
			printCreditCardData(resultCardList);
		}

		return resultCardList;
	}

	private static ArrayList<CreditCard> basedOnAnnualFee(String userInput, ArrayList<CreditCard> cardList)
	{
		ArrayList<CreditCard> resultCardList = new ArrayList<>();
		Scanner scanner = new Scanner(System.in);

		// Validate input
		while(!Validation.isValidInterestRate(userInput))
		{
			System.out.println("Invalid annual fee. Please enter a valid non-negative number:");
			userInput = scanner.nextLine();
		}

		// Convert the validated input to a double
		double userAnnualFee = -1;
		try
		{
			userAnnualFee = Double.parseDouble(userInput); // Convert String to double
		}
		catch(NumberFormatException e)
		{
			System.out.println("Error: Invalid number format.");
			return resultCardList; // Return empty list if input is invalid
		}

		// Filter the cards based on annual fee
		for(CreditCard card : cardList)
		{
			try
			{
				// Extract numeric part from the annual fee string (remove non-numeric characters)
				String annualFeeString = card.getAnnualFee().replaceAll("[^0-9.]", "");

				// If the string is not empty after removing non-numeric characters
				if(!annualFeeString.isEmpty())
				{
					double cardAnnualFee = Double.parseDouble(annualFeeString);

					// Now compare the annual fee
					if(cardAnnualFee <= userAnnualFee)
					{
						resultCardList.add(card);
					}
				}
				else
				{
					// Handle case where annual fee is not a valid number
					//System.out.println("Invalid annual fee for card: " + card.getCardName());
				}
			}
			catch(NumberFormatException e)
			{
				// In case parsing still fails
				System.out.println("Error parsing the annual fee for card: " + card.getCardName());
			}
		}

		// Ask the user for sorting preference: 1 for Ascending, 2 for Descending
		String orderChoice = "";
		while(!orderChoice.equals("1") && !orderChoice.equals("2"))
		{
			System.out.println("Do you want to sort the results in Annual Fee by: \n 1. Ascending order\n 2. Descending order");
			orderChoice = scanner.nextLine();

			if(!orderChoice.equals("1") && !orderChoice.equals("2"))
			{
				System.out.println("Invalid input! Please enter 1 for ascending or 2 for descending.");
			}
		}

		// Check if user chose ascending or descending
		boolean isAscending = true; // Default is ascending
		if(orderChoice.equals("2"))
		{
			isAscending = false;
		}

		// Sort the result list using MergeSort
		resultCardList = mergeSort.sort(resultCardList, isAscending); // Corrected method call

		// Handle the result and display
		if(resultCardList.isEmpty())
		{
			System.out.println("No credit cards found with an annual fee of " + userAnnualFee + " or less.");
		}
		else
		{
			System.out.println("Credit cards found with an annual fee of " + userAnnualFee + " or less:");
			printCreditCardData(resultCardList);
		}

		return resultCardList;
	}

	// implement word completion, spell checking and validation.
	private static ArrayList<CreditCard> basedOnCardType(String userInput, ArrayList<CreditCard> cardList)
	{
		ArrayList<CreditCard> resultCardList = new ArrayList<>();
		Scanner scanner = new Scanner(System.in);

		// Validate the user input (you may add any specific validation for the input)
		while(userInput == null || userInput.isEmpty())
		{
			System.out.println("Invalid input. Please enter a valid card type:");
			userInput = scanner.nextLine();
		}

		// Filter the cards based on the card type (case-insensitive matching)
		for(CreditCard card : cardList)
		{
			if(card.getCardType() != null && card.getCardType().toLowerCase().contains(userInput.toLowerCase()))
			{
				resultCardList.add(card);
			}
		}

		// Ask the user for sorting preference: 1 for Ascending, 2 for Descending
		String orderChoice = "";
		while(!orderChoice.equals("1") && !orderChoice.equals("2"))
		{
			System.out.println("Do you want to sort the results by Annual Fee in \n 1. Ascending order\n 2. Descending order");
			orderChoice = scanner.nextLine();

			if(!orderChoice.equals("1") && !orderChoice.equals("2"))
			{
				System.out.println("Invalid input! Please enter 1 for ascending or 2 for descending.");
			}
		}

		// Check if user chose ascending or descending
		boolean isAscending = true; // Default is ascending
		if(orderChoice.equals("2"))
		{
			isAscending = false;
		}

		// Sort the result list by Annual Fee
		resultCardList = mergeSort.sortByAnnualFee(resultCardList, isAscending);

		// Display the result
		if(resultCardList.isEmpty())
		{
			System.out.println("No credit cards found for the specified card type.");
		}
		else
		{
			System.out.println("Credit cards found for the card type: " + userInput);
			printCreditCardData(resultCardList);
		}

		return resultCardList;
	}

	private static void wordFrequencyRankBased(ArrayList<CreditCard> cardList) throws IOException {
		Scanner scanner = new Scanner(System.in); // Use Scanner for user input
		System.out.println("Enter the word you want to look for in the bank website:");
	
		// Get valid word input using the validation method
		String word = Validation.getWordOnlyInput(scanner, "Enter only words: ");
	
		// Process the input and perform the ranking
		Map<String, Integer> rankedWiseWebsite = PageRanking.RankBankBasedOnWordFrequency(word, cardList);
	
		// Display the ranking results
		for (Map.Entry<String, Integer> rankedData : rankedWiseWebsite.entrySet()) {
			System.out.println(rankedData.getKey() + " - Score: " + rankedData.getValue());
		}
	}
	

	//Case 4 Popular Search 
	public static void viewPopularSearchTerms() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String fieldChoice;
	
			while (true) {
				System.out.println("\nChoose a field to view popular search terms:");
				System.out.println("1. Bank Name");
				System.out.println("2. Card Name");
				System.out.println("3. Card Type");
				System.out.print("Enter your choice (1-3): ");
				
				fieldChoice = reader.readLine().trim(); // Read and trim input
	
				// Validate input
				if (fieldChoice.equals("1")) {
					SearchFrequencyRBTree.displaySearchTerms("Bank Name");
					break; // Exit the loop after processing a valid choice
				} else if (fieldChoice.equals("2")) {
					SearchFrequencyRBTree.displaySearchTerms("Card Name");
					break;
				} else if (fieldChoice.equals("3")) {
					SearchFrequencyRBTree.displaySearchTerms("Card Type");
					break;
				} else {
					System.out.println("Invalid choice. Please enter a valid option (1, 2, or 3).");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
