import com.google.gson.*;
import graph.scc.TarjanSCC;
import graph.topo.KahnTopoSort;
import graph.dagsp.DAGShortestPaths;
import util.SimpleMetrics;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Path dataDir = Paths.get("data");
        if (!Files.exists(dataDir)) {
            System.err.println("Folder 'data' not found!");
            return;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dataDir, "*.json")) {
            for (Path file : stream) {
                System.out.println("\n==============================");
                System.out.println("Dataset: " + file.getFileName());
                System.out.println("==============================");

                JsonObject obj = JsonParser.parseReader(new FileReader(file.toFile())).getAsJsonObject();
                int n = obj.get("n").getAsInt();
                int source = obj.has("source") ? obj.get("source").getAsInt() : 0;

                Map<Integer, List<Integer>> graph = new HashMap<>();
                for (int i = 0; i < n; i++) graph.put(i, new ArrayList<>());
                JsonArray edges = obj.getAsJsonArray("edges");
                for (JsonElement e : edges) {
                    JsonObject edge = e.getAsJsonObject();
                    int u = edge.get("u").getAsInt();
                    int v = edge.get("v").getAsInt();
                    graph.get(u).add(v);
                }

                SimpleMetrics metrics = new SimpleMetrics();
                TarjanSCC scc = new TarjanSCC(graph, metrics);
                List<List<Integer>> components = scc.run();

                System.out.println("Strongly Connected Components:");
                for (int i = 0; i < components.size(); i++) {
                    System.out.println("  SCC " + i + ": " + components.get(i));
                }

                Map<Integer, List<Integer>> dag = scc.buildCondensation();
                System.out.println("\nCondensation DAG:");
                dag.forEach((k, v) -> System.out.println("  " + k + " -> " + v));

                KahnTopoSort topo = new KahnTopoSort(dag, new SimpleMetrics());
                List<Integer> topoOrder = topo.sort();
                System.out.println("Topological Order of Components: " + topoOrder);

                Map<Integer, List<int[]>> weightedGraph = new HashMap<>();
                for (int i = 0; i < n; i++) weightedGraph.put(i, new ArrayList<>());
                for (JsonElement e : edges) {
                    JsonObject edge = e.getAsJsonObject();
                    int u = edge.get("u").getAsInt();
                    int v = edge.get("v").getAsInt();
                    int w = edge.has("w") ? edge.get("w").getAsInt() : 1;
                    weightedGraph.get(u).add(new int[]{v, w});
                }

                DAGShortestPaths dagsp = new DAGShortestPaths(weightedGraph, new SimpleMetrics());
                Map<Integer, Integer> shortest = dagsp.shortestPath(source, topoOrder);
                Map<Integer, Integer> longest = dagsp.longestPath(source, topoOrder);

                System.out.println("\nShortest Distances from Source " + source + ":");
                shortest.forEach((k, v) -> System.out.println("  " + k + " -> " + v));

                System.out.println("\nLongest Path Distances from Source " + source + ":");
                longest.forEach((k, v) -> System.out.println("  " + k + " -> " + v));

                System.out.println("==============================\n");
            }
        }
    }
}
