package graph.scc;

import java.util.*;
public class CondensationGraphBuilder {
    public static Map<Integer, List<Integer>> buildCondensationGraph(
            Map<Integer, List<Integer>> original,
            List<List<Integer>> sccs) {

        Map<Integer, Integer> vertexToComp = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++) {
            for (Integer v : sccs.get(i)) {
                vertexToComp.put(v, i);
            }
        }

        Map<Integer, Set<Integer>> dagSet = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++) {
            dagSet.put(i, new HashSet<>());
        }

        for (Map.Entry<Integer, List<Integer>> entry : original.entrySet()) {
            int u = entry.getKey();
            for (int v : entry.getValue()) {
                int cu = vertexToComp.get(u);
                int cv = vertexToComp.get(v);
                if (cu != cv) {
                    dagSet.get(cu).add(cv);
                }
            }
        }
        Map<Integer, List<Integer>> condensation = new HashMap<>();
        for (var e : dagSet.entrySet()) {
            condensation.put(e.getKey(), new ArrayList<>(e.getValue()));
        }
        return condensation;
    }

    public static void printCondensation(Map<Integer, List<Integer>> dag, List<List<Integer>> sccs) {
        System.out.println("\nCondensation DAG:");
        for (var entry : dag.entrySet()) {
            System.out.printf("Component %d (%s) -> %s%n",
                    entry.getKey(),
                    sccs.get(entry.getKey()),
                    entry.getValue());
        }
    }
}




