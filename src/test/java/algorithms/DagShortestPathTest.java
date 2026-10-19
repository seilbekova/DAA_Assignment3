package algorithms;

import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DagShortestPathTest {

    @Test
    void testShortestPaths() {

        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 4);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 7);
        graph.addEdge(2, 3, 3);
        graph.addEdge(3, 4, 1);

        Metrics metrics = new Metrics();

        DagShortestPath.Result result =
                DagShortestPath.shortestPaths(graph, metrics);

        assertEquals(0, result.distance[0]);
        assertEquals(2, result.distance[1]);
        assertEquals(3, result.distance[2]);
        assertEquals(6, result.distance[3]);
        assertEquals(7, result.distance[4]);
    }


    @Test
    void testNegativeWeights() {

        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 2, -4);
        graph.addEdge(2, 3, 3);

        Metrics metrics = new Metrics();

        DagShortestPath.Result result =
                DagShortestPath.shortestPaths(graph, metrics);

        assertEquals(0, result.distance[0]);
        assertEquals(2, result.distance[1]);
        assertEquals(-2, result.distance[2]);
        assertEquals(1, result.distance[3]);
    }


    @Test
    void testPathRestoring() {

        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 3);

        Metrics metrics = new Metrics();

        DagShortestPath.Result result =
                DagShortestPath.shortestPaths(graph, metrics);

        List<Integer> path = result.getPath(3);

        assertEquals(
                List.of(0, 1, 2, 3),
                path
        );
    }


    @Test
    void testCycleThrowsException() {

        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        Metrics metrics = new Metrics();

        assertThrows(
                IllegalArgumentException.class,
                () -> DagShortestPath.shortestPaths(graph, metrics)
        );
    }
}