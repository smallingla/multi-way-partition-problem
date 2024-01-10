package org.example;


import java.util.List;

public class Parition {

    Integer subsetSumDifference;//this holds the difference between the largest sum and smallest set
    List<Subset> subsets;

    public Parition(List<Subset> subsets) {
        this.subsets = subsets;
        this.subsetSumDifference = calculateDifference(subsets);
    }



    private static int calculateDifference(List<Subset> subsets) {
        if (subsets == null || subsets.isEmpty()) {
            throw new IllegalArgumentException("Subsets list must not be null or empty.");
        }

        int maxSum = Integer.MIN_VALUE;
        int minSum = Integer.MAX_VALUE;

        for (Subset subset : subsets) {
            if (subset.sum > maxSum) {
                maxSum = subset.sum;
            }
            if (subset.sum < minSum) {
                minSum = subset.sum;
            }
        }

        return maxSum - minSum;
    }


    @Override
    public String toString() {
        return "Parition{" +
                "subsetSumDifference=" + subsetSumDifference +
                ", subsets=" + subsets +
                '}';
    }
}
