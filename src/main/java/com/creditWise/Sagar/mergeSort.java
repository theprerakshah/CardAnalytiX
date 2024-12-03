package com.creditWise.Sagar;

import java.util.ArrayList;
import com.creditWise.CardAnalytiX.CreditCard;

public class mergeSort {

    // Merge sort method for CreditCard list with sorting order
    public static ArrayList<CreditCard> sort(ArrayList<CreditCard> cardList, boolean isAscending) {
        if (cardList.size() <= 1) {
            return cardList; // Base case: list is already sorted
        }

        // Divide the list into two halves
        int middle = cardList.size() / 2;
        ArrayList<CreditCard> left = new ArrayList<>(cardList.subList(0, middle));
        ArrayList<CreditCard> right = new ArrayList<>(cardList.subList(middle, cardList.size()));

        // Recursively sort both halves
        left = sort(left, isAscending);
        right = sort(right, isAscending);

        // Merge the sorted halves and return the sorted list
        return merge(left, right, isAscending);
    }

    // Merge two sorted lists with specified order
    private static ArrayList<CreditCard> merge(ArrayList<CreditCard> left, ArrayList<CreditCard> right, boolean isAscending) {
        ArrayList<CreditCard> result = new ArrayList<>();
        int leftIndex = 0, rightIndex = 0;

        // Compare and merge the two sorted lists based on the specified order
        while (leftIndex < left.size() && rightIndex < right.size()) {
            double leftInterestRate = extractInterestRate(left.get(leftIndex));
            double rightInterestRate = extractInterestRate(right.get(rightIndex));

            boolean condition;
            if (isAscending) {
                condition = leftInterestRate <= rightInterestRate;  // Ascending order
            } else {
                condition = leftInterestRate >= rightInterestRate;  // Descending order
            }

            if (condition) {
                result.add(left.get(leftIndex));
                leftIndex++;
            } else {
                result.add(right.get(rightIndex));
                rightIndex++;
            }
        }

        // Add remaining elements from left or right
        result.addAll(left.subList(leftIndex, left.size()));
        result.addAll(right.subList(rightIndex, right.size()));

        return result;
    }

    // Helper method to extract the numeric value of the purchase interest rate
    private static double extractInterestRate(CreditCard card) {
        String interestRateString = card.getPurchaseInterestRate().replaceAll("[^0-9.]", ""); // Remove non-numeric characters
        try {
            return Double.parseDouble(interestRateString); // Convert string to double
        } catch (NumberFormatException e) {
            return Double.MAX_VALUE; // If parsing fails, treat as a very large value
        }
    }

    // Helper method to extract the numeric value of the annual fee
    private static double extractAnnualFee(CreditCard card) {
        String annualFeeString = card.getAnnualFee().replaceAll("[^0-9.]", ""); // Remove non-numeric characters
        try {
            return Double.parseDouble(annualFeeString); // Convert string to double
        } catch (NumberFormatException e) {
            return Double.MAX_VALUE; // If parsing fails, treat as a very large value
        }
    }
    public static ArrayList<CreditCard> sortByAnnualFee(ArrayList<CreditCard> cardList, boolean isAscending) {
        if (cardList.size() <= 1) {
            return cardList; // Base case: list is already sorted
        }
    
        // Divide the list into two halves
        int middle = cardList.size() / 2;
        ArrayList<CreditCard> left = new ArrayList<>(cardList.subList(0, middle));
        ArrayList<CreditCard> right = new ArrayList<>(cardList.subList(middle, cardList.size()));
    
        // Recursively sort both halves
        left = sortByAnnualFee(left, isAscending);
        right = sortByAnnualFee(right, isAscending);
    
        // Merge the sorted halves and return the sorted list
        return mergeByAnnualFee(left, right, isAscending);
    }
    
    private static ArrayList<CreditCard> mergeByAnnualFee(ArrayList<CreditCard> left, ArrayList<CreditCard> right, boolean isAscending) {
        ArrayList<CreditCard> result = new ArrayList<>();
        int leftIndex = 0, rightIndex = 0;
    
        while (leftIndex < left.size() && rightIndex < right.size()) {
            // Parse annual fee with error handling
            double leftAnnualFee = parseAnnualFee(left.get(leftIndex).getAnnualFee());
            double rightAnnualFee = parseAnnualFee(right.get(rightIndex).getAnnualFee());
    
            boolean condition;
            if (isAscending) {
                condition = leftAnnualFee <= rightAnnualFee; // Ascending order
            } else {
                condition = leftAnnualFee >= rightAnnualFee; // Descending order
            }
    
            if (condition) {
                result.add(left.get(leftIndex));
                leftIndex++;
            } else {
                result.add(right.get(rightIndex));
                rightIndex++;
            }
        }
    
        // Add remaining elements from left or right
        result.addAll(left.subList(leftIndex, left.size()));
        result.addAll(right.subList(rightIndex, right.size()));
    
        return result;
    }
    
    // Helper method to parse annual fees
    private static double parseAnnualFee(String fee) {
        try {
            // Remove "$" and any non-numeric characters except "."
            String cleanedFee = fee.replaceAll("[^0-9.]", "").trim();
            return Double.parseDouble(cleanedFee);
        } catch (NumberFormatException e) {
            // Handle invalid format (e.g., "Annual Fee")
            System.out.println("Invalid annual fee format: " + fee);
            return Double.MAX_VALUE; // Assign a high value to sort it to the end
        }
    }
    
    
    
}
