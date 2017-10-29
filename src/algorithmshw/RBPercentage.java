package algorithmshw;


import edu.princeton.cs.algs4.StdRandom;

/**
 * @author Levi Muriuki
 */
public class RBPercentage {
    public static void main(String[] args)
    {
        int N = 10000;
        int noOfExpermts = 100;
        int counter = 0;

        double totalPercentage = 0.0;
        while(counter <= noOfExpermts)
        {
            double totalRed = 0.0;
            RedBlackBST<Integer, Integer> tree = new RedBlackBST<>();
            for(int i = 0; i < N; i++)
                tree.put(StdRandom.uniform(Integer.MAX_VALUE), i);
            totalRed = countRed(tree);
            counter++;
            /*System.out.println("n: " + N);
            System.out.println("total red: " + totalRed);*/

            totalPercentage += (totalRed/N);
            //System.out.println(totalRed/N);
        }


        double percentageRed = (totalPercentage / noOfExpermts) * 100;
        System.out.println(percentageRed + "%");

    }

    private static int countRed(RedBlackBST<Integer, Integer> tree)
    {
        if(tree.isEmpty())
            return 0;

        return countRed(tree.root);
    }

    private static int countRed(RedBlackBST.Node node)
    {
        if(node == null)
            return 0;
        int count = node.color == RedBlackBST.RED ? 1 : 0;

        return count + countRed(node.left) + countRed(node.right);
    }

}
