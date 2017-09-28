/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.Queue;
import static edu.princeton.cs.algs4.Merge.sort;
import java.util.ArrayList;
import java.util.List;

/**
 * Problem 2.2.14
 * @author levi
 */
public class MergeSortedQueues {
    private static<T> Queue mergeQueues(Queue<T> a, Queue<T> b)
    {
        List<Comparable> list = new ArrayList<>();
        
        Queue<T> finalQueue = new Queue<>();
        
        for(T item : a) list.add((Comparable)item);
        for(T item : b) list.add((Comparable)item);
        
        Comparable[] arr = new Comparable[a.size() + b.size()];
        arr = list.toArray(arr);
        
        //Merge sort
        sort((Comparable[])arr);
        
        for(Object item : arr) finalQueue.enqueue((T)item);
        
        return finalQueue;
    }
    public static void main(String[] args)
    {
        Queue<Integer> queue1 = new Queue<>();
        Queue<Integer> queue2 = new Queue<>();
        
        queue1.enqueue(5);
        queue1.enqueue(6);
        queue1.enqueue(9);
        queue1.enqueue(12);
        queue1.enqueue(17);
        
        queue2.enqueue(3);
        queue2.enqueue(6);
        queue2.enqueue(13);
        queue2.enqueue(21);
        
        System.out.println("Final queue: " + mergeQueues(queue1, queue2));
    }
}
