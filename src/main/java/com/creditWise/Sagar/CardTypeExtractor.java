package com.creditWise.Sagar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CardTypeExtractor {

    // Method to extract card types from all files in a folder
    public static List<String> extractCardTypes(String folderPath) throws IOException {
        List<String> cardTypes = new ArrayList<>();
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Error: The specified folder does not exist or is not a directory.");
            return cardTypes;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt")); // Filter only .txt files

        if (files == null || files.length == 0) {
            System.out.println("Error: No text files found in the folder.");
            return cardTypes;
        }

        // Iterate through all text files in the folder
        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    // Check if the line starts with "Card Type:"
                    if (line.startsWith("Card Type:")) {
                        // Extract the value after "Card Type:"
                        String cardType = line.substring("Card Type:".length()).trim();
                        if (!cardTypes.contains(cardType)) {
                            cardTypes.add(cardType);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + file.getName());
            }
        }

        return cardTypes;
    }
}
