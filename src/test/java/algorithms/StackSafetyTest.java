package algorithms;

import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StackSafetyTest {

    @Test
    void testKosarajuOn100000Vertices() {

        int n = 100000;

        Graph graph = new Graph(n, true);

        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(i, i + 1, 1);
        }

        Metrics metrics = new Metrics();

        assertDoesNotThrow(() -> {
            Kosaraju.Result result =
                    Kosaraju.findSCC(graph, metrics);

            assertEquals(n, result.components);
        });
    }
}