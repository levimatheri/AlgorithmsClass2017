package algorithmshw;


import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.Graph;

import java.util.HashSet;

/**
 * @author Levi Muriuki
 */
public class ReverseDeleteMST {
    private Queue<Edge> MST;

    private boolean[] visited;
    private int count;


    public ReverseDeleteMST(EdgeWeightedGraph G)
    {
        visited = new boolean[G.V()];
        count = 0;

        MST = new Queue<>();
        MaxPQ<Edge> pq = new MaxPQ<>();
        for (Edge e : G.edges()) {
            pq.insert(e);
        }

        while(!pq.isEmpty())
        {
            Edge edge = pq.delMax();
            //System.out.println("edge: " + edge.toString());
            G.deleteEdge(edge);
           // System.out.println("edges: " + G.E);

            int vertex = StdRandom.uniform(G.V);
            dfs(G,vertex);

            int countVisited = 0;
            for(boolean v : visited)
            {
                //System.out.println(v);
                if(v)
                    countVisited++;
            }
            //System.out.println("countVistied " + countVisited + ", Vertices: " + G.V);
            if(countVisited < G.V) {
                System.out.println("disconnects!");
                G.addEdge(edge);
            }
            else
                MST.enqueue(edge);
        }
    }

    private void dfs(EdgeWeightedGraph graph, int vertex) {
        visited[vertex] = true;

        for(Edge neighbor : graph.adj(vertex)) {
            int neighborVertex = neighbor.other(vertex);

            if(!visited[neighborVertex]) {
                dfs(graph, neighborVertex);
            }
        }
    }

    static class EdgeWeightedGraph {
        private static final String NEWLINE = System.getProperty("line.separator");

        private final int V;
        private int E;
        private HashSet<Edge>[] adj;

        private EdgeWeightedGraph(int V) {
            if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
            this.V = V;
            this.E = 0;
            adj = (HashSet<Edge>[]) new HashSet[V];
            for (int v = 0; v < V; v++) {
                adj[v] = new HashSet<Edge>();
            }
        }


        public int V() {
            return V;
        }

        public int E() {
            return E;
        }

        // throw an IllegalArgumentException unless {@code 0 <= v < V}
        private void validateVertex(int v) {
            if (v < 0 || v >= V)
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }

        private void addEdge(Edge e) {
            int v = e.either();
            int w = e.other(v);
            validateVertex(v);
            validateVertex(w);
            adj[v].add(e);
            adj[w].add(e);
            E++;
        }

        private void deleteEdge(Edge e)
        {
            int v1 = e.either();
            int v2 = e.other(v1);

            adj[v1].remove(e);
            adj[v2].remove(e);
            E--;
        }

        private Iterable<Edge> adj(int v) {
            validateVertex(v);
            return adj[v];
        }

        private Iterable<Edge> edges() {
            Bag<Edge> list = new Bag<Edge>();
            for (int v = 0; v < V; v++) {
                int selfLoops = 0;
                for (Edge e : adj(v)) {
                    if (e.other(v) > v) {
                        list.add(e);
                    }
                    // only add one copy of each self loop (self loops will be consecutive)
                    else if (e.other(v) == v) {
                        if (selfLoops % 2 == 0) list.add(e);
                        selfLoops++;
                    }
                }
            }
            return list;
        }
    }

    public static void main(String[] args)
    {
        EdgeWeightedGraph graph = new EdgeWeightedGraph(7);

        graph.addEdge(new Edge(0, 4, 0.42));
        graph.addEdge(new Edge(0, 1, 0.45));
        graph.addEdge(new Edge(1, 2, 0.12));
        graph.addEdge(new Edge(2, 3, 0.5));
        graph.addEdge(new Edge(2, 4, 0.82));
        graph.addEdge(new Edge(3, 4, 0.8));
        graph.addEdge(new Edge(1, 4, 0.1));

        ReverseDeleteMST mst = new ReverseDeleteMST(graph);

        System.out.println(mst.MST.toString());
    }
}


