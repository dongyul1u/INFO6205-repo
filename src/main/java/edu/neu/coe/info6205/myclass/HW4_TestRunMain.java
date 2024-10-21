package edu.neu.coe.info6205.myclass;

import scala.Int;
import scala.tools.nsc.doc.html.HtmlTags;

import java.util.Comparator;

public class HW4_TestRunMain {
    // just creat to run the domain function
    private static HW4_BasicBinaryHeap<Integer> bh = new HW4_BasicBinaryHeap<>(Comparator.comparingInt(a -> a));
    private static HW4_BinaryHeapFloyd<Integer> bhf = new HW4_BinaryHeapFloyd<>(Comparator.comparingInt(a->a));
    private static HW4_FourAryHeap<Integer> Fah = new HW4_FourAryHeap<>(Comparator.comparingInt(a->a));

    public static void main(String[] args) {

        int[] Removs = {10000,40000,160000,640000};//,8000,12000,16000
        int[] randomEles = {20000, 80000, 320000, 1280000};//, 20000, 24000, 28000
        for (int i = 0; i < Removs.length; i++) {
            System.out.printf("with %d elements and %d remove op %n",randomEles[i],Removs[i]);
            bh.doMain(randomEles[i], Removs[i]);
            bhf.doMain(randomEles[i], Removs[i]);
            Fah.domain_noFloyd(randomEles[i], Removs[i]);
            Fah.domain_Floyd(randomEles[i], Removs[i]);
            System.out.println('\n');
        }
    }

}
