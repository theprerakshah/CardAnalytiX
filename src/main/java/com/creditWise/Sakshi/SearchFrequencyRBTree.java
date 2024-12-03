package com.creditWise.Sakshi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SearchFrequencyRBTree {
    private static final String RESOURCE_PATH = "src/main/resources/";
    private static final String SEARCHES_CSV = "src/main/resources/search_terms.csv"; // Path to the CSV file

    private static RedBlackTree<String> bankNamesTree = new RedBlackTree<>();
    private static RedBlackTree<String> cardNamesTree = new RedBlackTree<>();
    private static RedBlackTree<String> cardTypesTree = new RedBlackTree<>();
    private static RedBlackTree<String> annualFeesTree = new RedBlackTree<>();
    private static RedBlackTree<String> interestRatesTree = new RedBlackTree<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Credit Card Search Tool!");
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

            System.out.print("Enter your choice (1-7): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter Bank Name: ");
                    String bankName = scanner.nextLine().toLowerCase();
                    addSearchTerm(bankName, "Bank Name");
                    break;
                case "2":
                    System.out.print("Enter Card Name: ");
                    String cardName = scanner.nextLine().toLowerCase();
                    addSearchTerm(cardName, "Card Name");
                    break;
                case "3":
                    System.out.print("Enter Card Type: ");
                    String cardType = scanner.nextLine().toLowerCase();
                    addSearchTerm(cardType, "Card Type");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

     public static void SearchInputs() {
        try {
            // Initialize BufferedReader with System.in
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("\nChoose an option:");
            System.out.println("1. Bank Name");
            System.out.println("2. Card Name");
            System.out.println("3. Card Type");
          
            System.out.print("Enter your choice (1-3): ");
            String choice = reader.readLine(); // Read input using BufferedReader

            switch (choice) {
                case "1":
                    System.out.print("Enter Bank Name: ");
                    String bankName = reader.readLine().toLowerCase(); // Get user input for Bank Name
                    addSearchTerm(bankName, "Bank Name");
                    break;
                case "2":
                    System.out.print("Enter Card Name: ");
                    String cardName = reader.readLine().toLowerCase(); // Get user input for Card Name
                    addSearchTerm(cardName, "Card Name");
                    break;
                case "3":
                    System.out.print("Enter Card Type: ");
                    String cardType = reader.readLine().toLowerCase(); // Get user input for Card Type
                    addSearchTerm(cardType, "Card Type");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            reader.close(); // Close the reader after use

        } catch (IOException e) {
            System.out.println("Error with reading input: " + e.getMessage());
        }
    }

    
    

    private static void addSearchTerm(String term, String field) {
        switch (field) {
            case "Bank Name":
                bankNamesTree.insert(term);
                break;
            case "Card Name":
                cardNamesTree.insert(term);
                break;
            case "Card Type":
                cardTypesTree.insert(term);
                break;
        }
        saveSearchTermToCSV(term, field);
    }

    private static void saveSearchTermToCSV(String term, String field) {
        File csvFile = new File(SEARCHES_CSV);
        Map<String, String[]> data = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;

            // Read all existing records and store them
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) { // Ensure correct column count
                    String existingTerm = parts[0].trim();
                    data.put(existingTerm, parts);
                }
            }

            // Update or add the record
            String[] updatedValues = data.getOrDefault(term, new String[]{"", "", "", "0"});
            switch (field) {
                case "Bank Name":
                    updatedValues[0] = term;
                    break;
                case "Card Name":
                    updatedValues[1] = term;
                    break;
                case "Card Type":
                    updatedValues[2] = term;
                    break;
            
              
            }
            updatedValues[3] = String.valueOf(Integer.parseInt(updatedValues[3]) + 1); // Increment frequency
            data.put(term, updatedValues);

            // Write back to CSV
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
                writer.write("Bank Name,Card Name,Card Type,Frequency\n"); // Header
                for (String[] values : data.values()) {
                    writer.write(String.join(",", values) + "\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   

    public static void displaySearchTerms(String field) {
        File csvFile = new File(SEARCHES_CSV);
        Map<String, Integer> termFrequencyMap = new HashMap<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isFirstLine = true; // Flag to skip the header row
    
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the first line
                    continue;
                }
    
                String[] parts = line.split(",");
                if (parts.length == 4) { // Validate column count
                    try {
                        String term = parts[0].trim(); // Bank Name is assumed to be the first column
                        int frequency = Integer.parseInt(parts[3].trim()); // Frequency column
    
                        if (!term.isEmpty()) {
                            termFrequencyMap.put(term, termFrequencyMap.getOrDefault(term, 0) + frequency);
                        }
                    } catch (NumberFormatException e) {
                        // Skip rows with invalid frequency values
                        System.out.println("Skipping invalid row: " + line);
                    }
                }
            }
    
            // Display sorted terms
            System.out.println("Popular " + field + "s based on user search frequency:");
            termFrequencyMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue() + " searches"));
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
   
}
