package graph.topo;

import util.Metrics;
import java.util.*;

public class KahnTopoSort {
    private final Map<Integer, List<Integer>> graph;
    private final Metrics metrics;

    public KahnTopoSort(Map<Integer, List<Integer>> graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public List<Integer> sort() {
        metrics.startTimer();
        Map<Integer, Integer> indegree = new HashMap<>();

        for (int u : graph.keySet()) {
            indegree.putIfAbsent(u, 0);
            for (int v : graph.get(u)) {
                indegree.put(v, indegree.getOrDefault(v, 0) + 1);
                metrics.increment("Edges_Processed");
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int node : indegree.keySet()) {
            if (indegree.get(node) == 0) q.add(node);
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            topoOrder.add(u);
            metrics.increment("Nodes_Dequeued");

            for (int v : graph.getOrDefault(u, Collections.emptyList())) {
                indegree.put(v, indegree.get(v) - 1);
                if (indegree.get(v) == 0) {
                    q.add(v);
                }
            }
        }
        if (topoOrder.size() != graph.size()) {
            throw new IllegalStateException("Cycle detected in graph — topological sort not possible.");
        }

        metrics.stopTimer();
        metrics.printSummary();
        return topoOrder;
    }
}
