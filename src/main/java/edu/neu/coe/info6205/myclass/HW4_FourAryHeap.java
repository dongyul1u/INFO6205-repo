package edu.neu.coe.info6205.myclass;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.neu.coe.info6205.pq.PQException;
import edu.neu.coe.info6205.util.Benchmark_Timer;

/**
 * 4-ary Heap Priority Queue Data Structure.
 * It is similar to the priority queue but uses 4 children per node.
 * It can serve as a minPQ or a maxPQ (define "max" as either false or true, respectively).
 * Supports Floyd's trick for batch heapify.
 *
 * @param <K> Generic type for elements in the Priority Queue
 */
public class HW4_FourAryHeap<K> implements Iterable<K> {

    // Instance variables
    private final boolean max;
    private final int first;
    private final Comparator<K> comparator;
    private final K[] heapArray;
    private int last;
    private final boolean floyd;

    /**
     * Constructor to initialize the 4-ary heap with given parameters.
     *
     * @param max        whether this is a Max Priority Queue (true) or Min PQ (false)
     * @param heapArray  the initial array to store elements
     * @param first      the index of the root element
     * @param last       the number of elements currently in the heap
     * @param comparator comparator for comparing elements
     * @param floyd      true to use Floyd's trick (batch heapify), false otherwise
     */
    public HW4_FourAryHeap(boolean max, Object[] heapArray, int first, int last, Comparator<K> comparator, boolean floyd) {
        this.max = max;
        this.first = first;
        this.comparator = comparator;
        this.last = last;
        //noinspection unchecked
        this.heapArray = (K[]) heapArray;
        this.floyd = floyd;

        // Initialize the heap structure based on whether Floyd's trick is used
        if (floyd) {
            for (int i = (last - 1) / 4; i >= first; i--) {
                doHeapify(i, (a, b) -> !unordered(a, b));  // Batch heapify using Floyd's trick
            }
        } else {
            for (int i = first; i <= last; i++) {
                swimUp(i);  // Heapify each element individually
            }
        }
    }

    /**
     * Constructor for setting max/min heap, capacity, and comparator
     *
     * @param n          the capacity of the heap
     * @param first      the index of the root element
     * @param max        whether this is a Max Priority Queue (true) or Min PQ (false)
     * @param comparator comparator for comparing elements
     * @param floyd      whether to use Floyd's trick
     */
    public HW4_FourAryHeap(int n, int first, boolean max, Comparator<K> comparator, boolean floyd) {
        this(max, new Object[n + first], first, 0, comparator, floyd);
    }

    /**
     * Constructor with capacity, defaulting to root at index 1.
     */
    public HW4_FourAryHeap(int n, boolean max, Comparator<K> comparator, boolean floyd) {
        this(n, 1, max, comparator, floyd);
    }

    /**
     * Constructor with capacity and no Floyd's trick.
     */
    public HW4_FourAryHeap(int n, boolean max, Comparator<K> comparator) {
        this(n, 1, max, comparator, false);
    }

    public HW4_FourAryHeap(int n, Comparator<K> comparator) {
        this(n, 1, true, comparator, true);
    }

    // for main running
    public <T> HW4_FourAryHeap(Comparator<K> tComparator) {
        this(1, 1, true, tComparator, true);
    }

    /**
     * @return true if the current heap size is zero.
     */
    public boolean isEmpty() {
        return last == 0;
    }

    /**
     * @return the number of elements stored in this 4-ary heap
     */
    public int size() {
        return last;
    }

    /**
     * Peek at the root element (highest priority).
     *
     * @return the root element
     */
    public K peek() {
        return heapArray[first];
    }

    /**
     * Insert an element into the 4-ary heap.
     *
     * @param key the value to insert
     */
    public void give(K key) {
        if (last == heapArray.length - first)
            last--; // If at capacity, discard the least eligible element
        heapArray[++last + first - 1] = key; // Insert the element at the end
        swimUp(last + first - 1); // Reorder the heap upwards
    }

