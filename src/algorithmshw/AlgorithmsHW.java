/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

/**
 *
 * @author Levi
 */
public class AlgorithmsHW {

    /**
     * @param args the command line arguments
     */
    public static Point2D[] getPoints(int n, Point2D center, Double rad)
    {
        Point2D[] pts = new Point2D[n];
        
        double angle = 360/n ;
        Point2D pt;
        
        for(int a = 0; a < n; a++)
        {
            pt = calPoint(rad, angle * a, center);
            pts[a] = new Point2D(pt.x(), pt.y());
        }
        
        return pts;
    }
    
    public static Point2D calPoint(double rad, double angle, Point2D center)
    {
        double a = (angle * Math.PI)/180;
        
        double dist_x = rad * Math.cos(a);
        double dist_y = rad * Math.sin(a);
        
        Point2D p = new Point2D(center.x() + dist_x, center.y() + dist_y);
        
        return p;
    }
            
    public static void random(Point2D[] points, double prob)
    {
        for(int i = 0; i < points.length; i++)
        {
            //draw
            StdDraw.setPenRadius(0.05);
            points[i].draw();
            
            StdDraw.setPenRadius();
            //next point
            int j = i + 1;
            while(true)
            {
                if(j > points.length - 1) {
                    break;
                }
                if(StdRandom.bernoulli(prob))
                    StdDraw.line(points[i].x(), points[i].y(), points[j].x(), points[j].y());   
                j += 1;
            }      
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        System.out.println(args[0]);
        System.out.println(args[1]);
        //set color
        StdDraw.setPenColor(StdDraw.GRAY);
        
        
        
        int N = Integer.parseInt(args[0]);
        
        Point2D center = new Point2D(0.5, 0.5);
        
       double rad = 0.5;
        
        Point2D[] pts = getPoints(N, center, rad);
        double probability = Double.parseDouble(args[1]);
        random(pts, probability);
       
    }
    
}
