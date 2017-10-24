package algorithmshw;

import edu.princeton.cs.algs4.BinarySearchST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.NoSuchElementException;

/**
 *
 * @author Levi
 */
public class BSInterpolation<Key extends Comparable<Key>, Value> {
    private static final int INIT_CAPACITY = 2;
    private Key[] keys;
    private Value[] vals;
    private int n = 0;

    public BSInterpolation() {
        this(INIT_CAPACITY);
    }

    
    public BSInterpolation(int capacity) { 
        keys = (Key[]) new Comparable[capacity]; 
        vals = (Value[]) new Object[capacity]; 
    }   

    // resize the underlying arrays
    private void resize(int capacity) {
        assert capacity >= n;
        Key[]   tempk = (Key[])   new Comparable[capacity];
        Value[] tempv = (Value[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            tempk[i] = keys[i];
            tempv[i] = vals[i];
        }
        vals = tempv;
        keys = tempk;
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }
    
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null"); 
        if (isEmpty()) return null;
        int i = rank(key); 
        if (i < n && keys[i].compareTo(key) == 0) return vals[i];
        return null;
    } 
    
    public int rank(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to rank() is null"); 

        int value = (Integer) key;
        
        int lo = 0, hi = n-1; 
        if(hi == -1)
        {
            System.out.println("Here");
            return lo;
        }
        int loVal = (Integer)keys[lo];
        int hiVal = (Integer)keys[hi];
        
        while (hiVal != loVal && (value >= loVal) && (value <= hiVal)) {
            System.out.println("Keys[hi] " + keys[hi]);
            System.out.println("Keys[lo] " + keys[lo]);
//            System.out.println(Math.abs((Integer)keys[hi] - (Integer)keys[lo]));
            int way = lo + ((value - loVal) * (hiVal - loVal) / (hiVal - loVal));
           
            System.out.println("way: " + way);
            //int mid = lo + (hi - lo) / 2; 
            int cmp = key.compareTo(keys[way]);
            if      (cmp < 0) hi = way - 1; 
            else if (cmp > 0) lo = way + 1; 
            else return way; 
//            int mid = lo + (hi - lo) / 2; 
//            int cmp = key.compareTo(keys[mid]);
//            if      (cmp < 0) hi = mid - 1; 
//            else if (cmp > 0) lo = mid + 1; 
//            else return mid; 
        } 
        return lo;
    } 

    public void put(Key key, Value val)  {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null"); 

        if (val == null) {
            delete(key);
            return;
        }

        int i = rank(key);

        // key is already in table
        if (i < n && keys[i].compareTo(key) == 0) {
            vals[i] = val;
            return;
        }

        // insert new key-value pair
        if (n == keys.length) resize(2*keys.length);

        for (int j = n; j > i; j--)  {
            keys[j] = keys[j-1];
            vals[j] = vals[j-1];
        }
        keys[i] = key;
        vals[i] = val;
        n++;

        assert check();
    } 
    
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null"); 
        if (isEmpty()) return;

        // compute rank
        int i = rank(key);

        // key not in table
        if (i == n || keys[i].compareTo(key) != 0) {
            return;
        }

        for (int j = i; j < n-1; j++)  {
            keys[j] = keys[j+1];
            vals[j] = vals[j+1];
        }

        n--;
        keys[n] = null;  // to avoid loitering
        vals[n] = null;

        // resize if 1/4 full
        if (n > 0 && n == keys.length/4) resize(keys.length/2);

        assert check();
    } 
    
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow error");
        delete(min());
    }

    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow error");
        delete(max());
    }

    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("called min() with empty symbol table");
        return keys[0]; 
    }
    
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("called max() with empty symbol table");
        return keys[n-1];
    }

    public Key select(int k) {
        if (k < 0 || k >= size()) {
            throw new IllegalArgumentException("called select() with invalid argument: " + k);
        }
        return keys[k];
    }

    public Key floor(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to floor() is null"); 
        int i = rank(key);
        if (i < n && key.compareTo(keys[i]) == 0) return keys[i];
        if (i == 0) return null;
        else return keys[i-1];
    }

    public Key ceiling(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to ceiling() is null"); 
        int i = rank(key);
        if (i == n) return null; 
        else return keys[i];
    }

    public int size(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to size() is null"); 
        if (hi == null) throw new IllegalArgumentException("second argument to size() is null"); 

        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else              return rank(hi) - rank(lo);
    }

    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null"); 
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null"); 

        Queue<Key> queue = new Queue<Key>(); 
        if (lo.compareTo(hi) > 0) return queue;
        for (int i = rank(lo); i < rank(hi); i++) 
            queue.enqueue(keys[i]);
        if (contains(hi)) queue.enqueue(keys[rank(hi)]);
        return queue; 
    }

    private boolean check() {
        return isSorted() && rankCheck();
    }

    // are the items in the array in ascending order?
    private boolean isSorted() {
        for (int i = 1; i < size(); i++)
            if (keys[i].compareTo(keys[i-1]) < 0) return false;
        return true;
    }

    // check that rank(select(i)) = i
    private boolean rankCheck() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (int i = 0; i < size(); i++)
            if (keys[i].compareTo(select(rank(keys[i]))) != 0) return false;
        return true;
    }


    /**
     * Unit tests the {@code BinarySearchST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {         
        BSInterpolation<Integer, Integer> st = new BSInterpolation<>();
        
        In text = new In(args[0]);
        
        for (int i = 0; !text.isEmpty(); i++) {
            int key = text.readInt();
            st.put(key, i);
        }
//            for (Integer s : st.keys())
//                StdOut.println(s + " " + st.get(s));

        Stopwatch timer = new Stopwatch();
        for(int c = 0; c < 10; c++)
        {          
            System.out.println(st.get(5));
        }
        System.out.println("Binary search ST time: " + (timer.elapsedTime() / 10) * 1000000);
    }
}
