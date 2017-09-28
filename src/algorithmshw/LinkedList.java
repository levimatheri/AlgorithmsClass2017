///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package algorithmshw;
//
//import edu.princeton.cs.algs4.StdOut;
//import java.util.NoSuchElementException;
//
///**
// *
// * @author Levi
// */
//
////Node class
//class Node<T>
//{
//    T value;
//    Node<T> next;
//    
//    Node(T x)
//    {
//        value = x;
//    } 
//    
//    public int count(Node<T> head)
//    {
//        int count = 0;
//        
//        Node<T> curr = head;
//        
//        while(curr != null)
//        {
//            count++;
//            curr = curr.next;
//        }
//        return count;
//    }
//    
//    public void printList(Node<T> head)
//    {
//        Node<T> curr = head;
//        
//        while(curr != null)
//        {
//            StdOut.println(curr.value);
//            curr = curr.next;
//        }
//    }
//    
//    //Problem 1.3.20
//    public Node delete(Node<T> head, int k)
//    {
//        Node<T> curr = head;
//        
//        if(k >= count(head))
//            throw new NoSuchElementException();
//        if(k == 0)
//        {
//            curr = curr.next;
//            return curr;     
//        }
//        
//        Node<T> prev = curr;
//        int counter = 0;
//        
//        while(curr != null)
//        {
//            counter++;
//            if(counter == k)
//            {
//                curr = curr.next.next;
//                prev.next = curr;
//                return head;
//            }
//            else
//            {
//                curr = curr.next;
//                prev = curr;        
//            }
//        }
//        return head;      
//    }
//    
//    //Problem 1.3.21
//    public boolean find(Node<String> list, String key)
//    {
//        Node<String> curr = list;
//        
//        while(curr != null)
//        {
//            if(curr.value.equals(key))
//                return true;
//            curr = curr.next;
//        }
//        return false;
//    }
//    
//    //Problem 1.3.27
//    public int max(Node<Integer> list)
//    {
//        int maxVal = 0; //don't need to set to Integer.MIN_VALUE since we are only using positive ints
//        
//        Node<Integer> curr = list;
//        while(curr != null)
//        {
//            if(curr.value > maxVal)
//            {
//                maxVal = curr.value;
//            }
//            curr = curr.next;
//        }
//        return maxVal;
//    }
//    
//    //Problem 1.3.28
//    public int maxRecursive(Node<Integer> list)
//    {
//        if(list == null)
//            return 0;
//        int max = maxRecursive(list.next);
//        return list.value > max ? list.value : max;
//    }
//}
//
////Testing class
//public class LinkedList<T> {
//    public static void main(String[] args)
//    {
//        Node<Integer> n = new Node<>(10);
//        Node<Integer> n1 = new Node<>(6);
//        Node<Integer> n2 = new Node<>(7);
//        Node<Integer> n3 = new Node<>(9);
//        Node<Integer> n4 = new Node<>(8);
//               
//        n.next = n1;
//        n1.next = n2;
//        n2.next = n3;
//        n3.next = n4;
//        n4.next = null;
//        
//        StdOut.println("Max value is: " + n.maxRecursive(n));
////        
////        StdOut.println("Before deletion:");
////        n.printList(n);
////        
////        n = n.delete(n, 1);
////        StdOut.println("After deletion:");
////        n.printList(n); 
////        Node<String> n = new Node<>("Levi");
////        Node<String> n1 = new Node<>("Jack");
////        Node<String> n2 = new Node<>("Jim");
////        Node<String> n3 = new Node<>("Sharon");
////        Node<String> n4 = new Node<>("Tom");
////        Node<String> n5 = new Node<>("Dennis");
////        
////        n.next = n1;
////        n1.next = n2;
////        n2.next = n3;
////        n3.next = n4;
////        n4.next = n5;
////        n5.next = null;
////        
////        StdOut.println(n.find(n, "Tom"));
////        StdOut.println(n.find(n, "Rick"));
//    }
//}
