import graph.scc.TarjanSCC;
import org.junit.jupiter.api.Test;
import util.SimpleMetrics;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCCTest {

    @Test
    void testSingleSCC() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, List.of(1));
        graph.put(1, List.of(2));
        graph.put(2, List.of(0));

        TarjanSCC scc = new TarjanSCC(graph, new SimpleMetrics());
        List<List<Integer>> result = scc.run();

        assertEquals(1, result.size());
        assertTrue(result.get(0).containsAll(List.of(0, 1, 2)));
    }

    @Test
    void testTwoComponents() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, List.of(1));
        graph.put(1, List.of());
        graph.put(2, List.of(3));
        graph.put(3, List.of());

        TarjanSCC scc = new TarjanSCC(graph, new SimpleMetrics());
        List<List<Integer>> result = scc.run();

        assertEquals(4, result.size());
    }

    @Test
    void testEdgeCaseEmptyGraph() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        TarjanSCC scc = new TarjanSCC(graph, new SimpleMetrics());
        List<List<Integer>> result = scc.run();

        assertTrue(result.isEmpty());
    }
}

