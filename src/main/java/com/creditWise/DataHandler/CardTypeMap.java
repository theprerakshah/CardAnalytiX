package com.creditWise.DataHandler;

import java.util.HashMap;

public class CardTypeMap {
    private static final HashMap<String, String> cardTypeMap = new HashMap<>();

//    Populating the HashMap
//    Unique Card Types Available-
//    Visa Card
//    American Express
//    COSTCO CARDS
//    STUDENT CARDS
//    CASH BACK CARDS
//    TRAVEL REWARDS CARDS
//    BUSINESS CREDIT CARDS
//    LOW INTEREST CARDS
//    Mastercard

    static {
        cardTypeMap.put("visacard", "visa card");
        cardTypeMap.put("visa card", "visa card");
        cardTypeMap.put("visa-card", "visa card");
        cardTypeMap.put("visa cards", "visa card");
        cardTypeMap.put("visacards", "visa card");
        cardTypeMap.put("visa-cards", "visa card");

        cardTypeMap.put("american express", "american express");
        cardTypeMap.put("americanexpress", "american express");
        cardTypeMap.put("american-express", "american express");

        cardTypeMap.put("mastercard", "mastercard");
        cardTypeMap.put("master card", "mastercard");
        cardTypeMap.put("mastercards", "mastercard");
        cardTypeMap.put("master cards", "mastercard");

        cardTypeMap.put("low interest cards", "low interest cards");
        cardTypeMap.put("low interest card", "low interest cards");
        cardTypeMap.put("lowinterestcards", "low interest cards");
        cardTypeMap.put("lowinterestcard", "low interest cards");

        cardTypeMap.put("student cards", "student cards");
        cardTypeMap.put("student card", "student cards");
        cardTypeMap.put("studentcards", "student cards");
        cardTypeMap.put("studentcard", "student cards");

        cardTypeMap.put("cash back cards", "cash back cards");
        cardTypeMap.put("cash back card", "cash back cards");
        cardTypeMap.put("cashbackcards", "cash back cards");
        cardTypeMap.put("cashbackcard", "cash back cards");

        cardTypeMap.put("travel rewards cards", "travel rewards cards");
        cardTypeMap.put("travel rewards card", "travel rewards cards");
        cardTypeMap.put("travel reward cards", "travel rewards cards");
        cardTypeMap.put("travel reward card", "travel rewards cards");

        cardTypeMap.put("business credit cards", "business credit cards");
        cardTypeMap.put("business credit card", "business credit cards");
    }

    // Method to get the canonical bank name
    public static String getCardType(String userInput) {
        // Normalize input (case-insensitive, trim spaces)
        userInput = userInput.toLowerCase().trim();

        // Check if input exists in the map
        return cardTypeMap.getOrDefault(userInput, "Null");
    }
}

