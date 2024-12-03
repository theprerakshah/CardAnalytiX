package com.creditWise.CardAnalytiX;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import com.creditWise.DataHandler.BankNameMap;
import com.creditWise.DataHandler.CardTypeMap;
import com.creditWise.DataHandler.HtmlToText;
import com.creditWise.DataHandler.Webcrawler;
import com.creditWise.Mahzabin.SpellCheck;
import com.creditWise.Mahzabin.WordCompletion;
import com.creditWise.Prerak.WordFrequency;
import com.creditWise.Prerak.WordSearcher;
import com.creditWise.Sagar.Validation;
import com.creditWise.Sagar.mergeSort;
import com.creditWise.Sakshi.SearchFrequencyRBTree;

/**
 * Credit Card Suggestion Tool
 */
public class Executer
{
	private static WordCompletion wordCompletion = new WordCompletion();
	private static SpellCheck spellCheck = new SpellCheck();
	private static BankNameMap bankNameMap =new BankNameMap();
	private static Webcrawler Webcrawler = new Webcrawler();

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
				    Webcrawler.main(args);
					HtmlToText.main(args);
					
					case2Handler();
					return;

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
			System.out.println("6. Go Back to Main Menu");
		
			System.out.print("Enter your choice (1, 2, 3, 4, 5 or 6): ");

			ArrayList<CreditCard> cardList = Engine.Engine1();
			int choice = scanner.nextInt();
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
				return;
				case 5:
				    viewPopularSearchTerms();
				    return;
				
				case 6:
				System.out.println("Going back to the main menu...");
				return;
				

				default:
					System.out.println("Invalid choice. Please restart the tool and enter 1, 2, 3, or 4:");
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

					if (sc.hasNextLine()) sc.nextLine(); // Clears leftover newline
					System.out.println("Select card type from this Options: [MasterCard, Visa]");
					System.out.println("Input:");
					userInput = sc.nextLine();

					while(!Validation.ValidationCardType(userInput))
					{
						System.out.println("Enter a correct Card Type");
						userInput = sc.nextLine();
					}

					userInput=spellCheckAndWordComplete(userInput);
					while(userInput.equalsIgnoreCase("Try Again")){
						System.out.println("No suggested Spelling or Word Completion Found for this input. Try Again.");
						userInput = sc.nextLine();
						userInput=spellCheckAndWordComplete(userInput);
					}

