package com.creditWise.CardAnalytiX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.creditWise.Sagar.BankNameExtractor;
import com.creditWise.Sagar.CardTypeExtractor;
import com.creditWise.Sagar.SpellChecker;
import com.creditWise.Sagar.Validation;

/**
 * Credit Card Suggestion Tool
 */
public class Executer
{

	public static void main(String[] args) throws IOException
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to the Credit Card Suggestion Tool!");
		System.out.println("Please answer the following question to get your best credit card suggestion:");
		System.out.println("1. Do you want to crawl the website?");
		System.out.println("2. Or do you want to use existing data?");
		System.out.print("Enter your choice (1 or 2): ");

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

			default:
				System.out.println("Invalid choice. Please restart the tool and enter 1 or 2.");
				break;
		}

		scanner.close();
	}

	public static void case2Handler() throws IOException
	{
		System.out.println("\n1. Show all Credit card Data");
		System.out.println("2. Fetch credit card according to this preferences: Card Type, Annual Fee, Bank Name, Interest Rate");
		System.out.println("Enter your choice (1 or 2): ");
		Scanner scanner = new Scanner(System.in);
		ArrayList<CreditCard> cardList = Engine.Engine1();
		//		for(CreditCard creditCard : cardList)
		//		{
		//			System.out.println(creditCard.cardType);
		//		}
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

			default:
				System.out.println("Invalid choice. Please restart the tool and enter 1 or 2.");
				break;
		}

		scanner.close();
	}

	private static void prefernceBaseCaseHandler(ArrayList<CreditCard> cardList)
	{
		System.out.println("\n1. for Searching card according to Card type ");
		System.out.println("2. for Searching card according to Annual Fee");
		System.out.println("3. for Searching card according to Bank Name");
		System.out.println("4. for Searching card according to Interest Rate");
		System.out.println("Enter your choice (1, 2, 3, or 4): ");
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		sc.nextLine();
		String userInput = "";
		ArrayList<CreditCard> resultCardList = new ArrayList<CreditCard>();
		switch(choice)
		{
			case 1:
				System.out.println("Select card type from this Options: [Master Card, Visa Card]");
				System.out.println("Input:");
				userInput = sc.nextLine();
				resultCardList = basedOnCardType(userInput, cardList);

				break;

			case 2:
				System.out.println("Select card based on Annual Fee, Enter Your preferd annual fee:");
				userInput = sc.nextLine();
				resultCardList = basedOnAnnualFee(userInput, cardList);

				break;
			case 3:
				System.out.println("Select card based on Bank Name, Enter Your preferd Bank:[RBC, Soctia Bank, CIBC, TD Bank]");
				userInput = sc.nextLine();
				resultCardList = basedOnBankName(userInput, cardList);

				break;
			case 4:
				System.out.println("Select card based on Annual Fee, Enter Your preferd annual fee:");
				userInput = sc.nextLine();
				resultCardList = basedOnInterestRate(userInput, cardList);

				break;

			default:
				System.out.println("Invalid choice. Please restart the tool and enter 1 or 2.");
				break;
		}

	}

	// implement Validation
	private static ArrayList<CreditCard> basedOnInterestRate(String userInput, ArrayList<CreditCard> cardList)
	{
		ArrayList<CreditCard> resultCardList = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    // Validate input
    while (!Validation.isValidInterestRate(userInput)) {
        System.out.println("Invalid Interest Rate. Please enter a valid non-negative number:");
        userInput = scanner.nextLine();
    }

    // Convert the validated input to a double
    double userInterestRate = Double.parseDouble(userInput);

    if (resultCardList.isEmpty()) {
        System.out.println("No credit cards found with an Interest Rate of " + userInterestRate + " or less.");
    }

    return resultCardList;
	}	
	

	// Implement word Completion, spell checking , validation
	private static ArrayList<CreditCard> basedOnBankName(String userInput, ArrayList<CreditCard> cardList) {
		ArrayList<CreditCard> resultCardList = new ArrayList<>();
		Scanner scanner = new Scanner(System.in);
	
		// Extract valid bank names
		List<String> validBankNames = extractBankNames(cardList);
	
		// Use SpellChecker to find the closest match for the bank name
		String correctedBankName = SpellChecker.findClosestMatch(userInput, validBankNames);
	
		if (correctedBankName != null) {
			System.out.println("Did you mean: " + correctedBankName + "?");
	
			// Prompt for confirmation from the user
			System.out.println("If yes, press Enter. If no, please enter the correct bank name:");
			String userConfirmation = scanner.nextLine().trim();
	
			// If the user confirms, use the corrected bank name
			if (userConfirmation.isEmpty()) {
				userInput = correctedBankName;
			} else {
				userInput = userConfirmation;
			}
		}
	
		// Validate the user input if necessary (could be a separate method for validation)
		while (!Validation.isValidBankName(userInput, validBankNames)) {
			System.out.println("Invalid bank name. Please enter a valid bank name:");
			userInput = scanner.nextLine();
		}
	
		// Filter the cards based on the validated or corrected bank name
		for (CreditCard card : cardList) {
			if (card.getBank().equalsIgnoreCase(userInput)) {
				resultCardList.add(card);
			}
		}
	
		if (resultCardList.isEmpty()) {
			System.out.println("No credit cards found for the bank: " + userInput);
		} else {
			System.out.println("Credit cards from the bank: " + userInput);
			for (CreditCard card : resultCardList) {
				System.out.println("Card Name: " + card.getCardName() + ", Card Type: " + card.getCardType());
			}
		}
	
		return resultCardList;
	}
	

