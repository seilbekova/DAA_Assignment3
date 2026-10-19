package algorithms;

import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {

    @Test
    void testKruskalAndPrimSameWeight() {

        Graph graph = new Graph(4, false);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 2);
        graph.addEdge(2, 3, 5);

        Metrics kruskalMetrics = new Metrics();
        Metrics primMetrics = new Metrics();

        Kruskal.Result kruskal =
                Kruskal.mst(graph, kruskalMetrics);

        Prim.Result prim =
                Prim.mst(graph, primMetrics);

        assertEquals(
                kruskal.totalWeight,
                prim.totalWeight
        );

        assertEquals(3, kruskal.edges.size());
        assertEquals(3, prim.edges.size());
    }


    @Test
    void testDisconnectedGraph() {

        Graph graph = new Graph(6, false);

        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 3);

        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 5, 4);

        Metrics kruskalMetrics = new Metrics();
        Metrics primMetrics = new Metrics();

        Kruskal.Result kruskal =
                Kruskal.mst(graph, kruskalMetrics);

        Prim.Result prim =
                Prim.mst(graph, primMetrics);

        assertEquals(2, kruskal.components);
        assertEquals(2, prim.components);

        assertEquals(
                kruskal.totalWeight,
                prim.totalWeight
        );
    }
}