package algorithmshw;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * @author Levi Muriuki
 */
public class SETSequential<Key>
{
    private class Node
    {
        Key key;
        Node next;

        public Node(Key key, Node next)
        {
            this.key = key;
            this.next = next;
        }
    }

    private Node head;
    private int n;

    public int size()
    {
        return n;
    }
    public boolean isEmpty()
    {
        return n == 0;
    }
    public void add(Key key)
    {
        if(key == null)
        {
            throw new IllegalArgumentException("Argument to contains() cannot be null");
        }

        for(Node node = head; node != null; node = node.next)
        {
            if(key.equals(node.key))
            {
                node.key = key;
                return;
            }
        }

        head = new Node(key, head);
        n++;

    }
    public boolean contains(Key key)
    {
        if(key == null)
        {
            throw new IllegalArgumentException("Argument to contains() cannot be null");
        }

        for(Node node = head; node != null; node = node.next)
        {
            if(key.equals(node.key))
                return true;
        }
        return false;
    }
    public void delete(Key key)
    {
        if(key == null)
        {
            throw new IllegalArgumentException("Argument to contains() cannot be null");
        }

        if(isEmpty())
            return;

        if(head.key.equals(key))
        {
            head = head.next;
            n--;
            return;
        }

        for(Node node = head; node != null; node = node.next)
        {
            if(node.next != null && node.next.key.equals(key))
            {
                node.next = node.next.next;
                n--;
                return;
            }
        }
    }

    public Iterable<Key> keys()
    {
        Queue<Key> keys = new Queue<>();

        for(Node node = head; node != null; node = node.next)
        {
            keys.enqueue(node.key);
        }

        return keys;
    }

    @Override
    public String toString()
    {
        if(isEmpty())
            return "{ }";

        StringBuilder stringBuilder = new StringBuilder("{");

        boolean isFirst = true;
        for(Key key : keys())
        {
            if(isFirst)
                isFirst = false;
            else
                stringBuilder.append(",");

            stringBuilder.append(key);
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static void main(String[] args)
    {
        SETSequential<Integer> setSequential = new SETSequential<>();

        setSequential.add(-4);
        setSequential.add(3);
        setSequential.add(55);
        setSequential.add(0);
        setSequential.add(1);
        setSequential.add(-14);
        setSequential.add(16);
        setSequential.add(2);
        setSequential.add(98);
        setSequential.add(5);

        StdOut.println("Keys: ");

        for(Integer key : setSequential.keys())
            StdOut.print(key + " ");

        StdOut.println("\ntoString: " + setSequential);

        StdOut.println("\nContains -14: " + setSequential.contains(-14));
        StdOut.println("\nContains 6: " + setSequential.contains(6));

        //Test delete()
        StdOut.println("\nDelete 1:");
        setSequential.delete(1);
        StdOut.println("\ntoString: " + setSequential);

        StdOut.println("\nDelete 98:");
        setSequential.delete(98);
        StdOut.println("\ntoString: " + setSequential);
    }
}

