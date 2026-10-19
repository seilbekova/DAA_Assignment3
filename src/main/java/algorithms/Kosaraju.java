package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Kosaraju {

    public static Result findSCC(Graph graph, Metrics metrics) {

        int n = graph.size();

        boolean[] visited = new boolean[n];
        List<Integer> finishOrder = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                iterativeDfsFinish(graph, i, visited, finishOrder, metrics);
            }
        }

        Graph reversed = reverseGraph(graph);

        Collections.reverse(finishOrder);

        boolean[] visitedReversed = new boolean[n];
        int components = 0;

        for (int vertex : finishOrder) {
            if (!visitedReversed[vertex]) {
                iterativeDfsMark(
                        reversed,
                        vertex,
                        visitedReversed,
                        metrics
                );

                components++;
            }
        }

        return new Result(components);
    }

    private static void iterativeDfsFinish(
            Graph graph,
            int start,
            boolean[] visited,
            List<Integer> finishOrder,
            Metrics metrics
    ) {

        class State {
            int vertex;
            int nextIndex;

            State(int vertex, int nextIndex) {
                this.vertex = vertex;
                this.nextIndex = nextIndex;
            }
        }

        Deque<State> stack = new ArrayDeque<>();

        visited[start] = true;
        stack.push(new State(start, 0));

        metrics.maxDepth = Math.max(metrics.maxDepth, stack.size());

        while (!stack.isEmpty()) {

            State current = stack.peek();

            List<Edge> neighbors = graph.getNeighbors(current.vertex);

            if (current.nextIndex < neighbors.size()) {

                Edge edge = neighbors.get(current.nextIndex);
                current.nextIndex++;

                metrics.edgeScans++;

                if (!visited[edge.to]) {
                    visited[edge.to] = true;
                    stack.push(new State(edge.to, 0));

                    metrics.maxDepth =
                            Math.max(metrics.maxDepth, stack.size());
                }

            } else {

                finishOrder.add(current.vertex);
                stack.pop();
            }
        }
    }

    private static void iterativeDfsMark(
            Graph graph,
            int start,
            boolean[] visited,
            Metrics metrics
    ) {

        Deque<Integer> stack = new ArrayDeque<>();

        stack.push(start);
        visited[start] = true;

        metrics.maxDepth = Math.max(metrics.maxDepth, stack.size());

        while (!stack.isEmpty()) {

            int vertex = stack.pop();

            for (Edge edge : graph.getNeighbors(vertex)) {

                metrics.edgeScans++;

                if (!visited[edge.to]) {
                    visited[edge.to] = true;
                    stack.push(edge.to);

                    metrics.maxDepth =
                            Math.max(metrics.maxDepth, stack.size());
                }
            }
        }
    }

    private static Graph reverseGraph(Graph graph) {

        Graph reversed = new Graph(graph.size(), true);

        for (Edge edge : graph.getEdges()) {
            reversed.addEdge(edge.to, edge.from, edge.weight);
        }

        return reversed;
    }

    public static class Result {

        public final int components;

        public Result(int components) {
            this.components = components;
        }
    }
}