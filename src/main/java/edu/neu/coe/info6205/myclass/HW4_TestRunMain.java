package edu.neu.coe.info6205.myclass;

import java.util.Comparator;

public class HW4_TestRunMain {
    // just creat to run the domain function
    private static HW4_BasicBinaryHeap<Integer> bh = new HW4_BasicBinaryHeap<>(Comparator.comparingInt(a -> a));
    private static HW4_BinaryHeapFloyd<Integer> bhf = new HW4_BinaryHeapFloyd<>(Comparator.comparingInt(a->a));

    public static void main(String[] args) {

        bh.doMain();
        System.out.println('\n');
        bhf.doMain();
    }

}
