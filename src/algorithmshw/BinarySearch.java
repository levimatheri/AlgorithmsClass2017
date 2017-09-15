/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.Counter;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

/**
 * Problem 1.2.9
 * @author Levi
 */
public class BinarySearch {
    public static int rank(int key, int[] a, Counter counter)
    {
        //Array must be sorted.
        int lo = 0;
        int hi = a.length - 1;
        while(lo <= hi)
        {
            //Key is in a[lo..hi] or not present
            int mid = lo + (hi - lo) / 2;
            counter.increment();
            if(key < a[mid]) hi = mid - 1;
            else if(key > a[mid]) lo = mid + 1;
            else return mid;
        }
        return -1;
    }
    
    public static void main(String[] args)
    {
        int[] whitelist = In.readInts(args[0]);
        
        Arrays.sort(whitelist);
        
        Counter c = new Counter("searches");
        
        while(!StdIn.isEmpty())
        {
            //Read key, print if not in whitelist
            int key = StdIn.readInt();
            if(rank(key, whitelist, c) < 0)
                StdOut.println(key);
        }
        
        StdOut.println(c);
    }
}