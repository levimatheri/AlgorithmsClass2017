package algorithmshw;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * @author Levi Muriuki
 */
public class PrimitiveTypeHash {
    class HashSTint<Value>
    {
        private static final int INIT_CAPACITY = 4;

        private int n;           // number of key-value pairs in the symbol table
        private int m;           // size of linear probing table
        private int[] keys;      // the keys
        private Value[] vals;    // the values


        private final static int EMPTY_KEY = Integer.MIN_VALUE;

        public HashSTint() {
            this(INIT_CAPACITY);
        }

        private HashSTint(int capacity) {
            m = capacity;
            n = 0;
            keys = new int[m];
            vals = (Value[]) new Object[m];
        }

        public int size() {
            return n;
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public boolean contains(int key) {
            if (key == EMPTY_KEY) throw new IllegalArgumentException("argument to contains() is null");
            return get(key) != null;
        }

        // hash function for keys - returns value between 0 and M-1
        private int hash(int key) {
            return (Integer.valueOf(key).hashCode() & 0x7fffffff) % m;
        }

        // resizes the hash table to the given capacity by re-hashing all of the keys
        private void resize(int capacity) {
            HashSTint<Value> temp = new HashSTint<Value>(capacity);
            for (int i = 0; i < m; i++) {
                if (keys[i] != EMPTY_KEY) {
                    temp.put(keys[i], vals[i]);
                }
            }
            keys = temp.keys;
            vals = temp.vals;
            m    = temp.m;
        }

        public void put(int key, Value val) {
            if (key == EMPTY_KEY) throw new IllegalArgumentException("first argument to put() is null");

            if (val == null) {
                delete(key);
                return;
            }

            // double table size if 50% full
            if (n >= m/2) resize(2*m);

            int i;
            for (i = hash(key); keys[i] != EMPTY_KEY; i = (i + 1) % m) {
                if (keys[i] == key) {
                    vals[i] = val;
                    return;
                }
            }
            keys[i] = key;
            vals[i] = val;
            n++;
        }

        public Value get(int key) {
            if (key == EMPTY_KEY) throw new IllegalArgumentException("argument to get() is null");
            for (int i = hash(key); keys[i] != EMPTY_KEY; i = (i + 1) % m)
                if (keys[i] == key)
                    return vals[i];
            return null;
        }

        public void delete(int key) {
            if (key == EMPTY_KEY) throw new IllegalArgumentException("argument to delete() is null");
            if (!contains(key)) return;

            // find position i of key
            int i = hash(key);
            while (!(key == keys[i])) {
                i = (i + 1) % m;
            }

            // delete key and associated value
            keys[i] = EMPTY_KEY;
            vals[i] = null;

            // rehash all keys in same cluster
            i = (i + 1) % m;
            while (keys[i] != EMPTY_KEY) {
                // delete keys[i] an vals[i] and reinsert
                int   keyToRehash = keys[i];
                Value valToRehash = vals[i];
                keys[i] = EMPTY_KEY;
                vals[i] = null;
                n--;
                put(keyToRehash, valToRehash);
                i = (i + 1) % m;
            }

            n--;

            // halves size of array if it's 12.5% full or less
            if (n > 0 && n <= m/8) resize(m/2);

            assert check();
        }

        public int[] keys() {
            Queue<Integer> queue = new Queue<>();
            for (int i = 0; i < m; i++)
                if (keys[i] != EMPTY_KEY) queue.enqueue(keys[i]);

            int[] keys = new int[queue.size()];
            for(int i = 0; i < keys.length; i++)
                keys[i] = queue.dequeue();

            Arrays.sort(keys);
            return keys;
        }

        // integrity check - don't check after each put() because
        // integrity not maintained during a delete()
        private boolean check() {

            // check that hash table is at most 50% full
            if (m < 2*n) {
                System.err.println("Hash table size m = " + m + "; array size n = " + n);
                return false;
            }

            // check that each key in table can be found by get()
            for (int i = 0; i < m; i++) {
                if (keys[i] == EMPTY_KEY) {
                }
                else if (get(keys[i]) != vals[i]) {
                    System.err.println("get[" + keys[i] + "] = " + get(keys[i]) + "; vals[i] = " + vals[i]);
                    return false;
                }
            }
            return true;
        }
    }

    class HashSTdouble<Value>
    {
        private static final int INIT_CAPACITY = 4;

        private int n;           // number of key-value pairs in the symbol table
        private int m;           // size of linear probing table
        private double[] keys_double;      // the keys
        private Value[] vals;    // the values


        private final static double EMPTY_KEY_DOUBLE = Double.MIN_VALUE;

        public HashSTdouble() {
            this(INIT_CAPACITY);
        }

        private HashSTdouble(int capacity) {
            m = capacity;
            n = 0;
            keys_double = new double[m];
            vals = (Value[]) new Object[m];
        }

