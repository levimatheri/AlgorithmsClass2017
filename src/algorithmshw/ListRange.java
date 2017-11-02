package algorithmshw;


import edu.princeton.cs.algs4.StdRandom;

import static algorithmshw.SeparateChainingHashST.N;

/**
 * @author Levi Muriuki
 * 3.4.43
 */
public class ListRange {
    public static void main(String[] args)
    {
        int totalShortest = 0;
        int totalLongest = 0;
        for(int i = 0; i < 10; i++)
        {
            SeparateChainingHashST<Integer, Integer> table = new SeparateChainingHashST<>();

            for(int j = 0; j < N; j++)
            {
                table.put(StdRandom.uniform(Integer.MAX_VALUE), j);
            }

            int shortest = Integer.MAX_VALUE;
            int longest = Integer.MIN_VALUE;

            for(SeparateChainingHashST.SequentialSearchST list : table.st)
            {
                if(list != null)
                {
                    if(list.size() < shortest) {
                        shortest = list.size();
                        totalShortest += shortest;
                    }
                    if(list.size() > longest) {
                        longest = list.size();
                        totalLongest += longest;
                    }
                }
            }
        }


        System.out.println("Ave. Length of shortest list for " + N + " keys is: " + totalShortest/10);
        System.out.println("Ave. Length of longest list for " + N + " keys is: " + totalLongest/10);
    }
}
