/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;
import java.util.Arrays;

/**
 *
 * @author Levi
 */
public class SimplePolygon {
    private static void sortPoints(Point2D[] p)
    {
        Arrays.sort(p, Point2D.Y_ORDER);
    }
    
    private static void drawPts(Point2D[] p)
    {
        for(Point2D point: p)
        {
            StdDraw.setPenColor(Color.RED);
            StdDraw.setPenRadius(0.02);            
            point.draw();
        }
    }
    
    private static void connectPts(Point2D[] p, Point2D min)
    {
        Arrays.sort(p, new Point2D(min.x(), min.y()).polarOrder());
        
        for(int a = 0; a < p.length; a++)
        {
            int b = a + 1;
            if(b > p.length - 1)
                break;
            StdDraw.setPenColor(Color.GRAY);
            StdDraw.setPenRadius(0.002);
            StdDraw.line(p[a].x(), p[a].y(), p[b].x(), p[b].y());
        }
        //Connect the end to the start
        StdDraw.line(p[p.length-1].x(), p[p.length-1].y(), p[0].x(), p[0].y());
    }
    public static void main(String[] args)
    {
        int N = Integer.parseInt(args[1]);
        
        
        Point2D[] points = new Point2D[N]; 
        
        In text = new In(args[0]);
        
        int i = 0;  
        while(text.hasNextLine())
        {
            String point = text.readLine();
            String[] coord = point.split(",");
            
            points[i] = new Point2D(Double.parseDouble(coord[0]), Double.parseDouble(coord[1]));
            i++;
        } 
        
        sortPoints(points);      
        StdDraw.setScale(-10, 10);
        drawPts(points);
        
        Point2D min = points[0];       
        connectPts(points, min);
    }
}
