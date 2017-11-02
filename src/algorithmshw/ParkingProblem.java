package algorithmshw;

import edu.princeton.cs.algs4.StdRandom;

/**
 * @author Levi Muriuki
 */
public class ParkingProblem {
    public static void main(String[] args)
    {
        int M = 1000;
        int totalComp = 0;
        for(int i = 0; i < 20; i++)
        {
            LinearProbingHashST<Integer, Integer> st = new LinearProbingHashST<>(M);
            for(int j = 0; j < M; j++)
            {
                st.put(StdRandom.uniform(Integer.MAX_VALUE), j);
            }
            totalComp += st.noOfCompares;
        }

        System.out.println("Ave. no of compares: " + totalComp/20);

    }
}
