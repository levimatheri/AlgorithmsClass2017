package algorithmsprograms;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;


public class Stooge {
    //private static final int MAXIMUM_INTEGER = 1000000;
    
    public static double timeTrial(int n)
    {
        int[] nums = new int[n];
        for(int i = 0; i < nums.length; i++)
        {
            nums[i] = StdRandom.uniform(-10000, 10000);
        }

        Stopwatch timer = new Stopwatch();
        stoogeSort(nums, 0, nums.length - 1);
        return timer.elapsedTime();
    }

    public static void stoogeSort(int[] L, int i, int j) {
        if (L[j] < L[i]) {
            int tmp = L[i];
            L[i] = L[j];
            L[j] = tmp;
        }
        if (j - i > 1) {
            int t = (j - i + 1) / 3;
            stoogeSort(L, i, j - t);
            stoogeSort(L, i + t, j);
            stoogeSort(L, i, j - t);
        }
    }
    
    public static void main(String[] args) {
        double prev = 0.0;
        for (int n = 125; true; n += n) {
            double time = 0.0;
            for(int i = 0; i < 15; i++)
            {
                time += timeTrial(n);
            }
            double aveTime = time/15;
            StdOut.printf("%6d %7.1f ", n, aveTime);
            StdOut.printf("%5.1f\n", aveTime / prev);
            prev = aveTime;
        }
    }
}
