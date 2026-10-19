package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private final int n;
    private final boolean directed;

    private final List<List<Edge>> adjacencyList;
    private final List<Edge> edges;

    public Graph(int n, boolean directed) {

        this.n = n;
        this.directed = directed;

        adjacencyList = new ArrayList<>();
        edges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int from, int to, int weight) {

        Edge edge = new Edge(from, to, weight);

        adjacencyList.get(from).add(edge);

        // сохраняем ребро один раз
        edges.add(edge);

        if (!directed) {
            Edge reverseEdge = new Edge(to, from, weight);
            adjacencyList.get(to).add(reverseEdge);
        }
    }

    public int size() {
        return n;
    }

    public boolean isDirected() {
        return directed;
    }

    public List<Edge> getNeighbors(int vertex) {
        return adjacencyList.get(vertex);
    }

    public List<Edge> getEdges() {
        return edges;
    }
}