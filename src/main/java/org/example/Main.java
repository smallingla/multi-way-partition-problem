package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) {

//        List<Integer> U = new ArrayList<>(generateRandomListWithDuplicate(500, 50, 1000));// you can use this to generate a list with duplicate element
        List<Integer> U = new ArrayList<>(generateRandomListWithoutDuplicate(500, 10, 800));// you can use this to generate a list without duplicate element
        int k = 3; // Number of subsets to partition U into
        int targetStandardDeviation = 270;


        minimizeWithDefaultConstraint(U, k); // (Question 3a)
//        minimizeWithDefaultConstraintAndStandardDeviationConstraint(U, k,targetStandardDeviation); //(Question 3b) uncomment to test
//        maximizeWithDefaultConstraintMaximisingTheNumberOfRelativePrimePairs(U,k); //(Question 3c) uncomment to test
    }


    /**
     *Question 3a The optimisation version of the multi-way partitioning problem. You should minimise the difference between the highest and lowest sums over all subsets.
     */
    private static void minimizeWithDefaultConstraint(List<Integer> U, int k){

        U.sort(Comparator.reverseOrder()); //sort elements in U in descending order

        System.out.println("*************optimization version of the multi-way partitioning problem************");
        System.out.println("U: " + U);
        System.out.println("k: " + k);
        System.out.println("size of list: "+U.size());

        Partition optimalPartition = new Partition();


        int numberOfIterations = 0; // declare variable to track number of iterations
        do{

            List<Subset> subsets = partitionUsingGrasp(U, k); // perform grasp. the grasp method returns the list of k subsets
            Partition partition = new Partition(subsets,false); //convert subsets to a potential partition (a partition is an object holding the subset difference and the subsets);

            /*
              check if the new partition is better than the optimal partition()
              criteria 1 : the subset sum difference is less than that of the current optimal partition
             */
            if (partition.subsetSumDifference < optimalPartition.subsetSumDifference) {
                optimalPartition = partition; // make current partition the optimal partition since it sum difference is smaller
            }

            numberOfIterations++;// increment the numberOfIterations

        }while(optimalPartition.subsetSumDifference!=0 && numberOfIterations < 10000); // set stopping criteria to be after  n number of iterations

        //format and print result to the console

        System.out.println("Format is: Subset n = sum of subset  [subset element]\n");
        int i = 1;
        for (Subset subset : optimalPartition.subsets){ //print results
            System.out.println("Subset " + i++ + " = " + subset.sum+ " " +subset.elements);
        }
        System.out.println("subset sum difference :" + optimalPartition.subsetSumDifference);
    }

    /**
     * Question 3b The optimisation version of the multi-way partitioning problem.
     * but with a constraint requiring the standard deviation among values in each set must be above some lower limit σmin.
     * The universe U may be a multiset (contain duplicates) in this case.
     */
    private static void minimizeWithDefaultConstraintAndStandardDeviationConstraint(List<Integer> U,int k, double targetStd){

        U.sort(Comparator.reverseOrder()); //sort elements in U in descending order

        System.out.println("\n\n*************optimization version, while ensuring the standard deviation is higher than target************");
        System.out.println("U: " + U);
        System.out.println("k: " + k);
        System.out.println("size of list: "+U.size());


        Partition optimalPartition = new Partition();
        int numberOfIterations = 0; // declare variable to track number of iterations
        do{
            List<Subset> subsets = partitionUsingGrasp(U, k); // perform grasp. the grasp method returns a list of k subsets,
            boolean isStandardDeviationConditionMet = doesAllSubsetMeetStandardDeviationRequirement(subsets,targetStd); // process the standard the deviation of the subset and check if the standard deviation of each subset is greater than targetStd
            Partition partition = new Partition(subsets,targetStd); //convert subsets to a potential partition (a partition is an object holding the subset difference, standard deviation difference and the k subsets);

            /*
              check if the new partition is better than the optimal partition
              criteria 1 : the subset sum difference is less than that of the current optimal partition
              criteria 2: the standard deviation of all subset is above targetStd or the new standard deviation difference is smaller than that of optimal partition (standard deviation difference =  difference between the lowest standard deviation for the subset and the targetStd+1 i.e. greater than)
             */
            if ((partition.subsetSumDifference < optimalPartition.subsetSumDifference)  &&  (isStandardDeviationConditionMet || partition.standardDeviationDifference > optimalPartition.standardDeviationDifference)) {
                optimalPartition = partition; // make current partition the optimal partition since it sum difference is smaller
            }
            if(optimalPartition.subsetSumDifference<=1 && isStandardDeviationConditionMet){ //if the partition has a sum difference of 1 terminate loop and make it the optimal partition
                break;
            }
            numberOfIterations++; // increment the numberOfIterations

        }while(numberOfIterations<1000); // set stopping criteria to be after n number of iterations



        //format and print result to the console
        System.out.println("processing..");
        int i = 1;
            System.out.println("Format is: Subset n = sum of subset | standard deviation | [subset element]\n");

            for (Subset subset : optimalPartition.subsets){
            System.out.println("Subset " + i++ + " = " + subset.sum+ " | " +subset.standardDeviation +" | " +subset.elements);
        }
        System.out.println("subset sum difference :" + optimalPartition.subsetSumDifference);
        System.out.println("lowes subset standard deviation difference from target :" + optimalPartition.standardDeviationDifference);
    }

    /**
     * Question 3c The optimisation version of the multi-way partitioning problem.
     * but with a secondary objective of maximising the number of relatively prime pair in each set
     */
    private static void maximizeWithDefaultConstraintMaximisingTheNumberOfRelativePrimePairs(List<Integer> U, int k){

        U.sort(Comparator.reverseOrder()); //sort elements in U in descending order

        System.out.println("\n\n*************optimization version, while maximising the number of relatively prime pair************");
        System.out.println("U: " + U);
        System.out.println("k: " + k);
        System.out.println("size of list: "+U.size());

        Partition optimalPartition = new Partition();


        int numberOfIterations = 0; // declare variable to track number of iterations
        do{
            List<Subset> subsets = partitionUsingGrasp(U, k); // perform grasp. the grasp method returns a list of k subsets,
            Partition partition = new Partition(subsets,true); //convert subsets to a potential partition (a partition is an object holding the subset difference, standard deviation difference and the k subsets);

            processNumberOfRelativePairsInSubSet(subsets);//calculate the number of relative prime pair in each subset
            /*
             * check if the new partition is better than the optimal partition()
             * criteria 1 : the subset sum difference is less than that of the current optimal partition
             * criteria 2 : the least number of relative prime pairs in the partition is greater than the optimal partition
             */
            boolean relativePrimePairIsGreater = partition.smallestRelativePrimePair > optimalPartition.smallestRelativePrimePair;
            if ((partition.subsetSumDifference - optimalPartition.subsetSumDifference < 0)  && relativePrimePairIsGreater) {
                optimalPartition = partition; // make current partition the optimal partition since it sum difference is smaller
            }
            if(optimalPartition.subsetSumDifference<=1 && relativePrimePairIsGreater){ //if the partition has a sum difference of 1 and has a better relative pair terminate loop and make it the optimal partition
                break;
            }
            numberOfIterations++; // increment the numberOfIterations by 1

        }while(numberOfIterations<10000); // set stopping criteria to be after n number of iterations


        //format and print result to the console
        System.out.println("processing..");
        int i = 1;
        System.out.println("Format is: Subset n = sum of subset | number of relative prime pairs | [subset element]\n");

        for (Subset subset : optimalPartition.subsets){
            System.out.println("Subset " + i++ + " = " + subset.sum+ " | " +subset.numberOfRelativePrimePairs +" | " +subset.elements);
        }
        System.out.println("subset sum difference :" + optimalPartition.subsetSumDifference);

    }



    /**
     * Implement a greedy randomized adaptive search procedure by selecting the top 3 numbers.
     * randomly select one from the numbers and put into the subset
     **/
    public static List<Subset> partitionUsingGrasp(List<Integer> U, int k) {

        List<Integer> unallocatedNumbers = new ArrayList<>(U); //copy the elements in U into the list of unallocatedNumbers to keep track of numbers that are not allocated

        PriorityQueue<Subset> pq = initializePriorityQueueForSubset(k); // initialize a priority queue for the subset

        //perform operation provided elements are still in the unallocated list
        while (!unallocatedNumbers.isEmpty()){

            //select a random element from the highest 3 elements in the unallocated list
             Integer selectedElement = pickRandomly(selectHighest3Elements(unallocatedNumbers));

            Subset smallestSubset = pq.poll(); // Get the subset with the smallest sum from the priority queue
            smallestSubset.addElement(selectedElement); // Add the current selected element to the smallest subset
            pq.add(smallestSubset); // Add the smallest subset back into the priority queue

          unallocatedNumbers.remove(selectedElement); //remove the selected element from the list of allocated because it has been allocated
        }
        return new ArrayList<>(pq);
    }

    /**
     *This method processes the standard deviation of all subset  a list.
     *It also checks if the subset meets the standard deviation requirement i.e. subSetStdDev > targetStd
     */
    private static boolean doesAllSubsetMeetStandardDeviationRequirement(List<Subset> subsets, double targetStd){
        boolean isPartitionValid = true;

        for(Subset subset : subsets){ //loop through the subset to process their standard deviation
            if(subset.calculateStandardDeviation() < targetStd){
                isPartitionValid = false; // since the standard deviation does not meet required, set isPartitionValid to valid
                //don't break to the loop so the standard deviation for each subset is still calculated and stored
            }
        }
        return isPartitionValid;
    }

    /**
     * This method processes the number of relative pairs in in a list subset
     */
    private static void processNumberOfRelativePairsInSubSet(List<Subset> subsets){

        for(Subset subset : subsets){ //loop through the subset to process their standard deviation
            subset.countNumberOfRelativelyPrimePairs();
        }
    }


    /**
     * This method selects the top 3 highest elements in a list
     */
    private static List<Integer> selectHighest3Elements(List<Integer> unallocatedNumbers) {
        List<Integer> top3Numbers = new ArrayList<>(); //declare an empty array to hold the highest 3 numbers
        int numberOfElementsToAdd = Math.min(3, unallocatedNumbers.size()); //this variable holds the number of elements to be selected. i.e.if the size of the unallocated list is less than 3, it sets the size to  number of element left
        //select the top 3 numbers from the unallocated
        for (int i = 0; i < numberOfElementsToAdd; i++) {
            top3Numbers.add(unallocatedNumbers.get(i));
        }
        return top3Numbers;
    }

    private static PriorityQueue<Subset> initializePriorityQueueForSubset(int k) {
        // Initialize priority queue (min-heap) to keep track of subsets by their sum
        PriorityQueue<Subset> pq = new PriorityQueue<>();

        // Create k subsets with initial sum of 0 and empty lists and put them in the priority queue
        for (int i = 0; i < k; i++) {
            pq.add(new Subset(i));
        }
        return pq;
    }


    /**
     *This method selects a random number from the list of highest number
     */
    private static int pickRandomly(List<Integer> topNumbers) {

        Random random = new Random();
        int randomIndex = random.nextInt(topNumbers.size());
        return topNumbers.get(randomIndex);

    }

    /**
     *This method checks if there exists an element greater than k in the list
     */
    public static boolean elementGreaterThanExpectedSumExists(List<Integer> list, int k) {
        for (int number : list) {
            if (number > k) {
                return true;
            }
        }
        return false;
    }

    /**
     *This method generates a random list without duplicate
     */
    private static Set<Integer> generateRandomListWithoutDuplicate(int size, int minValue, int maxValue) {

        if (maxValue - minValue + 1 < size) {
            throw new IllegalArgumentException("size is larger than the number of unique values in the range");
        }
        Random random = new Random();
        Set<Integer> uniqueNumbers = new LinkedHashSet<>();

        while (uniqueNumbers.size() < size) {
            int randomNumber = random.nextInt(maxValue - minValue + 1) + minValue;
            uniqueNumbers.add(randomNumber);
        }

        return uniqueNumbers;
    }

    /**
     *This method generates a random list of elements with duplicate
     */
    private static List<Integer> generateRandomListWithDuplicate(int size, int minValue, int maxValue) {
        Random random = new Random();

        return random.ints(size, minValue, maxValue + 1)
                .boxed()
                .collect(Collectors.toList());
    }


    /*
       COMPLEXITY DISCUSSION

      Given:
              U = List of positive integers
              n = number of elements in U
              k = number of subsets
              t = number of iteration
              m = average number of elements per subset

      The Grasp Complexity:  The initialization of the new unallocated list and the priority queue are both O(n) &
      O(k) respectively. The while loop that loops through the elements in the unallocated list which means it loops n
      times, inside the loop the top 3 elements is picked and one is randomly selected, this operation of selecting one
      occurs in constant time O(1). However the processing of updating the priority queue which involves reordering the
      queue has a complexity of O(log k), which makes the complexity of the GRASP to be O(n log k)



  3a) The optimisation version of the multi-way partitioning problem

      ## TIME COMPLEXITY
      There are 2 main operations, the Partitioning using GRASP, and Calculating the subset sum differences.
      The process of calculating the subset sum difference can be evaluated as :
      sum for one subset will be O(m), and for all subsets, it is O(mk). However, m can be expressed
      as n/k and thus this becomes O(n), comparing this complexity to that of the GRASP, it is obvious that the GRASP
      dominates. which makes the overall complexity O(t * n log k) and based on the implementation in this code,
      the worst case is O(10,000 * n log k) because t = 10,000.

      ## SPACE COMPLEXITY
      The list U which contains n elements requires O(n) space, additionally, a copy of the same list in the unallocated
      list also consumes O(n) space. Then the priority queue which manages the subsets requires O(k) space as it heavily
      dependent on the number of subsets. The space used by all other elements across the subset is O(n). All other
      variables can be ignored, which leads the space complexity to O(n) + O(n) + O(k). However since the space needed
      to store the elements of U is dominant, the complexity can be expressed as O(n).

  3b) Minimizing Subset Sum Differences with Standard Deviation Constraint

      ##TIME COMPLEXITY
      This is similar to 3a, but with an additional check for standard deviation. The GRASP complexity remains
      O(n log k), but the check for standard deviation adds an extra layer of complexity, as it involves calculating the
      standard deviation for each subset which basically can be expressed as O(mk) and can eventually be expressed as
      O(n), as m can be expressed as n/k. This layer makes the complexity O(n log k + n), but the GRASP complexity is
      still dominant and as such the complexity can be expressed as O(n log k) and based on the implementation in this
      code, the worst case is O(10,000 * n log k) because t = 10,000.

      ##SPACE COMPLEXITY
      The space complexity is very similar to 3a, as the inclusion of standard deviation calculations does not have any
      substantial effect to the space requirement. This makes the space complexity still O(n).

  3c) Minimizing Subset Sum Difference and maximising the number of relatively prime pair in each set

      ##TIME COMPLEXITY
      This is also similar to 3a, but with secondary objective of maximizing the number of relative prime pair in each
      set, This involves two major steps, the GRASP, and the process of counting relative prime pair for each subset.
      The complexity of GRASP remains the same. However, counting relative prime pairs for each subset can be expressed
      as O(m^2) because of the nested loop which checks each element in each subset. This can also be expressed as
      O((n/k)^2), which gives a quadratic and as such it cannot be simplified as O(n) and thus makes it somewhat complex,
      however the worst case complexity for this instance can definitely grow greater O(n log k) especially if the
      subsets have very large elements.

      ##SPACE COMPLEXITY
      The space complexity is also  similar to 3a, as the inclusion of relative prime pairs processing does not have any
      substantial effect to the space requirement as the process uses existing elements within subsets.
      This makes the space complexity still O(n).

     */






}