    /**
     * Remove and return the root element from the 4-ary heap.
     *
     * @return the root element
     * @throws PQException if the heap is empty
     */
    public K take() throws PQException {
        if (isEmpty()) throw new PQException("Priority queue is empty");
        return floyd ? doTake(this::sink) : doTake(this::sink);
    }

    // Perform take operation and adjust heap
    K doTake(Consumer<Integer> f) {
        K result = heapArray[first]; // Get the root element
        swap(first, last-- + first - 1); // Swap root with the last element
        f.accept(first); // Reorder the heap
        heapArray[last + first] = null; // Prevent loitering
        return result;
    }

    /**
     * Sink the element down the 4-ary heap.
     */
    void sink(int k) {
        doHeapify(k, (a, b) -> !unordered(a, b));
    }

    // Perform heapify for a 4-ary heap
    private int doHeapify(int k, BiPredicate<Integer, Integer> p) {
        int i = k;
        while (firstChild(i) <= last + first - 1) {
            int j = firstChild(i);
            int maxChild = j;

            // Find the largest child among 4 possible children
            for (int l = 1; l < 4; l++) {
                if (j + l <= last + first - 1 && unordered(maxChild, j + l)) {
                    maxChild = j + l;
                }
            }
            if (p.test(i, maxChild)) break;
            swap(i, maxChild); // Swap the parent with the largest child
            i = maxChild;
        }
        return i;
    }

