/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

/**
 * Problem 1.4.17
 * @author Levi
 */
public class OneDFarthestPair {
    private static double[] findFarthestPair(double[] points)
    {
        double[] farthestPair = new double[2];
        
        double minVal = Double.MAX_VALUE;
        double maxVal = 0;
        
        for(int j = 0; j < points.length; j++)
        {
            if(points[j] < minVal)
                minVal = points[j];
            if(points[j] > maxVal)
                maxVal = points[j];            
        }
        
        farthestPair[0] = minVal;
        farthestPair[1] = maxVal;
        
        return farthestPair;
    }
    public static void main(String[] args)
    {
        double[] pts = new double[]{2.3, 3.6, 2.1, 7.8, 9.3, 2.31, -2.2, -2.19, 1.1, 8.8,
        7.86, 1.32, 50.21, 33.4, -12.4, -33.399};
        
        StdOut.println("Farthest pts: " + Arrays.toString(findFarthestPair(pts)));
    }
}
