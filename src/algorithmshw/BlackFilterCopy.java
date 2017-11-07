package algorithmshw;

import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.SeparateChainingHashST;

import java.util.HashSet;

/**
 * @author Levi Muriuki
 */
public class BlackFilterCopy
{
    // Do not instantiate.
    private BlackFilterCopy() { }


    private HashSet<String> filterByRedBlackBST(String dict, String textFile)
    {
        HashSet<String> filteredWords = new HashSet<>();
        RedBlackBST<String, Boolean> set = new RedBlackBST<>();

        // read in strings and add to set
        In in = new In(dict);
        In doc = new In(textFile);
        while (!in.isEmpty()) {
            String word = in.readString();
            set.put(word, true);
        }

        String[] text = doc.readAllStrings();

        // read in string from standard input, printing out all exceptions
        for (String word : text) {
            if (!set.contains(word)) {
               // System.out.println(word);
                filteredWords.add(word);
            }
        }
        return filteredWords;
    }

    private HashSet<String> filterByLinearProbing(String dict, String textFile)
    {
        HashSet<String> filteredWords = new HashSet<>();
        LinearProbingHashST<String, Boolean> set = new LinearProbingHashST<>();

        // read in strings and add to set
        In in = new In(dict);
        In doc = new In(textFile);
        while (!in.isEmpty()) {
            String word = in.readString();
            set.put(word, true);
        }

        String[] text = doc.readAllStrings();

        // read in string from standard input, printing out all exceptions
        for (String word : text) {
            if (!set.contains(word)) {
               // System.out.println(word);
                filteredWords.add(word);
            }
        }
        return filteredWords;
    }

    private HashSet<String> filterBySeparateChaining(String dict, String textFile)
    {
        HashSet<String> filteredWords = new HashSet<>();
        SeparateChainingHashST<String, Boolean> set = new SeparateChainingHashST<>();

        // read in strings and add to set
        In in = new In(dict);
        In doc = new In(textFile);
        while (!in.isEmpty()) {
            String word = in.readString();
            set.put(word, true);
        }

        String[] text = doc.readAllStrings();

        // read in string from standard input, printing out all exceptions
        for (String word : text) {
            if (!set.contains(word)) {
                //System.out.println(word);
                filteredWords.add(word);
            }
        }
        return filteredWords;
    }

    public static void main(String[] args)
    {
        String dictFilePath = args[0];
        String textFilePath = args[1];

        double rbstime = 0.0;
        for(int i = 0; i < 5; i++)
        {
            BlackFilterCopy bf = new BlackFilterCopy();
            Stopwatch timer = new Stopwatch();
            bf.filterByRedBlackBST(dictFilePath, textFilePath);
            rbstime += timer.elapsedTime();
        }

        double redBlackBSTtime = rbstime / 5;

        double lptime = 0.0;

        for(int i = 0; i < 5; i++)
        {
            BlackFilterCopy bf = new BlackFilterCopy();
            Stopwatch timer = new Stopwatch();
            bf.filterByLinearProbing(dictFilePath, textFilePath);
            lptime += timer.elapsedTime();
        }
        double linearProbingTime = lptime / 5;

        double sctime = 0.0;

        for(int i = 0; i < 5; i++)
        {
            BlackFilterCopy bf = new BlackFilterCopy();
            Stopwatch timer = new Stopwatch();
            bf.filterBySeparateChaining(dictFilePath, textFilePath);
            sctime += timer.elapsedTime();
        }
        double separateChainingTime = sctime / 5;

        StdOut.printf("%20s %20s %20s\n", "Time spent RBST | ", "Time spent Linear Probing | ",
                "Time spent Separate Chaining");

        print(redBlackBSTtime, linearProbingTime, separateChainingTime);
    }

    private static void print(double RBST, double LP, double SC)
    {
        System.out.printf("%24.2f %31.2f %28.2f\n", RBST, LP, SC);
    }
}