// Local method to extract bank names
private static List<String> extractBankNames(ArrayList<CreditCard> cardList) {
    Set<String> bankNames = new HashSet<>();
    for (CreditCard card : cardList) {
        bankNames.add(card.getBank());
    }
    return new ArrayList<>(bankNames);
}

// Helper method to get all available bank names from the card list
/*private static List<String> getAvailableBankNames(ArrayList<CreditCard> cardList) {
    List<String> bankNames = new ArrayList<>();
    for (CreditCard card : cardList) {
        bankNames.add(card.getBank());
    }
    return bankNames;
	}*/

	// implement  validation.
	private static ArrayList<CreditCard> basedOnAnnualFee(String userInput, ArrayList<CreditCard> cardList) {
    ArrayList<CreditCard> resultCardList = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    // Validate input
    while (!Validation.isValidInterestRate(userInput)) {
        System.out.println("Invalid annual fee. Please enter a valid non-negative number:");
        userInput = scanner.nextLine();
    }

    // Convert the validated input to a double
    double userAnnualFee = Double.parseDouble(userInput);

    if (resultCardList.isEmpty()) {
        System.out.println("No credit cards found with an annual fee of " + userAnnualFee + " or less.");
    }

    return resultCardList;
}


	// implement word completion, spell checking and validation.
	private static ArrayList<CreditCard> basedOnCardType(String userInputCardType, ArrayList<CreditCard> cardList)
	{
		ArrayList<CreditCard> resultCardList = new ArrayList<>();

    // Path to folder containing card data files
    String folderPath = "text_pages";

    try {
        // Get valid card types from CardTypeExtractor
        List<String> validCardTypes = CardTypeExtractor.extractCardTypes(folderPath);

        // Use SpellChecker to find the closest match
        String correctedCardType = SpellChecker.findClosestMatch(userInputCardType, validCardTypes);

        if (correctedCardType != null) {
            System.out.println("Did you mean: " + correctedCardType + "?");

            // Filter the card list based on the corrected card type
            for (CreditCard card : cardList) {
                if (card.getCardType().equalsIgnoreCase(correctedCardType)) {
                    resultCardList.add(card);
                }
            }
        } else {
            System.out.println("No close match found for the card type: " + userInputCardType);
        }
    } catch (IOException e) {
        System.out.println("Error fetching card types: " + e.getMessage());
    }

    return resultCardList;
	}
}