    // Swim the element up the 4-ary heap to restore heap order
    void swimUp(int k) {
        int i = k;
        while (i > first && unordered(parent(i), i)) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    // Swap two elements in the heap
    private void swap(int i, int j) {
        K tmp = heapArray[i];
        heapArray[i] = heapArray[j];
        heapArray[j] = tmp;
    }

    // Compare two elements in the heap to check their order
    boolean unordered(int i, int j) {
        return (comparator.compare(heapArray[i], heapArray[j]) > 0) ^ max;
    }

    // Get the index of the parent of the element at index k in a 4-ary heap
    private int parent(int k) {
        return (k - 1) / 4;
    }

    // Get the index of the first child of the element at index k in a 4-ary heap
    private int firstChild(int k) {
        return 4 * k + 1;
    }


    // Iterator for the 4-ary heap
    public Iterator<K> iterator() {
        Collection<K> copy = new ArrayList<>(Arrays.asList(Arrays.copyOf(heapArray, last + first)));
        Iterator<K> result = copy.iterator();
        if (first > 0) result.next(); // Skip null element
        return result;
    }

    public void domain_noFloyd(int insertions, int removals) {
        Random random = new Random();
//        int insertions = 16000;
//        int removals = 4000;
        int maxSize = 4095;

        Supplier<Integer[]> supplierInit = () -> random.ints(maxSize+1, 0, 1000000).boxed().toArray(Integer[]::new);
        Supplier<Integer[]> supplierOP = () -> random.ints(insertions, 0, 1000000).boxed().toArray(Integer[]::new);

        Benchmark_Timer<Integer[]> benchmarkInit = new Benchmark_Timer<>(
                "4-ary Heap Benchmark initialization time",
                (arr) -> {
//                    Integer[] heapArray = new Integer[maxSize + 1];
//                    System.arraycopy(arr, 0, heapArray, 1, maxSize);
//                    heapArray[0] = Integer.MIN_VALUE;   // make sure nothing is null
                    HW4_FourAryHeap<Integer> bh = new HW4_FourAryHeap<>(true, arr, 1, maxSize, Comparator.comparingInt(a -> a == null ? Integer.MIN_VALUE : a), false);
//                    System.out.println("The highest priority: " + bh.peek());
                }
        );

        Benchmark_Timer<Integer[]> benchmarkOP = new Benchmark_Timer<>(
                "4-ary Heap Benchmark",
                (arr) -> {
                    Integer[] heapArray = new Integer[removals+1];
                    heapArray[0] = Integer.MIN_VALUE ;
                    System.arraycopy(arr, 0, heapArray, 1, removals);
                    HW4_FourAryHeap<Integer> bh = new HW4_FourAryHeap<>(true, heapArray, 1, removals, Comparator.comparingInt(a -> a == null ? Integer.MIN_VALUE : a), false);

                    // insert 16,000 elements
                    for (int i = 0; i < insertions; i++) {
                        bh.give(arr[i]);
                    }

                    // delete 4000 elements
                    Integer highestPrioritySpilled = Integer.MIN_VALUE;
                    for (int i = 0; i < removals; i++) {
                        try {
                            Integer removed = bh.take();
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




        double averageTime = benchmarkInit.runFromSupplier(supplierInit, 10);
        System.out.printf("Average time for 4-ary Heap Init with %d elements: %.2f ms%n", maxSize, averageTime);
//        System.out.println();

        averageTime = benchmarkOP.runFromSupplier(supplierOP, 10); // run 10 times
        System.out.printf("Average time for 4-ary Heap: %.2f ms%n-------------------------------------%n", averageTime);
//        System.out.println();


    }

    public void domain_Floyd(int insertions, int removals) {
        Random random = new Random();
//        int insertions = 16000;
//        int removals = 4000;
        int maxSize = 4095;

        Supplier<Integer[]> supplierInit = () -> random.ints(maxSize+1, 0, 1000000).boxed().toArray(Integer[]::new);
        Supplier<Integer[]> supplierOP = () -> random.ints(insertions, 0, 1000000).boxed().toArray(Integer[]::new);


        Benchmark_Timer<Integer[]> benchmarkInitFloyd = new Benchmark_Timer<>(
                "4-ary Heap Floyd Benchmark initialization time",
                (arr) -> {
//                    Integer[] heapArray = new Integer[maxSize + 1];
//                    System.arraycopy(arr, 0, heapArray, 1, maxSize);
//                    heapArray[0] = Integer.MIN_VALUE;   // make sure nothing is null
                    HW4_FourAryHeap<Integer> bh = new HW4_FourAryHeap<>(true, arr, 1, maxSize, Comparator.comparingInt(a -> a == null ? Integer.MIN_VALUE : a), false);
//                    System.out.println("The highest priority: " + bh.peek());
                }
        );


        Benchmark_Timer<Integer[]> benchmarkOPFloyd = new Benchmark_Timer<>(
                "4-ary Heap Floyd Benchmark",
                (arr) -> {

                    Integer[] heapArray = new Integer[removals + 1];
                    System.arraycopy(arr, 0, heapArray, 1, removals);
                    HW4_FourAryHeap<Integer> bh = new HW4_FourAryHeap<>(true, arr, 1, removals, Comparator.comparingInt(a -> a == null ? Integer.MIN_VALUE : a), true);

                    // insert 16,000 elements
                    for (int i = 0; i < insertions; i++) {
                        bh.give(arr[i]);
                    }

                    // delete 4000 elements
                    Integer highestPrioritySpilled = Integer.MIN_VALUE;
                    for (int i = 0; i < removals; i++) {
                        try {
                            Integer removed = bh.take();
                            if (removed > highestPrioritySpilled) {
                                highestPrioritySpilled = removed;
                            }
                        } catch (PQException e) {
                            e.printStackTrace();
                        }
                    }

                    // output the highest priority, ignore to draw the plot
//                    System.out.println("Spilled element with highest priority: " + highestPrioritySpilled);
                }
        );

        double averageTime = benchmarkInitFloyd.runFromSupplier(supplierInit, 10);
        System.out.printf("Average time for 4-ary Heap Floyd's trick Init with %d elements: %.2f ms%n", maxSize, averageTime);

        averageTime = benchmarkOPFloyd.runFromSupplier(supplierOP, 10); // run 10 times
        System.out.printf("Average time for 4-ary Heap with Floyd's trick: %.2f ms%n-------------------------------------%n", averageTime);
//        System.out.println();


    }


}
