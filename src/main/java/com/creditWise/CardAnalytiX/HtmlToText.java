package com.creditWise.CardAnalytiX;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class HtmlToText
{

	public static void main(String[] args)
	{
		// Load the JSON file containing the selectors
		String jsonFilePath = "src/selectors.json"; // Replace with the actual path to selector.json

		// Read the content of the selector.json file
		String jsonContent = readFile(jsonFilePath);

		// Parse the JSON content
		JSONObject jsonObject = new JSONObject(jsonContent);

		// Get the selectors for 'td', 'scotiabank', 'greatCanadianRebates', 'cibc', and
		// 'nationalBank'
		JSONObject tdSelectors = jsonObject.getJSONObject("selectors").getJSONObject("td");
		JSONObject scotiaSelectors = jsonObject.getJSONObject("selectors").getJSONObject("scotiabank");
		JSONObject canadianRebatesSelectors = jsonObject.getJSONObject("selectors").getJSONObject("greatCanadianRebates");
		JSONObject cibcSelectors = jsonObject.getJSONObject("selectors").getJSONObject("cibc");
		JSONObject nationalBankSelectors = jsonObject.getJSONObject("selectors").getJSONObject("nationalBank");

		// Extract the selectors
		String tdProductInfoSelector = tdSelectors.getString("product_info");
		String tdCardNameSelector = tdSelectors.getString("card_name");
		String tdCardAnuallFeeSelector = tdSelectors.getString("annualFee");
		String scotiaCardNameSelector = scotiaSelectors.getString("cardName");
		String rebateCardNameSelector = canadianRebatesSelectors.getString("cardName");
		String rebateAnnualFeeSelector = canadianRebatesSelectors.getString("annualFee");
		String rebateInterestRateSelector = canadianRebatesSelectors.getString("interestRates");
		String rebateAdditionalFeaturesSelector = canadianRebatesSelectors.getString("additionalFeatures");

		// CIBC Selectors (fixed key names)
		String cibcCardNameSelector = cibcSelectors.getString("card_name"); // Use "card_name"
		String cibcAnnualFeeSelector = cibcSelectors.getString("annual_fee"); // Use "annual_fee"
		String cibcInterestRateSelector = cibcSelectors.getString("purchase_rate"); // Use "purchase_rate"
		String cibcAdditionalFeaturesSelector = cibcSelectors.getString("benefits"); // Use "benefits"

		// National Bank Selectors
		String nationalBankCardNameSelector = nationalBankSelectors.getString("cardName");
		String nationalBankAnnualFeeSelector = nationalBankSelectors.getString("annualFee");
		String nationalBankPurchaseInterestRateSelector = nationalBankSelectors.getString("purchaseInterestRate");
		String nationalBankCardTypeSelector = nationalBankSelectors.getString("cardType");
		String nationalBankAdditionalFeaturesSelector = nationalBankSelectors.getString("additionalFeatures");

		// Directory paths for saved HTML pages and output txt file
		String inputDirectory = "saved_pages/"; // Directory containing crawled HTML pages
		String outputDirectory = "text_pages/"; // Directory where extracted data will be saved

		// Ensure output directory exists
		new File(outputDirectory).mkdirs();

		// Process all HTML files in the saved_pages directory
		try
		{
			List<Path> htmlFiles = Files.walk(Paths.get(inputDirectory)).filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".html")).toList();

			StringBuilder tdExtractedText = new StringBuilder();
			StringBuilder scotiaExtractedText = new StringBuilder();
			StringBuilder rebateExtractedText = new StringBuilder();
			StringBuilder cibcExtractedText = new StringBuilder();
			StringBuilder nationalBankExtractedText = new StringBuilder();

			for(Path htmlFile : htmlFiles)
			{
				String htmlContent = readFile(htmlFile.toString());
				Document doc = Jsoup.parse(htmlContent);

				// --- TD Data Extraction ---
				Elements tdCardNameElements = doc.select(tdCardNameSelector);
				Elements tdProductInfoElements = doc.select(tdProductInfoSelector);
				Elements tdAnnualFeeInfoElements = doc.select(tdCardAnuallFeeSelector);
				// Selector for the "Annual Fee" text
				// Fetch "$139"

				String tdCardNameRegex = "(?<=Recently Viewed\\s)(.*?)(?=\\sCard)";

				for(int i = 0; i < tdCardNameElements.size(); i++)
				{
					String rawCardName = tdCardNameElements.size() > i ? tdCardNameElements.get(i).text() : "No Card Name";
					String productInfo = tdProductInfoElements.size() > i ? tdProductInfoElements.get(i).text() : "No Product Info";

					String cardName = extractCardName(rawCardName, tdCardNameRegex);
					String annualFee = extractFee(tdAnnualFeeInfoElements, i);
					//					String cardAnuallFee = extractCardName(rawCardName, tdCardNameRegex);

					tdExtractedText.append("Card Name: ").append(cleanText(cardName)).append(" | ");
					tdExtractedText.append("Product Info: ").append(cleanText(productInfo)).append("\n");
					tdExtractedText.append("Annual Fee: ").append(cleanText(annualFee)).append("\n");
				}

				// --- Scotiabank Data Extraction ---
				Elements scotiaCardNameElements = doc.select(scotiaCardNameSelector);
				Elements scotiaAnnualFeeElements = doc.select("p:contains(Annual fee)");
				Elements scotiaPurchaseInterestRateElements = doc.select("p:contains(Purchase interest rate)");

				for(int i = 0; i < scotiaCardNameElements.size(); i++)
				{
					String cardName = scotiaCardNameElements.size() > i ? scotiaCardNameElements.get(i).text() : "No Card Name";
					String annualFee = extractFee(scotiaAnnualFeeElements, i);
					String purchaseInterestRate = extractFee(scotiaPurchaseInterestRateElements, i);

					scotiaExtractedText.append("Card Name: ").append(cardName).append(" | ");
					scotiaExtractedText.append("Annual Fee: ").append(annualFee).append(" | ");
					scotiaExtractedText.append("Purchase Interest Rate: ").append(purchaseInterestRate).append(" | ");
					scotiaExtractedText.append("\n");
				}

				// --- Great Canadian Rebates Data Extraction ---
				Elements rebateCardNameElements = doc.select(rebateCardNameSelector);
				Elements rebateAnnualFeeElements = doc.select(rebateAnnualFeeSelector);
				Elements rebateInterestRateElements = doc.select(rebateInterestRateSelector);
				Elements rebateAdditionalFeaturesElements = doc.select(rebateAdditionalFeaturesSelector);

				for(int i = 0; i < rebateCardNameElements.size(); i++)
				{
					String cardName = rebateCardNameElements.size() > i ? rebateCardNameElements.get(i).text() : "No Card Name";
					String annualFee = rebateAnnualFeeElements.size() > i ? rebateAnnualFeeElements.get(i).text() : "No Annual Fee";
					String interestRates = rebateInterestRateElements.size() > i ? rebateInterestRateElements.get(i).text() : "No Interest Rates";
					String additionalFeatures = rebateAdditionalFeaturesElements.size() > i ? rebateAdditionalFeaturesElements.get(i).text() : "No Additional Features";

					rebateExtractedText.append("Card Name: ").append(cardName).append(" | ");
					rebateExtractedText.append("Annual Fee: ").append(annualFee).append(" | ");
					rebateExtractedText.append("Interest Rates: ").append(interestRates).append(" | ");
					rebateExtractedText.append("Additional Features: ").append(additionalFeatures).append("\n");
				}

				// --- CIBC Data Extraction ---
				Elements cibcCardNameElements = doc.select(cibcCardNameSelector);
				Elements cibcAnnualFeeElements = doc.select(cibcAnnualFeeSelector);
				Elements cibcInterestRateElements = doc.select(cibcInterestRateSelector);
				Elements cibcAdditionalFeaturesElements = doc.select(cibcAdditionalFeaturesSelector);

				for(int i = 0; i < cibcCardNameElements.size(); i++)
				{
					String cardName = cibcCardNameElements.size() > i ? cibcCardNameElements.get(i).text() : "No Card Name";
					String annualFee = cibcAnnualFeeElements.size() > i ? cibcAnnualFeeElements.get(i).text() : "No Annual Fee";
					String interestRates = cibcInterestRateElements.size() > i ? cibcInterestRateElements.get(i).text() : "No Interest Rates";
					String additionalFeatures = cibcAdditionalFeaturesElements.size() > i ? cibcAdditionalFeaturesElements.get(i).text() : "No Additional Features";

					cibcExtractedText.append("Card Name: ").append(cardName).append(" | ");
					cibcExtractedText.append("Annual Fee: ").append(annualFee).append(" | ");
					cibcExtractedText.append("Interest Rates: ").append(interestRates).append(" | ");
					cibcExtractedText.append("Additional Features: ").append(additionalFeatures).append("\n");
				}

				// --- National Bank Data Extraction ---
				Elements nationalBankCardNameElements = doc.select(nationalBankCardNameSelector);
				Elements nationalBankAnnualFeeElements = doc.select(nationalBankAnnualFeeSelector);
				Elements nationalBankInterestRateElements = doc.select(nationalBankPurchaseInterestRateSelector);
				Elements nationalBankCardTypeElements = doc.select(nationalBankCardTypeSelector);
				Elements nationalBankAdditionalFeaturesElements = doc.select(nationalBankAdditionalFeaturesSelector);

				for(int i = 0; i < nationalBankCardNameElements.size(); i++)
				{
					String cardName = nationalBankCardNameElements.size() > i ? nationalBankCardNameElements.get(i).text() : "No Card Name";
					String annualFee = nationalBankAnnualFeeElements.size() > i ? nationalBankAnnualFeeElements.get(i).text() : "No Annual Fee";
					String purchaseInterestRate = nationalBankInterestRateElements.size() > i ? nationalBankInterestRateElements.get(i).text() : "No Purchase Interest Rate";
					String cardType = nationalBankCardTypeElements.size() > i ? nationalBankCardTypeElements.get(i).text() : "No Card Type";
					String additionalFeatures = nationalBankAdditionalFeaturesElements.size() > i ? nationalBankAdditionalFeaturesElements.get(i).text() : "No Additional Features";

					nationalBankExtractedText.append("Card Name: ").append(cardName).append(" | ");
					nationalBankExtractedText.append("Annual Fee: ").append(annualFee).append(" | ");
					nationalBankExtractedText.append("Purchase Interest Rate: ").append(purchaseInterestRate).append(" | ");
					nationalBankExtractedText.append("Card Type: ").append(cardType).append(" | ");
					nationalBankExtractedText.append("Additional Features: ").append(additionalFeatures).append("\n");
				}
			}

			// Save the extracted data to output files
			saveToFile(outputDirectory + "td_cards.txt", tdExtractedText.toString());
			saveToFile(outputDirectory + "scotiabank_cards.txt", scotiaExtractedText.toString());
			saveToFile(outputDirectory + "rebate_cards.txt", rebateExtractedText.toString());
			saveToFile(outputDirectory + "cibc_cards.txt", cibcExtractedText.toString());
			saveToFile(outputDirectory + "national_bank_cards.txt", nationalBankExtractedText.toString());

			System.out.println("Data extraction complete! Check the output files.");

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	// Method to read the content of a file
	private static String readFile(String path)
	{
		try
		{
			return new String(Files.readAllBytes(Paths.get(path)));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	// Method to clean text by trimming whitespace and handling special characters
	private static String cleanText(String text)
	{
		return text.trim().replaceAll("[\\r\\n]+", " ");
	}

	// Extract card name using regex (e.g., from "Recently Viewed Card Name")
	private static String extractCardName(String rawCardName, String regex)
	{
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(rawCardName);
		if(matcher.find())
		{
			return matcher.group(1).trim();
		}
		return rawCardName; // Return original name if no match found
	}

	// Helper method to extract fee or interest rate
	private static String extractFee(Elements elements, int index)
	{
		return elements.size() > index ? elements.get(index).text() : "No Data";
	}

	// Save extracted text to a file
	private static void saveToFile(String filePath, String content)
	{
		try
		{
			Files.write(Paths.get(filePath), content.getBytes());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
