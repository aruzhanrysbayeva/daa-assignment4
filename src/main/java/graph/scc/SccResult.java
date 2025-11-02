package graph.scc;

import java.util.List;

public class SccResult {
    private final List<List<Integer>> components;
    private final int dfsCalls;

    public SccResult(List<List<Integer>> components, int dfsCalls) {
        this.components = components;
        this.dfsCalls = dfsCalls;
    }

    public List<List<Integer>> getComponents() {
        return components;
    }

    public int getDfsCalls() {
        return dfsCalls;
    }

    public void printSummary() {
        System.out.println("Total SCCs: " + components.size());
        for (int i = 0; i < components.size(); i++) {
            System.out.println("SCC " + (i + 1) + " (" + components.get(i).size() + " nodes): " + components.get(i));
        }
        System.out.println("DFS calls: " + dfsCalls);
    }
}
