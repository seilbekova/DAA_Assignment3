package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Kahn {

    public static Result topologicalSort(Graph graph, Metrics metrics) {

        int n = graph.size();

        int[] indegree = new int[n];

        for (int u = 0; u < n; u++) {
            for (Edge edge : graph.getNeighbors(u)) {
                indegree[edge.to]++;

            }
        }

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

                metrics.edgeScans++;

                indegree[edge.to]--;

                if (indegree[edge.to] == 0) {
                    queue.add(edge.to);
                }
            }
        }

        boolean hasCycle = order.size() != n;

        return new Result(order, hasCycle);
    }

    public static class Result {

        public final List<Integer> order;
        public final boolean hasCycle;

        public Result(List<Integer> order, boolean hasCycle) {
            this.order = order;
            this.hasCycle = hasCycle;
        }
    }
}