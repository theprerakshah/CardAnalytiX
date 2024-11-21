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
		JSONObject rbcSelectors = jsonObject.getJSONObject("selectors").getJSONObject("rbc");

		// Extract the selectors
		String tdAdvantages = tdSelectors.getString("advantages");
		String tdCardNameSelector = tdSelectors.getString("card_name");
		String tdCardAnuallFeeSelector = tdSelectors.getString("annualFee");
		String tdPurchaseRate = tdSelectors.getString("purchaseInterestRate");
		String scotiaCardNameSelector = scotiaSelectors.getString("cardName");
		String scotiaAnnualFeeSelector = scotiaSelectors.getString("annualFee");
		String scotiaPurchaseRate = scotiaSelectors.getString("purchaseInterestRate");
		String scotiaAdditionalFeature = scotiaSelectors.getString("additionalFeatures");
		String rebateCardNameSelector = canadianRebatesSelectors.getString("cardName");
		String rebateAnnualFeeSelector = canadianRebatesSelectors.getString("annualFee");
		String rebateInterestRateSelector = canadianRebatesSelectors.getString("interestRates");
		String rebateAdditionalFeaturesSelector = canadianRebatesSelectors.getString("additionalFeatures");
		String rbcCardName = rbcSelectors.getString("cardName");

		// CIBC Selectors (fixed key names)
		String cibcCardNameSelector = cibcSelectors.getString("card_name"); // Use "card_name"
		String cibcAnnualFeeSelector = cibcSelectors.getString("annual_fee"); // Use "annual_fee"
		String cibcInterestRateSelector = cibcSelectors.getString("purchase_rate"); // Use "purchase_rate"
		String cibcAdditionalFeaturesSelector = cibcSelectors.getString("advatnages"); // Use "benefits"

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
			StringBuilder rbcExtractedText = new StringBuilder();


			for(Path htmlFile : htmlFiles)
			{
				String htmlContent = readFile(htmlFile.toString());
				Document doc = Jsoup.parse(htmlContent);

				// --- TD Data Extraction ---
				Elements tdCardNameElements = doc.select(tdCardNameSelector);
				Elements tdAnnualFeeInfoElements = doc.select(tdCardAnuallFeeSelector);
				Elements tdInterestRateElements = doc.select(tdPurchaseRate); // This will target the interest rate percentage
				Elements tdAdditionalFeatureElements = doc.select(tdAdvantages); // Select all paragraphs containing "earn"
				
				// Remove all <sup> tags from the selected elements
				tdCardNameElements.select("sup").remove();
				tdAnnualFeeInfoElements.select("sup").remove();
				tdInterestRateElements.select("sup").remove();
				tdAdditionalFeatureElements.select("sup").remove();
				
				String tdCardNameRegex = "(?<=Recently Viewed\\s)(.*?)(?=\\sCard)";
				
				for (int i = 0; i < tdCardNameElements.size(); i++) {
					String rawCardName = tdCardNameElements.size() > i ? tdCardNameElements.get(i).text() : "No Card Name";
					String cardName = extractCardName(rawCardName, tdCardNameRegex);
					String annualFee = extractFee(tdAnnualFeeInfoElements, i);
				
					if (annualFee.isEmpty() || "No data".equalsIgnoreCase(annualFee)) {
						annualFee = "$0";
					}
					
					// Extract the interest rate (only percentage values like 19.99%)
					String purchaseInterestRate = "No Interest Rate"; // Default in case no rate is found
					if (tdInterestRateElements.size() > i) {
						String interestText = tdInterestRateElements.get(i).text();
						// Extract only the percentage value using regex (e.g., 19.99%)
						purchaseInterestRate = interestText.replaceAll("[^\\d.]+%", "").trim() + "%";
					}
				
					// Extract additional feature if any paragraph contains "earn"
					String additionalFeature = "No Advantages"; // Default if no match
					if (i < tdAdditionalFeatureElements.size()) {
						String additionalText = tdAdditionalFeatureElements.get(i).text();
						
						// Remove unwanted parts (like hyperlinks and extra info like UDINQEN13SUP18)
						additionalText = additionalText.replaceAll("<a[^>]*>(.*?)</a>", ""); // Remove <a> tags (href links)
						additionalText = additionalText.replaceAll("\\s+Conditions Apply.*", ""); // Remove anything after "Conditions Apply"
						
						// Clean the text further by removing any non-relevant tags or content
						additionalFeature = additionalText.trim();
					}
				
					// Determine card type based on card name
					String cardType = "Unknown"; // Default card type
					if (cardName.toLowerCase().contains("visa")) {
						cardType = "Visa Card";
					} else if (cardName.toLowerCase().contains("mastercard")) {
						cardType = "MasterCard";
					}
				
					// Append extracted data
					tdExtractedText.append("Card Name: ").append(cleanText(cardName)).append("\n");
					tdExtractedText.append("Card Type: ").append(cardType).append("\n"); // Add card type to the output
					tdExtractedText.append("Purchase Interest Rate: ").append(cleanText(purchaseInterestRate)).append("\n");
					tdExtractedText.append("Annual Fee: ").append(cleanText(annualFee)).append("\n");
					tdExtractedText.append("Advantages: ").append(cleanText(additionalFeature)).append("\n\n");
				}
				




				// --- Scotiabank Data Extraction ---
				Elements scotiaCardNameElements = doc.select(scotiaCardNameSelector);
				Elements scotiaAnnualFeeElements = doc.select(scotiaAnnualFeeSelector);
				Elements scotiaPurchaseInterestRateElements = doc.select(scotiaPurchaseRate);
				Elements scotiaAdditionalFeatureElements = doc.select(scotiaAdditionalFeature);
				
				for (int i = 0; i < scotiaCardNameElements.size(); i++) {
					String cardName = scotiaCardNameElements.size() > i ? scotiaCardNameElements.get(i).text() : "No Card Name";
					String annualFee = extractFee(scotiaAnnualFeeElements, i);
					String purchaseInterestRate = extractFee(scotiaPurchaseInterestRateElements, i);
					 String additionalFeature = extractFeature(scotiaAdditionalFeatureElements, i);
				
				
					// Determine card type based on card name
					String cardType = "Unknown";
					if (cardName.toLowerCase().contains("visa")) {
						cardType = "Visa Card";
					} else if (cardName.toLowerCase().contains("mastercard")) {
						cardType = "MasterCard";
					} else if (cardName.toLowerCase().contains("american express")) {
						cardType = "American Express";
					}
				
					scotiaExtractedText.append("Card Name: ").append(cardName);
					scotiaExtractedText.append("\n");
					scotiaExtractedText.append("Card Type: ").append(cardType); // Add card type to output
					scotiaExtractedText.append("\n");
					scotiaExtractedText.append("Annual Fee: ").append(annualFee);
					scotiaExtractedText.append("\n");
					scotiaExtractedText.append("Purchase Interest Rate: ").append(purchaseInterestRate);
					scotiaExtractedText.append("\n");
					scotiaExtractedText.append("Additional Feature: ").append(additionalFeature);
					scotiaExtractedText.append("\n");
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
					rebateExtractedText.append("Advantages ").append(additionalFeatures).append("\n");
				}
				Elements cibcCardNameElements = doc.select(cibcCardNameSelector);
				Elements cibcAnnualFeeElements = doc.select(cibcAnnualFeeSelector);
				Elements cibcInterestRateElements = doc.select(cibcInterestRateSelector);
				Elements cibcAdditionalFeaturesElements = doc.select(cibcAdditionalFeaturesSelector);
				
				for (int i = 0; i < cibcCardNameElements.size(); i++) {
					// Extract card name, annual fee, interest rate, and additional features
					String cardName = cibcCardNameElements.size() > i ? cibcCardNameElements.get(i).text() : "No Card Name";
					String annualFee = cibcAnnualFeeElements.size() > i ? cibcAnnualFeeElements.get(i).text() : "No Annual Fee";
					String interestRates = cibcInterestRateElements.size() > i ? cibcInterestRateElements.get(i).text() : "No Interest Rates";
					String additionalFeatures = cibcAdditionalFeaturesElements.size() > i ? cibcAdditionalFeaturesElements.get(i).text() : "No Additional Features";
					
					// Determine card type based on card name
					String cardType = "Unknown Card Type"; // Default value
					if (cardName.toLowerCase().contains("visa")) {
						cardType = "Visa Card";
					} else if (cardName.toLowerCase().contains("mastercard")) {
						cardType = "MasterCard";
					} // Add more conditions here for other card types, if needed
				
					// Filter out non-percentage values from the interest rates
					if (interestRates.matches(".*\\d+\\.\\d+%$")) {
						interestRates = interestRates.replaceAll(".*(\\d+\\.\\d+%)$", "$1");
					} else {
						interestRates = "No Interest Rates";
					}
				
					// Append the results to the output
					cibcExtractedText.append("Card Name: ").append(cardName).append(" \n ");
					cibcExtractedText.append("Card Type: ").append(cardType).append(" \n "); // Include card type
					cibcExtractedText.append("Annual Fee: ").append(annualFee).append(" \n ");
					cibcExtractedText.append("Interest Rates: ").append(interestRates).append(" \n ");
					cibcExtractedText.append("Additional Features: ").append(additionalFeatures).append("\n\n");
				}
			// --- RBC Data Extraction ---
Elements rbcCardNameElements = doc.select(rbcCardName);

for (int i = 0; i < rbcCardNameElements.size(); i++) {
    String cardName = rbcCardNameElements.size() > i ? rbcCardNameElements.get(i).text() : "No Card Name";

    // Determine the card type based on the card name
    String cardType = "Unknown"; // Default card type
    if (cardName.toLowerCase().contains("visa")) {
        cardType = "Visa Card";
    } else if (cardName.toLowerCase().contains("mastercard")) {
        cardType = "MasterCard";
    }

    // Append extracted data
    rbcExtractedText.append("Card Name: ").append(cardName).append(" \n ");
    rbcExtractedText.append("Card Type: ").append(cardType).append("\n");
}

				
				// --- National Bank Data Extraction ---
				Elements nationalBankCardNameElements = doc.select(nationalBankCardNameSelector);
				Elements nationalBankAnnualFeeElements = doc.select(".text-image-text-container b");
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

					nationalBankExtractedText.append("Annual Fee: ").append(annualFee).append(" | ");
			
				}
			}

			// Save the extracted data to output files
			saveToFile(outputDirectory + "td_cards.txt", tdExtractedText.toString());
			saveToFile(outputDirectory + "scotiabank_cards.txt", scotiaExtractedText.toString());
			saveToFile(outputDirectory + "rebate_cards.txt", rebateExtractedText.toString());
			saveToFile(outputDirectory + "cibc_cards.txt", cibcExtractedText.toString());
			saveToFile(outputDirectory + "national_bank_cards.txt", nationalBankExtractedText.toString());
			saveToFile(outputDirectory + "rbc_cards.txt", rbcExtractedText.toString());

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
	public static String extractFeature(Elements elements, int index) {
		if (elements.size() > index) {
			return elements.get(index).text(); // Get the text from the matching element
		}
		return "No Additional Feature"; // Default value if no additional feature found
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
