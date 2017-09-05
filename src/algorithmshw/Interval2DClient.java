/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.Counter;
import edu.princeton.cs.algs4.Interval1D;
import edu.princeton.cs.algs4.Interval2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Problem 1.2.3
 * @author Levi
 */
public class Interval2DClient {
    //get random 2d-intervals
    
    static Interval1D[] oneDimX;
    static Interval1D[] oneDimY;
    static Interval2D[] getIntervals(int len, double min, double max)
    {
        Interval2D[] intvs = new Interval2D[len]; 
        
        oneDimX = new Interval1D[len];
        oneDimY = new Interval1D[len];
        
        for(int a = 0; a < len; a++)
        {
            double x_lo = StdRandom.uniform(min, max);
            double x_hi = StdRandom.uniform(min, max);
            
            //incase StdRandom chooses an x_lo bigger than x_hi
            if(x_lo > x_hi)
            {
                double temp = x_hi;
                x_hi = x_lo;
                x_lo = temp;      
            }
                        
            double y_lo = StdRandom.uniform(min, max);
            double y_hi = StdRandom.uniform(min, max);
            
            //incase StdRandom chooses an y_lo bigger than y_hi
            if(y_lo > y_hi)
            {
                double temp = y_hi;
                y_hi = y_lo;
                y_lo = temp;      
            }
            Interval1D x = new Interval1D(x_lo, x_hi);
            Interval1D y = new Interval1D(y_lo, y_hi);
            
            oneDimX[a] = x;
            oneDimY[a] = y;
            
            //add interval
            intvs[a] = new Interval2D(x, y);
        }
        return intvs;
    }
    
    //print intervals that intersect
    static void printIntersectContain(Interval2D[] intervals)
    {
        Counter intersects = new Counter("intervals intersects in one another");
        Counter contains = new Counter("intervals contained in one another");
        for(int i = 0; i <= intervals.length - 2; i++)
        {
            for(int j = i+1; j <= intervals.length - 1; j++)
            {
                //increment intersects whenever there's an intersection
                if(intervals[i].intersects(intervals[j]))
                {
                    intersects.increment();
                    StdOut.println(intervals[i] + " intersects with " 
                    + intervals[j]);
                }
                
                //increment contains
                if((oneDimX[i].min() <= oneDimX[j].min() && oneDimX[i].max() >= 
                        oneDimX[j].max()) && (oneDimY[i].min() <= oneDimY[j].min() 
                        && oneDimY[i].max() >= oneDimY[j].max()))
                    contains.increment();
            }
        }
        
        StdOut.println(intersects);
        StdOut.println(contains);
    }
    
    
    static void drawIntervals(Interval2D[] intvs)
    {
        for(Interval2D interval : intvs)
            interval.draw();
    }
    public static void main(String[] args)
    {
        int N = Integer.parseInt(args[0]);
        double min = Double.parseDouble(args[1]);
        double max = Double.parseDouble(args[2]);
        
        Interval2D[] myIntervals = getIntervals(N, min, max);
        
        drawIntervals(myIntervals);
        printIntersectContain(myIntervals);   
    }
}