package algorithmshw;

/*
    * Problem 3.2.14
    * @author Levi Muriuki
*/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class BSTNoRecursion<Key extends Comparable<Key>, Value>
{
    private Node root;             // root of BST

    private class Node {
        private Key key;           // sorted by key
        private Value val;         // associated data
        private Node left, right;  // left and right subtrees
        private int size;          // number of nodes in subtree

        public Node(Key key, Value val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    private Key min()
    {
        Node curr = root;

        while(curr != null)
        {
            if(curr.left == null)
                return curr.key;
            else
                curr = curr.left;
        }
        return null;
    }

    private Key max()
    {
        Node curr = root;

        while(curr != null)
        {
            if(curr.right == null)
                return curr.key;
            else
                curr = curr.right;
        }
        return null;
    }

    private Key floor(Key key)
    {
        Node curr = root;
        Key ceilingFloor = null;

        while(curr != null)
        {
            int cmp = key.compareTo(curr.key);

            if(cmp < 0)
            {
                curr = curr.left;
            }
            else if(cmp > 0)
            {
                ceilingFloor = curr.key;
                curr = curr.right;
            }
            else
            {
                ceilingFloor = curr.key;
                break;
            }
        }
        return ceilingFloor;
    }

    private Key ceiling(Key key)
    {
        Node curr = root;
        Key ceilingCurr = null;

        while(curr != null)
        {
            int cmp = key.compareTo(curr.key);

            if(cmp < 0)
            {
                ceilingCurr = curr.key;
                curr = curr.left;
            }
            else if(cmp > 0)
            {
                curr = curr.right;
            }
            else
            {
                ceilingCurr = curr.key;
                break;
            }
        }
        return ceilingCurr;
    }

    private int rank(Key key)
    {
        Node curr = root;

        int rank = 0;

        while(curr != null)
        {
            int cmp = key.compareTo(curr.key);

            if(cmp < 0)
                curr = curr.left;
            else if(cmp > 0)
            {
                rank += size(curr.left) + 1;//+1 for the curr node;
                curr = curr.left;
            }
            else
            {
                rank += size(curr.left);
                return rank; //we've reached the end!
            }
        }
        return rank;
    }

    private Key select(int k)
    {
        //System.out.println("Here");
        Node curr = root;

        while(curr != null)
        {
            int leftSize = size(curr.left);

            if(leftSize == k)
              return curr.key;
            else if(leftSize > k)
                curr = curr.left;
            else
            {
                k = k - leftSize - 1;
               // System.out.println("k is " + k);
                curr = curr.right;
                System.out.println(curr.key);
            }
        }
        return null;
    }
    private void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("calledput() with a null key");
        if (val == null) {
            delete(key);
            return;
        }
        root = put(root, key, val);
        assert check();
    }
    private void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("called delete() with a null key");
        root = delete(root, key);
        assert check();
    }
    private boolean check() {
        if (!isBST())            StdOut.println("Not in symmetric order");
        if (!isSizeConsistent()) StdOut.println("Subtree counts not consistent");
        if (!isRankConsistent()) StdOut.println("Ranks not consistent");
        return isBST() && isSizeConsistent() && isRankConsistent();
    }

    private Iterable<Key> keys() {
        return keys(min(), max());
    }

    private Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");

        Queue<Key> queue = new Queue<Key>();
        keys(root, queue, lo, hi);
        return queue;
    }

    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
        if (x == null) return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key);
        if (cmphi > 0) keys(x.right, queue, lo, hi);
    }
    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
        private boolean isBST() {
            return isBST(root, null, null);
        }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private boolean isBST(Node x, Key min, Key max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
    }

    // are the size fields correct?
    private boolean isSizeConsistent() { return isSizeConsistent(root); }
    private boolean isSizeConsistent(Node x) {
        if (x == null) return true;
        if (x.size != size(x.left) + size(x.right) + 1) return false;
        return isSizeConsistent(x.left) && isSizeConsistent(x.right);
    }

    // check that ranks are consistent
    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (Key key : keys())
            if (key.compareTo(select(rank(key))) != 0) return false;
        return true;
    }
    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = put(x.left,  key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else              x.val   = val;
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }
    private int size() {
        return size(root);
    }

    // return number of key-value pairs in BST rooted at x
    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }
    private Node delete(Node x, Key key) {
        if (x == null) return null;

        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = delete(x.left,  key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else {
            if (x.right == null) return x.left;
            if (x.left  == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }
    private Node min(Node x) {
        if (x.left == null) return x;
        else                return min(x.left);
    }
    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    public static void main(String[] args)
    {
        BSTNoRecursion<String, Integer> st = new BSTNoRecursion<>();
        In text = new In(args[0]);
        for (int i = 0; !text.isEmpty(); i++) {
            String key = text.readString();
            st.put(key, i);
        }

        System.out.println("Min: " + st.min());
        System.out.println("Max: " + st.max());
        System.out.println("Ceiling: " + st.ceiling("Q"));
        System.out.println("Floor: " + st.floor("Q"));
        System.out.println("Rank: " + st.rank("J"));
        System.out.println("Select: " + st.select(2));


    }

}
