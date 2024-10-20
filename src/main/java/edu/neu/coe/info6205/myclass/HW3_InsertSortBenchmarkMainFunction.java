package edu.neu.coe.info6205.myclass;

import edu.neu.coe.info6205.sort.elementary.InsertionSortBasic;
import edu.neu.coe.info6205.util.Benchmark_Timer;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class HW3_InsertSortBenchmarkMainFunction {

    public static void main(String[] args) {
        int[]  sizes = {500,1000,2000,4000,8000};
        Random random = new Random();

        InsertionSortBasic<Integer> insertSorter = InsertionSortBasic.create();

        System.out.println("For random array");
        for (int n : sizes) {
            // random array
            Supplier<Integer[]> randomArray = () -> generateRandomArray(n, random);

            benchMarkCall(insertSorter, n, randomArray);
        }
        System.out.println("-------------------------");
        System.out.println("For ordered array");
        for (int n : sizes) {
            // random array
            Supplier<Integer[]> randomArray = () -> generateOrderedArray(n);

            benchMarkCall(insertSorter, n, randomArray);
        }
        System.out.println("-------------------------");
        System.out.println("For partially ordered array");
        for (int n : sizes) {
            // random array
            Supplier<Integer[]> randomArray = () -> generatePartiallyOrderedArray(n);

            benchMarkCall(insertSorter, n, randomArray);
        }
        System.out.println("-------------------------");
        System.out.println("For reverse array");
        for (int n : sizes) {
            // random array
            Supplier<Integer[]> randomArray = () -> generateReverseOrderedArray(n);

            benchMarkCall(insertSorter, n, randomArray);
        }
        System.out.println("-------------------------");
    }

    private static void benchMarkCall(InsertionSortBasic<Integer> insertSorter, int n, Supplier<Integer[]> randomArray) {
        Consumer<Integer[]> sortFunction = insertSorter::sort;

        Benchmark_Timer<Integer[]> benchmark = new Benchmark_Timer<>(
                "Insertion Sort Benchmark",
                sortFunction
        );

        double averageTime = benchmark.runFromSupplier(randomArray, 10);
        System.out.printf("Average time taken for size %d: %.2f ms%n", n, averageTime);
        System.out.println();
    }


    // Generate a random array of given size
    private static Integer[] generateRandomArray(int size, Random random) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt();
        }
        return array;
    }

    // Generate an ordered array of given size
    private static Integer[] generateOrderedArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    // Generate a partially ordered array of given size
    private static Integer[] generatePartiallyOrderedArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        // Shuffle half of the array to make it partially ordered
        int shuffleCount = size / 4;
        Random random = new Random();
        for (int i = 0; i < shuffleCount; i++) {
            int index1 = random.nextInt(size);
            int index2 = random.nextInt(size);
            int temp = array[index1];
            array[index1] = array[index2];
            array[index2] = temp;
        }
        return array;
    }

    // Generate a reverse ordered array of given size
    private static Integer[] generateReverseOrderedArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i;
        }
        return array;
    }
}
