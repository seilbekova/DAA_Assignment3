package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Kruskal {

    public static Result mst(Graph graph, Metrics metrics) {

        List<Edge> edges = new ArrayList<>(graph.getEdges());

        edges.sort((a, b) -> {
            metrics.comparisons++;
            return Integer.compare(a.weight, b.weight);
        });

        DSU dsu = new DSU(graph.size(), metrics);

        List<Edge> mstEdges = new ArrayList<>();

        long totalWeight = 0;

        for (Edge edge : edges) {

            metrics.edgeScans++;

            if (dsu.find(edge.from) != dsu.find(edge.to)) {

                dsu.union(edge.from, edge.to);

                mstEdges.add(edge);

                totalWeight += edge.weight;

                if (mstEdges.size() == graph.size() - 1) {
                    break;
                }
            }
        }

        return new Result(
                mstEdges,
                totalWeight,
                dsu.components()
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