/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import java.awt.Color;
import java.util.Iterator;

/**
 *
 * @author Levi
 */
public class SimplePolygon {
    private static MinPQ<Point2D> pq;
    
    private static void sort(Point2D[] p)
    {
        pq = new MinPQ<>(Point2D.Y_ORDER);
        for(Point2D pt : p)
        {
            pq.insert(pt);
        }
//        
//        Iterator<Point2D> it = pq.iterator();
//        while(it.hasNext())
//        {
//            System.out.println(it.next());
//        }
    }
    private static Point2D getMinPoint(Point2D[] p)
    {
        return pq.delMin();
    }
    
    private static void drawPts(MinPQ<Point2D> myQueue)
    {
        Iterator<Point2D> it = myQueue.iterator();
        
        while(it.hasNext())
        {   
            StdDraw.setPenColor(Color.RED);
            StdDraw.setPenRadius(0.02);            
            it.next().draw();
        }
    }
    
    private static void connectPts(Point2D[] p, Point2D min)
    {
        StdDraw.setPenColor(Color.GRAY);
        StdDraw.setPenRadius(0.002);
        
        MinPQ<Point2D> mpq;
        mpq = new MinPQ<>(new Point2D(min.x(), min.y()).polarOrder());
        
        for(Point2D pt : p)
        {
            if(pt.equals(min))
                continue;
            mpq.insert(pt);
        }
      
        Point2D prev = mpq.delMin();
        
        StdDraw.line(min.x(), min.y(), prev.x(), prev.y());
        
        Point2D curr;
        while(!mpq.isEmpty())
        {
            curr = mpq.delMin();
            StdDraw.line(prev.x(), prev.y(), curr.x(), curr.y());
            prev = curr;
        }
        //Connect the end to the start
        StdDraw.line(prev.x(), prev.y(), min.x(), min.y());
    }
    public static void main(String[] args)
    {
        int N = Integer.parseInt(args[0]);
        
        
        Point2D[] points = new Point2D[N]; 
        
        for(int i = 0; i < N; i++)
            points[i] = new Point2D(StdRandom.uniform(-20.0, 20.0), StdRandom.uniform(-20.0, 20.0));
              
        sort(points);
        StdDraw.setScale(-30, 30);
        drawPts(pq);
        
        Point2D min = getMinPoint(points);   
        connectPts(points, min);
    }
}
