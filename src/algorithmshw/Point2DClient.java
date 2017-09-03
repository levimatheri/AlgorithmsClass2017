/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

/**
 * Problem 1.2.1
 * @author Levi
 */
public class Point2DClient {
    private static final Point2D CENTER = new Point2D(0, 0);
    
    private static Point2D calculatePoint(Point2D center)
    {
        double dx = Math.random();
        double dy = Math.random();
        
        Point2D point = new Point2D(center.x() + dx, center.x() + dy);
        System.out.println("point " + point);
        return point;
    }
    
    private static Point2D[] getPoints(int n)
    {
        Point2D[] pts = new Point2D[n];
        Point2D pt;
        for(int i = 0; i < n; i++)
        {
            pt = calculatePoint(CENTER);
            pts[i] = new Point2D(pt.x(), pt.y());
        }   
        return pts;
    }
    
    public static double distanceOfClosest(Point2D[] points)
    {
        double minValue = Double.MAX_VALUE;
        
        for(int a = 0; a < points.length; a++)
        {
            if((a+1) > points.length - 1)
                break;
            double dist = points[a].distanceTo(points[a+1]);
            if(dist < minValue)
                minValue = dist;
        }
        return minValue;
    }
    
    public static void main(String[] args)
    {
        StdDraw.setPenColor(StdDraw.GRAY);
//        StdDraw.setXscale(-1, 1);
//        StdDraw.setYscale(-1, 1);
        StdDraw.setPenRadius(0.01);
        int length = Integer.parseInt(args[0]);
        
        Point2D[] myPoints = getPoints(length);
        
        for (Point2D myPoint : myPoints) {
            // System.out.println("myPoints: " + myPoints[j]);
            myPoint.draw();
        } 
        System.out.println("Min dist: " + distanceOfClosest(myPoints));
    }
}
