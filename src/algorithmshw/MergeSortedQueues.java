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
 *
 * @author levi
 */
public class MergeSortedQueues {
    private static<T> Queue mergeQueues(Queue<T> a, Queue<T> b)
    {
        List<Comparable> list = new ArrayList<>();
        
        Queue<T> finalQueue = new Queue<>();
        
        for(T item : a)
        {
            list.add((Comparable)item);
        }
        for(T item : b)
        {
            list.add((Comparable)item);
        }
        
        Comparable arr[] = (Comparable[])list.toArray();
        //Merge sort
        sort(arr);
        
        for(Comparable item : arr)
        {
            finalQueue.enqueue((T)item);
        }
        return finalQueue;
    }
    public static void main(String[] args)
    {
        
    }
}
