package edu.neu.coe.info6205.threesum;

import edu.neu.coe.info6205.util.Stopwatch;

import java.util.function.Supplier;

public class TimeObserver {
    public static void main(String[] args){
        int[] Ns = {100, 300, 900, 2700, 8100};
        System.out.println("Three Sum Cubic");
        for (int n : Ns){
            Supplier<int[]> intsSupplier = new Source(n, n).intsSupplier(10);
            int[] ints = intsSupplier.get();

            Stopwatch time = new Stopwatch();
            ThreeSum target = new ThreeSumCubic(ints);
            Triple[] triplesQuadratic = target.getTriples();
            long time1_ = time.lap();
            System.out.println("N = " + n + " time = " + time1_);
        }

        System.out.println();
        System.out.println("Three Sum Quadrithmic");
        for (int n : Ns){
            Supplier<int[]> intsSupplier = new Source(n, n).intsSupplier(10);
            int[] ints = intsSupplier.get();

            Stopwatch time = new Stopwatch();
            ThreeSum target = new ThreeSumQuadrithmic(ints);
            Triple[] triplesQuadratic = target.getTriples();
            long time1_ = time.lap();
            System.out.println("N = " + n + " time = " + time1_);
        }

        System.out.println();
        System.out.println("Three Sum Quadratic");
        for (int n : Ns){
            Supplier<int[]> intsSupplier = new Source(n, n).intsSupplier(10);
            int[] ints = intsSupplier.get();

            Stopwatch time = new Stopwatch();
            ThreeSum target = new ThreeSumQuadratic(ints);
            Triple[] triplesQuadratic = target.getTriples();
            long time1_ = time.lap();
            System.out.println("N = " + n + " time = " + time1_);
        }

        System.out.println();
        System.out.println("Three Sum Quadratic With Calipers");
        for (int n : Ns){
            Supplier<int[]> intsSupplier = new Source(n, n).intsSupplier(10);
            int[] ints = intsSupplier.get();

            Stopwatch time = new Stopwatch();
            ThreeSum target = new ThreeSumQuadraticWithCalipers(ints);
            Triple[] triplesQuadratic = target.getTriples();
            long time1_ = time.lap();
            System.out.println("N = " + n + " time = " + time1_);
        }
    }
}
