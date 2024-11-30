package com.creditWise.CardAnalytiX;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.creditWise.Sagar.SpellChecker;
import com.creditWise.Sagar.Validation;

/**
 * Credit Card Suggestion Tool
 */
public class Executer {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Credit Card Suggestion Tool!");
        System.out.println("Please answer the following question to get your best credit card suggestion:");
        System.out.println("1. Do you want to crawl the website?");
        System.out.println("2. Or do you want to use existing data?");
        System.out.print("Enter your choice (1 or 2): ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("You chose to crawl the website.");
                System.out.println("Initializing web crawler...");
               // WebCrawler webCrawler = new WebCrawler();
               // webCrawler.startCrawling();
                break;

            case 2:
                System.out.println("You chose to use existing data.");
                System.out.println("Fetching existing credit card data...");
                //ExistingData existingData = new ExistingData();
                //existingData.displaySuggestions();
                break;

            default:
                System.out.println("Invalid choice. Please restart the tool and enter 1 or 2.");
                break;
        }

        scanner.close();
    }
}

