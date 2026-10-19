package benchmark;

import algorithms.DagShortestPath;
import algorithms.Kahn;
import algorithms.Kosaraju;
import algorithms.Kruskal;
import algorithms.Prim;
import graph.Graph;
import metrics.Metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;

public class Benchmark {

    private static final int[] SIZES = {
            1000,
            3162,
            10000,
            31623,
            100000
    };

    public static void main(String[] args) throws IOException {

        File resultsFolder = new File("results");

        if (!resultsFolder.exists()) {
            resultsFolder.mkdirs();
        }

        PrintWriter writer =
                new PrintWriter(new FileWriter("results/results.csv"));

        writer.println(
                "algorithm,graph,n,m,time_ms,dsu_ops," +
                        "edge_scans,comparisons,max_depth,result"
        );

        for (int n : SIZES) {

            runForDensity(n, 4 * n, "sparse", writer);
            runForDensity(n, 20 * n, "dense", writer);
        }

        writer.close();

        System.out.println();
        System.out.println("Benchmark finished.");
        System.out.println("Results saved to results/results.csv");
    }


    private static void runForDensity(
            int n,
            int m,
            String density,
            PrintWriter writer
    ) {

        System.out.println(
                "Running n=" + n +
                        ", m=" + m +
                        " (" + density + ")"
        );

        Graph undirected =
                GraphGenerator.generateUndirected(n, m);

        Graph directed =
                GraphGenerator.generateDirected(n, m);

        Graph dag =
                GraphGenerator.generateDAG(n, m);


        // KRUSKAL
        Metrics kruskalMetrics = new Metrics();

        Kruskal.Result kruskalResult =
                Kruskal.mst(undirected, kruskalMetrics);

        double kruskalTime =
                medianKruskalTime(undirected);

        writeRow(
                writer,
                "Kruskal",
                density + "_undirected",
                n,
                m,
                kruskalTime,
                kruskalMetrics,
                kruskalResult.totalWeight
        );


        // PRIM
        Metrics primMetrics = new Metrics();

        Prim.Result primResult =
                Prim.mst(undirected, primMetrics);

        double primTime =
                medianPrimTime(undirected);

        writeRow(
                writer,
                "Prim",
                density + "_undirected",
                n,
                m,
                primTime,
                primMetrics,
                primResult.totalWeight
        );


        // Проверка: Kruskal и Prim должны совпасть
        if (kruskalResult.totalWeight != primResult.totalWeight) {

            throw new IllegalStateException(
                    "ERROR: Kruskal and Prim weights are different!"
            );
        }


        // KOSARAJU
        Metrics kosarajuMetrics = new Metrics();

        Kosaraju.Result kosarajuResult =
                Kosaraju.findSCC(directed, kosarajuMetrics);

        double kosarajuTime =
                medianKosarajuTime(directed);

        writeRow(
                writer,
                "Kosaraju",
                density + "_directed",
                n,
                m,
                kosarajuTime,
                kosarajuMetrics,
                kosarajuResult.components
        );


        // KAHN
        Metrics kahnMetrics = new Metrics();

        Kahn.Result kahnResult =
                Kahn.topologicalSort(dag, kahnMetrics);

        if (kahnResult.hasCycle) {
            throw new IllegalStateException(
                    "ERROR: Generated DAG contains a cycle!"
            );
        }

        double kahnTime =
                medianKahnTime(dag);

        writeRow(
                writer,
                "Kahn",
                density + "_dag",
                n,
                m,
                kahnTime,
                kahnMetrics,
                kahnResult.order.size()
        );


        // DAG SHORTEST PATH
        Metrics pathMetrics = new Metrics();

        DagShortestPath.Result pathResult =
                DagShortestPath.shortestPaths(dag, pathMetrics);

        long distanceSum = sumDistances(pathResult.distance);

        double pathTime =
                medianDagShortestPathTime(dag);

        writeRow(
                writer,
                "DagShortestPath",
                density + "_dag",
                n,
                m,
                pathTime,
                pathMetrics,
                distanceSum
        );


        System.out.println(
                "  MST weight = " + kruskalResult.totalWeight
        );

        System.out.println(
                "  SCC count = " + kosarajuResult.components
        );

        System.out.println(
                "  DAG distance sum = " + distanceSum
        );
    }


    private static void writeRow(
            PrintWriter writer,
            String algorithm,
            String graph,
            int n,
            int m,
            double time,
            Metrics metrics,
            long result
    ) {

        writer.printf(
                Locale.US,
                "%s,%s,%d,%d,%.3f,%d,%d,%d,%d,%d%n",
                algorithm,
                graph,
                n,
                m,
                time,
                metrics.dsuOps,
                metrics.edgeScans,
                metrics.comparisons,
                metrics.maxDepth,
                result
        );

        writer.flush();
    }


    private static long sumDistances(long[] distance) {

        long sum = 0;

        for (long d : distance) {

            if (d != Long.MAX_VALUE) {
                sum += d;
            }
        }

        return sum;
    }


    private static double medianKruskalTime(Graph graph) {

        double[] times = new double[5];

        for (int i = 0; i < 5; i++) {

            Metrics metrics = new Metrics();

            long start = System.nanoTime();

            Kruskal.mst(graph, metrics);

            long end = System.nanoTime();

            times[i] = (end - start) / 1_000_000.0;
        }

        return median(times);
    }


    private static double medianPrimTime(Graph graph) {

        double[] times = new double[5];

        for (int i = 0; i < 5; i++) {

            Metrics metrics = new Metrics();

            long start = System.nanoTime();

            Prim.mst(graph, metrics);

            long end = System.nanoTime();

            times[i] = (end - start) / 1_000_000.0;
        }

        return median(times);
    }


    private static double medianKosarajuTime(Graph graph) {

        double[] times = new double[5];

        for (int i = 0; i < 5; i++) {

            Metrics metrics = new Metrics();

            long start = System.nanoTime();

            Kosaraju.findSCC(graph, metrics);

            long end = System.nanoTime();

            times[i] = (end - start) / 1_000_000.0;
        }

        return median(times);
    }


    private static double medianKahnTime(Graph graph) {

        double[] times = new double[5];

        for (int i = 0; i < 5; i++) {

            Metrics metrics = new Metrics();

            long start = System.nanoTime();

            Kahn.topologicalSort(graph, metrics);

            long end = System.nanoTime();

            times[i] = (end - start) / 1_000_000.0;
        }

        return median(times);
    }


    private static double medianDagShortestPathTime(Graph graph) {

        double[] times = new double[5];

        for (int i = 0; i < 5; i++) {

            Metrics metrics = new Metrics();

            long start = System.nanoTime();

            DagShortestPath.shortestPaths(graph, metrics);

            long end = System.nanoTime();

            times[i] = (end - start) / 1_000_000.0;
        }

        return median(times);
    }


    private static double median(double[] values) {

        Arrays.sort(values);

        return values[values.length / 2];
    }
}