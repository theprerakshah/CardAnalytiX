package com.creditWise.Sakshi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SearchFrequency {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // File path for CSV and text files
        String resourceFolder = "src/main/resources/";
        String csvFile = resourceFolder + "search_terms.csv"; // CSV file to store user input search terms
        String textPagesFolder = "text_pages/"; // Folder where card details text files are stored

        // Ensure the resources and text_pages folders exist
        try {
            Files.createDirectories(Paths.get(resourceFolder));
            Files.createDirectories(Paths.get(textPagesFolder));
        } catch (IOException e) {
            System.out.println("Error creating directories: " + e.getMessage());
            return;
        }

        // Initialize CSV file with headers if it doesn't exist
        if (!Files.exists(Paths.get(csvFile))) {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFile))) {
                writer.write("Bank Name,Card Type,Annual Fee,Bank Name,Interest Rate,Frequency\n");
            } catch (IOException e) {
                System.out.println("Error creating CSV file: " + e.getMessage());
                return;
            }
        }

        System.out.println("Welcome to the Credit Card Search Tool!");
        System.out.println("Please provide your search terms for analysis:");

        boolean continueSearching = true;
        while (continueSearching) {
            
            System.out.println("\nChoose an option:");
            System.out.println("1. Bank Name");
            System.out.println("2. Card Name");
            System.out.println("3. Card Type");
            System.out.println("4. Annual Fee");
            System.out.println("5. Interest Rate");
            System.out.println("6. View popular search terms");
            System.out.println("7. Exit");

            System.out.print("Enter your choice (1-6): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                System.out.print("Enter Bank Name (e.g., 'Chase', 'Bank of America'): ");
                String bankName = scanner.nextLine().toLowerCase();
                saveToCSV(csvFile, null, null, bankName, null);
                break;
                case "2":
                System.out.print("Enter Card Type (e.g., 'Visa', 'MasterCard'): ");
                String cardName = scanner.nextLine().toLowerCase();
                saveToCSV(csvFile, cardName, null, null, null);
                break;

                case "10":
                    System.out.print("Enter Card Type (e.g., 'Visa', 'MasterCard'): ");
                    String cardType = scanner.nextLine().toLowerCase();
                    saveToCSV(csvFile, cardType, null, null, null);
                    break;
                case "20":
                break;

                case "4":
                    System.out.print("Enter Annual Fee (e.g., '99', '199'): ");
                    String annualFee = scanner.nextLine().toLowerCase();
                    saveToCSV(csvFile, null, annualFee, null, null);
                    break;

             
                case "5":
                    System.out.print("Enter Interest Rate (e.g., '3.5', '5.0'): ");
                    String interestRate = scanner.nextLine().toLowerCase();
                    saveToCSV(csvFile, null, null, null, interestRate);
                    break;

                case "6":
                    // Step 1: Ask user for the field they want popular terms from
                    System.out.println("Choose a field to view popular search terms:");
                    System.out.println("1. Card Type");
                    System.out.println("2. Bank Name");
                    System.out.println("3. Annual Fee");
                    System.out.println("4. Interest Rate");
                    String fieldChoice = scanner.nextLine();

                    // Step 2: View popular search terms for the selected field
                    viewPopularSearchTerms(csvFile, fieldChoice, scanner);
                    break;

                case "7":
                    System.out.println("Thank you for using the tool. Goodbye!");
                    continueSearching = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to save user input to the CSV file
    private static void saveToCSV(String csvFile, String cardType, String annualFee, String bankName, String interestRate) {
        // Frequency is set to 1 for each new entry, as it's a fresh input
        int frequency = 1;

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFile), StandardOpenOption.APPEND)) {
            writer.write(String.format("%s,%s,%s,%s,%d\n",
                    cardType != null ? cardType : "",
                    annualFee != null ? annualFee : "",
                    bankName != null ? bankName : "",
                    interestRate != null ? interestRate : "",
                    frequency));
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    // Method to view and analyze popular search terms from the CSV file for the selected field
    private static void viewPopularSearchTerms(String csvFile, String fieldChoice, Scanner scanner) {
        Map<String, Integer> termFrequencyMap = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(csvFile))) {
            String line;
            reader.readLine(); // Skip the header line
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                String searchTerm = "";

                // Based on user field choice, we will collect data from the selected column
                switch (fieldChoice) {
                    case "1": // Card Type
                        searchTerm = columns[0];
                        break;
                    case "2": // Bank Name
                        searchTerm = columns[2];
                        break;
                    case "3": // Annual Fee
                        searchTerm = columns[1];
                        break;
                    case "4": // Interest Rate
                        searchTerm = columns[3];
                        break;
                    default:
                        System.out.println("Invalid field choice.");
                        return;
                }

                // If the term is not empty, count its frequency
                if (!searchTerm.isEmpty()) {
                    termFrequencyMap.put(searchTerm, termFrequencyMap.getOrDefault(searchTerm, 0) + Integer.parseInt(columns[4]));
                }
            }

            // Display the most popular search terms for the selected field as a numbered list
            System.out.println("\nBased on your choice, the most popular search terms are:");
            List<String> terms = new ArrayList<>(termFrequencyMap.keySet());
            Collections.sort(terms, (a, b) -> termFrequencyMap.get(b) - termFrequencyMap.get(a));

            // Display terms with their frequencies
            int count = 1;
            for (String term : terms) {
                System.out.println(count + ". Term: '" + term + "' - Frequency: " + termFrequencyMap.get(term));
                count++;
            }

            // Step 3: Allow user to choose a term to see cards
            System.out.print("\nChoose the term you want to see cards from (1-" + terms.size() + "): ");
            int termChoice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character left by nextInt()

            // Validate choice
            if (termChoice < 1 || termChoice > terms.size()) {
                System.out.println("Invalid choice.");
                return;
            }

            String chosenTerm = terms.get(termChoice - 1);
            System.out.println("\nHere is the list of cards from your chosen term '" + chosenTerm + "':");
            showCardsBasedOnTerm(chosenTerm); // Corrected method call

        } catch (IOException e) {
            System.out.println("Error reading from CSV file: " + e.getMessage());
        }
    }

    // Method to display cards based on selected term, fetching details from text files
    private static void showCardsBasedOnTerm(String chosenTerm) {
        try {
            // Define the list of card files to read from
            String[] cardFiles = {"text_pages/scotiabank_cards.txt", "text_pages/td_cards.txt", "text_pages/cibc_cards.txt"};

            for (String filePath : cardFiles) {
                File file = new File(filePath);

                if (file.exists() && file.isFile()) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    boolean termFound = false;
                    while ((line = reader.readLine()) != null) {
                        // If the term is present in the file, display the card details
                        if (line.toLowerCase().contains(chosenTerm.toLowerCase())) {
                            termFound = true;
                            System.out.println("\nCard Details from " + file.getName() + ":");
                            // Print the entire file contents after the term
                            do {
                                System.out.println(line);
                                line = reader.readLine();
                            } while (line != null && !line.trim().isEmpty());
                        }
                    }
                    reader.close();

                    if (!termFound) {
                        System.out.println("No cards found for term '" + chosenTerm + "' in " + file.getName());
                    }
                } else {
                    System.out.println("Card file not found: " + filePath);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading card details from text files: " + e.getMessage());
        }
    }
}

