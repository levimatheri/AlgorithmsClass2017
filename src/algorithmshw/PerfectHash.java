package algorithmshw;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Levi Muriuki
 * 3.4.4
 */
public class PerfectHash {
    public static void main(String[] args)
    {
        int[] result = returnPerfect();
        if(result != null)
        {
            System.out.println("a: " + result[0]);
            System.out.println("M: " + result[1]);
        }
    }
    static int[] returnPerfect()
    {
        int[] returnVals = new int[2]; //array containing a & m to return

        //S E A R C H X M P L
        int[] alphabetMap = new int[]{19, 5, 1, 18, 3, 8, 24, 13, 16, 12};

        for(int m = 2; m <= 20; m++)
        {
            for(int a = 1; a <= 10; a++)
            {
                Set<Integer> set = new HashSet<>();
                for(int k = 0; k < alphabetMap.length; k++)
                {
                    int hash = hashFunction(a, k, m);
                    if(!set.add(hash))
                        System.out.println("Cannot add: " + hash);
                    set.add(hash);
                }
                if(set.size() == 10)
                {
                    System.out.println(Arrays.toString(set.toArray()));
                    returnVals[0] = a;
                    returnVals[1] = m;
                    return returnVals;
                }
            }
        }
        return null;
    }
    static int hashFunction(int a, int k, int m)
    {
        return (a * k) % m;
    }
}
