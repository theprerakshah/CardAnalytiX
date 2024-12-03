package com.creditWise.CardAnalytiX;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.creditWise.DataHandler.BankNameMap;
import com.creditWise.Mahzabin.SpellCheck;
import com.creditWise.Mahzabin.WordCompletion;
import com.creditWise.Prerak.WordFrequency;
import com.creditWise.Prerak.WordSearcher;
import com.creditWise.Sagar.Validation;

/**
 * Credit Card Suggestion Tool
 */
public class Executer
{
	private static WordCompletion wordCompletion = new WordCompletion();
	private static SpellCheck spellCheck = new SpellCheck();
	private static BankNameMap bankNameMap =new BankNameMap();

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

			int choice = scanner.nextInt();

			switch(choice)
			{
				case 1:
					System.out.println("\nYou chose to crawl the website.");
					System.out.println("Initializing web crawler...");
					// WebCrawler webCrawler = new WebCrawler();
					// webCrawler.startCrawling();
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
					System.out.println("Invalid choice. Please restart the tool and enter 1 or 2.");
					break;
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
			System.out.println("4. Go Back to Main Menu");
			System.out.print("Enter your choice (1, 2, 3, or 4): ");

			ArrayList<CreditCard> cardList = Engine.Engine1();
			int choice = scanner.nextInt();
			switch(choice)
			{
				case 1:
					System.out.println("Fetching all Credit card Data");
					System.out.println("Showing all Credit card Data");
					break;

				case 2:
					prefernceBaseCaseHandler(cardList);

					break;
				case 3:
					documentWordSearchAndFrequency(cardList);

					break;
				case 4:
					System.out.println("Going back to the main menu...");
					return;

				default:
					System.out.println("Invalid choice. Please restart the tool and enter 1 or 2.");
					break;
			}
		}
	}

	private static void prefernceBaseCaseHandler(ArrayList<CreditCard> cardList)
	{
		Scanner sc = new Scanner(System.in);
		while(true)
		{
			System.out.println("\n1. for Searching card according to Card type ");
			System.out.println("2. for Searching card according to Annual Fee");
			System.out.println("3. for Searching card according to Bank Name");
			System.out.println("4. for Searching card according to Interest Rate");
			System.out.println("5. Go Back to Previous Menu");
			System.out.print("Enter your choice (1, 2, 3, 4, or 5): ");

			int choice = sc.nextInt();
			String userInput = "";
			ArrayList<CreditCard> resultCardList = new ArrayList<CreditCard>();
			switch(choice)
			{
				case 1:
					System.out.println("Select card type from this Options: [MasterCard, Visa]");
					System.out.println("Input:");
					userInput = sc.next();

					while(!Validation.ValidationCardType(userInput))
					{
						System.out.println("Enter a correct Card Type");
						userInput = sc.next();
					}

					userInput=spellCheckAndWordComplete(userInput);
					while(userInput.equalsIgnoreCase("Try Again")){
						System.out.println("No suggested Spelling or Word Completion Found for this input. Try Again.");
						userInput = sc.next();
						userInput=spellCheckAndWordComplete(userInput);
					}
					resultCardList = basedOnCardType(userInput, cardList);

					break;

				case 2:
					System.out.println("Select card based on Annual Fee, Enter Your preferd annual fee:");
					userInput = sc.next();

					while(!Validation.ValidationAnnualFee(userInput))
					{
						System.out.println("Enter Annual Fee in Correct Format ");
						userInput = sc.next();
					}
					resultCardList = basedOnAnnualFee(userInput, cardList);

					break;
				case 3:
					System.out.println("Select card based on Bank Name, Enter Your preferd Bank:[RBC, Soctia Bank, CIBC, TD Bank]");
					userInput = sc.next();

					while(!Validation.ValidationBankName(userInput))
					{
						System.out.println("Enter a Correct Bank Name ");
						userInput = sc.next();
					}
					userInput=spellCheckAndWordComplete(userInput);
					while(userInput.equalsIgnoreCase("Try Again")){
						System.out.println("No suggested Spelling or Word Completion Found for this input. Try Again.");
						userInput = sc.next();
						userInput=spellCheckAndWordComplete(userInput);
					}


					String bankName = bankNameMap.getBankName(userInput);
					while (bankName.equalsIgnoreCase("Null")){
						System.out.println("Please Enter a Valid Bank Name. [Like- TD bank/ CIBC/ RBC/ Scotia Bank]");
						userInput = sc.next();
						bankName =bankNameMap.getBankName(userInput);
					}
					resultCardList = basedOnBankName(userInput, cardList);

					break;
				case 4:
					System.out.println("Select card based on Interest Rate, Enter Your preferd Interest Rate:");
					userInput = sc.next();

					while(!Validation.ValidationInterestRate(userInput))
					{
						System.out.println("Enter Interest Rate in Correct Format");
						userInput = sc.next();
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
	}


	//Spell checking and word completion implementation.
	private static String spellCheckAndWordComplete(String userInput)
	{
		Scanner scanner = new Scanner(System.in);
		String correctspelledWord;
		if(spellCheck.search(userInput))
		{
			//Spelled Correctly
			correctspelledWord = userInput;
			// So move to autocomplete
			System.out.println("Suggested Autocomplete words: ");
			// Generating list of word complete suggestions based on user's input.
			List<String> suggestions = wordCompletion.autocomplete(correctspelledWord, 5);
			for(int i = 0; i < suggestions.size(); i++)
			{
				System.out.println((i + 1) + " " + suggestions.get(i));
			}
			System.out.println("To choose a suggested word type the number associated with it. or Type 0 to not choose any of the suggestions.");
			int input = Integer.parseInt(scanner.nextLine());
			if(input == 0)
			{
				return correctspelledWord;
			}
			else
			{
				return suggestions.get(input - 1);
			}
		}
		else
		{
			if(wordCompletion.doesPrefixExist(userInput))
			{
				System.out.println("Suggested Autocomplete words: ");
				// Generating list of word complete suggestions based on user's input.
				List<String> suggestions = wordCompletion.autocomplete(userInput, 5);
				for(int i = 0; i < suggestions.size(); i++)
				{
					System.out.println((i + 1) + " " + suggestions.get(i));
				}
				System.out.println("To choose a suggested word type the number associated with it. or Type 0 to not choose any of the suggestions.");
				int input = Integer.parseInt(scanner.nextLine());
				if(input == 0)
				{
					return userInput;
				}
				else
				{
					return suggestions.get(input - 1);
				}
			}
			else
			{
				System.out.println(userInput + " might be spelled incorrectly.");
				List<String> correctSpell = spellCheck.suggestAlternatives(userInput);
				if(!correctSpell.isEmpty())
				{
					System.out.println("Did you mean these?");
					for(int i = 0; i < correctSpell.size(); i++)
					{
						System.out.println((i + 1) + " " + correctSpell.get(i));
					}
					System.out.println("To choose a suggested spelling type the number associated with it. Type 0 to not choose any.");
					int input = Integer.parseInt(scanner.nextLine());
					if(input == 0)
					{
						return userInput;
					}
					else
					{
						return correctSpell.get(input - 1);
					}
				}
				else
				{
					System.out.println("No suggested Spelling or Word Completion Found for this input");
					return userInput;
				}
			}

		}

	}

	private static void documentWordSearchAndFrequency(ArrayList<CreditCard> cardList) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the word you want to look for in bank website:");
		String word = reader.readLine();
		HashMap<String, HashMap<String, ArrayList<String>>> dataForFrequecy = WordSearcher.invertedIndexing(cardList, word.trim().toLowerCase());
		if(dataForFrequecy != null)
		{
			System.out.println("Do you want word count from this bankWebsite? (Y | N):");
			String freqInput = reader.readLine();
			freqInput = freqInput.trim();

			if(freqInput.equalsIgnoreCase("Y"))
			{
				WordFrequency.countFrequency(dataForFrequecy);
			}
			else if(freqInput.equalsIgnoreCase("N"))
			{
				System.out.println("Skipping word count...");
			}
			else
			{
				System.out.println("Invalid input. Please enter 'Y' or 'N'.");
			}
		}

	}

	// implement Validation
	private static ArrayList<CreditCard> basedOnInterestRate(String userInput, ArrayList<CreditCard> cardList) {
		ArrayList<CreditCard> resultCardList = new ArrayList<>();
		Scanner scanner = new Scanner(System.in);
<<<<<<< HEAD
	
		// Validate input
		while (!Validation.isValidInterestRate(userInput)) {
			System.out.println("Invalid Interest Rate. Please enter a valid non-negative number:");
			userInput = scanner.nextLine();
		}
	
		// Convert the validated input to a double
		double userInterestRate = -1;
		try {
			userInterestRate = Double.parseDouble(userInput); // Convert String to double
		} catch (NumberFormatException e) {
			System.out.println("Error: Invalid number format.");
			return resultCardList; // Return empty list if input is invalid
		}
	
		// Filter the cards based on interest rate
		for (CreditCard card : cardList) {
			try {
				// Extract numeric part from the interest rate string (remove non-numeric characters)
				String interestRateString = card.getPurchaseInterestRate().replaceAll("[^0-9.]", "");
	
				// If the string is not empty after removing non-numeric characters
				if (!interestRateString.isEmpty()) {
					double cardInterestRate = Double.parseDouble(interestRateString);
	
					// Now compare the interest rate
					if (cardInterestRate <= userInterestRate) {
						resultCardList.add(card);
					}
				} else {
					// Handle case where interest rate is not a valid number
					System.out.println("Invalid interest rate for card: " + card.getCardName());
				}
			} catch (NumberFormatException e) {
				// In case parsing still fails
				System.out.println("Error parsing the interest rate for card: " + card.getCardName());
			}
		}
	
		// Handle the result and display
		if (resultCardList.isEmpty()) {
			System.out.println("No credit cards found with an interest rate of " + userInterestRate + " or less.");
		} else {
			System.out.println("Credit cards found with an interest rate of " + userInterestRate + " or less:");
			for (CreditCard card : resultCardList) {
				System.out.println("Card Name: " + card.getCardName() + ", Interest Rate: " + card.getPurchaseInterestRate());
			}
		}
	
=======

		// Validate input
		while(!Validation.isValidInterestRate(userInput))
		{
			System.out.println("Invalid Interest Rate. Please enter a valid non-negative number:");
			userInput = scanner.nextLine();
		}

		// Convert the validated input to a double
		double userInterestRate = Double.parseDouble(userInput);

		if(resultCardList.isEmpty())
		{
			System.out.println("No credit cards found with an Interest Rate of " + userInterestRate + " or less.");
		}

>>>>>>> 077da0a6a0fb9558bdfa5c9aeaa26e6f16aabbc5
		return resultCardList;
	}

	// Implement word Completion, spell checking , validation
	private static ArrayList<CreditCard> basedOnBankName(String userInput, ArrayList<CreditCard> cardList)
	{
		// TODO Auto-generated method stub

		return null;
	}

	// implement  validation.
	private static ArrayList<CreditCard> basedOnAnnualFee(String userInput, ArrayList<CreditCard> cardList) {
		ArrayList<CreditCard> resultCardList = new ArrayList<>();
		Scanner scanner = new Scanner(System.in);
<<<<<<< HEAD
	
		// Validate input
		while (!Validation.isValidInterestRate(userInput)) {
			System.out.println("Invalid annual fee. Please enter a valid non-negative number:");
			userInput = scanner.nextLine();
		}
	
		// Convert the validated input to a double
		double userAnnualFee = -1;
		try {
			userAnnualFee = Double.parseDouble(userInput); // Convert String to double
		} catch (NumberFormatException e) {
			System.out.println("Error: Invalid number format.");
			return resultCardList; // Return empty list if input is invalid
		}
	
		// Filter the cards based on annual fee
		for (CreditCard card : cardList) {
			try {
				// Extract numeric part from the annual fee string (remove non-numeric characters)
				String annualFeeString = card.getAnnualFee().replaceAll("[^0-9.]", "");
				
				// If the string is not empty after removing non-numeric characters
				if (!annualFeeString.isEmpty()) {
					double cardAnnualFee = Double.parseDouble(annualFeeString);
					
					// Now compare the annual fee
					if (cardAnnualFee <= userAnnualFee) {
						resultCardList.add(card);
					}
				} else {
					// Handle case where annual fee is not a valid number
					System.out.println("Invalid annual fee for card: " + card.getCardName());
				}
			} catch (NumberFormatException e) {
				// In case parsing still fails
				System.out.println("Error parsing the annual fee for card: " + card.getCardName());
			}
		}
		
	
		// Handle the result and display
		if (resultCardList.isEmpty()) {
			System.out.println("No credit cards found with an annual fee of " + userAnnualFee + " or less.");
		} else {
			System.out.println("Credit cards found with an annual fee of " + userAnnualFee + " or less:");
			for (CreditCard card : resultCardList) {
				System.out.println("Card Name: " + card.getCardName() + ", Annual Fee: " + card.getAnnualFee());
			}
		}
	
=======

		// Validate input
		while(!Validation.isValidInterestRate(userInput))
		{
			System.out.println("Invalid annual fee. Please enter a valid non-negative number:");
			userInput = scanner.nextLine();
		}

		// Convert the validated input to a double
		double userAnnualFee = Double.parseDouble(userInput);

		if(resultCardList.isEmpty())
		{
			System.out.println("No credit cards found with an annual fee of " + userAnnualFee + " or less.");
		}

>>>>>>> 077da0a6a0fb9558bdfa5c9aeaa26e6f16aabbc5
		return resultCardList;
	}
	
	

	// implement word completion, spell checking and validation.
	private static ArrayList<CreditCard> basedOnCardType(String userInputCardType, ArrayList<CreditCard> cardList)
	{
		ArrayList<CreditCard> resultCardList = new ArrayList<CreditCard>();
		return resultCardList;
	}

}
