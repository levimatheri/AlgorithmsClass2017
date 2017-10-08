/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.In;
import static edu.princeton.cs.algs4.Quick3string.sort;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

/**
 *
 * @author Levi
 */
public class CompoundWords {
    private static void findCompoundWords(String[] wordArray)
    {
        for(int i = 0; i < wordArray.length; i++)
        {
            for(int j = i + 1; j < wordArray.length; j++)
            {
                if(wordArray[j].startsWith(wordArray[i]))
                {
                    String suffix = wordArray[j].substring(wordArray[i].length(), wordArray[j].length());
                    for (String word : wordArray) {
                        if (word.equals(suffix)) {
                            StdOut.println(wordArray[j]);
                        }
                    }
                    break;               
                }
                else
                    break;
            }
        }
    }
    
    public static void main(String[] args)
    {
        In input = new In(args[0]);
        
        String[] words = input.readAllStrings();
        
        sort(words);
        
        StdOut.println(Arrays.toString(words));
        findCompoundWords(words);
    }
}
