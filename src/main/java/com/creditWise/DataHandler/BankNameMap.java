package com.creditWise.DataHandler;
import java.util.HashMap;
import java.util.Scanner;

public class BankNameMap {
    private static final HashMap<String, String> bankMap = new HashMap<>();

    // Populating the HashMap
    static {
        bankMap.put("td", "td bank");
        bankMap.put("td bank", "td bank");
        bankMap.put("toronto dominion bank", "td bank");

        bankMap.put("cibc", "cibc");
        bankMap.put("canadian imperial bank of commerce", "cibc");
        bankMap.put("cibc bank", "cibc");

        bankMap.put("rbc", "rbc");
        bankMap.put("royal bank of canada", "rbc");
        bankMap.put("rbc bank", "rbc");

        bankMap.put("scotiabank", "scotia bank");
        bankMap.put("scotia bank", "scotia bank");
        bankMap.put("scotia", "scotia bank");
        bankMap.put("the bank of nova scotia", "scotia bank");
    }

    // Method to get the acceptable bank names
    public static String getBankName(String userInput) {
        // Normalize input (case-insensitive, trim spaces)
        userInput = userInput.toLowerCase().trim();

        // Check if input exists in the map
        return bankMap.getOrDefault(userInput, "Null");
    }

}
