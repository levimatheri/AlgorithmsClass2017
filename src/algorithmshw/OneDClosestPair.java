/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

/**
 * 1.4.16
 * @author Levi
 */
public class OneDClosestPair {
    private static double[] findClosestPair(double[] points)
    {
        Arrays.sort(points);
        double minVal = Double.MAX_VALUE;
        
        double[] closestPair = new double[2];
        
        for(int a = 0; a < points.length - 1; a++)
        {
            double diff = points[a+1] - points[a];
            if(diff < minVal)
            {
                minVal = diff;
                closestPair[0] = points[a];
                closestPair[1] = points[a+1];
            }
        }
        return closestPair;
    }
    public static void main(String[] args)
    {
        double[] pts = new double[]{2.3, 3.6, 2.1, 7.8, 9.3, 2.31, -2.2, -2.19, 1.1, 8.8,
        7.86, 1.32, 33.21, 33.4, -12.4, -12.399};
        
        StdOut.println(Arrays.toString(findClosestPair(pts)));
    }
}