        public int size() {
            return n;
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public boolean contains(double key) {
            if (key == EMPTY_KEY_DOUBLE) throw new IllegalArgumentException("argument to contains() is null");
            return get(key) != null;
        }

        // hash function for keys - returns value between 0 and M-1
        private int hash(double key) {
            return (Double.valueOf(key).hashCode() & 0x7fffffff) % m;
        }

        // resizes the hash table to the given capacity by re-hashing all of the keys
        private void resize(int capacity) {
            HashSTdouble<Value> temp = new HashSTdouble<Value>(capacity);
            for (int i = 0; i < m; i++) {
                if (keys_double[i] != EMPTY_KEY_DOUBLE) {
                    temp.put(keys_double[i], vals[i]);
                }
            }
            keys_double = temp.keys_double;
            vals = temp.vals;
            m    = temp.m;
        }

        public void put(double key, Value val) {
            if (key == EMPTY_KEY_DOUBLE) throw new IllegalArgumentException("first argument to put() is null");

            if (val == null) {
                delete(key);
                return;
            }

            // double table size if 50% full
            if (n >= m/2) resize(2*m);

            int i;
            for (i = hash(key); keys_double[i] != EMPTY_KEY_DOUBLE; i = (i + 1) % m) {
                if (keys_double[i] == key) {
                    vals[i] = val;
                    return;
                }
            }
            keys_double[i] = key;
            vals[i] = val;
            n++;
        }

        public Value get(double key) {
            if (key == EMPTY_KEY_DOUBLE) throw new IllegalArgumentException("argument to get() is null");
            for (int i = hash(key); keys_double[i] != EMPTY_KEY_DOUBLE; i = (i + 1) % m)
                if (keys_double[i] == key)
                    return vals[i];
            return null;
        }

        public void delete(double key) {
            if (key == EMPTY_KEY_DOUBLE) throw new IllegalArgumentException("argument to delete() is null");
            if (!contains(key)) return;

            // find position i of key
            int i = hash(key);
            while (!(key == keys_double[i])) {
                i = (i + 1) % m;
            }

            // delete key and associated value
            keys_double[i] = EMPTY_KEY_DOUBLE;
            vals[i] = null;

            // rehash all keys in same cluster
            i = (i + 1) % m;
            while (keys_double[i] != EMPTY_KEY_DOUBLE) {
                // delete keys[i] an vals[i] and reinsert
                double   keyToRehash = keys_double[i];
                Value valToRehash = vals[i];
                keys_double[i] = EMPTY_KEY_DOUBLE;
                vals[i] = null;
                n--;
                put(keyToRehash, valToRehash);
                i = (i + 1) % m;
            }

            n--;

            // halves size of array if it's 12.5% full or less
            if (n > 0 && n <= m/8) resize(m/2);

            assert check();
        }

        public double[] keys() {
            Queue<Double> queue = new Queue<>();
            for (int i = 0; i < m; i++)
                if (keys_double[i] != EMPTY_KEY_DOUBLE) queue.enqueue(keys_double[i]);

            double[] keys = new double[queue.size()];
            for(int i = 0; i < keys.length; i++)
                keys[i] = queue.dequeue();

            Arrays.sort(keys);
            return keys;
        }

        // integrity check - don't check after each put() because
        // integrity not maintained during a delete()
        private boolean check() {

            // check that hash table is at most 50% full
            if (m < 2*n) {
                System.err.println("Hash table size m = " + m + "; array size n = " + n);
                return false;
            }

            // check that each key in table can be found by get()
            for (int i = 0; i < m; i++) {
                if (keys_double[i] == EMPTY_KEY_DOUBLE) {
                }
                else if (get(keys_double[i]) != vals[i]) {
                    System.err.println("get[" + keys_double[i] + "] = " + get(keys_double[i]) + "; vals[i] = " + vals[i]);
                    return false;
                }
            }
            return true;
        }
    }

    private void testHashSTint()
    {
        StdOut.println("Testing HashSTint: ");
        HashSTint<Integer> hashSTint = new HashSTint<>(5);

        hashSTint.put(5, 5);
        hashSTint.put(2, 2);
        hashSTint.put(0, 0);
        hashSTint.put(-5, -5);

        StdOut.println("Keys: ");

        for(Integer key : hashSTint.keys())
            StdOut.print(key + " ");

        StdOut.println("\ntoString: " + hashSTint);

        StdOut.println("\nContains -5: " + hashSTint.contains(-5));
        StdOut.println("\nContains 2: " + hashSTint.contains(2));

        //Test delete()
        StdOut.println("\nDelete 0:");
        hashSTint.delete(0);
        StdOut.println("\ntoString: " + hashSTint);

        StdOut.println("\nDelete 5:");
        hashSTint.delete(5);
        StdOut.println("\ntoString: " + hashSTint);
    }

    private void testHashSTdouble()
    {
        StdOut.println("Testing HashSTdouble: ");
        HashSTdouble<Double> hashSTdouble = new HashSTdouble<Double>(5);

        hashSTdouble.put(5.0, 5.0);
        hashSTdouble.put(2.1, 2.1);
        hashSTdouble.put(0.0, 0.0);
        hashSTdouble.put(96.9, 96.9);

        StdOut.println("Keys: ");

        for(Double key : hashSTdouble.keys())
            StdOut.print(key + " ");

        StdOut.println("\ntoString: " + hashSTdouble);

        StdOut.println("\nContains 5.0: " + hashSTdouble.contains(5.0));
        StdOut.println("\nContains 2.0: " + hashSTdouble.contains(2.0));

        //Test delete()
        StdOut.println("\nDelete 96.9:");
        hashSTdouble.delete(96.9);
        StdOut.println("\ntoString: " + hashSTdouble);

        StdOut.println("\nDelete 5.0:");
        hashSTdouble.delete(5.0);
        StdOut.println("\ntoString: " + hashSTdouble);
    }

    public static void main(String[] args)
    {
        PrimitiveTypeHash p = new PrimitiveTypeHash();
        p.testHashSTint();
        p.testHashSTdouble();
    }
}
