package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class DagShortestPath {

    public static Result shortestPaths(Graph graph, Metrics metrics) {

        int n = graph.size();

        int[] indegree = new int[n];

        for (int u = 0; u < n; u++) {
            for (Edge edge : graph.getNeighbors(u)) {
                indegree[edge.to]++;
            }
        }

        int[] originalIndegree = indegree.clone();

        Queue<Integer> queue = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> order = new ArrayList<>();

        while (!queue.isEmpty()) {

            int vertex = queue.poll();
            order.add(vertex);

            for (Edge edge : graph.getNeighbors(vertex)) {

                indegree[edge.to]--;

                if (indegree[edge.to] == 0) {
                    queue.add(edge.to);
                }
            }
        }

        if (order.size() != n) {
            throw new IllegalArgumentException(
                    "Graph contains a cycle. Shortest path requires a DAG."
            );
        }

        long[] distance = new long[n];
        int[] parent = new int[n];

        Arrays.fill(distance, Long.MAX_VALUE);
        Arrays.fill(parent, -1);

        for (int i = 0; i < n; i++) {
            if (originalIndegree[i] == 0) {
                distance[i] = 0;
            }
        }

        for (int vertex : order) {

            if (distance[vertex] == Long.MAX_VALUE) {
                continue;
            }

            for (Edge edge : graph.getNeighbors(vertex)) {

                metrics.edgeScans++;

                long newDistance =
                        distance[vertex] + edge.weight;

                if (newDistance < distance[edge.to]) {

                    distance[edge.to] = newDistance;
                    parent[edge.to] = vertex;
                }
            }
        }

        return new Result(distance, parent);
    }

    public static class Result {

        public final long[] distance;
        public final int[] parent;

        public Result(long[] distance, int[] parent) {
            this.distance = distance;
            this.parent = parent;
        }

        public List<Integer> getPath(int target) {

            List<Integer> path = new ArrayList<>();

            int current = target;

            while (current != -1) {
                path.add(current);
                current = parent[current];
            }

            java.util.Collections.reverse(path);

            return path;
        }
    }
}