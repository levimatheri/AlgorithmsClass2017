package algorithmshw;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * @author Levi Muriuki
 */
public class PrimitiveTypeSet
{
    class HashSETint
    {
        private static final int INIT_CAPACITY = 4;

        private int n;           // number of key-value pairs in the symbol table
        private int m;           // size of linear probing table
        private int[] keys;      // the keys


        private final static int EMPTY_KEY = Integer.MIN_VALUE;

        public HashSETint() {
            this(INIT_CAPACITY);
        }

        private HashSETint(int capacity) {
            m = capacity;
            n = 0;
            keys = new int[m];
        }

        public int size() {
            return n;
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public boolean contains(int key) {
            if (key == EMPTY_KEY) throw new IllegalArgumentException("argument to contains() is null");
            for (int i = hash(key); keys[i] != EMPTY_KEY; i = (i + 1) % m) {
                if (keys[i] == key)
                    return true;
            }
            return false;
        }

        // hash function for keys - returns value between 0 and M-1
        private int hash(double key) {
            return (Double.valueOf(key).hashCode() & 0x7fffffff) % m;
        }

        // resizes the hash table to the given capacity by re-hashing all of the keys
        private void resize(int capacity) {
            HashSETint temp = new HashSETint(capacity);
            for (int i = 0; i < m; i++) {
                if (keys[i] != EMPTY_KEY) {
                    temp.add(keys[i]);
                }
            }
            keys = temp.keys;
            m    = temp.m;
        }

        public void add(int key) {
            if (key == EMPTY_KEY) throw new IllegalArgumentException("first argument to put() is null");

            // double table size if 50% full
            if (n >= m/2) resize(2*m);

            int i;
            for (i = hash(key); keys[i] != EMPTY_KEY; i = (i + 1) % m) {
                if (keys[i] == key) {
                    keys[i] = key;
                    return;
                }
            }
            keys[i] = key;
            n++;
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

            // rehash all keys in same cluster
            i = (i + 1) % m;
            while (keys[i] != EMPTY_KEY) {
                // delete keys[i] an vals[i] and reinsert
                int   keyToRehash = keys[i];
                keys[i] = EMPTY_KEY;
                n--;
                add(keyToRehash);
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
            }
            return true;
        }
    }

    class HashSETdouble
    {
        private static final int INIT_CAPACITY = 4;

        private int n;           // number of key-value pairs in the symbol table
        private int m;           // size of linear probing table
        private double[] keys_double;      // the keys


        private final static int EMPTY_KEY_DOUBLE = Integer.MIN_VALUE;

        public HashSETdouble() {
            this(INIT_CAPACITY);
        }

        private HashSETdouble(int capacity) {
            m = capacity;
            n = 0;
            keys_double = new double[m];
        }

        public int size() {
            return n;
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public boolean contains(double key) {
            if (key == EMPTY_KEY_DOUBLE) throw new IllegalArgumentException("argument to contains() is null");
            for (int i = hash(key); keys_double[i] != EMPTY_KEY_DOUBLE; i = (i + 1) % m) {
                if (keys_double[i] == key)
                    return true;
            }
            return false;
        }

        // hash function for keys - returns value between 0 and M-1
        private int hash(double key) {
            return (Double.valueOf(key).hashCode() & 0x7fffffff) % m;
        }

        // resizes the hash table to the given capacity by re-hashing all of the keys
        private void resize(int capacity) {
            HashSETdouble temp = new HashSETdouble(capacity);
            for (int i = 0; i < m; i++) {
                if (keys_double[i] != EMPTY_KEY_DOUBLE) {
                    temp.add(keys_double[i]);
                }
            }
            keys_double = temp.keys_double;
            m    = temp.m;
        }

        public void add(double key) {
            if (key == EMPTY_KEY_DOUBLE) throw new IllegalArgumentException("first argument to put() is null");

            // double table size if 50% full
            if (n >= m/2) resize(2*m);

            int i;
            for (i = hash(key); keys_double[i] != EMPTY_KEY_DOUBLE; i = (i + 1) % m) {
                if (keys_double[i] == key) {
                    keys_double[i] = key;
                    return;
                }
            }
            keys_double[i] = key;
            n++;
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

            // rehash all keys in same cluster
            i = (i + 1) % m;
            while (keys_double[i] != EMPTY_KEY_DOUBLE) {
                // delete keys[i] an vals[i] and reinsert
                double   keyToRehash = keys_double[i];
                keys_double[i] = EMPTY_KEY_DOUBLE;
                n--;
                add(keyToRehash);
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
                keys_double[i] = queue.dequeue();

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
            }
            return true;
        }
    }

    private void testHashSETint()
    {
        StdOut.println("Testing HashSETint: ");
        HashSETint hashSETint = new HashSETint(5);

        hashSETint.add(5);
        hashSETint.add(2);
        hashSETint.add(0);
        hashSETint.add(-5);

        StdOut.println("Keys: ");

        for(Integer key : hashSETint.keys())
            StdOut.print(key + " ");

        StdOut.println("\ntoString: " + hashSETint);

        StdOut.println("\nContains -5: " + hashSETint.contains(-5));
        StdOut.println("\nContains 2: " + hashSETint.contains(2));

        //Test delete()
        StdOut.println("\nDelete 0:");
        hashSETint.delete(0);
        StdOut.println("\ntoString: " + hashSETint);

        StdOut.println("\nDelete 5:");
        hashSETint.delete(5);
        StdOut.println("\ntoString: " + hashSETint);
    }

    private void testHashSETdouble()
    {
        StdOut.println("Testing HashSETdouble: ");
        HashSETdouble hashSETdouble = new HashSETdouble(5);

        hashSETdouble.add(5.0);
        hashSETdouble.add(2.1);
        hashSETdouble.add(0.0);
        hashSETdouble.add(96.9);

        StdOut.println("Keys: ");

        for(Double key : hashSETdouble.keys())
            StdOut.print(key + " ");

        StdOut.println("\ntoString: " + hashSETdouble);

        StdOut.println("\nContains 5.0: " + hashSETdouble.contains(5.0));
        StdOut.println("\nContains 2.0: " + hashSETdouble.contains(2.0));

        //Test delete()
        StdOut.println("\nDelete 96.9:");
        hashSETdouble.delete(96.9);
        StdOut.println("\ntoString: " + hashSETdouble);

        StdOut.println("\nDelete 5.0:");
        hashSETdouble.delete(5.0);
        StdOut.println("\ntoString: " + hashSETdouble);
    }

    public static void main(String[] args)
    {
        PrimitiveTypeSet p = new PrimitiveTypeSet();
        p.testHashSETint();
        p.testHashSETdouble();
    }
}
