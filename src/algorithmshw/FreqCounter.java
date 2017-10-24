/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import static algorithmshw.DoublingTest.timeTrial;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.SequentialSearchST;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

/**
 *
 * @author Levi
 */
public class FreqCounter {
    public static void main(String[] args) {
        int distinct = 0, words = 0;
        int N = Integer.parseInt(args[0]);
        In text = new In(args[1]);
        //ST<String, Integer> st = new ST<>();
        SequentialSearchST<String, Integer> st = new SequentialSearchST<>();

        double prev = 0.0;
        
        for(int i = N; true; i *= 2)
        {
            double totalTime = 0;
            Stopwatch timer = new Stopwatch();
            for(int j = 0; j < 10; j++)
            {          
                int cutoff = 0;
                // compute frequency counts
                while (!text.isEmpty() && cutoff < i) {
                    String key = text.readString();
                    cutoff++;
        //            if (key.length() < minlen) continue;
        //            words++;

                    if (st.contains(key)) {
                        st.put(key, st.get(key) + 1);
                    }
                    else {
                        st.put(key, 1);
                        distinct++;
                    }
                }

                // find a key with the highest frequency count
                String max = "";
                st.put(max, 0);
                for (String word : st.keys()) {
                    if (st.get(word) > st.get(max))
                        max = word;
                }
                //StdOut.println(max + " " + st.get(max));
                totalTime += timer.elapsedTime();
            }
            double aveTime = totalTime / 10;
            StdOut.printf("%6d %7.1f ", i, aveTime);
            StdOut.printf("%5.1f\n", aveTime/prev);
            prev = aveTime;
        }
                
       // StdOut.println("distinct = " + distinct);
        //StdOut.println("words    = " + words);
    }
}
