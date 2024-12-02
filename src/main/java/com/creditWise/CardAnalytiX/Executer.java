package com.creditWise.CardAnalytiX;

import com.creditWise.Mahzabin.SpellCheck;
import com.creditWise.Mahzabin.WordCompletion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
		String userInput = "";
		ArrayList<CreditCard> resultCardList = new ArrayList<CreditCard>();
		switch(choice)
		{
			case 1:
				System.out.println("Select card type from this Options: [MasterCard, Visa]");
				System.out.println("Input:");
				userInput = sc.next();
				userInput=spellCheckAndWordComplete(userInput);
				resultCardList = basedOnCardType(userInput, cardList);

				break;

			case 2:
				System.out.println("Select card based on Annual Fee, Enter Your preferd annual fee:");
				userInput = sc.next();
				resultCardList = basedOnAnnualFee(userInput, cardList);

				break;
			case 3:
				System.out.println("Select card based on Bank Name, Enter Your preferd Bank:[RBC, Soctia Bank, CIBC, TD Bank]");
				userInput = sc.next();
				userInput=spellCheckAndWordComplete(userInput);
				resultCardList = basedOnBankName(userInput, cardList);

				break;
			case 4:
				System.out.println("Select card based on Interest Rate, Enter Your preferd Interest Rate:");
				userInput = sc.next();
				resultCardList = basedOnInterestRate(userInput, cardList);

				break;

			default:
				System.out.println("Invalid choice. Please restart the tool and enter 1 or 2.");
				break;
		}

	}

	private static WordCompletion wordCompletion = new WordCompletion();
	private static SpellCheck spellCheck = new SpellCheck();


	//Spell checking and word completion implementation.
	private static String spellCheckAndWordComplete(String userInput){
		Scanner scanner = new Scanner(System.in);
		String correctspelledWord;
		if (spellCheck.search(userInput)) {
			//Spelled Correctly
			correctspelledWord=userInput;
		}
		else {
			System.out.println(userInput + " might be spelled incorrectly.");
			List<String> correctSpell = spellCheck.suggestAlternatives(userInput);
			if(!correctSpell.isEmpty()){
				System.out.println("Did you mean these?");
				for (int i=0;i<correctSpell.size();i++) {
					System.out.println((i+1)+" " + correctSpell.get(i));
				}
				System.out.println("To choose a suggested spelling type the number associated with it. Type 0 to not choose any.");
				int input = Integer.parseInt(scanner.nextLine());
				if(input==0){
					correctspelledWord=userInput;
				}
				else{
					correctspelledWord= correctSpell.get(input-1);
				}
			}
			else{
				correctspelledWord=userInput;
			}
		}

		System.out.println("Suggested Autocomplete words: ");
		// Generating list of word complete suggestions based on user's input.
		List<String> suggestions = wordCompletion.autocomplete(correctspelledWord,3);
		for (int i=0;i<suggestions.size();i++) {
			System.out.println((i+1)+" " + suggestions.get(i));
		}
		System.out.println("To choose a suggested word type the number associated with it. or Type 0 to not choose any of the suggestions.");
		int input = Integer.parseInt(scanner.nextLine());
		if(input==0){
			return correctspelledWord;
		}
		else{
			return suggestions.get(input-1);
		}
	}


	// implement Validation
	private static ArrayList<CreditCard> basedOnInterestRate(String userInput, ArrayList<CreditCard> cardList)
	{
		// TODO Auto-generated method stub
		return null;
	}

	// Implement word Completion, spell checking , validation
	private static ArrayList<CreditCard> basedOnBankName(String userInput, ArrayList<CreditCard> cardList)
	{
		// TODO Auto-generated method stub

		return null;
	}

	// implement  validation.
	private static ArrayList<CreditCard> basedOnAnnualFee(String userInput, ArrayList<CreditCard> cardList)
	{
		ArrayList<CreditCard> resultCardList = new ArrayList<CreditCard>();

		return resultCardList;
	}

	// implement word completion, spell checking and validation.
	private static ArrayList<CreditCard> basedOnCardType(String userInputCardType, ArrayList<CreditCard> cardList)
	{
		ArrayList<CreditCard> resultCardList = new ArrayList<CreditCard>();
		return resultCardList;
	}

}
