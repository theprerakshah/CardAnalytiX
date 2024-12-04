package com.creditWise.CardAnalytiX;

import java.util.ArrayList;
import java.util.Scanner;

import com.creditWise.Sagar.Validation;
import com.creditWise.Sagar.mergeSort;

public class PreferenceBasedFilter
{

	public static ArrayList<CreditCard> basedOnInterestRate(String userInput, ArrayList<CreditCard> cardList)
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

	public static ArrayList<CreditCard> basedOnAnnualFee(String userInput, ArrayList<CreditCard> cardList)
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
				String annualFeeString = card.getAnnualFee().replaceAll("[^0-9.]", "").trim();

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
			System.out.println("Do you want to sort the results by Interest Rate by: \n 1. Ascending order\n 2. Descending order");
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

	public static ArrayList<CreditCard> basedOnBankName(String userInput, ArrayList<CreditCard> cardList)
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
		resultCardList = mergeSort.sort(resultCardList, isAscending);

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

	// implement word completion, spell checking and validation.
	public static ArrayList<CreditCard> basedOnCardType(String userInput, ArrayList<CreditCard> cardList)
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
			System.out.println("Do you want to sort the results by Interest Rate in \n 1. Ascending order\n 2. Descending order");
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
		resultCardList = mergeSort.sort(resultCardList, isAscending);

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
}
