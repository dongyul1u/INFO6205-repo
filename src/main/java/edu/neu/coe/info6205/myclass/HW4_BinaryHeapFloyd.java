package edu.neu.coe.info6205.myclass;

import edu.neu.coe.info6205.pq.PQException;
import edu.neu.coe.info6205.pq.PriorityQueue;
import edu.neu.coe.info6205.util.Benchmark_Timer;

import java.util.Comparator;
import java.util.Random;
import java.util.function.Supplier;

public class HW4_BinaryHeapFloyd <K> {
    private final PriorityQueue<K> pq;

    public HW4_BinaryHeapFloyd(Comparator<K> comp){
        // for main function test
        this.pq = new PriorityQueue<>(0, true, comp, true);
    }

    public HW4_BinaryHeapFloyd( Object[] array, int last, Comparator<K> comparator, boolean floyd) {
        this.pq = new PriorityQueue<>(true, array, 1, last, comparator, floyd);
    }

    public HW4_BinaryHeapFloyd(int capacity, Comparator<K> comparator) {
        // capacity, max heap, increase comparator, floyd false
        this.pq = new PriorityQueue<>(capacity, true, comparator, true);
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

    public void doMain() {
        Random random = new Random();
        int insertions = 16000;
        int removals = 4000;
        int maxSize = 4095;

        // this array is used as binHeap in priority queue. So size must greater than max Size
        Supplier<Integer[]> supplierInit = () -> random.ints(maxSize+1, 0, 1000000).boxed().toArray(Integer[]::new);
        Supplier<Integer[]> supplierOP = () -> random.ints(insertions, 0, 1000000).boxed().toArray(Integer[]::new);

        Benchmark_Timer<Integer[]> benchmarkInit = new Benchmark_Timer<>(
                "Basic Binary Heap Benchmark initialization time",
                (arr) -> {
                    Integer[] heapArray = new Integer[maxSize + 10];
                    System.arraycopy(arr, 0, heapArray, 1, maxSize);
                    HW4_BasicBinaryHeap<Integer> bh = new HW4_BasicBinaryHeap<>(heapArray, maxSize, Comparator.comparingInt(a -> a), true);
                    System.out.println("The highest priority: " + bh.peek());
                }

        );

        Benchmark_Timer<Integer[]> benchmarkOP = new Benchmark_Timer<>(
                "Binary Heap with Floyd's trick Benchmark",
                (arr) -> {
                    // use HW4_BasicBinaryHeap
                    HW4_BinaryHeapFloyd<Integer> binaryHeap = new HW4_BinaryHeapFloyd<>(maxSize, Comparator.comparingInt(a -> a));

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
                    System.out.println("Spilled element with highest priority: " + highestPrioritySpilled);
                }
        );

        // run benchmark
        double averageTime = benchmarkInit.runFromSupplier(supplierInit, 10);
        System.out.println("Average time for Binary Heap Floyd's trick Init with 4095 elements: " + averageTime + " ms\n");

        averageTime = benchmarkOP.runFromSupplier(supplierOP, 10); // run 10 times
        System.out.println("Average time for Binary Heap with Floyd's trick: " + averageTime + " ms\n" +
                "-------------------------------------");
    }
}
