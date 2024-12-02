package com.creditWise.Sakshi;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SearchFrequency {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // File path for CSV and text files
        String resourceFolder = "src/main/resources/";
        String csvFile = resourceFolder + "search_terms.csv"; // CSV file to store user input search terms
        String textPagesFolder = "src/main/resources/"; // Folder where card details text files are stored

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
                writer.write("Card Type,Annual Fee,Bank Name,Interest Rate,Frequency\n");
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
            System.out.println("1. Card Type");
            System.out.println("2. Annual Fee");
            System.out.println("3. Bank Name");
            System.out.println("4. Interest Rate");
            System.out.println("5. View popular search terms");
            System.out.println("6. Exit");

            System.out.print("Enter your choice (1-6): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter Card Type (e.g., 'Visa', 'MasterCard'): ");
                    String cardType = scanner.nextLine().toLowerCase();
                    saveToCSV(csvFile, cardType, null, null, null);
                    break;

                case "2":
                    System.out.print("Enter Annual Fee (e.g., '99', '199'): ");
                    String annualFee = scanner.nextLine().toLowerCase();
                    saveToCSV(csvFile, null, annualFee, null, null);
                    break;

                case "3":
                    System.out.print("Enter Bank Name (e.g., 'Chase', 'Bank of America'): ");
                    String bankName = scanner.nextLine().toLowerCase();
                    saveToCSV(csvFile, null, null, bankName, null);
                    break;

                case "4":
                    System.out.print("Enter Interest Rate (e.g., '3.5', '5.0'): ");
                    String interestRate = scanner.nextLine().toLowerCase();
                    saveToCSV(csvFile, null, null, null, interestRate);
                    break;

                case "5":
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

                case "6":
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
            String[] cardFiles = {"src/main/resources/scotiabank_cards.txt", "src/main/resources/td_cards.txt", "src/main/resources/cibc_cards.txt"};

            // Variable to store the final output of card details to show
            StringBuilder cardDetailsOutput = new StringBuilder();

            for (String filePath : cardFiles) {
                File file = new File(filePath);

                if (file.exists() && file.isFile()) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    boolean termFound = false;

                    // Read file line by line and look for the chosen term
                    while ((line = reader.readLine()) != null) {
                        // If the term is present in the line, add the card details
                        if (line.toLowerCase().contains(chosenTerm.toLowerCase())) {
                            termFound = true;

                            // Collect and format card details related to the found term
                            String[] cardData = parseCardDetails(line);

                            // Skip Bank Name in the output
                            cardDetailsOutput.append("Card Name: ").append(cardData[1]).append("\n");
                            cardDetailsOutput.append("Card Type: ").append(cardData[2]).append("\n");
                            cardDetailsOutput.append("Annual Fee: ").append(cardData[3]).append("\n");
                            cardDetailsOutput.append("Interest Rate: ").append(cardData[4]).append("\n");
                            cardDetailsOutput.append("Advantages: ").append(cardData[5]).append("\n");
                            cardDetailsOutput.append("--------------------------------------------------\n");
                        }
                    }
                    reader.close();

                    // If term is not found in the file, print a message
                    if (!termFound) {
                        cardDetailsOutput.append("\nNo cards found for the term '").append(chosenTerm).append("' in ").append(file.getName()).append(".\n");
                    }
                }
            }

            // Print final output with all card details found
            if (cardDetailsOutput.length() > 0) {
                System.out.println(cardDetailsOutput.toString());
            } else {
                System.out.println("No cards found for the term '" + chosenTerm + "'.");
            }
        } catch (IOException e) {
            System.out.println("Error reading card details: " + e.getMessage());
        }
    }

    // Helper method to parse card details from a line of text
    private static String[] parseCardDetails(String line) {
        // Example format: Card Name, Card Type, Bank Name, Annual Fee, Interest Rate, Advantages
        // Adjust based on actual file format
        return line.split(",\\s*");
    }
}