					String cardType = CardTypeMap.getCardType(userInput);
					while (cardType.equalsIgnoreCase("Null")){
						System.out.println("Please Enter a Valid Card Type. [Like- Visa Card\n" +
								"    American Express\n" +
								"    COSTCO CARDS\n" +
								"    STUDENT CARDS\n" +
								"    CASH BACK CARDS\n" +
								"    TRAVEL REWARDS CARDS\n" +
								"    BUSINESS CREDIT CARDS\n" +
								"    LOW INTEREST CARDS\n" +
								"    Mastercard]");
						userInput = sc.nextLine();
						cardType =CardTypeMap.getCardType(userInput);
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

					if (sc.hasNextLine()) sc.nextLine(); // Clears leftover newline
					System.out.println("Select card based on Bank Name, Enter Your preferd Bank:[RBC, Scotia Bank, CIBC, TD Bank]");
					userInput = sc.nextLine();

					while(!Validation.ValidationBankName(userInput))
					{
						System.out.println("Enter a Correct Bank Name ");
						userInput = sc.nextLine();
					}
					userInput=spellCheckAndWordComplete(userInput);
					while(userInput.equalsIgnoreCase("Try Again")){
						System.out.println("Enter a Bank Name");
						userInput = sc.nextLine();
						userInput=spellCheckAndWordComplete(userInput);
					}


					String bankName = bankNameMap.getBankName(userInput);
					while (bankName.equalsIgnoreCase("Null")){
						System.out.println("Please Enter a Valid Bank Name. [Like- TD bank/ CIBC/ RBC/ Scotia Bank]");
						userInput = sc.nextLine();
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
				System.out.println("To choose a suggested word type the number associated with it. or Type 0 to not choose any and type again.");
				int input = Integer.parseInt(scanner.nextLine());
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
				System.out.println(userInput + " might be spelled incorrectly.");
				List<String> correctSpell = spellCheck.suggestAlternatives(userInput);
				if(!correctSpell.isEmpty())
				{
					System.out.println("Did you mean these?");
					for(int i = 0; i < correctSpell.size(); i++)
					{
						System.out.println((i + 1) + " " + correctSpell.get(i));
					}
					System.out.println("To choose a suggested spelling type the number associated with it. Type 0 if none matches and you want to Type again.");
					int input = Integer.parseInt(scanner.nextLine());
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
////Test remove later
//	private static void printCreditCardType(ArrayList<CreditCard> cardList) {
//		// Use a HashSet to store unique card types
//		HashSet<String> uniqueCardTypes = new HashSet<>();
//
//		// Add each card type to the HashSet
//		for (CreditCard card : cardList) {
//			uniqueCardTypes.add(card.getCardType());
//		}
//
//		// Print the unique card types
//		System.out.println("Unique Credit Card Types:");
//		for (String cardType : uniqueCardTypes) {
//			System.out.println(cardType);
//		}
//	}
	// Method to print all credit card data
	private static void printCreditCardData(ArrayList<CreditCard> cardList) {
		if (cardList.isEmpty()) {
			System.out.println("No credit card data available.");
			return;
		}

		System.out.println("Credit Card Details:");
		for (CreditCard card : cardList) {
			System.out.println("-----------------------------------------");
			System.out.println("Bank Name: " + card.getBank());
			System.out.println("Card Name: " + card.getCardName());
			System.out.println("Card Type: " + card.getCardType());
			System.out.println("Annual Fee: " + card.getAnnualFee());
			System.out.println("Purchase Interest Rate: " + card.getPurchaseInterestRate());
			System.out.println("Additional Features: " + card.getAdditionalFeatures());

		}
	}

	private static void documentWordSearchAndFrequency(ArrayList<CreditCard> cardList) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String word;
	
		// Loop until valid input is provided
		while (true) {
			System.out.println("Enter the word you want to look for in the bank website:");
			word = reader.readLine();
	
			// Validate input using the regex
			if (Validation.documentWordSearchAndFrequency(word)) {
				break; // Exit loop if input is valid
			} else {
				System.out.println("Invalid input. Please enter a valid word.");
			}
		}
	
		HashMap<String, HashMap<String, ArrayList<String>>> dataForFrequency = WordSearcher.invertedIndexing(cardList, word.trim().toLowerCase());
		if (dataForFrequency != null) {
			System.out.println("Do you want word count from this bankWebsite? (Y | N):");
			String freqInput = reader.readLine();
			freqInput = freqInput.trim();
	
			if (freqInput.equalsIgnoreCase("Y")) {
				WordFrequency.countFrequency(dataForFrequency);
			} else if (freqInput.equalsIgnoreCase("N")) {
				System.out.println("Skipping word count...");
			} else {
				System.out.println("Invalid input. Please enter 'Y' or 'N'.");
			}
		}
	}

	private static ArrayList<CreditCard> basedOnInterestRate(String userInput, ArrayList<CreditCard> cardList) {
		ArrayList<CreditCard> resultCardList = new ArrayList<>();
		Scanner scanner = new Scanner(System.in);
	
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
					System.out.println("Invalid interest rate for card: " + card.getCardName());
				}
			} catch (NumberFormatException e) {
				System.out.println("Error parsing the interest rate for card: " + card.getCardName());
			}
		}
	
