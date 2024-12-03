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
            double leftAnnualFee = extractAnnualFee(left.get(leftIndex));
            double rightAnnualFee = extractAnnualFee(right.get(rightIndex));

            boolean condition;
            if (isAscending) {
                condition = leftAnnualFee <= rightAnnualFee;  // Ascending order
            } else {
                condition = leftAnnualFee >= rightAnnualFee;  // Descending order
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

    // Helper method to extract the numeric value of the annual fee
    private static double extractAnnualFee(CreditCard card) {
        String annualFeeString = card.getAnnualFee().replaceAll("[^0-9.]", ""); // Remove non-numeric characters
        try {
            return Double.parseDouble(annualFeeString); // Convert string to double
        } catch (NumberFormatException e) {
            return Double.MAX_VALUE; // If parsing fails, treat as a very large value
        }
    }
}
