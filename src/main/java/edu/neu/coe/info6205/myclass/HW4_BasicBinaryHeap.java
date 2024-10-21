package edu.neu.coe.info6205.myclass;

import edu.neu.coe.info6205.pq.PQException;
import edu.neu.coe.info6205.pq.PriorityQueue;
import edu.neu.coe.info6205.util.Benchmark_Timer;

import java.util.Comparator;
import java.util.Random;
import java.util.function.Supplier;

public class HW4_BasicBinaryHeap <K>{
    private final PriorityQueue<K> pq;

    // init with a new unordered array
    public HW4_BasicBinaryHeap( Object[] array, int last, Comparator<K> comparator, boolean floyd) {
        this.pq = new PriorityQueue<>(true, array, 1, last, comparator, floyd);
    }

    public HW4_BasicBinaryHeap(int capacity, Object[] array,Comparator<K> comparator) {
        // capacity, max heap, increase comparator, floyd false
        this.pq = new PriorityQueue<>(true, array,1, 0, comparator, false);
    }

    public HW4_BasicBinaryHeap(Comparator<K> comp){
        // for main function test
        this.pq = new PriorityQueue<>(0, true, comp, false);
    }

    public void insert(K key) {
        pq.give(key);
    }

    public K remove() throws PQException {
        return pq.take();
    }

    public int size() {
        return pq.size();
    }

    public boolean isEmpty() {
        return pq.isEmpty();
    }

    public K peek() {
        return pq.peek();
    }

    public void doMain(int insertions, int removals) {
        Random random = new Random();
//        int insertions = 16000;
//        int removals = 4000;
        int maxSize = 4095;
                                                // this array is used as binHeap in priority queue. So size must greater than max Size
        Supplier<Integer[]> supplierInit = () -> random.ints(maxSize, 0, 1000000).boxed().toArray(Integer[]::new);
        Supplier<Integer[]> supplierOP = () -> random.ints(insertions, 0, 1000000).boxed().toArray(Integer[]::new);

        Benchmark_Timer<Integer[]> benchmarkInit = new Benchmark_Timer<>(
                "Basic Binary Heap Benchmark initialization time",
                (arr) -> {
                    Integer[] heapArray = new Integer[maxSize + 10];
                    System.arraycopy(arr, 0, heapArray, 1, maxSize);
                    HW4_BasicBinaryHeap<Integer> bh = new HW4_BasicBinaryHeap<>(heapArray, maxSize, Comparator.comparingInt(a -> a), false);
//                    System.out.println("The highest priority: " + bh.peek());
                }

        );

        Benchmark_Timer<Integer[]> benchmarkOP = new Benchmark_Timer<>(
                "Basic Binary Heap Benchmark",
                (arr) -> {

                    Integer[] heapArray = new Integer[removals + 1];

                    System.arraycopy(arr, 0, heapArray, 1, removals);
                    // use HW4_BasicBinaryHeap
                    HW4_BasicBinaryHeap<Integer> binaryHeap = new HW4_BasicBinaryHeap<>(removals, heapArray,Comparator.comparingInt(a -> (int) a));

                    // insert 16,000 elements
                    for (int i = 0; i < insertions; i++) {
                        binaryHeap.insert(arr[i]);
                    }

                    // delete 4000 elements
                    Integer highestPrioritySpilled = Integer.MIN_VALUE;
                    for (int i = 0; i < removals; i++) {
                        try {
                            Integer removed = binaryHeap.remove();
                            if (removed > highestPrioritySpilled) {
                                highestPrioritySpilled = removed;
                            }
                        } catch (PQException e) {
                            e.printStackTrace();
                        }
                    }

                    // output the highest priority
//                    System.out.println("Spilled element with highest priority: " + highestPrioritySpilled);
                }
        );

        // run benchmark
        double averageTime = benchmarkInit.runFromSupplier(supplierInit, 10); // run 10 times
        System.out.printf("Average time for Basic Binary Heap Init with %d elements: %.2f ms%n", maxSize, averageTime);

        averageTime = benchmarkOP.runFromSupplier(supplierOP, 10); // run 10 times
        System.out.printf("Average time for Basic Binary Heap: %.2f ms%n-------------------------------------%n", averageTime);
//        System.out.println();
    }
}
