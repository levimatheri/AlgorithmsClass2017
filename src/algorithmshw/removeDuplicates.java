/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.In;
import static edu.princeton.cs.algs4.Quick3way.sort;
import java.util.Arrays;

/**
 *
 * @author Levi
 */
public class removeDuplicates {
    private static String[] dedup(String[] a)
    {
        sort(a);
        System.out.println(Arrays.toString(a));
        for(int i = 0; i < a.length - 1; i++)
        {
            if(a[i].equals(""))
                continue;           
            int j = i + 1;
            
            while(j < a.length && (a[j].substring(0).equals(a[i].substring(0))))
            {       
                //System.out.println("a[j] " + a[j] + " a[i] " + a[i]);
                if(a[j].equals(a[i]))
                {           
                    a[j] = "";
                }
                j++;
            }
        }
        
        return a;
    }
    
    public static void main(String[] args)
    {
        In input = new In(args[0]);
        
        String[] words = input.readAllStrings();
        
        words = dedup(words);
        
        for(String word : words)
        {
            if(!word.equals(""))
                System.out.print(word + ", ");
        }
    }
}
