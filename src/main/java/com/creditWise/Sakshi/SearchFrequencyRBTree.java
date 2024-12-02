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
import java.util.Stack;

public class SearchFrequencyRBTree {
    private static RedBlackTree<String> bankNamesTree = new RedBlackTree<>();
    private static RedBlackTree<String> cardNamesTree = new RedBlackTree<>();
    private static RedBlackTree<String> cardTypesTree = new RedBlackTree<>();
    private static RedBlackTree<String> annualFeesTree = new RedBlackTree<>();
    private static RedBlackTree<String> interestRatesTree = new RedBlackTree<>();

    private static final String RESOURCE_PATH = "src/main/resources/";
    private static final String SEARCHES_CSV = "src/main/resources/searches.csv"; // Path to the CSV file

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

                case "4":
                    System.out.print("Enter Annual Fee: ");
                    String annualFee = scanner.nextLine();
                    addSearchTerm(annualFee, "Annual Fee");
                    break;

                case "5":
                    System.out.print("Enter Interest Rate: ");
                    String interestRate = scanner.nextLine();
                    addSearchTerm(interestRate, "Interest Rate");
                    break;

                case "6":
                    viewPopularSearchTerms(scanner);
                    break;

                case "7":
                    System.out.println("Thank you for using the tool. Goodbye!");
                    continueSearching = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
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
            case "Annual Fee":
                annualFeesTree.insert(term);
                break;
            case "Interest Rate":
                interestRatesTree.insert(term);
                break;
        }
        saveSearchTermToCSV(term, field);
    }

    private static void saveSearchTermToCSV(String term, String field) {
        File csvFile = new File(SEARCHES_CSV);
        Map<String, String[]> data = new HashMap<>(); // Key: Term, Value: [Bank Name, Card Type, Annual Fee, Interest Rate, Frequency]

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            // Read all existing records and store them
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String existingTerm = parts[0].trim(); // Assuming the first column is the term
                    String[] values = new String[5];
                    System.arraycopy(parts, 0, values, 0, 5);
                    data.put(existingTerm, values);
                }
            }

            // Update the record based on the field and term
            String[] updatedValues = data.getOrDefault(term, new String[]{"", "", "", "", "0"});

            switch (field) {
                case "Bank Name":
                    updatedValues[0] = term;
                    break;
                case "Card Type":
                    updatedValues[1] = term;
                    break;
                case "Annual Fee":
                    updatedValues[2] = term;
                    break;
                case "Interest Rate":
                    updatedValues[3] = term;
                    break;
            }

            // Update frequency (increment by 1)
            updatedValues[4] = String.valueOf(Integer.parseInt(updatedValues[4]) + 1);
            data.put(term, updatedValues);

            // Write updated data back to CSV
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
                writer.write("Bank Name,Card Type,Annual Fee,Interest Rate,Frequency\n");  // Header
                for (String[] values : data.values()) {
                    writer.write(String.join(",", values) + "\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void viewPopularSearchTerms(Scanner scanner) {
        System.out.println("\nChoose a field to view popular search terms:");
        System.out.println("1. Bank Name");
        System.out.println("2. Card Name");
        System.out.println("3. Card Type");
        System.out.println("4. Annual Fee");
        System.out.println("5. Interest Rate");

        System.out.print("Enter your choice (1-5): ");
        String fieldChoice = scanner.nextLine();

        switch (fieldChoice) {
            case "1":
                displaySearchTerms(bankNamesTree, "Bank Name", scanner);
                break;
            case "2":
                displaySearchTerms(cardNamesTree, "Card Name", scanner);
                break;
            case "3":
                displaySearchTerms(cardTypesTree, "Card Type", scanner);
                break;
            case "4":
                displaySearchTerms(annualFeesTree, "Annual Fee", scanner);
                break;
            case "5":
                displaySearchTerms(interestRatesTree, "Interest Rate", scanner);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void displaySearchTerms(RedBlackTree<String> tree, String field, Scanner scanner) {
        Map<String, Integer> termFrequencyMap = new HashMap<>();

        // Perform in-order traversal iteratively and count frequencies
        RedBlackTree.Node current = tree.getRoot();
        Stack<RedBlackTree.Node> stack = new Stack<>();

        while (current != tree.NIL || !stack.isEmpty()) {
            if (current != tree.NIL) {
                stack.push(current);
                current = current.left;
            } else {
                current = stack.pop();
                termFrequencyMap.put((String) current.value, termFrequencyMap.getOrDefault((String) current.value, 0) + 1);
                current = current.right;
            }
        }

        // Display terms sorted by frequency
        System.out.println("Here is a list of popular " + field.toLowerCase() + "s based on user search frequency:");

        // Sort terms by frequency (descending)
        termFrequencyMap.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue() - entry1.getValue())
                .forEach(entry -> System.out.println(entry.getKey() + " with " + entry.getValue() + " searches"));

        // Allow the user to select a term by number
        System.out.print("\nSelect which " + field + " you want to see: ");
        int selectedIndex = Integer.parseInt(scanner.nextLine()) - 1;

        // Get the selected term from the list
        String selectedTerm = (String) termFrequencyMap.keySet().toArray()[selectedIndex];

        // Fetch and display the card details from file
        displayCardDetailsForSelectedTerm(selectedTerm);
    }

    private static void displayCardDetailsForSelectedTerm(String selectedTerm) {
        // Convert the selected term to lowercase to match the file names
        String fileName = selectedTerm.toLowerCase() + ".txt";
        File cardFile = new File(RESOURCE_PATH + fileName);

        if (cardFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(cardFile))) {
                String line;
                System.out.println("\nDetails for " + selectedTerm + ":");
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No details available for the selected term.");
        }
    }
}
