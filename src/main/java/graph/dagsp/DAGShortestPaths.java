package graph.dagsp;

import util.Metrics;
import java.util.*;

public class DAGShortestPaths {
    private final Map<Integer, List<int[]>> graph;
    private final Metrics metrics;

    public DAGShortestPaths(Map<Integer, List<int[]>> graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public Map<Integer, Integer> shortestPath(int source, List<Integer> topoOrder) {
        metrics.startTimer();
        Map<Integer, Integer> dist = new HashMap<>();
        for (int v : graph.keySet()) dist.put(v, Integer.MAX_VALUE);
        dist.put(source, 0);

        for (int u : topoOrder) {
            if (dist.get(u) != Integer.MAX_VALUE) {
                for (int[] edge : graph.getOrDefault(u, Collections.emptyList())) {
                    int v = edge[0], w = edge[1];
                    if (dist.get(v) > dist.get(u) + w) {
                        dist.put(v, dist.get(u) + w);
                        metrics.increment("Relaxations");
                    }
                }
            }
        }

        metrics.stopTimer();
        metrics.printSummary();
        return dist;
    }

    public Map<Integer, Integer> longestPath(int source, List<Integer> topoOrder) {
        metrics.startTimer();
        Map<Integer, Integer> dist = new HashMap<>();
        for (int v : graph.keySet()) dist.put(v, Integer.MIN_VALUE);
        dist.put(source, 0);

        for (int u : topoOrder) {
            if (dist.get(u) != Integer.MIN_VALUE) {
                for (int[] edge : graph.getOrDefault(u, Collections.emptyList())) {
                    int v = edge[0], w = edge[1];
                    if (dist.get(v) < dist.get(u) + w) {
                        dist.put(v, dist.get(u) + w);
                        metrics.increment("Relaxations");
                    }
                }
            }
        }

        metrics.stopTimer();
        metrics.printSummary();
        return dist;
    }
}
