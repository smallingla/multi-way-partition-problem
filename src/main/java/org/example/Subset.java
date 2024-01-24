package org.example;

import java.util.ArrayList;
import java.util.List;

public class Subset implements Comparable<Subset>{

    int sum;
    double standardDeviation;
    int numberOfRelativePrimePairs;
    int index;
    List<Integer> elements;


    /**
     * This constructor creates an instance of a subset initializing all the variables as empty except the index
     */
    Subset(int index) {
        this.index = index;
        this.sum = 0;
        this.elements = new ArrayList<>();
        this.standardDeviation = 0;
    }

    /**
     *This method calculates the sum of the element when a new element is added to the set
     */
    void addElement(int element) {
        elements.add(element);
        sum += element;
    }

    public int compareTo(Subset other) {
        return Integer.compare(this.sum, other.sum);
    }


    /**
     * This method computes the standard deviation for the elements in the set
     */
    public double calculateStandardDeviation() {
        if (elements.size() <= 1) { //if element is 1 or 0 return 0
            return 0;
        }

        double mean = this.sum / (double) this.elements.size(); // calculate the mean of the elements
        double sumSquaredDifferences = 0;

        for (int element : elements) {
            sumSquaredDifferences += Math.pow(element - mean, 2); // process the sum of the squared difference
        }

        double variance = sumSquaredDifferences / (this.elements.size() - 1); // Using sample variance formula (n-1 in the denominator)
        standardDeviation = Math.sqrt(variance);
        return standardDeviation;
    }


    /**
     * This method counts the number of relative prime pairs in the set
     */
    public void countNumberOfRelativelyPrimePairs() {
        int count = 0;
        for (int i = 0; i < elements.size(); i++) { //this loops over all the items in the set
            for (int j = i + 1; j < elements.size(); j++) { //this loops over from the next element of i to the end
                if (isGreatestCommonDividerOne(elements.get(i), elements.get(j))) { //check if the gcd is one
                    count++;
                }
            }
        }
        this.numberOfRelativePrimePairs = count; //set the subset instance to the count
    }

    /**
     *implement the gcd using the Euclid's method
     *code adapted from willeM_ Van Onsem, 2015
     */
    private boolean isGreatestCommonDividerOne(int a, int b) {
        while (b != 0) { //keep looping while b is not 0 i.e., When b becomes 0, a contains the GreatestCommonDivider
            int temp = b;  // Temporary variable to hold the value of b
            b = a % b;     // Replace b with the remainder of the division of a by b
            a = temp;      // Replace a with the original value of b
        }
        return a == 1;
    }

    @Override
    public String toString() {
        return "Subset{" +
                "sum=" + sum +
                ", standardDeviation=" + standardDeviation +
                ", numberOfRelativePrimePairs=" + numberOfRelativePrimePairs +
                ", index=" + index +
                ", elements=" + elements +
                '}';
    }
}
