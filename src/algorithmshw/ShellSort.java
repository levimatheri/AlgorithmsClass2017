/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import static edu.princeton.cs.algs4.Shell.sort;
import java.util.Arrays;

/**
 *
 * @author Levi
 */
public class ShellSort {
    private static Integer[] getArray()
    {
        Integer[] a = new Integer[100];
        
        int counter = 100;
        
        for(int i = 0; i < 100; i++)
        {
            a[i] = counter;
            counter--;
        }
        return a;
    }
    public static void main(String[] args)
    {
        Integer[] array = getArray();
        
        System.out.println("Before sorting: " + Arrays.toString(array));
        sort((Comparable[]) array);
        
        System.out.println("Sorted: " + Arrays.toString(array));
    }
}
