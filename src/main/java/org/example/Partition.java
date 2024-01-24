package org.example;


import java.util.ArrayList;
import java.util.List;

public class Partition {

    /**
     *This variable holds the difference between the smallest standard deviation among all subsets and the desired threshold (target standard deviation + 1).
     *We add 1 to the target standard deviation to ensure that the smallest standard deviation is not just equal to, but greater than the target.
     */
    double standardDeviationDifference;
    Integer subsetSumDifference; //this holds the difference between the largest sum and smallest sum in the subsets
    Integer smallestRelativePrimePair;//this holds the smallest relative pair in the list of substes for the partition
    List<Subset> subsets; //this holds the list of subsets in the partition

    /**
     * This constructor creates an instance of partition for the optimization problem with or wothout the secondary objective.
     */
    public Partition(List<Subset> subsets, boolean hasSecondaryObjective) {
        if(hasSecondaryObjective){
            this.subsets = subsets;
            processSubSetSumDifferenceAndLeastNumberOfRelativePrimePairs(subsets);
        }
        this.subsets = subsets;
        this.subsetSumDifference = calculateSubSetSumDifference(subsets);
    }

    /**
     *This constructor creates an instance of partition when the standard deviation constraint is needed.
     */
    public Partition(List<Subset> subsets, double targetStandardDeviation) {
        this.subsets = subsets;
        processSubSetSumDifferenceAndSmallestStandardDeviation(subsets,targetStandardDeviation);
    }

    /**
     * This default constructor creates an optimal partition instance i.e., the system compares the result to this.
     * NOTE: This partition is more like the worst possible.
     */
    public Partition(){
        this.subsets = new ArrayList<>();
        this.subsetSumDifference = Integer.MAX_VALUE;
        this.standardDeviationDifference = Double.MIN_VALUE;
        this.smallestRelativePrimePair = Integer.MIN_VALUE;
    }


    /**
     * This method calculates the difference between the highest subset sum and lowest subset sum
     */
    private static int calculateSubSetSumDifference(List<Subset> subsets) {
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

    /**
     * Purpose1: calculates the difference between the highest subset sum and lowest subset sum
     * Purpose2: get the smallest standard deviation in the list of subsets
     */
    private void processSubSetSumDifferenceAndSmallestStandardDeviation(List<Subset> subsets,  double targetStandardDeviation) {
        if (subsets == null || subsets.isEmpty()) {
            throw new IllegalArgumentException("Subsets list must not be null or empty.");
        }

        int maxSum = Integer.MIN_VALUE;
        int minSum = Integer.MAX_VALUE;
        double minStandardDev = Double.MAX_VALUE;

        for (Subset subset : subsets) {
            if (subset.sum > maxSum) {
                maxSum = subset.sum;
            }
            if (subset.sum < minSum) {
                minSum = subset.sum;
            }
            if(subset.standardDeviation < minStandardDev){ //get the subset with the smallest stdDeviation
                minStandardDev = subset.standardDeviation;
            }
        }

        this.subsetSumDifference = maxSum - minSum;
        this.standardDeviationDifference = (targetStandardDeviation+1) - minStandardDev;
    }

    /**
     * Purpose1: calculates the difference between the highest subset sum and lowest subset sum
     * Purpose2: get the smallest number of relative pairs in the list of sets
     */
    private void processSubSetSumDifferenceAndLeastNumberOfRelativePrimePairs(List<Subset> subsets) {
        if (subsets == null || subsets.isEmpty()) {
            throw new IllegalArgumentException("Subsets list must not be null or empty.");
        }

        int maxSum = Integer.MIN_VALUE;
        int minSum = Integer.MAX_VALUE;
        int minRelativePairs = Integer.MAX_VALUE;

        for (Subset subset : subsets) {
            if (subset.sum > maxSum) {
                maxSum = subset.sum;
            }
            if (subset.sum < minSum) {
                minSum = subset.sum;
            }
            if(subset.numberOfRelativePrimePairs < minRelativePairs){
                minRelativePairs = subset.numberOfRelativePrimePairs;
            }
        }

        this.subsetSumDifference = maxSum - minSum;
        this.smallestRelativePrimePair = minRelativePairs;
    }

    @Override
    public String toString() {
        return "Partition{" +
                "standardDeviationDifference=" + standardDeviationDifference +
                ", subsetSumDifference=" + subsetSumDifference +
                ", smallestRelativePrimePair=" + smallestRelativePrimePair +
                ", subsets=" + subsets +
                '}';
    }
}
