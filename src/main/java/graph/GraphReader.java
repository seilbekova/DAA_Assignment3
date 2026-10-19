package graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GraphReader {

    public static Graph readFromFile(String fileName) throws FileNotFoundException {

        Scanner scanner = new Scanner(new File(fileName));

        int n = scanner.nextInt();
        int m = scanner.nextInt();
        String type = scanner.next();

        boolean directed = type.equalsIgnoreCase("directed");

        Graph graph = new Graph(n, directed);

        for (int i = 0; i < m; i++) {
            int from = scanner.nextInt();
            int to = scanner.nextInt();
            int weight = scanner.nextInt();

            graph.addEdge(from, to, weight);
        }

        scanner.close();

        return graph;
    }
}