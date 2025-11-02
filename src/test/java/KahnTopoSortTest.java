import graph.topo.KahnTopoSort;
import org.junit.jupiter.api.Test;
import util.SimpleMetrics;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class KahnTopoSortTest {

    @Test
    void testSimpleDAG() {
        Map<Integer, List<Integer>> dag = new HashMap<>();
        dag.put(0, List.of(1, 2));
        dag.put(1, List.of(3));
        dag.put(2, List.of(3));
        dag.put(3, List.of());

        KahnTopoSort sorter = new KahnTopoSort(dag, new SimpleMetrics());
        List<Integer> order = sorter.sort();

        assertEquals(4, order.size());
        // Must start with 0, end with 3
        assertEquals(0, order.get(0));
        assertEquals(3, order.get(3));
    }

    @Test
    void testDisconnectedDAG() {
        Map<Integer, List<Integer>> dag = new HashMap<>();
        dag.put(0, List.of());
        dag.put(1, List.of());
        dag.put(2, List.of(3));
        dag.put(3, List.of());

        KahnTopoSort sorter = new KahnTopoSort(dag, new SimpleMetrics());
        List<Integer> order = sorter.sort();

        assertEquals(4, order.size());
        assertTrue(order.containsAll(List.of(0, 1, 2, 3)));
    }

    @Test
    void testCycleThrows() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, List.of(1));
        graph.put(1, List.of(0));

        KahnTopoSort sorter = new KahnTopoSort(graph, new SimpleMetrics());
        assertThrows(IllegalStateException.class, sorter::sort);
    }
}

