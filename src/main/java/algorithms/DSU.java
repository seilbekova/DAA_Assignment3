package algorithms;

import metrics.Metrics;

public class DSU {

    private final int[] parent;
    private final int[] rank;
    private int components;
    private final Metrics metrics;

    public DSU(int n, Metrics metrics) {
        this.metrics = metrics;

        parent = new int[n];
        rank = new int[n];
        components = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) {
        metrics.dsuOps++;

        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }

        return parent[x];
    }

    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;
        }

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }

        metrics.dsuOps++;
        components--;

        return true;
    }

    public int components() {
        return components;
    }
}