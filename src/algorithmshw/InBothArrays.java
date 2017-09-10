/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Problem 1.4.12
 * @author Levi
 */
public class InBothArrays {
    private static void printMatching(int[] a, int[] b)
    {
        String result = "";
        
        HashSet<Integer> encountered = new HashSet<>();
       
        for(int i = 0; i < a.length; i++)
            encountered.add(a[i]);
       
        for(int j = 0; j < b.length; j++)
        {
            if(!encountered.add(b[j]))
            {
                result += "," + b[j];
            }
        }
        
        if(result.charAt(0) == ',')
            result = result.substring(1, result.length());
        
        StdOut.println(result);
    }
    public static void main(String[] args)
    {
        int[] arrA = new int[]{2, 3, 1, 5, 7, 8, 6, 11, 10, 4};
        int[] arrB = new int[]{15, 12, 9, 3, 4, 18, 13, 6, 19, 2};
       
        Arrays.sort(arrA);
        Arrays.sort(arrB);  
        
        printMatching(arrA, arrB);
    }
}
