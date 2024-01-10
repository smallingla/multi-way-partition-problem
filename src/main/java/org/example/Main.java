package org.example;

import java.util.*;

public class Main {


    //this map holds the subset combination with its
    static Parition optimalPartition;



    public static void main(String[] args) {

        // Example usage
        List <Integer> U = new ArrayList<>(Arrays.asList(2,4,6,8,10));
        U.addAll(Arrays.asList(87,45,32,56,3,56,21,90,87,54,23,56,78,34,95,89,84,83,81,71,62,59,56,55,54,53,40,39,29,20,11,10,7,6,2,3,4,12,3,4,2,4,5,6,87,23,5,50,34,5,78,23));
        U.addAll(Arrays.asList(87,45,32,56,3,56,21,87,54,23,56,78,34,95,89,84,83,81,71,62,59,56,55,54,53,40,39,29,20,11,10,7,6,2,3,4,12,3,4,2,4,5,6,87,23,5,50,34,5,78,23));
        U.addAll(Arrays.asList(87,4,54,23,56,78,34,95,89,84,83,81,71,62,59,56,55,54,53,40,39,29,20,11,10,7,6,2,3,4,12,3,4,2,4,5,6,87,23,5,50,34,5,78,23));
        int k = 5; // Number of subsets to partition U into

        int sumOfU = U.stream().mapToInt(Integer::intValue).sum();// get the sum of elements in U
        minimizeWithDefaultConstraint(U,sumOfU, k); //process the multipart problem with no addditional contraint


    }


    /**
     *Question 2a The optimisation version of the multi-way partitioning problem. You should minimise the difference between the highest and lowest sums over all subsets.
     */
    private static void minimizeWithDefaultConstraint(List<Integer> U,Integer sumOfElements, int k){

        if(sumOfElements % k == 0){ // check if for sure there is a possible equal partition
            int subsetSumDifference ;
            do{
                List<Subset> subsets = partitionUsingGrasp(U, k); // perform grasp
                Parition parition = new Parition(subsets); //pass all subsets as a potential partition
                subsetSumDifference = parition.subsetSumDifference; //get the sum difference of the partition
                optimalPartition = parition;
            }
            while( subsetSumDifference>0); //keep performing the grasp until the sum difference is 0
        }else{  //if there is no equal partition
            int numberOfIterations = 0;
            do{
                List<Subset> subsets = partitionUsingGrasp(U, k); // perform grasp
                Parition parition = new Parition(subsets); //convert all subsets as a potential partition
                if(numberOfIterations==0){
                    optimalPartition = parition;
                }else {
                    if (parition.subsetSumDifference - optimalPartition.subsetSumDifference < 0) { //gte the sum difference of the partition
                        System.out.println(parition);
                        optimalPartition = parition;
                    }
                }
                numberOfIterations++;
            }while(numberOfIterations<100);
        }

        System.out.println("processing..");
        int i = 1;
        for (Subset subset : optimalPartition.subsets){ //print results
            System.out.println("Subset" + i++ + " = " + subset.sum+ " " +subset.elements);
        }
        System.out.println("Difference :" + optimalPartition.subsetSumDifference);
    }


    /** implement a greedy randomized adaptive search procedure by selecting the top 3 numbers
     and randomizing them to select the one to put into the subset
     **/
    public static List<Subset> partitionUsingGrasp(List<Integer> U, int k) {

        //copy the elements in U into the set of unallocatedNumbers
        List<Integer> unallocatedNumbers = new ArrayList<>(U);
        unallocatedNumbers.sort(Comparator.reverseOrder());

        PriorityQueue<Subset> pq = initializePriotyQueueForSubset(k);

        //create a list that holds the unallocated elements in descending order. we need to make it modifieable


        //perform operation provided elements are still int the unallocated
        while (!unallocatedNumbers.isEmpty()){

            List<Integer> top3Numbers = new ArrayList<>(); //variable to hold the top 3 elements
            //this set to the smaller of 3 or the size of unallocated size
            int numberOfElementsToAdd = Math.min(3, unallocatedNumbers.size());
            //select the top 3 numbers from the unallocated
            for (int i = 0; i < numberOfElementsToAdd; i++) {
                top3Numbers.add(unallocatedNumbers.get(i));
            }


             //select a random element from the highest 3
             Integer selectedElement = pickRandomly(top3Numbers);

            Subset minSubset = pq.poll(); // Get the subset with the smallest sum
            minSubset.addElement(selectedElement); // Add the current selected element to it
            pq.add(minSubset); // Put it back into the priority queue

          unallocatedNumbers.remove(selectedElement); //remove the selected element from the list of allocated because it has been alllocated
        }
        return new ArrayList<>(pq);

    }

    private static PriorityQueue<Subset> initializePriotyQueueForSubset(int k) {
        // Initialize priority queue (min-heap) to keep track of subsets by their sum
        PriorityQueue<Subset> pq = new PriorityQueue<>();

        // Create k subsets with initial sum of 0 and empty lists and put them in the priority queue
        for (int i = 0; i < k; i++) {
            pq.add(new Subset(i));
        }
        return pq;
    }


    // method to pick a random number from the top numbers
    private static int pickRandomly(List<Integer> topNumbers) {

        Random random = new Random();
//        System.out.println(topNumbers);
        int randomIndex = random.nextInt(topNumbers.size());
        return topNumbers.get(randomIndex);

    }











//    /** implement a greedy randomized adaptive search by selecting the top 3 numbers
//     and randomizing them to select the one to put into the subset
//     **/
//    public static List<List<Integer>> partitionUsingGraps(int[] U, int k) {
//        // Sort the array in ascending order
//        Arrays.sort(U);
//
//        // Initialize priority queue (min-heap) to keep track of subsets by their sum
//        PriorityQueue<Subset> pq = new PriorityQueue<>();
//
//        // Create k subsets with initial sum of 0 and empty lists and put them in the priority queue
//        for (int i = 0; i < k; i++) {
//            pq.add(new Subset(i));
//        }
//
//        System.out.println(Arrays.toString(U));
//        // Distribute the numbers into subsets starting with the largest number. i.e. from the end of the array
//        for (int i = U.length - 1; i >= 0; i--) {
//            Subset minSubset = pq.poll(); // Get the subset with the smallest sum
//
//            minSubset.addElement(U[i]); // Add the current element to it
//            pq.add(minSubset); // Put it back into the priority queue
//
//            System.out.println(minSubset.elements);
//        }
//
//        // Collect the subsets
//        List<List<Integer>> subsets = new ArrayList<>();
//        while (!pq.isEmpty()) {
//            subsets.add(pq.poll().elements);
//        }
//
//        return subsets;
//    }
}

