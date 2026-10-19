package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Prim {

    public static Result mst(Graph graph, Metrics metrics) {

        int n = graph.size();

        boolean[] visited = new boolean[n];

        PriorityQueue<Edge> pq =
                new PriorityQueue<>((a, b) -> {
                    metrics.comparisons++;
                    return Integer.compare(a.weight, b.weight);
                });

        List<Edge> mstEdges = new ArrayList<>();

        long totalWeight = 0;
        int components = 0;

        for (int start = 0; start < n; start++) {

            if (visited[start]) {
                continue;
            }

            components++;
            visited[start] = true;

            for (Edge edge : graph.getNeighbors(start)) {
                pq.add(edge);
            }

            while (!pq.isEmpty()) {

                Edge edge = pq.poll();
                metrics.edgeScans++;

                if (visited[edge.to]) {
                    continue;
                }

                visited[edge.to] = true;

                mstEdges.add(edge);
                totalWeight += edge.weight;

                for (Edge next : graph.getNeighbors(edge.to)) {
                    if (!visited[next.to]) {
                        pq.add(next);
                    }
                }
            }
        }

        return new Result(
                mstEdges,
                totalWeight,
                components
        );
    }

    public static class Result {

        public final List<Edge> edges;
        public final long totalWeight;
        public final int components;

        public Result(List<Edge> edges, long totalWeight, int components) {
            this.edges = edges;
            this.totalWeight = totalWeight;
            this.components = components;
        }
    }
}