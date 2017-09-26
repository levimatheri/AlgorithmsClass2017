/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

//import static edu.princeton.cs.algs4.Insertion.sort;
//import static edu.princeton.cs.algs4.Selection.sort;
import static edu.princeton.cs.algs4.Shell.sort;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 *
 * @author Levi
 */
public class SortDoublingTest {
    public static double timeTrial(int length) {
        
        Integer[] randArr = new Integer[length];
        for(int i = 0; i < length; i++)
        {
            randArr[i] = StdRandom.uniform(0, 1000000);
        }
              
        Stopwatch timer = new Stopwatch();
        //sort(randArr); //insertion sort
        //sort(randArr); //selection sort
        sort(randArr);
        
        return timer.elapsedTime() * 1000;
    }
    
    public static void main(String[] args)
    {
        double prev = 500;
        double prevN = 1000;
       // double prev = timeTrial(125);
        for(int N = 1000; true; N += N)
        {
            double time = timeTrial(N);
            StdOut.printf("%6d %7.1f ", N, time);
            StdOut.printf("%5.1f ", time/prev);
            StdOut.printf("%5.1f\n", N/prevN);
            prev = time;
            prevN = N;
        }
    }
}
