package algorithms;

import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SCCTest {

    @Test
    void testOneStronglyConnectedComponent() {

        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        Metrics metrics = new Metrics();

        Kosaraju.Result result =
                Kosaraju.findSCC(graph, metrics);

        assertEquals(1, result.components);
    }


    @Test
    void testSeveralComponents() {

        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 0, 1);

        graph.addEdge(1, 2, 1);

        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 2, 1);

        graph.addEdge(3, 4, 1);

        Metrics metrics = new Metrics();

        Kosaraju.Result result =
                Kosaraju.findSCC(graph, metrics);

        assertEquals(3, result.components);
    }
}