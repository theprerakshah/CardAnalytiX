package com.creditWise.DataHandler;

import com.opencsv.CSVWriter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class CIBCwebscraper
{
	public static void main(String[] args) throws IOException
	{
		// Automatically manage ChromeDriver
		WebDriverManager.chromedriver().setup();

		// Create a WebDriver with ChromeDriver Object and launch the browser
		WebDriver driver = new ChromeDriver();
		// Navigate to the CIBC website
		driver.get("https://www.cibc.com");
		// click on the credit cards option
		driver.findElement(By.id("CreditCards")).click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		// waiting for the cookie popup to show up and then closing it
		WebElement closeCookie = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".onetrust-close-btn-handler.onetrust-close-btn-ui.banner-close-button.ot-close-icon")));
		closeCookie.click();
		// Locate the "Explore all credit cards" button
		WebElement exploreAllCards = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonid-1667922471126")));

		// Use JavaScriptExecutor to scroll the "Explore all credit cards" button into view
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", exploreAllCards);

		// Use Actions to move to the element and click it
		Actions actions = new Actions(driver);
		actions.moveToElement(exploreAllCards);
		js.executeScript("arguments[0].click();", exploreAllCards);

		// Listing links of all the types of cards
		List<WebElement> cardLinks = driver.findElements(By.cssSelector(".product-title-link.marketingLink"));
		// Listing annual rate and interest rates of all cards
		List<WebElement> rateValues = driver.findElements(By.cssSelector("div.product-rate-value"));
		// Listing benefits or rewards of all the cards
		List<WebElement> benefits = driver.findElements(By.cssSelector("div.product-benefits"));

		String[] titles = new String[cardLinks.size()];
		String[] annualFee = new String[cardLinks.size()];
		String[] purchaseInterestRate = new String[cardLinks.size()];
		String[] cashInterestRate = new String[cardLinks.size()];
		String[] links = new String[cardLinks.size()];
		String[] cardTypes = new String[cardLinks.size()];
		String[] cardBenefits = new String[benefits.size()];

		for(int i = 0; i < cardLinks.size(); i++)
		{
			// Getting the actual url
			links[i] = (cardLinks.get(i).getAttribute("href"));
			cardBenefits[i] = benefits.get(i).getText().replaceAll("[\\r\\n]", " ");
		}
		for(int i = 0, j = 0, k = 2; j < annualFee.length && k < rateValues.size(); j++, i += 3, k += 3)
		{
			annualFee[j] = rateValues.get(i).getText();
			purchaseInterestRate[j] = rateValues.get(i + 1).getText();
			cashInterestRate[j] = rateValues.get(k).getText().replaceAll("[\\r\\n]", " ");
		}
		for(int i = 0; i < cardLinks.size(); i++)
		{
			//Opening each Link and calling methods to extract data from each webpage.
			driver.navigate().to(links[i]);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
			cardTypes[i] = GetCreditCardType(driver);
			titles[i] = GetCreditCardTitle(driver);
		}
		String[] label = {"Card Names", "Card Category", "Annual Fee", "Purchase Interest Rate", "Cash Interest Rate", "Benefits", "Links"};
		// Writing data to CSV file
		CSVWriter csvWriter = new CSVWriter(new FileWriter("CIBC_scraped_data.csv"));
		csvWriter.writeNext(label);
		for(int i = 0; i < titles.length; i++)
		{
			String[] row = {titles[i], cardTypes[i], annualFee[i], purchaseInterestRate[i], cashInterestRate[i], cardBenefits[i], links[i]};
			csvWriter.writeNext(row);
		}

		// Close the writer
		csvWriter.flush();

		driver.quit();
	}

	public static String GetCreditCardTitle(WebDriver driver)
	{
		// Getting Information of Credit Card Names/Titles
		try
		{
			WebElement cTitle = (driver.findElement(By.cssSelector("span.link-color span.headline-1")));
			return cTitle.getText();
		}
		catch(NoSuchElementException e)
		{
			// If the first element is not found, look for the second element
			try
			{
				WebElement cTitle = driver.findElement(By.cssSelector("h1 span.link-color"));
				return cTitle.getText();
			}
			catch(NoSuchElementException ex)
			{
				return "Neither element was found.";
			}
		}
	}

	public static String GetCreditCardType(WebDriver driver)
	{
		// Getting Information of Credit Card Types/ Categories
		try
		{
			WebElement cType = (driver.findElement(By.cssSelector("span.callout-title-allcaps a.iconLink.DefaultColor span.link-color")));
			return cType.getText();
		}
		catch(NoSuchElementException e)
		{
			// If the first element is not found, look for the second element
			try
			{
				WebElement cType = driver.findElement(By.cssSelector("a.iconLink.reverseColor"));
				return cType.getText();
			}
			catch(NoSuchElementException ex)
			{
				return "Neither element was found.";
			}
		}
	}
}
