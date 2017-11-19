package algorithmshw;

import edu.princeton.cs.algs4.StdRandom;

/**
 * @author Levi Muriuki
 */
public class RandomEuclideanDigraph extends EuclideanDigraph {

   private RandomEuclideanDigraph(int vertices, double radius)
   {
        super(vertices);
        EuclideanDigraph randomEuclideanDigraph = new EuclideanDigraph(vertices);

        EuclideanDigraph.Vertex[] allVertices = new EuclideanDigraph.Vertex[vertices];

        for(int vId = 0; vId < vertices; vId++)
        {
            //choose random Coordinates
            double randXCoord = StdRandom.uniform();
            double randYCoord = StdRandom.uniform();

            EuclideanDigraph.Vertex vertex = randomEuclideanDigraph.new Vertex(vId, randXCoord, randYCoord);
            allVertices[vId] = vertex;

            randomEuclideanDigraph.addVertex(vertex);
        }

        for(int vId = 0; vId < vertices; vId++)
        {
            for(int other = vId + 1; other < vertices; other++)
            {
                double dist = distanceBetweenPoints(allVertices[vId].xCoordinate, allVertices[vId].yCoordinate, allVertices[other].xCoordinate, allVertices[other].yCoordinate);

                if(dist <= radius)
                {
                    //choose random direction
                    int randDir = StdRandom.uniform(2);
                    if(randDir == 0)
                        randomEuclideanDigraph.addEdge(vId, other);
                    else
                        randomEuclideanDigraph.addEdge(other, vId);
                }
            }
        }
   }

    private double distanceBetweenPoints(double xCoordinate, double yCoordinate, double xCoordinate1, double yCoordinate1)
    {
        return Math.sqrt(Math.pow(xCoordinate - xCoordinate1, 2) + Math.pow(yCoordinate - yCoordinate1, 2));
    }

    public static void main(String[] args)
    {
        int vertices = 6;
        double rad = 0.5;

        EuclideanDigraph randEDG = new RandomEuclideanDigraph(vertices, rad);

        randEDG.show(-0.1, 1.1, -0.1, 1.1, 0.01, 0.03);
    }
}
