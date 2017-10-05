/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 *
 * @author Levi
 */
public class PercentageConstruction {
    public static void main(String[] args)
    {
        MaxPQ<Integer> pq = new MaxPQ<>();
  
        double constTime = 0;
        double totalTime = 0;
        
        
        for(int a = 0; a < 1; a++)
        {
            Stopwatch total = new Stopwatch();          
            Stopwatch constHeapTime = new Stopwatch();
            
            for(int i = 0; i < 1000000000; i++)
                pq.insert(StdRandom.uniform(1000000000)); 
            
            constTime += constHeapTime.elapsedTime(); 
            
            while(!pq.isEmpty())
                pq.delMax();

            totalTime += total.elapsedTime();
            
        }
        
        double aveTotal = totalTime / 20;
        double aveConstr = constTime / 20;
        System.out.println("ave total time: " + aveTotal);

        System.out.println("Percentage: " + aveConstr / aveTotal);
        
        
        
        
        
        
    }
}
