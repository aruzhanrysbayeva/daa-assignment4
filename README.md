#REPORT
- Data summary:
| Dataset | Number of vertices | Number of edges | Structure type |
| --- | --- | --- | --- |
| Small1 | 8   | 7   | cyclic |
| Small2 | 7   | 7   | DAG |
| Small3 | 9   | 8   | cyclic |
| Medium1 | 12  | 13  | DAG |
| Medium2 | 15  | 16  | DAG |
| Medium3 | 18  | 19  | cyclic |
| Large1 | 20  | 20  | DAG |
| Large2 | 30  | 32  | cyclic |
| Large3 | 50  | 49  | DAG |

Weight model: edge.

- Implementation:

TarjanSCC - Tarjan's algorithm to detect strongly connected components; builds condensation DAG.

KahnTopoSort-Kahn's algorithm for topological sorting (with cycle detection).

DAGSP-Dynamic programming shortest and longest paths over topologically ordered DAG.

- Results:

**1\. Dataset: large1.json**

- **DFS Calls:** 20 | **Edges Visited:** 20 | **Execution Time:** 0.46 ms
- **SCCs Found:** 20 (each node forms its own component)
- **Condensation DAG:** long chain structure with some branching
- **Topological Sort:** Successful; 20 components ordered
- **Shortest Path:** Source 0 reaches nodes 1-3; others unreachable
- **Longest Path:** Limited propagation (only nodes 0-3 valid values)

### ****2\. Dataset: large2.json****

- **DFS Calls:** 30 | **Edges Visited:** 32 | **Execution Time:** 0.30 ms
- **SCCs:** 22 total, with a few multi-node clusters (e.g., {0-2}, {3-5}, {6-10})
- **Condensation DAG:** Linear chain of 22 nodes
- **Shortest Path:** Only nodes 0-1 reachable from source 0
- **Longest Path:** Only node 1 valid; others unreachable

### ****3\. Dataset: large3.json****

- **DFS Calls:** 50 | **Edges Visited:** 49 | **Execution Time:** 0.22 ms
- **SCCs:** 50 (each vertex separate)
- **Condensation DAG:** Perfect linear chain (0 → 1 → … → 49)
- **Shortest Path:** Only node 1 reachable from 0
- **Longest Path:** Same pattern; rest unreachable

### ****4\. Dataset: medium1.json****

- **DFS Calls:** 12 | **Edges Visited:** 13 | **Execution Time:** 0.06 ms
- **SCCs:** 9 (some small cycles)
- **Condensation DAG:** Sequential chain of components
- **Shortest Path:** Nodes 0-1 reachable; others isolated
- **Longest Path:** Similar pattern

### ****5\. Dataset: medium2.json****

- **DFS Calls:** 15 | **Edges Visited:** 16 | **Execution Time:** 0.11 ms
- **SCCs:** 15, several singletons plus small cycles
- **Condensation DAG:** Complex branching (14 → 11, 13 → 1,12, etc.)
- **Shortest Path:** Reachable up to node 6; others unreachable
- **Longest Path:** Similar but slightly deeper due to extra edges

### ****6\. Dataset: medium3.json****

- **DFS Calls:** 18 | **Edges Visited:** 19 | **Execution Time:** 0.07 ms
- **SCCs:** 14 (two multi-node clusters)
- **Condensation DAG:** Linear chain structure
- **Shortest Path:** Only nodes 0-1 reachable
- **Longest Path:** Same pattern

### ****7\. Dataset: small1.json****

- **DFS Calls:** 8 | **Edges Visited:** 7 | **Execution Time:** 0.04 ms
- **SCCs:** 6 (some with multiple nodes, e.g., {1,2,3})
- **Shortest Path (source 4):** Node 5 reachable with distance 2
- **Longest Path:** Same pattern

### ****8\. Dataset: small2.json****

- **DFS Calls:** 7 | **Edges Visited:** 7 | **Execution Time:** 0.04 ms
- **SCCs:** 7 (no cycles)
- **Condensation DAG:** Tree-like, branching from node 6
- **Shortest Path:** Nodes 0-2 reachable (distances 0-3)
- **Longest Path:** Matches shortest path pattern

### ****9\. Dataset: small3.json****

- **DFS Calls:** 9 | **Edges Visited:** 8 | **Execution Time:** 0.07 ms
- **SCCs:** 5 (two 3-node clusters, others singletons)
- **Condensation DAG:** Shallow; only two edges
- **Shortest & Longest Paths:** Minimal propagation due to isolated SCCs

- **Analysis**

### ****SCC (Strongly Connected Components)****

The Tarjan algorithm efficiently detected cyclic dependencies within the task graphs.  
For **dense graphs** (especially in the medium datasets), the algorithm's DFS recursion depth and edge visits increased notably, as each additional edge increased the number of recursive calls and stack operations.  
In contrast, **sparser graphs** and those with long linear chains (large1, large3) produced mostly single-vertex SCCs, resulting in a near-linear runtime.  
The **bottleneck** arises primarily from DFS stack operations in large cyclic regions - once multiple tasks form a cycle, the algorithm must traverse all their edges before identifying a strongly connected group.

### ****Topological Ordering****

Kahn's algorithm performed consistently well across all condensation DAGs.  
The **critical performance factor** was the **in-degree distribution** of the DAG: graphs with many zero-in-degree vertices allowed more parallel processing of nodes, while those with long chains produced sequential dependency resolution.  
Topological sort failed only when cycles remained uncompressed, confirming correctness of the SCC preprocessing step.  
For all datasets, topological ordering completed in negligible time compared to SCC detection.

### ****DAG Shortest and Longest Paths (DAG-SP)****

Shortest-path calculations on DAGs were implemented via dynamic programming over the topological order.  
In most datasets, only a subset of nodes were reachable from the given source due to sparse connectivity - this limited relaxation steps and kept runtimes low.  
The **longest-path algorithm** (using sign inversion and DP) exposed critical paths in more connected graphs (medium2, large2), where cumulative task durations grew across chains of dependencies.  
Bottlenecks appear when multiple long branches converge toward shared sinks, requiring repeated relaxation of overlapping nodes.

- **Conclusion**

SCC detection (Tarjan) efficiently identifies cycles and clusters dependent tasks.  
Topological sorting (Kahn) works best on acyclic graphs, producing valid task execution orders with minimal time.  
DAG shortest and longest path algorithms reveal optimal and critical task chains for scheduling.

In dense graphs, SCC detection is the main bottleneck, while in sparse graphs, path computation dominates.  

Overall, use **SCC** to handle cycles, **Topological Sort** for ordering, and **DAG paths** for time or priority optimization.
