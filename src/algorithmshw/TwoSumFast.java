/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import java.util.Arrays;
import edu.princeton.cs.algs4.BinarySearch;

/**
 *
 * @author Levi
 */
public class TwoSumFast {
    public static int count(int[] a)
    {
        Arrays.sort(a);
        int N = a.length;
        int cnt = 0; 
        for(int i = 0; i < N; i++)
        {
            if(BinarySearch.rank(-a[i], a) > i)
                cnt++;
        }
        return cnt;
    }
}
