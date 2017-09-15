/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.Counter;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

/**
 * Problem 1.4.8
 * @author Levi
 */
public class EqualPairs {
    private static String findEqualPairs(int[] list)
    {
        Counter c = new Counter("pairs");
        String result = "Equal pairs:\n";
        //sort the array
        Arrays.sort(list);
        
        for(int i = 0; i < list.length - 1; i++)
        {
            if(list[i] == list[i+1])
            {
                result += "value " + list[i] + '\n';
                c.increment();
            }
        }
        result += "NUMBER OF PAIRS: " + c;
        return result;
    }
    public static void main(String[] args)
    {
        In arr = new In(args[0]);
        
        int[] numbers = null;
        
        while(arr.hasNextLine()) numbers = arr.readAllInts();
        
        StdOut.println(findEqualPairs(numbers));
    }
}