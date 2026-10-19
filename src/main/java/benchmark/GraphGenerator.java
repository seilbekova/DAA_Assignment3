package benchmark;

import graph.Graph;

import java.util.Random;

public class GraphGenerator {

    private static final long SEED = 42;

    // 1. UNDIRECTED GRAPH — для Kruskal и Prim
    public static Graph generateUndirected(int n, int m) {

        Random rnd = new Random(SEED);

        Graph graph = new Graph(n, false);

        int[] perm = createShuffledArray(n, rnd);

        // Сначала создаём spanning tree,
        // чтобы граф точно был connected
        for (int i = 1; i < n; i++) {

            int parentIndex = rnd.nextInt(i);

            int a = perm[parentIndex];
            int b = perm[i];

            int weight = 1 + rnd.nextInt(1000);

            graph.addEdge(a, b, weight);
        }

        // Остальные случайные рёбра
        int remaining = m - (n - 1);

        for (int i = 0; i < remaining; i++) {

            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);

            while (b == a) {
                b = rnd.nextInt(n);
            }

            int weight = 1 + rnd.nextInt(1000);

            graph.addEdge(a, b, weight);
        }

        return graph;
    }


    // 2. DIRECTED GRAPH — для Kosaraju
    public static Graph generateDirected(int n, int m) {

        Random rnd = new Random(SEED);

        Graph graph = new Graph(n, true);

        for (int i = 0; i < m; i++) {

            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);

            while (b == a) {
                b = rnd.nextInt(n);
            }

            int weight = 1 + rnd.nextInt(1000);

            graph.addEdge(a, b, weight);
        }

        return graph;
    }


    // 3. DAG — для Kahn и DagShortestPath
    public static Graph generateDAG(int n, int m) {

        Random rnd = new Random(SEED);

        Graph graph = new Graph(n, true);

        int[] rank = createShuffledArray(n, rnd);

        for (int i = 0; i < m; i++) {

            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);

            while (b == a) {
                b = rnd.nextInt(n);
            }

            // Направляем ребро только "вперёд"
            // по rank, чтобы не было циклов
            if (rank[a] > rank[b]) {
                int temp = a;
                a = b;
                b = temp;
            }

            int weight = 1 + rnd.nextInt(1000);

            graph.addEdge(a, b, weight);
        }

        return graph;
    }


    // Создаёт массив 0,1,2,...,n-1
    // и перемешивает его
    private static int[] createShuffledArray(int n, Random rnd) {

        int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = i;
        }

        for (int i = n - 1; i > 0; i--) {

            int j = rnd.nextInt(i + 1);

            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        return array;
    }
}