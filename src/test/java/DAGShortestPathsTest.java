import graph.dagsp.DAGShortestPaths;
import org.junit.jupiter.api.Test;
import util.SimpleMetrics;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class DAGShortestPathsTest {

    @Test
    void testSimpleDAGShortest() {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        graph.put(0, List.of(new int[]{1, 1}, new int[]{2, 4}));
        graph.put(1, List.of(new int[]{2, 2}, new int[]{3, 6}));
        graph.put(2, List.of(new int[]{3, 3}));
        graph.put(3, List.of());

        List<Integer> topo = List.of(0, 1, 2, 3);

        DAGShortestPaths sp = new DAGShortestPaths(graph, new SimpleMetrics());
        Map<Integer, Integer> result = sp.shortestPath(0, topo);

        assertEquals(0, result.get(0));
        assertEquals(1, result.get(1));
        assertEquals(3, result.get(2));
        assertEquals(6, result.get(3));
    }

    @Test
    void testSimpleDAGLongest() {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        graph.put(0, List.of(new int[]{1, 5}, new int[]{2, 3}));
        graph.put(1, List.of(new int[]{3, 6}, new int[]{2, 2}));
        graph.put(2, List.of(new int[]{3, 7}));
        graph.put(3, List.of());

        List<Integer> topo = List.of(0, 1, 2, 3);

        DAGShortestPaths sp = new DAGShortestPaths(graph, new SimpleMetrics());

        Map<Integer, Integer> result = sp.longestPath(0, topo);

        assertEquals(0, result.get(0));
        assertEquals(5, result.get(1));
        assertEquals(7, result.get(2));
        assertEquals(14, result.get(3));

    }

    @Test
    void testDisconnectedVertices() {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        graph.put(0, List.of(new int[]{1, 2}));
        graph.put(1, List.of());
        graph.put(2, List.of()); // disconnected

        List<Integer> topo = List.of(0, 1, 2);

        DAGShortestPaths sp = new DAGShortestPaths(graph, new SimpleMetrics());
        Map<Integer, Integer> result = sp.shortestPath(0, topo);

        assertEquals(Integer.MAX_VALUE, result.get(2));
    }
}
