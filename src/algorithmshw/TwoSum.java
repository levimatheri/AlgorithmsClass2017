/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

/**
 *
 * @author Levi
 */
public class TwoSum {
    public static int count(int[] a)
    {
        int N = a.length;
        int cnt = 0;
        
        for(int i = 0; i < N; i++)
        {
            for(int j = i+1; j < N; j++)
            {
                if(a[i] + a[j] == 0)
                    cnt++;
            }
        }
        return cnt;
    }
}
