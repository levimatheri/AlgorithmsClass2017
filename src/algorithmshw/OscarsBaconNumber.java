package algorithmshw;

import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.SymbolGraph;
import edu.princeton.cs.algs4.Graph;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Levi Muriuki
 */
public class OscarsBaconNumber {
    private static void getGetKevinBaconNumber(String filePath, String delimiter)
    {
        SymbolGraph sg = new SymbolGraph(filePath, delimiter);

        String kbName = "Bacon, Kevin";
        //Get graph
        Graph g = sg.graph();
        int baconId = sg.indexOf(kbName);

        //KB numbers
        BreadthFirstPaths bfp = new BreadthFirstPaths(g, baconId);

        List<String> oscarNominees = new ArrayList<>();
        oscarNominees.add("Affleck, Casey");
        oscarNominees.add("Gosling, Ryan (I)");
        oscarNominees.add("Mortensen, Viggo");
        oscarNominees.add("Washington, Denzel");

        oscarNominees.add("Huppert, Isabelle");
        oscarNominees.add("Negga, Ruth");
        oscarNominees.add("Portman, Natalie");
        oscarNominees.add("Streep, Meryl");

        oscarNominees.add("Bridges, Jeff (I)");
        oscarNominees.add("Shannon, Michael (V)");

        oscarNominees.add("Davis, Viola (I)");
        oscarNominees.add("Harris, Naomie");
        oscarNominees.add("Kidman, Nicole");
        oscarNominees.add("Spencer, Octavia");
        oscarNominees.add("Williams, Michelle (I)");

        LinearProbingHashST<String, Integer> baconNumbers = getKBNoFromGraph(oscarNominees, sg, bfp);

        for(String nominee : baconNumbers.keys())
        {
            System.out.println(nominee + ": "  + baconNumbers.get(nominee));
        }

    }

    private static LinearProbingHashST<String, Integer> getKBNoFromGraph(List<String> oscarNominees,
                                                                         SymbolGraph movieSG,
                                                                         BreadthFirstPaths bfp)
    {
        LinearProbingHashST<String, Integer> st = new LinearProbingHashST<>();

        for(String nominee : oscarNominees)
        {
            int kbNo = Integer.MAX_VALUE;

            if(movieSG.contains(nominee))
            {
                int nomineeId = movieSG.indexOf(nominee);
                //Actor-Movie-Actor
                kbNo = bfp.distTo(nomineeId)/2;
            }
            st.put(nominee, kbNo);
        }
        return st;
    }
    public static void main(String[] args)
    {
        getGetKevinBaconNumber(args[0], args[1]);
    }
}
