/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.Interval1D;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Problem 1.2.2
 * @author Levi
 */
public class Interval1DClient {
    public static void main(String[] args)
    {
        int noOfPairs = Integer.parseInt(args[0]);
        
        int count = 0;
        
        Interval1D[] intervals = new Interval1D[noOfPairs];
        
        StdOut.println("Please enter the double pairs");
         
        //get user input for pairs (size=noOfPairs)
        while(StdIn.hasNextLine() && count < noOfPairs)
        {
            String input = StdIn.readString();
            String[] pairs = input.split(",");
            Interval1D interval = new Interval1D(Double.parseDouble(pairs[0]), 
                    Double.parseDouble(pairs[1]));
            
            intervals[count] = interval;
            count++;
        }
        
        //loop through to find intersection of intervals 
        //O(n^2)?? Not sure because we are not going through 
        //the entire array for each element
        for(int a = 0; a <= noOfPairs - 2; a++)
        {
            for(int b = a+1; b <= noOfPairs - 1; b++)
            {
                if(intervals[a].intersects(intervals[b]))
                {
                    System.out.println(intervals[a] + " intersects with " + 
                            intervals[b]);
                }
            }
        }
        
    }
}