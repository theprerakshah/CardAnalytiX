package com.creditWise.CardAnalytiX;

import java.io.IOException;
import java.util.ArrayList;
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
