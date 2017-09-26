/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import static edu.princeton.cs.algs4.Shell.sort;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.Arrays;

/**
 *
 * @author Levi
 */
public class GeometricShell {
    public static void sort(Comparable[] a) {
        int n = a.length;

        // 3x+1 increment sequence:  1, 4, 13, 40, 121, 364, 1093, ... 
        int h = 1;
        while (h < n/3) h = 5*h; 

        while (h >= 1) {
            // h-sort the array
            for (int i = h; i < n; i++) {
                for (int j = i; j >= h && less(a[j], a[j-h]); j -= h) {
                    exch(a, j, j-h);
                }
            }
            assert isHsorted(a, h); 
            h /= 3;
        }
        assert isSorted(a);
    }
    
     // is v < w ?
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }
        
    // exchange a[i] and a[j]
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }


   /***************************************************************************
    *  Check if array is sorted - useful for debugging.
    ***************************************************************************/
    private static boolean isSorted(Comparable[] a) {
        for (int i = 1; i < a.length; i++)
            if (less(a[i], a[i-1])) return false;
        return true;
    }

    // is the array h-sorted?
    private static boolean isHsorted(Comparable[] a, int h) {
        for (int i = h; i < a.length; i++)
            if (less(a[i], a[i-h])) return false;
        return true;
    }

    // print array to standard output
    private static void show(Comparable[] a) {
        for (Comparable a1 : a) {
            StdOut.println(a1);
        }
    }
    
    private static Integer[] createArr()
    {
        int length = 1000000;
        
        Integer[] arr = new Integer[1000000];
        
        for(int i = 0; i < length; i++)
        {            
            arr[i] = StdRandom.uniform(1, 999500);
        }
        
        return arr;
    }
    
    public static void main(String[] args)
    {
        Stopwatch timer = new Stopwatch();
        Integer[] array = createArr();
        
        //System.out.println("Before sorting: " + Arrays.toString(array));
        sort((Comparable[]) array);
        
        System.out.println("Elapsed time " + timer.elapsedTime() * 1000);
        
        //System.out.println("Sorted: " + Arrays.toString(array));
    }
}
