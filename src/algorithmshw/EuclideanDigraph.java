package algorithmshw;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;

/**
 * @author Levi Muriuki
 */
public class EuclideanDigraph {
    //Vertex class
    public class Vertex {
        private String name;
        protected int id;
        protected double xCoordinate;
        protected double yCoordinate;

        Vertex(int id, double xCoordinate, double yCoordinate)
        {
            this(id, String.valueOf(id), xCoordinate, yCoordinate);
        }
        Vertex(int id, String name, double xCoordinate, double yCoordinate)
        {
            this.id = id;
            this.name = name;
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
        }

        public void updateName(String name)
        {
            this.name = name;
        }
    }

    private final int vertices;
    private int edges;
    private Vertex[] allVertices;
    private Bag<Integer>[] adj;

    private int[] indegrees;
    private int[] outdegrees;

    public EuclideanDigraph(int vertices)
    {
        this.vertices = vertices;
        this.edges = 0;
        allVertices = new Vertex[vertices];
        adj = (Bag<Integer>[])new Bag[vertices];

        indegrees = new int[vertices];
        outdegrees = new int[vertices];

        for(int i = 0; i < vertices; i++)
            adj[i] = new Bag<>();
    }

    public void addVertex(Vertex vertex)
    {
        allVertices[vertex.id] = vertex;
    }

    public void addEdge(int v1Id, int v2Id)
    {
        if(allVertices[v1Id] == null || allVertices[v2Id] == null)
            throw new IllegalArgumentException("Vertex id not found");

        adj[v1Id].add(v2Id);

        edges++;
        outdegrees[v1Id]++;
        indegrees[v2Id]++;
    }
    public int V()
    {
        return vertices;
    }

    public int E()
    {
        return edges;
    }

