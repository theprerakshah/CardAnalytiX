package com.creditWise.Sakshi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.creditWise.CardAnalytiX.Executer;


public class SearchFrequencyRBTree {
    private static final String RESOURCE_PATH = "src/main/resources/";
    private static final String SEARCHES_CSV = "src/main/resources/search_terms.csv"; // Path to the CSV file

    private static RedBlackTree<String> bankNamesTree = new RedBlackTree<>();
    private static RedBlackTree<String> cardNamesTree = new RedBlackTree<>();
    private static RedBlackTree<String> cardTypesTree = new RedBlackTree<>();
    

    public static void main(String[] args) {
      
    }

     public static void SearchInputs() {
    Scanner scanner = new Scanner(System.in); // Use Scanner instead of BufferedReader

    System.out.println("\nChoose an option:");
    System.out.println("1. Bank Name");
    System.out.println("2. Card Name");
    System.out.println("3. Card Type");
    System.out.println("4. Exit");

    System.out.print("Enter your choice (1-3): ");
    String choice = scanner.nextLine(); // Read input using Scanner

    switch (choice) {
        case "1":
            System.out.print("Enter Bank Name: ");
            String bankName = scanner.nextLine().toLowerCase(); // Get user input for Bank Name
            addSearchTerm(bankName, "Bank Name");
            break;
        case "2":
            System.out.print("Enter Card Name: ");
            String cardName = scanner.nextLine().toLowerCase(); // Get user input for Card Name
            addSearchTerm(cardName, "Card Name");
            break;
        case "3":
            System.out.print("Enter Card Type: ");
            String cardType = scanner.nextLine().toLowerCase(); // Get user input for Card Type
            addSearchTerm(cardType, "Card Type");
            break;
            case "4":
				System.out.println("Going Back To The Search Menu...");
				break;
        default:
            System.out.println("Invalid choice. Please try again.");
    }
}

    
    

private static void addSearchTerm(String term, String field) {
    // Call the autocomplete and get the selected term
    String selectedTerm = Executer.spellCheckAndWordComplete(term);

    // Only proceed if a valid selection is made
    if (selectedTerm == null || selectedTerm.isEmpty()) {
        System.out.println("No valid selection made. Term not saved.");
        return;
    }

    // Insert into the appropriate tree
    switch (field) {
        case "Bank Name":
            bankNamesTree.insert(selectedTerm);
            break;
        case "Card Name":
            cardNamesTree.insert(selectedTerm);
            break;
        case "Card Type":
            cardTypesTree.insert(selectedTerm);
            break;
    }

    // Save the selected term to the CSV
    saveSearchTermToCSV(selectedTerm, field);
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
                return;
            case "Card Name":
                updatedValues[1] = term;
                return;
            case "Card Type":
                updatedValues[2] = term;
                return;
            
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
                .forEach( e -> System.out.println(e.getKey() + " is popular tith " + e.getValue() + " Searches"));
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
   
}
