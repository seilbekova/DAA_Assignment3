package algorithms;

import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KahnTest {

    @Test
    void testValidTopologicalOrder() {

        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        Metrics metrics = new Metrics();

        Kahn.Result result =
                Kahn.topologicalSort(graph, metrics);

        assertFalse(result.hasCycle);

        assertEquals(4, result.order.size());
    }


    @Test
    void testCycleDetection() {

        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        Metrics metrics = new Metrics();

        Kahn.Result result =
                Kahn.topologicalSort(graph, metrics);

        assertTrue(result.hasCycle);
    }
}