/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.StdDraw;

/**
 * Problem 1.2.10
 * @author Levi
 */
public class VisualCounter {
    private final int noOfOperations, maxCounterValue;
    private static int operationCount;
    private int count;
    public VisualCounter(int N, int max)
    {
        noOfOperations = N;
        maxCounterValue = max;
        
        StdDraw.setXscale(0, N);
        StdDraw.setYscale(0, max);
        StdDraw.setPenRadius(.01);
        operationCount = 0;
    }
    
    public void incrementCounter()
    {
        if(operationCount > noOfOperations){System.out.println("constant");}
        else
        {
            if(Math.abs(count) <= maxCounterValue)
            {
                count++;      
                operationCount++;
            }
            plotPoint(count);
        }
        System.out.println("incrementing");
        //System.out.println("Counter tally: " + c.tally());
    }
    
    public void decrementCounter()
    {
        if(operationCount > noOfOperations){System.out.println("constant");}
        else
        {
            if(Math.abs(count) <= maxCounterValue)
            {
                count--;
                operationCount++;
                plotPoint(count);
            }
        }
        System.out.println("decrementing");
        //System.out.println("Counter tally: " + tally);
    }
    
    public void plotPoint(int c_value)
    {
        System.out.println("c value: " + c_value);
        StdDraw.setPenColor(StdDraw.DARK_GRAY);
        StdDraw.point(operationCount, c_value);    
    }   
}
