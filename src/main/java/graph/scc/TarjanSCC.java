package graph.scc;

import util.Metrics;
import java.util.*;

public class TarjanSCC {
    private final Map<Integer, List<Integer>> graph;
    private final Metrics metrics;

    private final Map<Integer, Integer> indexMap = new HashMap<>();
    private final Map<Integer, Integer> lowLinkMap = new HashMap<>();
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final Set<Integer> onStack = new HashSet<>();

    private final List<List<Integer>> sccList = new ArrayList<>();
    private int index = 0;

    public TarjanSCC(Map<Integer, List<Integer>> graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public List<List<Integer>> run() {
        metrics.startTimer();
        for (Integer v : graph.keySet()) {
            if (!indexMap.containsKey(v)) {
                dfs(v);
            }
        }
        metrics.stopTimer();
        metrics.printSummary();
        return sccList;
    }

    private void dfs(int v) {
        metrics.increment("DFS_Calls");
        indexMap.put(v, index);
        lowLinkMap.put(v, index);
        index++;
        stack.push(v);
        onStack.add(v);

        for (int w : graph.getOrDefault(v, Collections.emptyList())) {
            metrics.increment("Edges_Visited");
            if (!indexMap.containsKey(w)) {
                dfs(w);
                lowLinkMap.put(v, Math.min(lowLinkMap.get(v), lowLinkMap.get(w)));
            } else if (onStack.contains(w)) {
                lowLinkMap.put(v, Math.min(lowLinkMap.get(v), indexMap.get(w)));
            }
        }

        if (lowLinkMap.get(v).equals(indexMap.get(v))) {
            List<Integer> component = new ArrayList<>();
            int w;
            do {
                w = stack.pop();
                onStack.remove(w);
                component.add(w);
            } while (w != v);
            sccList.add(component);
        }
    }

    public List<List<Integer>> getSCCs() {
        return sccList;
    }

    public Map<Integer, List<Integer>> buildCondensation() {
        Map<Integer, List<Integer>> dag = new HashMap<>();
        Map<Integer, Integer> componentMap = new HashMap<>();

        for (int i = 0; i < sccList.size(); i++) {
            for (int node : sccList.get(i)) {
                componentMap.put(node, i);
            }
            dag.put(i, new ArrayList<>());
        }

        for (int u : graph.keySet()) {
            for (int v : graph.getOrDefault(u, Collections.emptyList())) {
                int cu = componentMap.get(u);
                int cv = componentMap.get(v);
                if (cu != cv && !dag.get(cu).contains(cv)) {
                    dag.get(cu).add(cv);
                }
            }
        }
        return dag;
    }
}
