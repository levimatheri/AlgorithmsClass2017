package algorithmshw;

import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Graph;

/**
 * @author Levi Muriuki
 */
public class GraphProperties {
    private boolean[] marked;    // marked[v] = is there an s-v path?
    private int count = 0;
    private int[] eccentricities;
    private int diameter;
    private int radius;
    private int center;


    public GraphProperties(Graph G)
    {
        marked = new boolean[G.V()];
        int source = StdRandom.uniform(G.V());
        dfs(G, source);
        if(count != G.V())
            throw new RuntimeException("Graph must be connected");

        eccentricities = new int[G.V()];

        getProperties(G);
    }

    private void getProperties(Graph graph)
    {
        diameter = 0;
        radius = Integer.MAX_VALUE;
        center = 0;

        for(int vertex = 0; vertex < graph.V(); vertex++)
        {
            BreadthFirstPaths bfp = new BreadthFirstPaths(graph, vertex);

            for(int other = 0; other < graph.V(); other++)
            {
                if(other == vertex)
                    continue;

                eccentricities[vertex] = Math.max(eccentricities[vertex], bfp.distTo(other));
            }

            if(eccentricities[vertex] < radius)
            {
                radius = eccentricities[vertex];
                center = vertex;
            }

            if(eccentricities[vertex] > diameter)
                diameter = eccentricities[vertex];
        }
    }

    private int eccentricity(int v)
    {
        return eccentricities[v];
    }

    private int diameter()
    {
        return diameter;
    }

    private int radius()
    {
        return radius;
    }

    private int center()
    {
        return center;
    }

    // depth first search from v
    private void dfs(Graph G, int v) {
        count++;
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    public static void main(String[] args)
    {
        Graph g = new Graph(9);

        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        g.addEdge(5, 6);
        g.addEdge(6, 7);
        g.addEdge(7, 8);

        GraphProperties gp = new GraphProperties(g);
        System.out.println("Eccentricity: " + gp.eccentricity(3) + " exp: 5");
        System.out.println("Diameter: " + gp.diameter() + " exp: 8");
        System.out.println("Radius: " + gp.radius() + " exp: 4");
        System.out.println("Center: " + gp.center() + " exp: 4");
    }
}