		// Ask the user for sorting preference: 1 for Ascending, 2 for Descending
		String orderChoice = "";
		while (!orderChoice.equals("1") && !orderChoice.equals("2")) {
			System.out.println("Do you want to sort the results by Interest Rate in : \n 1. Ascending order\n 2. Descending order");
			orderChoice = scanner.nextLine();
	
			if (!orderChoice.equals("1") && !orderChoice.equals("2")) {
				System.out.println("Invalid input! Please enter 1 for ascending or 2 for descending.");
			}
		}
	
		// Check if user chose ascending or descending
		boolean isAscending = true; // Default is ascending
		if (orderChoice.equals("2")) {
			isAscending = false;
		}
	
		// Sort the result list using MergeSort
		resultCardList = mergeSort.sort(resultCardList, isAscending);
	
		// Handle the result and display
		if (resultCardList.isEmpty()) {
			System.out.println("No credit cards found with an interest rate of " + userInterestRate + " or less.");
		} else {
			System.out.println("Credit cards found with an interest rate of " + userInterestRate + " or less:");
			for (CreditCard card : resultCardList) {
				System.out.println("Card Name: " + card.getCardName() + ", Interest Rate: " + card.getPurchaseInterestRate());
			}
		}
	
		return resultCardList;
	}
	

	// Implement word Completion, spell checking , validation
	private static ArrayList<CreditCard> basedOnBankName(String userInput, ArrayList<CreditCard> cardList) {
		ArrayList<CreditCard> resultCardList = new ArrayList<>();
		Scanner scanner = new Scanner(System.in);
	
		// Validate the user input (you may add any specific validation for the input)
		while (userInput == null || userInput.isEmpty()) {
			System.out.println("Invalid input. Please enter a valid bank name:");
			userInput = scanner.nextLine();
		}
	
		// Filter the cards based on the bank name (case insensitive matching)
		for (CreditCard card : cardList) {
			if (card.getBank() != null && card.getBank().toLowerCase().contains(userInput.toLowerCase())) {
				resultCardList.add(card);
			}
		}
	
		// Ask the user for sorting preference: 1 for Ascending, 2 for Descending
		String orderChoice = "";
		while (!orderChoice.equals("1") && !orderChoice.equals("2")) {
			System.out.println("Do you want to sort the results in : \n 1. Ascending order\n 2. Descending order");
			orderChoice = scanner.nextLine();
	
			if (!orderChoice.equals("1") && !orderChoice.equals("2")) {
				System.out.println("Invalid input! Please enter 1 for ascending or 2 for descending.");
			}
		}
	
		// Check if user chose ascending or descending
		boolean isAscending = true; // Default is ascending
		if (orderChoice.equals("2")) {
			isAscending = false;
		}
	
		// Sort the result list by Annual Fee
		resultCardList = mergeSort.sortByAnnualFee(resultCardList, isAscending);
	
		// Display the result
		if (resultCardList.isEmpty()) {
			System.out.println("No credit cards found for the specified bank name.");
		} else {
			System.out.println("Credit cards found for the bank: " + userInput);
			for (CreditCard card : resultCardList) {
				System.out.println("Card Name: " + card.getCardName() + ", Bank Name: " + card.getBank() + ", Annual Fee: " + card.getAnnualFee());
			}
		}
	
		return resultCardList;
	}
	

	private static ArrayList<CreditCard> basedOnAnnualFee(String userInput, ArrayList<CreditCard> cardList) {
        ArrayList<CreditCard> resultCardList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
    
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
                    //System.out.println("Invalid annual fee for card: " + card.getCardName());
                }
            } catch (NumberFormatException e) {
                // In case parsing still fails
                System.out.println("Error parsing the annual fee for card: " + card.getCardName());
            }
        }
    
        // Ask the user for sorting preference: 1 for Ascending, 2 for Descending
        String orderChoice = "";
        while (!orderChoice.equals("1") && !orderChoice.equals("2")) {
            System.out.println("Do you want to sort the results in Annual Fee by: \n 1. Ascending order\n 2. Descending order");
            orderChoice = scanner.nextLine();
            
            if (!orderChoice.equals("1") && !orderChoice.equals("2")) {
                System.out.println("Invalid input! Please enter 1 for ascending or 2 for descending.");
            }
        }
    
        // Check if user chose ascending or descending
        boolean isAscending = true; // Default is ascending
        if (orderChoice.equals("2")) {
            isAscending = false;
        }
    
        // Sort the result list using MergeSort
        resultCardList = mergeSort.sort(resultCardList, isAscending);  // Corrected method call
    
        // Handle the result and display
        if (resultCardList.isEmpty()) {
            System.out.println("No credit cards found with an annual fee of " + userAnnualFee + " or less.");
        } else {
            System.out.println("Credit cards found with an annual fee of " + userAnnualFee + " or less:");
            for (CreditCard card : resultCardList) {
                System.out.println("Card Name: " + card.getCardName() + ", Annual Fee: " + card.getAnnualFee());
            }
        }
    
        return resultCardList;
    }
    

	// implement word completion, spell checking and validation.
	private static ArrayList<CreditCard> basedOnCardType(String userInput, ArrayList<CreditCard> cardList) {
		ArrayList<CreditCard> resultCardList = new ArrayList<>();
		Scanner scanner = new Scanner(System.in);
	
		// Validate the user input (you may add any specific validation for the input)
		while (userInput == null || userInput.isEmpty()) {
			System.out.println("Invalid input. Please enter a valid card type:");
			userInput = scanner.nextLine();
		}
	
		// Filter the cards based on the card type (case-insensitive matching)
		for (CreditCard card : cardList) {
			if (card.getCardType() != null && card.getCardType().toLowerCase().contains(userInput.toLowerCase())) {
				resultCardList.add(card);
			}
		}
	
		// Ask the user for sorting preference: 1 for Ascending, 2 for Descending
		String orderChoice = "";
		while (!orderChoice.equals("1") && !orderChoice.equals("2")) {
			System.out.println("Do you want to sort the results by Annual Fee in \n 1. Ascending order\n 2. Descending order");
			orderChoice = scanner.nextLine();
	
			if (!orderChoice.equals("1") && !orderChoice.equals("2")) {
				System.out.println("Invalid input! Please enter 1 for ascending or 2 for descending.");
			}
		}
	
		// Check if user chose ascending or descending
		boolean isAscending = true; // Default is ascending
		if (orderChoice.equals("2")) {
			isAscending = false;
		}
	
		// Sort the result list by Annual Fee
		resultCardList = mergeSort.sortByAnnualFee(resultCardList, isAscending);
	
		// Display the result
		if (resultCardList.isEmpty()) {
			System.out.println("No credit cards found for the specified card type.");
		} else {
			System.out.println("Credit cards found for the card type: " + userInput);
			for (CreditCard card : resultCardList) {
				System.out.println("Card Name: " + card.getCardName() + ", Card Type: " + card.getCardType() + ", Annual Fee: " + card.getAnnualFee());
			}
		}
	
		return resultCardList;
	}
	


	//Case 4 Popular Search 
	  public static void viewPopularSearchTerms() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("\nChoose a field to view popular search terms:");
            System.out.println("1. Bank Name");
            System.out.println("2. Card Name");
            System.out.println("3. Card Type");
        
    
            System.out.print("Enter your choice (1-3): ");
            String fieldChoice = reader.readLine(); // Read input
    
            switch (fieldChoice) {
                case "1":
                    SearchFrequencyRBTree.displaySearchTerms("Bank Name");
                    break;
                case "2":
                    SearchFrequencyRBTree.displaySearchTerms("Card Name");
                    break;
                case "3":
                    SearchFrequencyRBTree.displaySearchTerms("Card Type");
                    break;
            
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
