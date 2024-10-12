package edu.neu.coe.info6205.union_find;

import java.util.Random;

public class UF_HWQUPCMain {


    public static int count(int n) {
        UF_HWQUPC uf = new UF_HWQUPC(n);
        Random random = new Random();
        int pairOP = 0;

        while(uf.components() != 1) {
            int i = random.nextInt(n);
            int j = random.nextInt(n);
            pairOP++;
            if(!uf.isConnected(i,j)) {
                uf.union(i,j);
            }
//            System.out.println(i + " " + j + " " + uf.components());
        }
        return pairOP;
    }

    public static void main(String[] args) {
        int[] ns = {100, 200, 400, 800, 1600};
        for (int n : ns) {
            int paris = count(n);
            System.out.println("Elements: " + n + " Pairs:" + paris);
        }
//        int n = 10;
//        int paris = count(n);
//        System.out.println("Elements: " + n + " Paris:" + paris);
    }
}
