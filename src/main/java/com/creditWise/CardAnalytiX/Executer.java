package com.creditWise.CardAnalytiX;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
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
						PreferenceBasedFilter.printCreditCardData(cardList);

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
							scanner.nextLine();
						while(true)
						{
							System.out.println("Select Card Type From These Options. \n" + "    Visa Card\n" + "    American Express\n" + "    Costco Cards\n" + "    Student Cards\n"
									+ "    Cash Back Cards\n" + "    Travel Rewards Cards\n" + "    Business Credit Cards\n" + "    Low Interest Cards\n" + "    Mastercard");
							System.out.println("Input:");

							userInput = scanner.nextLine();

							if(!Validation.ValidationCardType(userInput))
							{
								System.out.println("Please Enter a Valid Credit Card Type.");
								continue;
							}
							if(!CardTypeMap.getCardType(userInput).equalsIgnoreCase("Null"))
							{
								resultCardList = PreferenceBasedFilter.basedOnCardType(userInput, cardList);
								break;
							}

							userInput = spellCheckAndWordComplete(userInput);

							if(userInput.equalsIgnoreCase("Try Again"))
							{
								System.out.println("Invalid input after suggestions. Please try again.");
								continue;
							}

							String cardType = CardTypeMap.getCardType(userInput);

							if(cardType.equalsIgnoreCase("Null"))
							{
								System.out.println("Please Enter a Valid Card Type.");
								continue;
							}

							resultCardList = PreferenceBasedFilter.basedOnCardType(userInput, cardList);
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
						resultCardList = PreferenceBasedFilter.basedOnAnnualFee(userInput, cardList);

						break;
					case 3:
						if(scanner.hasNextLine())
							scanner.nextLine();
						while(true)
						{
							System.out.println("Select card based on Bank Name, Enter Your preferred Bank: [RBC, Scotia Bank, CIBC, TD Bank]");

							userInput = scanner.nextLine();

							if(!Validation.ValidationBankName(userInput))
							{
								System.out.println("Invalid Bank Name. Please enter a valid bank name.");
								continue;
							}
							if(!bankNameMap.getBankName(userInput).equalsIgnoreCase("Null"))
							{
								resultCardList = PreferenceBasedFilter.basedOnBankName(userInput, cardList);
								break;
							}

							userInput = spellCheckAndWordComplete(userInput);

							if(userInput.equalsIgnoreCase("Try Again"))
							{
								System.out.println("Invalid input after suggestions. Please try again.");
								continue;
							}

							String bankName = bankNameMap.getBankName(userInput);

							if(bankName.equalsIgnoreCase("Null"))
							{
								System.out.println("Bank Name not found. Please enter a valid bank name from the options.");
								continue;
							}

							resultCardList = PreferenceBasedFilter.basedOnBankName(userInput, cardList);
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
						resultCardList = PreferenceBasedFilter.basedOnInterestRate(userInput, cardList);

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
				System.out.println("Invalid input! Please enter a number between 1 and.");

				scanner.nextLine();
			}
			catch(Exception e)
			{
				System.out.println("An unexpected error occurred: " + e.getMessage());
				scanner.nextLine();
			}
		}
	}

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
			while(true)
			{
				System.out.println("Do you want word count from this bankWebsite? (Y | N):");
				String freqInput = reader.readLine();
				freqInput = freqInput.trim();

				if(freqInput.equalsIgnoreCase("Y"))
				{
					WordFrequency.countFrequency(dataForFrequency);
					break; // Exit the loop after valid input
				}
				else if(freqInput.equalsIgnoreCase("N"))
				{
					System.out.println("Skipping word count...");
					break; // Exit the loop after valid input
				}
				else
				{
					System.out.println("Invalid input. Please enter 'Y' or 'N'.");
				}
			}
		}
	}

	private static void wordFrequencyRankBased(ArrayList<CreditCard> cardList) throws IOException
	{
		Scanner scanner = new Scanner(System.in); // Use Scanner for user input
		System.out.println("Enter the word you want to look for in the bank website:");

		// Get valid word input using the validation method
		String word = Validation.getWordOnlyInput(scanner, "Enter only words: ");

		// Process the input and perform the ranking
		Map<String, Integer> rankedWiseWebsite = PageRanking.RankBankBasedOnWordFrequency(word, cardList);

		// Display the ranking results
		for(Map.Entry<String, Integer> rankedData : rankedWiseWebsite.entrySet())
		{
			System.out.println(rankedData.getKey() + " - Score: " + rankedData.getValue());
		}
	}

	public static void viewPopularSearchTerms()
	{
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String fieldChoice;

			while(true)
			{
				System.out.println("\nChoose a field to view popular search terms:");
				System.out.println("1. Bank Name");
				System.out.println("2. Card Name");
				System.out.println("3. Card Type");
				System.out.print("Enter your choice (1-3): ");

				fieldChoice = reader.readLine().trim();

				if(fieldChoice.equals("1"))
				{
					SearchFrequencyRBTree.displaySearchTerms("Bank Name");
					break;
				}
				else if(fieldChoice.equals("2"))
				{
					SearchFrequencyRBTree.displaySearchTerms("Card Name");
					break;
				}
				else if(fieldChoice.equals("3"))
				{
					SearchFrequencyRBTree.displaySearchTerms("Card Type");
					break;
				}
				else
				{
					System.out.println("Invalid choice. Please enter a valid option (1, 2, or 3).");
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

}
