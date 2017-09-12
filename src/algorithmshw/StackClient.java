/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

/**
 * Problem 1.3.12
 * @author Levi
 */
public class StackClient {
    private static Stack<String> copy(Stack<String> inputStack)
    {
        Stack<String> stackCopy = new Stack<>();
        Iterator<String> i = inputStack.iterator();
        
        while(i.hasNext())
        {
            stackCopy.push(i.next());
        }
        return stackCopy;
    }
}