    public void show(double xLower, double xUpper, double yLower, double yUpper, double padding, double arrowLength)
    {
        StdDraw.setCanvasSize(600, 400);
        StdDraw.setXscale(xLower, xUpper);
        StdDraw.setYscale(yLower, yUpper);

        StdDraw.setPenRadius(0.002D);
        StdDraw.setPenColor(Color.BLACK);

        double arrowWidth = padding * 2;

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            for(Integer neighbor : adjacent(vertexId)) {
                Vertex neighborVertex = allVertices[neighbor];

                // Edges pointing up
                if(allVertices[vertexId].yCoordinate < neighborVertex.yCoordinate) {
                    if(allVertices[vertexId].xCoordinate < neighborVertex.xCoordinate) {
                        // Edge pointing diagonally up and right
                        drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate + padding,
                                neighborVertex.xCoordinate, neighborVertex.yCoordinate - padding, arrowWidth, arrowLength);
                    } else if(allVertices[vertexId].xCoordinate > neighborVertex.xCoordinate) {
                        // Edge pointing diagonally up and left
                        drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate + padding,
                                neighborVertex.xCoordinate, neighborVertex.yCoordinate - padding, arrowWidth, arrowLength);
                    } else {
                        // Edge pointing up
                        drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate + padding * 2,
                                neighborVertex.xCoordinate, neighborVertex.yCoordinate - padding, arrowWidth, arrowLength);
                    }
                } if(allVertices[vertexId].yCoordinate > neighborVertex.yCoordinate) {
                    //Edges pointing down
                    if(allVertices[vertexId].xCoordinate < neighborVertex.xCoordinate) {
                        // Edge pointing diagonally down and right
                        drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate - padding * 2,
                                neighborVertex.xCoordinate, neighborVertex.yCoordinate + padding * 4, arrowWidth, arrowLength);
                    } else if(allVertices[vertexId].xCoordinate > neighborVertex.xCoordinate) {
                        // Edge pointing diagonally down and left
                        drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate - padding * 2,
                                neighborVertex.xCoordinate, neighborVertex.yCoordinate + padding * 4, arrowWidth, arrowLength);
                    } else {
                        // Edge pointing down
                        drawArrowLine(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate - padding * 2,
                                neighborVertex.xCoordinate, neighborVertex.yCoordinate + padding * 2, arrowWidth, arrowLength);
                    }
                } else if(allVertices[vertexId].yCoordinate == neighborVertex.yCoordinate) {
                    // Horizontal edges
                    if(allVertices[vertexId].xCoordinate < neighborVertex.xCoordinate) {
                        // Edge pointing right
                        drawArrowLine(allVertices[vertexId].xCoordinate + padding * 2, allVertices[vertexId].yCoordinate,
                                neighborVertex.xCoordinate - padding * 2, neighborVertex.yCoordinate, arrowWidth, arrowLength);
                    } else if(allVertices[vertexId].xCoordinate > neighborVertex.xCoordinate) {
                        // Edge pointing left
                        drawArrowLine(allVertices[vertexId].xCoordinate - padding * 2, allVertices[vertexId].yCoordinate,
                                neighborVertex.xCoordinate + padding * 2, neighborVertex.yCoordinate, arrowWidth, arrowLength);
                    }
                }
            }
        }

        StdDraw.setPenColor(Color.BLUE);

        for(int vertexId = 0; vertexId < vertices; vertexId++) {
            if(allVertices[vertexId] != null) {
                StdDraw.text(allVertices[vertexId].xCoordinate, allVertices[vertexId].yCoordinate,
                        allVertices[vertexId].name);
            }
        }
    }

    private void drawArrowLine(double x1, double y1, double x2, double y2, double arrowWidth, double arrowHeight)
    {
        double xDistance = x2 - x1;
        double yDistance = y2 - y1;
        double distance = Math.sqrt(xDistance * xDistance + yDistance * yDistance);

        double xm = distance - arrowWidth;
        double xn = xm;
        double ym = arrowHeight;
        double yn = -arrowHeight;
        double x;

        double sin = yDistance / distance;
        double cos = xDistance / distance;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        double[] xPoints = {x2, xm, xn};
        double[] yPoints = {y2, ym, yn};

        StdDraw.line(x1, y1, x2, y2);
        StdDraw.filledPolygon(xPoints, yPoints);
    }

    public int degree(int vertex)
    {
        return adj[vertex].size();
    }

    public Iterable<Integer> adjacent(int vId)
    {
        return adj[vId];
    }

    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        for(int vertex = 0; vertex < V(); vertex++) {
            stringBuilder.append(vertex).append(": ");

            for(int neighbor : adjacent(vertex)) {
                stringBuilder.append(neighbor).append(" ");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    /*public static void main(String[] args)
    {
        EuclideanGraph euclideanGraph = new EuclideanGraph(7);

        EuclideanGraph.Vertex vertex0 = euclideanGraph.new Vertex(0, 6.1, 1.3);
        EuclideanGraph.Vertex vertex1 = euclideanGraph.new Vertex(1, 7.2, 2.5);
        EuclideanGraph.Vertex vertex2 = euclideanGraph.new Vertex(2, 8.4, 1.3);
        EuclideanGraph.Vertex vertex3 = euclideanGraph.new Vertex(3, 8.4, 15.3);
        EuclideanGraph.Vertex vertex4 = euclideanGraph.new Vertex(4, 6.1, 15.3);
        EuclideanGraph.Vertex vertex5 = euclideanGraph.new Vertex(5, 7.2, 5.2);
        EuclideanGraph.Vertex vertex6 = euclideanGraph.new Vertex(6, 7.2, 8.4);

        euclideanGraph.addVertex(vertex0);
        euclideanGraph.addVertex(vertex1);
        euclideanGraph.addVertex(vertex2);
        euclideanGraph.addVertex(vertex3);
        euclideanGraph.addVertex(vertex4);
        euclideanGraph.addVertex(vertex5);
        euclideanGraph.addVertex(vertex6);

        euclideanGraph.addEdge(0, 1);
        euclideanGraph.addEdge(2, 1);
        euclideanGraph.addEdge(0, 2);
        euclideanGraph.addEdge(3, 6);
        euclideanGraph.addEdge(4, 6);
        euclideanGraph.addEdge(3, 4);
        euclideanGraph.addEdge(1, 5);
        euclideanGraph.addEdge(5, 6);

        euclideanGraph.show(0.5);
        StdOut.println(euclideanGraph);
    }*/
}

