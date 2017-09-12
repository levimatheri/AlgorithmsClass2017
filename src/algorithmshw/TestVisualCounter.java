/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.Counter;
import edu.princeton.cs.algs4.StdRandom;

/**
 *
 * @author Levi
 */
public class TestVisualCounter {
    private static void chooseOperation(VisualCounter vc, int methodNumber)
    {
        if(methodNumber == 1)
        {
            vc.incrementCounter();
        }
        else if(methodNumber == 2)
        {
            vc.decrementCounter();
        }
    }
    public static void main(String[] args)
    {
        Counter c = new Counter("items");
        
        int operations = 1000;
        VisualCounter v = new VisualCounter(operations, 100);
        int p = 0;
        
        while(p < operations)
        {
            int mthd = StdRandom.uniform(1, 3);
            chooseOperation(v, mthd);
            p++;
        }
    }
}
