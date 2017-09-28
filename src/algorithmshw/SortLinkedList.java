/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmshw;

import edu.princeton.cs.algs4.StdOut;


/**
 * Problem 2.2.17
 * @author Levi
 */

class Node<T>
{
    T value;
    Node<T> next;

    Node(T x)
    {
        value = x;
    } 

    public int count(Node<T> head)
    {
        int count = 0;

        Node<T> curr = head;

        while(curr != null)
        {
            count++;
            curr = curr.next;
        }
        return count;
    }
    
    public Node mergeLists(Node<T> i, Node<T> j)
    {
        Node returnNode = null;
        if(i == null)
            return j;
        if(j == null)
            return i;
        
        if((Integer)i.value <= (Integer)j.value)
        {
            returnNode = i;
            returnNode.next = mergeLists(i.next, j);
        }
        else
        {
            returnNode = j;
            returnNode.next = mergeLists(i, j.next);
        }
        return returnNode;
    }
    
    public Node mergeSort(Node<T> head)
    {    
        if(head == null || head.next == null)
            return head;
        
        //find midpoint
        Node mid = getMiddle(head);
        Node midNext = mid.next;
        
        //separate the lists
        mid.next = null;
        
        //sort lists recursively
        Node first = mergeSort(head);
        
        Node second = mergeSort(midNext);
        
        //merge the two lists
        Node finalList = mergeLists(first, second);
        
        return finalList;      
    } 
    
    public Node getMiddle(Node<T> h)
    {
        if(h == null)
            return h;
        Node fast = h.next;
        Node slow = h;
        
        while(fast != null)
        {
            fast = fast.next;
            if(fast != null)
            {
                slow = slow.next;
                fast = fast.next;
            }
        }
        return slow;
    }
    
    public void printList(Node<T> head)
    {
        Node<T> curr = head;
        
        while(curr != null)
        {
            StdOut.print(curr.value + " ");
            curr = curr.next;
        }
    }
}

public class SortLinkedList<T> {
    public static void main(String[] args)
    {
        Node first = new Node(6);
        Node second = new Node(3);
        Node third = new Node(5);
        Node fourth = new Node(12);
        Node fifth = new Node(17);
        Node sixth = new Node(13);
        Node seventh = new Node(21);
        Node eighth = new Node(9);
        
        first.next = second;
        second.next = third;
        third.next = fourth;
        fourth.next = fifth;
        fifth.next = sixth;
        sixth.next = seventh;
        seventh.next = eighth;
        eighth.next = null;
        
        first.printList(first.mergeSort(first)); 
    }
}


