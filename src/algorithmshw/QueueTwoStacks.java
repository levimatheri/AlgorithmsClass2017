/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

/**
 * Problem 1.4.27
 * @author Levi
 */
public class QueueTwoStacks {
    private static <T> void operations(Queue<T> q)
    {
        StdOut.println("Queue " + q.toString());
        Stack<T> first = new Stack<>();
        Stack<T> second = new Stack<>();
        
        while(!q.isEmpty()) first.push(q.dequeue());
        while(!first.isEmpty()) second.push(first.pop());
        
        StdOut.println("Second stack " + second.toString());
    }
    
    public static void main(String[] args)
    {
        Queue<String> queue = new Queue<>();
        
        queue.enqueue("Josh");
        queue.enqueue("Jake");
        queue.enqueue("Peter");
        queue.enqueue("Alex");
        queue.enqueue("Levi");
        queue.enqueue("Jean");
        
        operations(queue);
    }
}
